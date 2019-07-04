package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.ibm.icu.text.SimpleDateFormat;

import modelo.GrupoMateria;
import modelo.PaseListaAlumno;
import tablas.TablaGrupoMateria;
import tablas.TablaMateria;
import tablas.TablaPaseLista;
import tablas.TablaPaseListaAlumno;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PaseLista extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DefaultTableModel modelo;
	private JTable tabla;
	private JScrollPane scroll;
	private PreparedStatement statement;
	private Connection conexion;
	private String sql;
	private JComboBox<String> combo;
	private JLabel labelFech;
	private JTextField textFieldIzq;
	private JButton cancel;
	private JButton acpt;
	private Long[] bajaAlumnos;
	private TablaGrupoMateria tablaGrupoMateria;
	private GrupoMateria gpo;
	private GrupoMateria[] gpos;
	private TablaPaseLista tablaPaseLista;
	private TablaMateria tablaMateria;

	public PaseLista(Connection conexion, Container contentPane) {
		this.conexion = conexion;
		tablaGrupoMateria = new TablaGrupoMateria(conexion);
		tablaPaseLista = new TablaPaseLista(conexion);
		tablaMateria = new TablaMateria(conexion);
		gpos = tablaGrupoMateria.getCurrentGroups();
		setLayout(new BorderLayout());
		JPanel panelSuperior = new JPanel();
		panelSuperior.setBackground(new Color(153, 204, 204));
		add(panelSuperior, BorderLayout.NORTH);
		panelSuperior.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		labelFech = new JLabel("Pase de Lista Correspondiente a la Fecha: ".concat(sd.format(new Date())));
		labelFech.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelSuperior.add(labelFech);

		combo = new JComboBox<>();
		combo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		agregarItems();
		panelSuperior.add(combo);
		panelSuperior.add(labelFech);
		textFieldIzq = new JTextField();
		textFieldIzq.setColumns(10);

		combo.addActionListener(x -> {

			int seleccion = combo.getSelectedIndex() - 1;
			if (seleccion > -1) {
				gpo = gpos[seleccion];
				bajaAlumnos = tablaPaseLista.getAlumnosConFaltas(gpo);
				RenderTabla miRender = new RenderTabla(bajaAlumnos);
				RenderTablaCheck renderCheck = new RenderTablaCheck(bajaAlumnos);
				tabla.setDefaultRenderer(Integer.class, miRender);
				tabla.setDefaultRenderer(Object.class, miRender);
				tabla.setDefaultRenderer(Boolean.class, renderCheck);
				setColums();
				añadirLista(gpo);
			}

		});

		JPanel panelInferior = new JPanel();
		panelInferior.setBackground(new Color(102, 153, 153));
		acpt = new JButton("Aceptar");
		acpt.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cancel = new JButton("Cancelar");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPane.removeAll();
				contentPane.repaint();
				contentPane.setVisible(true);
			}
		});
		cancel.setFont(new Font("Tahoma", Font.PLAIN, 15));

		panelInferior.add(acpt);
		panelInferior.add(cancel);

		acpt.addActionListener(x -> {
			int seleccion = combo.getSelectedIndex() - 1;
			if (seleccion > -1) {
				generarPaseLista(gpo);
				agregarItems();
				contentPane.removeAll();
				contentPane.repaint();
				contentPane.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(null, "Seleccione un Grupo Valido");

			}
		});

		add(panelInferior, BorderLayout.SOUTH);

		initComponents();
	}

	private void initComponents() {

		String[] cabecera = { "#", "Numero de Control", "Nombre del Alumno", "Asistencia" };
		tabla = new JTable();
		tabla.setBackground(new Color(204, 204, 204));
		tabla.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tabla.setRowHeight(25);
		scroll = new JScrollPane(tabla);
		tabla.setFillsViewportHeight(true);
		modelo = new DefaultTableModel() {

			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			@Override
			public boolean isCellEditable(int f, int c) {
				return c == cabecera.length - 1 ? true : false;
			}

		};

		modelo.setColumnIdentifiers(cabecera);
		tabla.setModel(modelo);

		add(scroll, BorderLayout.CENTER);
	}

	private void agregarItems() {
		combo.removeAllItems();
		combo.addItem("--Seleccione--");
		List<GrupoMateria> list = new ArrayList<GrupoMateria>();
		for (GrupoMateria gpo : gpos) {
			if (tablaPaseLista.sePuedePasarLista(gpo)) {
				combo.addItem(tablaMateria.getMateria(gpo.getCveMateria()).getNombre()
						.concat(" ".concat(gpo.getNombreGrupo())));
				list.add(gpo);
			}

		}
		gpos = list.toArray(new GrupoMateria[list.size()]);
	}

	private void generarPaseLista(GrupoMateria gpo) {

		TablaPaseLista paseLista = new TablaPaseLista(conexion);
		modelo.PaseLista lis = new modelo.PaseLista();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
		lis.setFecha(sd.format(new Date()));
		lis.setGruMat(gpo.getClaveGrupoMateria());

		if (!paseLista.existe(lis)) {
			System.out.println(paseLista.agregar(lis));

			lis = paseLista.buscar(lis);

			TablaPaseListaAlumno paseLisAlum = new TablaPaseListaAlumno(conexion);

			for (int i = 0; i < modelo.getRowCount(); i++) {
				PaseListaAlumno paseAl = new PaseListaAlumno();
				paseAl.setAlumno(modelo.getValueAt(i, 1).toString());
				paseAl.setPaseLista(lis.getClave());
				paseAl.setEstatus(
						Boolean.parseBoolean(modelo.getValueAt(i, modelo.getColumnCount() - 1).toString()) == true ? "1"
								: "0");
				paseLisAlum.agregar(paseAl);
			}

			JOptionPane.showMessageDialog(null, "Registrada");

		} else
			JOptionPane.showMessageDialog(null, "Lista Ya Registrada");

	}

	private void setColums() {

		TableColumn column = null;
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		int tamaño = scroll.getSize().width;
		column = tabla.getColumnModel().getColumn(0);
		column.setMinWidth(80);
		column.setPreferredWidth((int) (tamaño * .20));
		column = tabla.getColumnModel().getColumn(1);
		column.setMinWidth(130);
		column.setPreferredWidth((int) (tamaño * .20));
		column = tabla.getColumnModel().getColumn(2);
		column.setMinWidth(400);
		column.setPreferredWidth((int) (tamaño * .40));
		column = tabla.getColumnModel().getColumn(3);
		column.setMinWidth(100);
		column.setPreferredWidth((int) (tamaño * .20));

	}

	private void añadirLista(GrupoMateria gpo) {
		sql = "select a.cve_alum, a.nom_alum from alumno a join  "
				+ "(select cve_alum from grupomatealum gma where gma.cve_grumat = " + gpo.getClaveGrupoMateria() + ")"
				+ " as j on j.cve_alum = a.cve_alum order by a.nom_alum";

		modelo.setRowCount(0);
		try {
			statement = conexion.prepareStatement(sql);
			ResultSet result = statement.executeQuery();

			for (int i = 1; result.next(); i++) {
				modelo.addRow(new Object[] { i, result.getString(1), result.getString(2), true });
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
