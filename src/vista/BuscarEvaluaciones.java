package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import com.placeholder.PlaceHolder;

import modelo.Criterio;
import modelo.CriterioUnidad;
import modelo.EstructuraLista;
import modelo.GeneradorPDF;
import modelo.Grupo;
import modelo.GrupoMateria;
import modelo.Materia;
import modelo.Revision;
import modelo.Unidad;
import tablas.TablaCriterio;
import tablas.TablaGrupoMateria;
import tablas.TablaMateria;
import tablas.TablaPaseLista;
import tablas.TablaRevision;
import tablas.TablaUnidad;

public class BuscarEvaluaciones extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JComboBox<String> comboCriterio;
	private JComboBox<String> comboUnidad;
	private JComboBox<String> comboGrupoMateria;
	private GrupoMateria[] gpos;
	private Unidad[] unidades;
	private modelo.CriterioUnidad[] criterios;
	private TablaGrupoMateria tablaGrupoMateria;
	private TablaUnidad tablaUnidad;
	private TablaMateria tablaMateria;
	private Unidad unidad;
	private CriterioUnidad criterio;
	private GrupoMateria grupoMateria;
	private Materia materia;
	private TablaCriterio tablaCriterio;
	private JTable tabla;
	private JScrollPane scroll;
	private DefaultTableModel modelo;
	private String sql;
	private PreparedStatement statement;
	private Connection conexion;
	private TablaRevision tablaRevision;
	private JCheckBox chckbxMostarCriterios;
	private JCheckBox chckbxMostrarRevisionesDe;
	private JButton cancelButton;
	private Long[] bajaAlumnos;
	private TablaPaseLista tablaPaseLista;
	private JTextField textFieldBusq;
	private TableRowSorter<DefaultTableModel> trs;

	/**
	 * Launch the application.
	 */
	public BuscarEvaluaciones(Connection conexion) {
		this.conexion = conexion;
		tablaPaseLista = new TablaPaseLista(conexion);
		tablaRevision = new TablaRevision(conexion);
		tablaGrupoMateria = new TablaGrupoMateria(conexion);
		tablaMateria = new TablaMateria(conexion);
		tablaCriterio = new TablaCriterio(conexion);
		tablaUnidad = new TablaUnidad(conexion);
		gpos = tablaGrupoMateria.getCurrentGroups();
		setSize(450, 300);
		setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		scroll = new JScrollPane();

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);

		DefaultComboBoxModel<String> modeloCombo = new DefaultComboBoxModel<>();
		panel.setLayout(new GridLayout(2, 1, 0, 0));

		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(153, 153, 153));
		panel.add(panel_4);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(153, 153, 153));
		panel_4.add(panel_1);
		panel_1.setBorder(new TitledBorder(null, "Seleccione el Grupo y Materia", TitledBorder.LEADING,
				TitledBorder.TOP, Vista.fuentePrincipal, null));
		comboGrupoMateria = new JComboBox<>(modeloCombo);
		comboGrupoMateria.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		panel_1.add(comboGrupoMateria);
		comboGrupoMateria.addItem("--Seleccione--");

		comboGrupoMateria.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (comboGrupoMateria.getSelectedIndex() > 0) {
					removerItems(comboUnidad);
					removerItems(comboCriterio);
					grupoMateria = gpos[comboGrupoMateria.getSelectedIndex() - 1];
					materia = tablaMateria.getMateria(grupoMateria.getCveMateria());
					unidades = tablaMateria.getUnidades(materia);
					for (Unidad u : unidades) {
						comboUnidad.addItem(u.getNumeroUnidad() + " " + u.getNombre());
					}
					if (comboUnidad.getItemCount() == 1) {
						JOptionPane.showMessageDialog(null, "No se han Registrado Unidades para la Materia");
						comboUnidad.setEnabled(false);
						comboCriterio.setEnabled(false);
					} else {
						comboUnidad.setEnabled(true);
						añadirLista(grupoMateria.getClaveGrupoMateria());
						unidades = tablaMateria.getUnidades(tablaMateria.getMateria(grupoMateria.getCveMateria()));
						for (Unidad unidad : unidades) {
							añadirUnidad(unidad);
						}
						añadirPromedioPeriodo(materia);
					}
					chckbxMostarCriterios.setEnabled(true);
					chckbxMostrarRevisionesDe.setSelected(false);
					chckbxMostrarRevisionesDe.setEnabled(false);
					textFieldBusq.setEnabled(true);
				}
			}
		});

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(153, 153, 153));
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(null, "Seleccione la Unidad", TitledBorder.LEADING, TitledBorder.TOP,
				Vista.fuentePrincipal, null));
		comboUnidad = new JComboBox<String>();
		comboUnidad.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		panel_2.add(comboUnidad);
		comboUnidad.addItem("--Seleccione--");
		comboUnidad.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (comboUnidad.getSelectedIndex() > 0) {
					unidad = unidades[comboUnidad.getSelectedIndex() - 1];
					criterios = tablaUnidad.getCriterios(unidad, grupoMateria);
					removerItems(comboCriterio);
					for (int i = 0; i < criterios.length; i++) {

						Criterio c = new Criterio();
						c.setClave(criterios[i].getCvecri());
						comboCriterio.addItem(tablaCriterio.getCriterio(c).getNombre());

					}
					if (comboCriterio.getItemCount() == 1) {
						JOptionPane.showMessageDialog(null, "No se Han Registrado Criterios Para Esta Unidad");
						comboCriterio.setEnabled(false);
						chckbxMostarCriterios.setEnabled(false);
						chckbxMostrarRevisionesDe.setSelected(false);
						chckbxMostrarRevisionesDe.setEnabled(false);
					} else {

						comboCriterio.setEnabled(true);
						añadirLista(grupoMateria.getClaveGrupoMateria());
						añadirUnidad(unidad);
						chckbxMostarCriterios.setEnabled(true);
						chckbxMostrarRevisionesDe.setSelected(false);
						chckbxMostrarRevisionesDe.setEnabled(false);

					}

				}
			}
		});
		comboUnidad.setEnabled(false);

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(153, 153, 153));
		panel_4.add(panel_3);
		panel_3.setBorder(new TitledBorder(null, "Seleccione la Actividad a Revisar", TitledBorder.LEADING,
				TitledBorder.TOP, Vista.fuentePrincipal, null));
		comboCriterio = new JComboBox<>();
		comboCriterio.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		panel_3.add(comboCriterio);
		comboCriterio.addItem("--Seleccione--");

		comboCriterio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (comboCriterio.getSelectedIndex() > 0) {
					criterio = criterios[comboCriterio.getSelectedIndex() - 1];
					añadirLista(grupoMateria.getClaveGrupoMateria());
					añadirCriterio(criterio);
					chckbxMostrarRevisionesDe.setEnabled(true);
					chckbxMostarCriterios.setEnabled(false);
				}
				chckbxMostarCriterios.setSelected(false);
			}
		});
		comboCriterio.setEnabled(false);

		JPanel panel_5 = new JPanel();
		panel_5.setBackground(new Color(153, 153, 153));
		panel.add(panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));

		for (GrupoMateria g : gpos) {
			comboGrupoMateria.addItem(
					tablaMateria.getMateria(g.getCveMateria()).getNombre().concat(" ").concat(g.getNombreGrupo()));
		}
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(153, 153, 153));
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		add(buttonPane, BorderLayout.SOUTH);
		cancelButton = new JButton("Salir");
		cancelButton.setFont(new Font("Bahnschrift", Font.PLAIN, 15));

		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		JPanel panel_7 = new JPanel();
		panel_7.setBackground(new Color(153, 153, 153));
		panel_5.add(panel_7, BorderLayout.CENTER);

		JPanel panel_6 = new JPanel();
		panel_7.add(panel_6);
		panel_6.setBackground(new Color(153, 153, 153));
		panel_6.setBorder(new TitledBorder(null, "Opciones", TitledBorder.LEADING, TitledBorder.TOP,
				Vista.fuentePrincipal, null));

		chckbxMostarCriterios = new JCheckBox("Mostar Criterios");
		chckbxMostarCriterios.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		chckbxMostarCriterios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (comboGrupoMateria.getSelectedIndex() > 0 || comboUnidad.getSelectedIndex() > 0) {
					añadirLista(grupoMateria.getClaveGrupoMateria());
					if (comboUnidad.getSelectedIndex() > 0) {
						añadirUnidad(unidad);
					} else {
						for (Unidad unidad : unidades) {
							añadirUnidad(unidad);
						}
					}
					if (chckbxMostarCriterios.isSelected()) {
						chckbxMostrarRevisionesDe.setEnabled(true);
						chckbxMostrarRevisionesDe.setSelected(false);
					} else {
						chckbxMostrarRevisionesDe.setSelected(false);
						chckbxMostrarRevisionesDe.setEnabled(false);
					}

					if (comboUnidad.getSelectedIndex() < 1) {
						añadirPromedioPeriodo(materia);
					}
				}
			}
		});
		panel_6.add(chckbxMostarCriterios);

		chckbxMostrarRevisionesDe = new JCheckBox("Mostrar Revisiones de Criterios");
		chckbxMostrarRevisionesDe.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		panel_6.add(chckbxMostrarRevisionesDe);

		chckbxMostrarRevisionesDe.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (comboGrupoMateria.getSelectedIndex() > 0 || comboUnidad.getSelectedIndex() > 0) {
					añadirLista(grupoMateria.getClaveGrupoMateria());
					if (comboUnidad.getSelectedIndex() > 0 && comboCriterio.getSelectedIndex() < 1) {
						añadirUnidad(unidad);
					} else if (comboCriterio.getSelectedIndex() > 0) {
						añadirCriterio(criterio);
					} else {
						for (Unidad unidad : unidades) {
							añadirUnidad(unidad);
						}
					}

				}
			}
		});
		chckbxMostarCriterios.setEnabled(false);
		chckbxMostarCriterios.setSelected(false);
		chckbxMostrarRevisionesDe.setEnabled(false);
		chckbxMostrarRevisionesDe.setSelected(false);

		JPanel panel_8 = new JPanel();
		panel_8.setBackground(new Color(153, 153, 153));
		panel_5.add(panel_8, BorderLayout.EAST);
		panel_8.setLayout(new BorderLayout(0, 0));

		JPanel panel_9 = new JPanel();
		panel_9.setBackground(new Color(153, 153, 153));
		panel_8.add(panel_9, BorderLayout.SOUTH);

		textFieldBusq = new JTextField();
		panel_9.add(textFieldBusq);
		textFieldBusq.setFont(new Font("Tahoma", Font.PLAIN, 15));
		new PlaceHolder(textFieldBusq, "Numero de Control o Nombre");
		textFieldBusq.setColumns(25);

		JButton btnNewButton = new JButton("");
		btnNewButton.setPreferredSize(new Dimension(20, 20));
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setIcon(new ImageIcon(BuscarEvaluaciones.class.getResource("/vista/Imgs/cancelar.png")));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldBusq.setText("");
				textFieldBusq.requestFocus();
			}
		});
		panel_9.add(btnNewButton);
		textFieldBusq.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				JTextField text = (JTextField) e.getSource();
				text.setSelectionStart(0);
				text.setSelectionEnd(text.getText().length());
			}
		});
		textFieldBusq.getDocument().addDocumentListener(new DocumentListener() {
			private String getExp() {
				if (textFieldBusq.hasFocus()) {
					return ".*(?i)".concat(textFieldBusq.getText());
				}
				return ".*(?i)";
			}

			private void methodz() {
				if (trs != null) {
					trs.setRowFilter(RowFilter.regexFilter(getExp()));
				}

			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				methodz();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				methodz();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				methodz();
			}
		});
		textFieldBusq.setEnabled(false);

		JPanel panel_10 = new JPanel();
		panel_10.setBackground(new Color(153, 153, 153));
		panel_8.add(panel_10, BorderLayout.NORTH);

		JButton btnGenerarPdf = new JButton("Generar PDF");
		btnGenerarPdf.setFont(new Font("Calibri", Font.BOLD, 10));
		btnGenerarPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Vector<?> data = modelo.getDataVector();
				Object[][] dat = new Object[data.size()][];
				for (int i = 0; i < data.size(); i++) {
					Object[] je = ((Vector<?>) data.get(i)).toArray();
					dat[i] = je;
				}
				EstructuraLista estruct = new EstructuraLista();
				estruct.setDepartamento("DEPARTAMENTO DE SISTEMAS Y COMPUTACION");
				estruct.setAlumnos(modelo.getRowCount());
				estruct.setClave("dasfas");
				estruct.setPeriodo(Vista.periodo);
				Grupo g = new Grupo(grupoMateria.getNombreGrupo());
				estruct.setGrupo(g);
				estruct.setProfesor(Vista.docente);
				estruct.setMateria(materia);
				File archivo = new File("C:\\Users\\JORGE\\Desktop\\arcvhi.pdf");
				String[] names = new String[modelo.getColumnCount()];
				for (int i = 0; i < names.length; i++) {
					names[i] = modelo.getColumnName(i);
				}
				GeneradorPDF.createPDF(dat, estruct, "Promedios Finales", archivo, names);
			}
		});
		panel_10.add(btnGenerarPdf);
		setSize(1100, 800);
		initComponents();
		eventoTecla(tabla);
		eventoTecla(comboGrupoMateria);
		eventoTecla(comboCriterio);
		eventoTecla(comboUnidad);
		eventoTecla(chckbxMostarCriterios);
		eventoTecla(chckbxMostrarRevisionesDe);

	}

	private void eventoTecla(Component comp) {

		comp.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && e.getKeyCode() == 70) {
					textFieldBusq.requestFocus();
				}
			}
		});
	}

	private void initComponents() {

		String[] cabecera = { "#", "Numero de Control", "Nombre del Alumno" };
		tabla = new JTable();
		tabla.setBackground(new Color(204, 204, 204));
		tabla.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tabla.setRowHeight(25);

		scroll = new JScrollPane(tabla);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		tabla.setFillsViewportHeight(true);
		modelo = new DefaultTableModel() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int f, int c) {
				return false;
			}

		};

		tabla.setModel(modelo);
		scroll.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				setColums();
			}
		});
		modelo.setColumnIdentifiers(cabecera);

		add(scroll, BorderLayout.CENTER);
		setVisible(true);
	}

	private void añadirLista(String gpo) {
		borrarcols();
		sql = "select a.cve_alum, a.nom_alum from alumno a join  "
				+ "(select cve_alum from grupomatealum gma where gma.cve_grumat = " + gpo + ")"
				+ " as j on j.cve_alum = a.cve_alum  order by nom_alum ";

		modelo.setRowCount(0);

		try {
			statement = conexion.prepareStatement(sql);
			ResultSet result = statement.executeQuery();

			for (int i = 1; result.next(); i++) {
				modelo.addRow(new Object[] { i, result.getString(1), result.getString(2) });
			}
			trs = new TableRowSorter<DefaultTableModel>(modelo);
			tabla.setRowSorter(trs);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void añadirCriterio(CriterioUnidad cu) {
		Revision[] revisiones = tablaRevision.getRevisionesCriterio(cu);
		if (chckbxMostrarRevisionesDe.isSelected()) {

			for (int j = 0; j < revisiones.length; j++) {
				sql = "select ifnull(y.calf_renRev,-1) from (select * from alumno where cve_alum in(select cve_alum from grupomatealum where cve_grumat = "
						+ grupoMateria.getClaveGrupoMateria()
						+ ")) as x left join (select cve_alum, calf_renRev from renglonrevision rr where rr.cve_rev = '"
						+ revisiones[j].getCveRev() + "') as y on y.cve_alum = x.cve_alum order by x.nom_alum ";
				añadirColumna(sql, revisiones[j].getNomRev());
			}
		}
		sql = "select sum(ifnull(y.calf_renRev,-1))/" + revisiones.length
				+ " from (select * from alumno where cve_alum in(select cve_alum from grupomatealum where cve_grumat = "
				+ grupoMateria.getClaveGrupoMateria()
				+ ")) as x left join (select cve_alum, calf_renRev from renglonrevision rr where rr.cve_rev in (select cve_rev from revision where cve_criuni = "
				+ cu.getClave() + ")) as y on y.cve_alum = x.cve_alum group by x.cve_alum order by x.nom_alum ";
		Criterio c = new Criterio();
		c.setClave(cu.getCvecri());
		añadirColumna(sql, tablaCriterio.getCriterio(c).getNombre());
	}

	private void añadirUnidad(Unidad u) {
		CriterioUnidad criterios[] = tablaRevision.getEvaluacionesRealizadas(grupoMateria, u);

		if (chckbxMostarCriterios.isSelected()) {

			for (int i = 0; i < criterios.length; i++) {
				Revision[] revisiones = tablaRevision.getRevisionesCriterio(criterios[i]);
				if (chckbxMostrarRevisionesDe.isSelected()) {

					for (int j = 0; j < revisiones.length; j++) {
						sql = "select ifnull(y.calf_renRev,-1) from (select * from alumno where cve_alum in(select cve_alum from grupomatealum where cve_grumat = "
								+ grupoMateria.getClaveGrupoMateria()
								+ ")) as x left join (select cve_alum, calf_renRev from renglonrevision rr where rr.cve_rev = '"
								+ revisiones[j].getCveRev() + "') as y on y.cve_alum = x.cve_alum order by x.nom_alum ";
						añadirColumna(sql, revisiones[j].getNomRev());
					}
				}

				sql = "select ((sum(ifnull(y.calf_renRev,-1))/" + revisiones.length + ")/100)*"
						+ criterios[i].getValor()
						+ " from (select * from alumno where cve_alum in(select cve_alum from grupomatealum where cve_grumat = "
						+ grupoMateria.getClaveGrupoMateria()
						+ ")) as x left join (select cve_alum, calf_renRev from renglonrevision rr where rr.cve_rev in (select cve_rev from revision where cve_criuni = "
						+ criterios[i].getClave()
						+ ")) as y on y.cve_alum = x.cve_alum group by x.cve_alum order by x.nom_alum ";
				Criterio c = new Criterio();
				c.setClave(criterios[i].getCvecri());
				c = tablaCriterio.getCriterio(c);
				añadirColumna(sql, "Total " + c.getNombre());
			}
		}

		if (criterios.length > 0) {
			sql = "select ifnull(sum(y.prom),0)"
					+ " from (select * from alumno where cve_alum in(select cve_alum from grupomatealum where cve_grumat = "
					+ grupoMateria.getClaveGrupoMateria()
					+ ")) as x left join (select rr.cve_alum, ((sum(rr.calf_renRev)/revisiones)/100)*cu.valor_cri prom from renglonrevision rr join revision r on r.cve_rev = rr.cve_rev join criteriounidad cu on cu.cve_criuni = r.cve_criuni join (select cve_criuni, count(*) revisiones from revision where cve_criuni in(select cve_criuni from criteriounidad where cve_uni = "
					+ u.getClave()
					+ ") group by cve_criuni)as rev on rev.cve_criuni = cu.cve_criuni where cu.cve_uni = "
					+ u.getClave()
					+ " group by cu.cve_criuni,  rr.cve_alum) as y on x.cve_alum = y.cve_alum group by x.cve_alum order by x.nom_alum";
			añadirColumna(sql, "Promedio Unidad " + u.getNumeroUnidad());
		}

	}

	private void añadirPromedioPeriodo(Materia mat) {
		Unidad[] units = tablaMateria.getUnidades(mat);
		Integer[] pro = new Integer[modelo.getRowCount()];
		for (int i = 0; i < pro.length; i++) {
			pro[i] = 0;
		}
		for (int i = 0; i < units.length; i++) {
			sql = "select ifnull(sum(y.prom),0) from (select * from alumno where cve_alum in(select cve_alum from grupomatealum where cve_grumat = "
					+ grupoMateria.getClaveGrupoMateria()
					+ ")) as x left join (select rr.cve_alum, ((sum(rr.calf_renRev)/revisiones)/100)*cu.valor_cri prom from renglonrevision rr join revision r on r.cve_rev = rr.cve_rev join criteriounidad cu on cu.cve_criuni = r.cve_criuni join (select cve_criuni, count(*) revisiones from revision where cve_criuni in(select cve_criuni from criteriounidad where cve_uni = "
					+ units[i].getClave()
					+ ") group by cve_criuni)as rev on rev.cve_criuni = cu.cve_criuni where cu.cve_uni = "
					+ units[i].getClave()
					+ " group by cu.cve_criuni,  rr.cve_alum) as y on x.cve_alum = y.cve_alum group by x.cve_alum order by x.nom_alum";

			try {
				statement = conexion.prepareStatement(sql);
				ResultSet result = statement.executeQuery();

				for (int j = 0; result.next(); j++) {
					int dato = 0;
					try {
						dato = (int) result.getDouble(1);
					} finally {
						pro[j] += dato;
					}

				}

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		for (int j = 0; j < modelo.getRowCount(); j++) {
			pro[j] = pro[j] / units.length;
		}
		modelo.addColumn("Promedio Final", pro);
		setColums();
		trs = new TableRowSorter<DefaultTableModel>(modelo);
		tabla.setRowSorter(trs);
	}

	private void añadirColumna(String sql, String nombre) {

		try {
			statement = conexion.prepareStatement(sql);
			ResultSet result = statement.executeQuery();

			List<Object> lista = new ArrayList<>();

			while (result.next()) {
				lista.add(result.getInt(1) == -1 ? -0 : result.getInt(1));
			}
			modelo.addColumn(nombre, lista.toArray(new Object[lista.size()]));

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		setColums();
		trs = new TableRowSorter<DefaultTableModel>(modelo);
		tabla.setRowSorter(trs);
	}

	private void borrarcols() {
		if (bajaAlumnos == null && grupoMateria != null) {
			bajaAlumnos = tablaPaseLista.getAlumnosConFaltas(grupoMateria);
		}
		tabla.setDefaultRenderer(Object.class, new RenderTabla(tablaPaseLista.getAlumnosConFaltas(grupoMateria)));
		tabla.setDefaultRenderer(Integer.class, new RenderTabla(tablaPaseLista.getAlumnosConFaltas(grupoMateria)));
		modelo = new DefaultTableModel() {

			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int c) {
				if (c == 2)
					return String.class;
				return Integer.class;
			}

			@Override
			public boolean isCellEditable(int f, int c) {
				return false;
			}

		};
		modelo.setColumnIdentifiers(new String[] { "#", "Numero de Control", "Nombre del Alumno" });
		tabla.setModel(modelo);
		trs = new TableRowSorter<DefaultTableModel>(modelo);
		tabla.setRowSorter(trs);

	}

	private void setColums() {

		TableColumn column = null;
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		int tamaño = scroll.getSize().width;
		column = tabla.getColumnModel().getColumn(0);
		column.setMinWidth(70);
		column = tabla.getColumnModel().getColumn(1);
		column.setMinWidth(120);
		column = tabla.getColumnModel().getColumn(2);
		column.setMinWidth(350);
		tamaño = tamaño - 630;
		tamaño = tamaño / ((modelo.getColumnCount() - 4) != 0 ? (modelo.getColumnCount() - 4) : 1);
		if (modelo.getColumnCount() == 3 || modelo.getColumnCount() == 4) {
			tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		} else {
			for (int i = 3; i < modelo.getColumnCount() - 1; i++) {
				column = tabla.getColumnModel().getColumn(i);
				column.setMinWidth(50);
				column.setPreferredWidth(tamaño);
			}
			column = tabla.getColumnModel().getColumn(tabla.getColumnCount() - 1);
			column.setMinWidth(80);
			column.setPreferredWidth(80);
		}

	}

	public JButton getBotonSalir() {
		return cancelButton;
	}

	private void removerItems(JComboBox<String> combo) {
		combo.removeAllItems();
		combo.addItem("--Seleccione--");
	}

}
