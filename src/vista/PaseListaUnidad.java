package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.ibm.icu.text.SimpleDateFormat;

import modelo.GrupoMateria;
import modelo.PaseListaAlumno;
import tablas.TablaPaseLista;
import tablas.TablaPaseListaAlumno;

public class PaseListaUnidad extends JDialog {

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

	public PaseListaUnidad(Connection conexion) {
		this.conexion = conexion;
		getContentPane().setLayout(new BorderLayout());
		JPanel panelSuperior = new JPanel();
		getContentPane().add(panelSuperior, BorderLayout.NORTH);
		panelSuperior.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		labelFech = new JLabel("Pase de Lista Correspondiente a la Fecha: ".concat(sd.format(new Date())));
		panelSuperior.add(labelFech);

		combo = new JComboBox<>();
		for (String s : grupoMateriaToArray(getGrupoMateria())) {
			combo.addItem(s);
		}
		panelSuperior.add(combo);
		panelSuperior.add(labelFech);
		textFieldIzq = new JTextField();
		// panelSuperior.add(textFieldIzq);
		textFieldIzq.setColumns(10);

		combo.addItemListener(x -> {

			int seleccion = combo.getSelectedIndex() - 1;
			if (seleccion > -1) {
				String op = getGrupoMateria()[seleccion].getClaveGrupoMateria();
				añadirLista(op);
			}

		});

		JPanel panelInferior = new JPanel();

		JButton acpt = new JButton("Aceptar");
		JButton cancel = new JButton("Cancelar");

		panelInferior.add(acpt);
		panelInferior.add(cancel);

		cancel.addActionListener(x -> {
			dispose();
		});

		acpt.addActionListener(x -> {
			int seleccion = combo.getSelectedIndex() - 1;
			if (seleccion > -1) {
				generarPaseLista(getGrupoMateria()[seleccion]);
			} else {
				JOptionPane.showMessageDialog(null, "Seleccione un Grupo Valido");
			}
		});

		add(panelInferior, BorderLayout.SOUTH);

		setSize(800, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Pase de Lista");

		setLocationRelativeTo(null);
		initComponents();
		// setModal(true);
		// setVisible(true);
	}

	private void initComponents() {

		String[] cabecera = { "#", "Numero de Control", "Nombre del Alumno", "Asistencia" };
		tabla = new JTable();
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

		for (int i = 0; i < modelo.getColumnCount() - 1; i++) {
			DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
			tcr.setHorizontalAlignment(SwingConstants.CENTER);
			tabla.getColumnModel().getColumn(i).setCellRenderer(tcr);
		}

		getContentPane().add(scroll, BorderLayout.CENTER);
		setModal(true);
		setVisible(true);

		TableColumn column = null;

		int total = scroll.getWidth();

		column = tabla.getColumnModel().getColumn(0);
		column.setPreferredWidth((int) (total * .10));
		column = tabla.getColumnModel().getColumn(1);
		column.setPreferredWidth((int) (total * .25));
		column = tabla.getColumnModel().getColumn(2);
		column.setPreferredWidth((int) (total * .35));
		column = tabla.getColumnModel().getColumn(3);
		column.setPreferredWidth((int) (total * .30));
	}

	private GrupoMateria[] getGrupoMateria() {
		sql = " select  g.cve_grumat, g.nom_gru ,m.nom_mate  ,p.cve_per from periodo p,grupomateria g, materia m where g.cve_mate=m.cve_mate and g.cve_per = p.cve_per";
		try {
			statement = conexion.prepareStatement(sql);
			ResultSet result = statement.executeQuery();

			List<GrupoMateria> gps = new ArrayList<GrupoMateria>();

			while (result.next()) {
				gps.add(new GrupoMateria(result.getString(1), result.getString(2), result.getString(3),
						result.getInt(4)));
			}

			return gps.toArray(new GrupoMateria[gps.size()]);

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String[] grupoMateriaToArray(GrupoMateria[] gpos) {

		String[] lista = new String[gpos.length + 1];
		lista[0] = "--Seleccione--";

		for (int i = 0; i < lista.length - 1; i++) {
			lista[i + 1] = gpos[i].getCveMateria().concat(" ").concat(gpos[i].getNombreGrupo());
		}
		return lista;

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

	private void añadirLista(String gpo) {
		sql = "select a.cve_alum, a.nom_alum from alumno a join  "
				+ "(select cve_alum from grupomatealum gma where gma.cve_grumat = " + gpo + ")"
				+ " as j on j.cve_alum = a.cve_alum";

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
