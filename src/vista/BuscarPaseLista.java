package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import com.placeholder.PlaceHolder;
import com.toedter.calendar.JDateChooser;

import modelo.GrupoMateria;
import modelo.PaseListaAlumno;
import tablas.TablaGrupoMateria;
import tablas.TablaMateria;
import tablas.TablaPaseLista;
import tablas.TablaPaseListaAlumno;

public class BuscarPaseLista extends JPanel {

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
	private JComboBox<String> comboGrupoMateria;
	private JComboBox<String> comboFecha;
	private TablaPaseLista paseLista;
	private JDateChooser dateChooserI;
	private JDateChooser dateChooserF;
	private GrupoMateria[] gruposMat;
	private JButton buttonAnt;
	private JButton buttonSig;
	private LocalDate[] fechas;
	private GrupoMateria gpo;
	private DateTimeFormatter dTF;
	private TablaGrupoMateria tablaGrupoMateria;
	private TablaMateria tablaMateria;
	private JPanel panel;
	private JPanel panel_1;
	private JLabel lblPaseDeLista;
	private LocalDate fecha;
	private JButton botonBusquedaPeriodo;
	private JPanel panelSubP;
	private JPanel panelOpcionesPaseLista;
	private SimpleDateFormat sDF;
	private JCheckBox checkPeriodo;
	private JPanel panel_2;
	private JTextField textFieldBusq;
	private JPanel panel_3;
	private TableRowSorter<DefaultTableModel> trs;
	private boolean posibleSaldia;
	private JButton acpt;
	private JButton cancel;
	private Long[] bajaAlumnos;
	private JPanel panel_4;
	private boolean modificar;
	private JCheckBox chckbxNewCheckBox;
	private JButton botonBorrar;

	public BuscarPaseLista(Connection conexion, Container contentPane) {
		this.conexion = conexion;
		tablaGrupoMateria = new TablaGrupoMateria(conexion);
		tablaMateria = new TablaMateria(conexion);
		dTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		sDF = new SimpleDateFormat("yyyy-MM-dd");
		setLayout(new BorderLayout());
		bajaAlumnos = null;
		paseLista = new TablaPaseLista(conexion);
		gruposMat = tablaGrupoMateria.getCurrentGroups();

		JPanel panelInferior = new JPanel();
		panelInferior.setBackground(new Color(102, 153, 153));
		panelOpcionesPaseLista = new JPanel();
		panelOpcionesPaseLista.setBackground(new Color(153, 204, 204));
		panelOpcionesPaseLista.setBorder(new TitledBorder(null, "Seleccion de Fecha", TitledBorder.LEADING,
				TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 15), null));

		acpt = new JButton("Aceptar");
		acpt.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cancel = new JButton("Cancelar");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				contentPane.removeAll();
				contentPane.repaint();
				contentPane.setVisible(true);
			}
		});
		cancel.setFont(new Font("Tahoma", Font.PLAIN, 15));

		panelInferior.add(acpt);
		panelInferior.add(cancel);

		acpt.addActionListener(x -> {
			textFieldBusq.setText("");
			int seleccion = comboGrupoMateria.getSelectedIndex();
			if (seleccion > 0 && modelo.getRowCount() > 0) {

				generarPaseLista(gpo);
				modelo.setRowCount(0);
				contentPane.removeAll();
				contentPane.repaint();
				contentPane.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(null, "Seleccione un Grupo Valido");
			}
		});

		add(panelInferior, BorderLayout.SOUTH);

		setSize(1100, 800);
		initComponents();
		eventoTecla(tabla);
		eventoTecla(comboGrupoMateria);
		eventoTecla(comboFecha);
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

	private String getFechaFormateada(LocalDate fecha) {
		Month mes = fecha.getMonth();
		DayOfWeek dia = fecha.getDayOfWeek();
		Locale lo = new Locale("es", "ES");
		return dia.getDisplayName(TextStyle.FULL, lo).concat(" ") + fecha.getDayOfMonth()
				+ " de ".concat(mes.getDisplayName(TextStyle.FULL, lo) + " " + (fecha.getYear()));
	}

	private void initComponents() {

		tabla = new JTable();
		tabla.setBackground(new Color(204, 204, 204));
		tabla.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tabla.setRowHeight(25);
		scroll = new JScrollPane(tabla);
		tabla.setFillsViewportHeight(true);

		crearModelo();
		// tabla.setAutoCreateRowSorter(true);

		add(scroll, BorderLayout.CENTER);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		tabla.getColumnModel().getColumn(0).setCellRenderer(tcr);

		// TableColumn check = tabla.getColumnModel().getColumn(3);
		// JCheckBox checkbox = new JCheckBox();
		// check.setCellEditor(new DefaultCellEditor(checkbox));

		panel = new JPanel();
		panel.setBackground(new Color(153, 204, 204));
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		JPanel panelSuperior = new JPanel();
		panelSuperior.setBackground(new Color(153, 204, 204));
		panel.add(panelSuperior, BorderLayout.NORTH);
		panelSuperior.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		dateChooserI = new JDateChooser();

		JPanel panelPeriodos = new JPanel(new BorderLayout());
		panelPeriodos.setBackground(new Color(153, 204, 204));
		panelPeriodos.setBorder(new TitledBorder(null, "Busqueda Por Periodo", TitledBorder.LEADING, TitledBorder.TOP,
				new Font("Tahoma", Font.PLAIN, 15), null));

		checkPeriodo = new JCheckBox("Habilitar Busqueda Por Periodo");
		checkPeriodo.setBackground(new Color(153, 204, 204));
		checkPeriodo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelPeriodos.add(checkPeriodo, BorderLayout.NORTH);

		checkPeriodo.addActionListener(x -> {
			if (checkPeriodo.isSelected()) {
				if (panelSubP == null) {
					panelSubP = new JPanel();
					panelSubP.setBackground(new Color(153, 204, 204));
					dateChooserI = new JDateChooser();
					dateChooserI.setFont(new Font("Tahoma", Font.PLAIN, 16));
					dateChooserI.setDateFormatString("yyyy-MM-dd");
					dateChooserI.setBorder(new TitledBorder(null, "Desde:", TitledBorder.LEADING, TitledBorder.TOP,
							new Font("Tahoma", Font.PLAIN, 16), null));

					dateChooserF = new JDateChooser();
					dateChooserF.setFont(new Font("Tahoma", Font.PLAIN, 16));
					dateChooserF.setDateFormatString("yyyy-MM-dd");
					dateChooserF.setBorder(new TitledBorder(null, "Hasta:", TitledBorder.LEADING, TitledBorder.TOP,
							new Font("Tahoma", Font.PLAIN, 16), null));

					JPanel centroSubP = new JPanel(new GridLayout(1, 2));
					centroSubP.setBackground(new Color(153, 204, 204));
					centroSubP.add(dateChooserI);
					centroSubP.add(dateChooserF);
					panelSubP.add(centroSubP);
					JCheckBox checkBusquedaPeriodo = new JCheckBox("Buscar Por Periodo Completo");
					checkBusquedaPeriodo.setFont(new Font("Tahoma", Font.PLAIN, 16));
					checkBusquedaPeriodo.setBackground(new Color(153, 204, 204));
					checkBusquedaPeriodo.addActionListener(y -> {
						if (checkBusquedaPeriodo.isSelected()) {
							dateChooserF.setEnabled(false);
							dateChooserI.setEnabled(false);
						} else {
							dateChooserF.setEnabled(true);
							dateChooserI.setEnabled(true);
						}
					});
					panelSubP.add(checkBusquedaPeriodo);
					botonBusquedaPeriodo = new JButton("Buscar");
					botonBusquedaPeriodo.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panelSubP.add(botonBusquedaPeriodo);
					botonBusquedaPeriodo.addActionListener(z -> {
						if (!checkBusquedaPeriodo.isSelected()) {
							if (dateChooserI.getDate() != null) {
								if (dateChooserF.getDate() != null) {
									LocalDate desde = LocalDate.parse(sDF.format(dateChooserI.getDate()).toString(),
											dTF);
									LocalDate hasta = LocalDate.parse(sDF.format(dateChooserF.getDate()).toString(),
											dTF);
									if (desde.isBefore(LocalDate.now())
											&& (hasta.isBefore(LocalDate.now()) || hasta.isEqual(LocalDate.now()))) {

										if (desde.isBefore(hasta)) {
											if (hasta.isAfter(desde.plus(2, ChronoUnit.DAYS))) {
												fechas = getFechasPeriodo(desde, hasta, gpo);
												if (fechas.length > 0) {
													añadirLista(gpo, fechas);
												} else
													JOptionPane.showMessageDialog(null,
															"No se ha Pasado Lista en El periodo", "Error",
															JOptionPane.ERROR_MESSAGE);

											} else {
												JOptionPane.showMessageDialog(null,
														"La Fecha Inicial Debe ser por lo Menos dos Dias Menor a la Final",
														"Error", JOptionPane.ERROR_MESSAGE);
											}
										} else {
											JOptionPane.showMessageDialog(null,
													"La Fecha Inicial Debe ser Menor a la Final", "Error",
													JOptionPane.ERROR_MESSAGE);
										}
									} else {
										JOptionPane.showMessageDialog(null, "Seleccione Fechas Menores a la Actual",
												"Error", JOptionPane.ERROR_MESSAGE);
									}
								} else {
									JOptionPane.showMessageDialog(null, "Seleccione una Fecha de Final", "Error",
											JOptionPane.ERROR_MESSAGE);
								}
							} else {
								JOptionPane.showMessageDialog(null, "Seleccione una Fecha de Inicio", "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						} else {
							fechas = fechasLista(gpo);
							if (fechas.length > 0) {
								añadirLista(gpo, fechas);
							} else
								JOptionPane.showMessageDialog(null, "No se ha Pasado Lista en El periodo", "Error",
										JOptionPane.ERROR_MESSAGE);
						}
					});
				}

				panelPeriodos.add(panelSubP, BorderLayout.CENTER);
				panelSuperior.remove(panelOpcionesPaseLista);
			} else {
				panelSuperior.remove(panelPeriodos);
				panelSuperior.add(panelOpcionesPaseLista);
				panelSuperior.add(panelPeriodos);
				panelPeriodos.remove(panelSubP);
			}
			scroll.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent componentEvent) {
					if (modelo.getColumnCount() >= 4) {
						setColums();
					}

				}
			});
			validate();
		});

		panel_4 = new JPanel();
		panel_4.setBackground(new Color(153, 204, 204));
		panel_4.setBorder(new TitledBorder(null, "Seleccione Grupo y Materia", TitledBorder.LEADING, TitledBorder.TOP,
				new Font("Tahoma", Font.PLAIN, 16), null));
		panelSuperior.add(panel_4);

		comboGrupoMateria = new JComboBox<>();
		panel_4.add(comboGrupoMateria);
		comboGrupoMateria.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboGrupoMateria.addItem("--Seleccione--");

		comboGrupoMateria.addActionListener(x -> {
			int seleccion = comboGrupoMateria.getSelectedIndex();
			if (seleccion > 0) {
				modelo.setRowCount(0);
				gpo = gruposMat[seleccion - 1];
				checkPeriodo.setEnabled(true);
				fechas = fechasLista(gpo);
				if (fechas.length > 0) {
					limpiarCombo(comboFecha);
					for (LocalDate localDate : fechas) {
						comboFecha.addItem(getFechaFormateada(localDate));
					}
					if (fechas.length == 1) {
						fecha = fechas[0];
						añadirLista(gpo, fecha);
						establecerFechaLabel();
						validateButtonList();
						comboFecha.setSelectedIndex(1);
					}
					comboFecha.setEnabled(true);

				} else {
					JOptionPane.showMessageDialog(null, "No Hay Pases de Lista Registrados", "Error",
							JOptionPane.ERROR_MESSAGE);
					fechas = null;
					comboFecha.setEnabled(false);
					checkPeriodo.setEnabled(false);
					textFieldBusq.setEnabled(false);
				}
				bajaAlumnos = null;
			} else {
				checkPeriodo.setEnabled(false);
				textFieldBusq.setEnabled(false);
			}

			validateButtonList();
		});

		comboFecha = new JComboBox<String>();
		comboFecha.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboFecha.setEnabled(false);
		comboFecha.addItem("--Seleccione Grupo--");

		panelOpcionesPaseLista.add(comboFecha);

		for (GrupoMateria gpo : gruposMat) {
			comboGrupoMateria.addItem(
					tablaMateria.getMateria(gpo.getCveMateria()).getNombre().concat(" ").concat(gpo.getNombreGrupo()));
		}

		buttonAnt = new JButton("<--");
		buttonAnt.setFont(new Font("Tahoma", Font.PLAIN, 15));
		buttonAnt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buscarFechas(1);
			}
		});
		panelOpcionesPaseLista.add(buttonAnt);

		buttonSig = new JButton("-->");
		buttonSig.setFont(new Font("Tahoma", Font.PLAIN, 15));
		buttonSig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarFechas(2);
			}
		});
		panelOpcionesPaseLista.add(buttonSig);

		panelSuperior.add(panelOpcionesPaseLista);
		panelSuperior.add(panelPeriodos);

		panel_1 = new JPanel();
		panel_1.setBackground(new Color(153, 204, 204));
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		panel_2 = new JPanel();
		panel_2.setBackground(new Color(153, 204, 204));
		panel_1.add(panel_2, BorderLayout.EAST);

		textFieldBusq = new JTextField();
		textFieldBusq.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldBusq.setActionCommand("alt\r\nf\r\n");
		new PlaceHolder(textFieldBusq, "Numero de Control o Nombre");
		panel_2.add(textFieldBusq);
		textFieldBusq.setColumns(25);
		textFieldBusq.getDocument().addDocumentListener(new DocumentListener() {
			private String getExp() {
				if (textFieldBusq.hasFocus()) {
					return ".*(?i)".concat(textFieldBusq.getText());
				}
				return ".*(?i)";
			}

			private void methodz() {
				trs.setRowFilter(RowFilter.regexFilter(getExp()));
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

		textFieldBusq.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				JTextField text = (JTextField) e.getSource();
				text.setSelectionStart(0);
				text.setSelectionEnd(text.getText().length());
			}
		});
		panel_3 = new JPanel();
		panel_3.setBackground(new Color(153, 204, 204));
		panel_1.add(panel_3, BorderLayout.CENTER);

		lblPaseDeLista = new JLabel();
		lblPaseDeLista.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_3.add(lblPaseDeLista);
		checkPeriodo.setEnabled(false);

		chckbxNewCheckBox = new JCheckBox("Modificar Fechas");
		chckbxNewCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		chckbxNewCheckBox.setBackground(new Color(153, 204, 204));
		panelSuperior.add(chckbxNewCheckBox);
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxNewCheckBox.isSelected()) {
					modificar = true;
				} else {
					modificar = false;
				}
			}
		});
		textFieldBusq.setEnabled(false);

		botonBorrar = new JButton("");
		botonBorrar.setContentAreaFilled(false);
		botonBorrar.setBorderPainted(false);
		botonBorrar.setIcon(new ImageIcon(BuscarPaseLista.class.getResource("/vista/Imgs/cancelar.png")));
		botonBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldBusq.setText("");
				textFieldBusq.requestFocus();
			}
		});
		botonBorrar.setPreferredSize(new Dimension(20, 20));
		panel_2.add(botonBorrar);

		comboFecha.addActionListener(x -> {

			int seleccion = comboFecha.getSelectedIndex();
			if (seleccion > 0) {
				fecha = fechas[seleccion - 1];
				añadirLista(gpo, fecha);
				establecerFechaLabel();

				validateButtonList();
			} else
				textFieldBusq.setEnabled(false);
		});
		validateButtonList();
		setVisible(true);

	}

	private void limpiarCombo(JComboBox<String> combo) {
		combo.removeAllItems();
		combo.addItem("Seleccione");
	}

	private void validateButtonList() {
		if (fechas != null) {
			if (fechas.length > 1) {
				int n = comboFecha.getSelectedIndex() - 1;
				if (n > 0) {
					buttonAnt.setEnabled(true);
				} else
					buttonAnt.setEnabled(false);
				if (n < fechas.length - 1) {
					buttonSig.setEnabled(true);
				} else
					buttonSig.setEnabled(false);
			} else {
				buttonAnt.setEnabled(false);
				buttonSig.setEnabled(false);
			}
		} else {
			buttonAnt.setEnabled(false);
			buttonSig.setEnabled(false);
		}
	}

	private void buscarFechas(int arg) {
		if (fechas != null) {
			int pivote = comboFecha.getSelectedIndex() - 1;
			if (arg == 1) {
				if (pivote > 0) {
					añadirLista(gpo, fechas[--pivote]);
				}

			} else if (pivote + 1 < fechas.length) {
				añadirLista(gpo, fechas[++pivote]);
			}
			comboFecha.setSelectedIndex(pivote + 1);
			fecha = fechas[pivote];
			establecerFechaLabel();
			validateButtonList();
		}

	}

	private void establecerFechaLabel() {
		lblPaseDeLista.setText("Pase de Lista Correspondiente al: " + getFechaFormateada(fecha));
	}

	private LocalDate[] fechasLista(GrupoMateria gpo) {

		sql = "select fecha_palis from paselista pl, grupomateria gm where pl.cve_grumat = gm.cve_grumat and gm.cve_per = (select max(cve_per) from periodo) and pl.cve_grumat = "
				+ gpo.getClaveGrupoMateria();

		try {
			statement = conexion.prepareStatement(sql);
			ResultSet result = statement.executeQuery();

			List<LocalDate> fechas = new ArrayList<LocalDate>();

			while (result.next()) {
				fechas.add(LocalDate.parse(result.getString(1)));
			}

			return fechas.toArray(new LocalDate[fechas.size()]);

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private LocalDate[] getFechasPeriodo(LocalDate desde, LocalDate hasta, GrupoMateria gpo) {
		sql = "select fecha_palis from paselista where fecha_palis >'" + desde + "' and fecha_palis < '" + hasta
				+ "' and cve_grumat = " + gpo.getClaveGrupoMateria();
		try {
			statement = conexion.prepareStatement(sql);
			ResultSet result = statement.executeQuery();

			List<LocalDate> fechas = new ArrayList<LocalDate>();

			while (result.next()) {
				fechas.add(LocalDate.parse(result.getString(1)));
			}

			return fechas.toArray(new LocalDate[fechas.size()]);

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void generarPaseLista(GrupoMateria gpo) {

		if (modelo.getRowCount() > 0) {
			TablaPaseListaAlumno paseLisAlum = new TablaPaseListaAlumno(conexion);
			modelo.PaseLista lis = new modelo.PaseLista();
			lis.setGruMat(gpo.getClaveGrupoMateria());
			if (tabla.getColumnCount() == 4) {

				lis.setFecha(fecha.format(dTF));

				lis = paseLista.buscar(lis);

				for (int i = 0; i < modelo.getRowCount(); i++) {
					PaseListaAlumno paseAl = new PaseListaAlumno();
					paseAl.setAlumno(modelo.getValueAt(i, 1).toString());
					paseAl.setPaseLista(lis.getClave());
					paseAl.setEstatus(
							Boolean.parseBoolean(modelo.getValueAt(i, modelo.getColumnCount() - 1).toString()) == true
									? "1"
									: "0");
					if (paseLisAlum.existe(paseAl)) {
						paseLisAlum.modificar(paseAl);
					} else
						paseLisAlum.agregar(paseAl);

				}
			} else {

				for (int i = 0; i < fechas.length; i++) {
					lis.setFecha(fechas[i].format(dTF));
					lis = paseLista.buscar(lis);
					for (int j = 0; j < modelo.getRowCount(); j++) {
						PaseListaAlumno paseAl = new PaseListaAlumno();
						paseAl.setAlumno(modelo.getValueAt(j, 1).toString());
						paseAl.setPaseLista(lis.getClave());
						paseAl.setEstatus(
								Boolean.parseBoolean(modelo.getValueAt(j, i + 3).toString()) == true ? "1" : "0");
						if (paseLisAlum.existe(paseAl)) {
							paseLisAlum.modificar(paseAl);
						} else
							paseLisAlum.agregar(paseAl);

					}
				}

			}

			JOptionPane.showMessageDialog(null, "Actualizada");

		} else
			JOptionPane.showMessageDialog(null, "Selecciona Una Lista");

	}

	private void añadirLista(GrupoMateria gpo, LocalDate... fecha) {

		modelo.setRowCount(0);
		textFieldBusq.setEnabled(true);
		textFieldBusq.setText("");
		new PlaceHolder(textFieldBusq, "Numero de Control o Nombre");
		crearModelo();
		if (checkPeriodo.isSelected()) {
			lblPaseDeLista.setText("");
		}

		try {
			sql = "select a.cve_alum, a.nom_alum from alumno a join grupomatealum gma on gma.cve_alum = a.cve_alum where gma.cve_grumat = "
					+ gpo.getClaveGrupoMateria() + " order by a.nom_alum";
			statement = conexion.prepareStatement(sql);
			ResultSet result = statement.executeQuery(sql);
			for (int i = 1; result.next(); i++) {
				modelo.addRow(new Object[] { i, result.getString(1), result.getString(2) });

			}

			for (int i = 0; i < fecha.length; i++) {
				sql = "select ifnull(estatus_palisalu,0) from (select pas.cve_alum,  pas.estatus_palisalu, al.nom_alum from paselista pa join paselistaalumno pas on pas.cve_palis"
						+ " = pa.cve_palis join alumno al on al.cve_alum = pas.cve_alum " + "where pa.fecha_palis = '"
						+ fecha[i].format(dTF) + "' and pa.cve_grumat = '" + gpo.getClaveGrupoMateria() + "' "
						+ ") as c1 right join (select a.cve_alum, a.nom_alum from grupomatealum g join alumno a on a.cve_alum = g.cve_alum where cve_grumat = "
						+ gpo.getClaveGrupoMateria() + ") as gma on gma.cve_alum = c1.cve_alum order by gma.nom_alum";
				statement = conexion.prepareStatement(sql);
				result = statement.executeQuery();
				Object[] columnData = new Object[modelo.getRowCount()];
				for (int j = 0; result.next(); j++) {
					Object estatus = new Object();
					estatus = new Boolean(result.getInt(1) == 1 ? true : false);
					columnData[j] = estatus;
				}
				modelo.addColumn(fecha[i], columnData);
			}

			if (fecha.length > 1) {
				sql = "select sum(pl.estatus_palisalu) from paselistaalumno pl join alumno a on a.cve_alum =pl.cve_alum where pl.cve_palis in(select cve_palis from paselista where fecha_palis >='"
						+ fecha[0] + "' and fecha_palis <= '" + fecha[fecha.length - 1] + "' and cve_grumat = "
						+ gpo.getClaveGrupoMateria() + ") group by pl.cve_alum" + " order by a.nom_alum";
				statement = conexion.prepareStatement(sql);
				result = statement.executeQuery();
				Object[] columnData = new Object[modelo.getRowCount()];
				for (int j = 0; result.next(); j++) {
					columnData[j] = result.getInt(1) + " de " + fecha.length;
				}
				modelo.addColumn("Asistencias", columnData);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		setColums();
	}

	public boolean isPosibleSaldia() {
		return posibleSaldia;
	}

	public JButton getAcpt() {
		return acpt;
	}

	public JButton getCancel() {
		return cancel;
	}

	private void crearModelo() {
		String[] cabecera = { "#", "Numero de Control", "Nombre del Alumno" };

		if (bajaAlumnos == null && gpo != null) {
			bajaAlumnos = paseLista.getAlumnosConFaltas(gpo);
		}
		modelo = new DefaultTableModel() {

			private static final long serialVersionUID = 1L;

			Class<?> tipos[] = { Integer.class, Integer.class, String.class };

			@Override
			public Class<?> getColumnClass(int c) {
				if (c < tipos.length) {
					return tipos[c];
				}
				if ((c == modelo.getColumnCount() - 1) && modelo.getColumnCount() > 4) {
					return String.class;
				}
				return Boolean.class;
			}

			@Override
			public boolean isCellEditable(int f, int c) {
				if (c < 3 || (modelo.getColumnCount() > 4 && c == modelo.getColumnCount() - 1)) {
					return false;
				}
				if (modificar) {
					return true;
				}
				return false;

			}

		};
		modelo.setColumnIdentifiers(cabecera);
		trs = new TableRowSorter<DefaultTableModel>(modelo);
		RenderTabla miRender = new RenderTabla(bajaAlumnos);
		RenderTablaCheck renderCheck = new RenderTablaCheck(bajaAlumnos);
		tabla.setDefaultRenderer(Integer.class, miRender);
		tabla.setDefaultRenderer(Object.class, miRender);
		tabla.setDefaultRenderer(Boolean.class, renderCheck);
		tabla.setRowSorter(trs);
		tabla.setModel(modelo);

	}

	private void setColums() {

		TableColumn column = null;
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		int tamaño = scroll.getSize().width;
		column = tabla.getColumnModel().getColumn(0);
		column.setMinWidth(80);
		column = tabla.getColumnModel().getColumn(1);
		column.setMinWidth(130);
		column = tabla.getColumnModel().getColumn(2);
		column.setMinWidth(400);
		tamaño = tamaño - 690;
		tamaño = tamaño / ((modelo.getColumnCount() - 4) != 0 ? (modelo.getColumnCount() - 4) : 1);
		if (modelo.getColumnCount() == 4) {
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
}
