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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import com.ibm.icu.text.SimpleDateFormat;
import com.placeholder.PlaceHolder;

import modelo.Criterio;
import modelo.GrupoMateria;
import modelo.Materia;
import modelo.RenglonRevision;
import modelo.Revision;
import modelo.Unidad;
import tablas.TablaCriterio;
import tablas.TablaPaseLista;
import tablas.TablaRenglonRevision;
import tablas.TablaRevision;

public class CalificarCriterio extends JPanel {

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
	private JLabel labelFech;
	private TablaCriterio tablaCriterio;
	private GrupoMateria g;
	private GrupoMateria gpo;
	private JComboBox<Object> comb;
	private TablaRevision tablaRevison;
	private TablaRenglonRevision tablaRenglonRevision;
	private Object[] arrLimpio;
	private Revision[] revisiones;
	private JTextField textFieldCriterio;
	private JComboBox<String> comboBox;
	private JButton cancel;
	private JButton acpt;
	private boolean sePuedeCerrar;
	private Long[] bajaAlumnos;
	private TablaPaseLista tablaPaseLista;
	private TableRowSorter<DefaultTableModel> trs;
	private JTextField textFieldBusq;

	public CalificarCriterio(Connection conexion, Unidad u, modelo.CriterioUnidad cU, GrupoMateria g, Materia mat,
			Container contenedorPrincipal) {
		gpo = g;
		this.g = g;
		tablaRenglonRevision = new TablaRenglonRevision(conexion);
		tablaPaseLista = new TablaPaseLista(conexion);
		tablaRevison = new TablaRevision(conexion);
		revisiones = tablaRevison.getRevisionesCriterio(cU);
		this.conexion = conexion;
		tablaCriterio = new TablaCriterio(conexion);
		setLayout(new BorderLayout());
		JPanel panelSuperior = new JPanel();
		panelSuperior.setBackground(new Color(153, 204, 204));
		panelSuperior.setBorder(new LineBorder(new Color(0, 0, 0)));
		add(panelSuperior, BorderLayout.NORTH);

		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		Criterio c = new Criterio();
		c.setClave(cU.getCvecri());
		c = tablaCriterio.getCriterio(c);
		panelSuperior.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBackground(new Color(153, 204, 204));
		panelSuperior.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(2, 1, 0, 8));

		JLabel lblNewLabel = new JLabel(mat.getNombre() + " " + g.getNombreGrupo() + " " + sd.format(new Date()));
		lblNewLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 17));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel);
		labelFech = new JLabel("Evaluacion de " + c.getNombre() + " Correspondiente a la Unidad "
				+ u.getNumeroUnidad().concat(" ".concat(u.getNombre())) + " Valor: " + cU.getValor());
		labelFech.setFont(new Font("Bahnschrift", Font.PLAIN, 17));
		labelFech.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(labelFech);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(153, 204, 204));
		panelSuperior.add(panel_1);

		JLabel lblNewLabel_1 = new JLabel("Agregar Nuev@ " + c.getNombre());
		lblNewLabel_1.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		panel_1.add(lblNewLabel_1);

		textFieldCriterio = new JTextField();
		textFieldCriterio.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		panel_1.add(textFieldCriterio);
		textFieldCriterio.setColumns(20);

		new PlaceHolder(textFieldCriterio, c.getNombre());

		textFieldCriterio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Criterio c = new Criterio();
				c.setClave(cU.getCvecri());
				c = tablaCriterio.getCriterio(c);
				if (!textFieldCriterio.getText().isEmpty()) {
					Revision re = new Revision();
					re.setNomRev(textFieldCriterio.getText());
					re.setCveCriUni(cU.getClave());
					if (!tablaRevison.existe(re)) {
						añadirColumna(textFieldCriterio.getText());
						textFieldCriterio.setText(c.getNombre().concat("_" + (modelo.getColumnCount() - 2)));
					} else
						JOptionPane.showMessageDialog(null, "Introduzca un Nombre Diferente para la Revision", "Err",
								JOptionPane.ERROR_MESSAGE);

				} else
					JOptionPane.showMessageDialog(null, "Introduzca al Menos una Letra", "Err",
							JOptionPane.ERROR_MESSAGE);
			}
		});

		JButton btnAgregar = new JButton("Agregar");
		btnAgregar.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		panel_1.add(btnAgregar);
		btnAgregar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Criterio c = new Criterio();
				c.setClave(cU.getCvecri());
				c = tablaCriterio.getCriterio(c);
				if (!textFieldCriterio.getText().isEmpty()) {
					Revision re = new Revision();
					re.setNomRev(textFieldCriterio.getText());
					re.setCveCriUni(cU.getClave());
					if (!tablaRevison.existe(re)) {
						añadirColumna(textFieldCriterio.getText());
						textFieldCriterio.setText(c.getNombre().concat("_" + (modelo.getColumnCount() - 2)));
					} else
						JOptionPane.showMessageDialog(null, "Introduzca un Nombre Diferente para la Revision", "Err",
								JOptionPane.ERROR_MESSAGE);
				} else
					JOptionPane.showMessageDialog(null, "Introduzca al Menos una Letra", "Err",
							JOptionPane.ERROR_MESSAGE);
			}
		});
		textFieldCriterio.setText(c.getNombre().concat("_" + (revisiones.length + 1)));

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(153, 204, 204));
		panel_2.setBorder(new TitledBorder(null, "Eliminar Revision", TitledBorder.LEADING, TitledBorder.TOP,
				new Font("Bahnschrift", Font.PLAIN, 15), null));
		panel_1.add(panel_2);

		comboBox = new JComboBox<>();
		comboBox.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		panel_2.add(comboBox);

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_3.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panel_3.setBackground(new Color(153, 204, 204));
		panelSuperior.add(panel_3, BorderLayout.SOUTH);

		textFieldBusq = new JTextField();

		textFieldBusq = new JTextField();
		textFieldBusq.setFont(new Font("Tahoma", Font.PLAIN, 15));
		new PlaceHolder(textFieldBusq, "Numero de Control o Nombre");
		panel_3.add(textFieldBusq);
		textFieldBusq.setColumns(25);

		JButton button = new JButton("");
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldBusq.setText("");
				textFieldBusq.requestFocus();
			}
		});
		button.setPreferredSize(new Dimension(20, 20));
		button.setIcon(new ImageIcon(VistaBuscarLista.class.getResource("/vista/Imgs/cancelar.png")));
		panel_3.add(button);
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
		comboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (comboBox.getSelectedIndex() > 0) {
					Revision re = new Revision();
					re.setNomRev(comboBox.getSelectedItem().toString());
					re.setCveCriUni(cU.getClave());
					if (tablaRevison.existe(re)) {
						int op = JOptionPane.showConfirmDialog(null,
								"Seguro que Deseas Eliminar " + comboBox.getSelectedItem());
						if (op == JOptionPane.YES_OPTION) {
							re = tablaRevison.buscar(re);
							tablaRenglonRevision.eliminarRenglones(re);
							tablaRevison.eliminar(re);
							borrarColumna(2 + comboBox.getSelectedIndex());
						}

					} else {
						borrarColumna(2 + comboBox.getSelectedIndex());
					}
					setComb();
				}
			}
		});

		JPanel panelInferior = new JPanel();
		panelInferior.setBackground(new Color(153, 204, 204));

		acpt = new JButton("Aceptar");
		acpt.setFont(new Font("Tahoma", Font.PLAIN, 15));
		acpt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textFieldBusq.setText(null);
				if (getCalificacionesAsignadas() > 0) {

					sePuedeCerrar = true;
					for (int c = 3; c < modelo.getColumnCount(); c++) {
						Revision revision = new Revision();
						revision.setCveCriUni(cU.getClave());
						revision.setNomRev(modelo.getColumnName(c));
						revision.setNumRev((short) (c - 2));
						if (!tablaRevison.existe(revision)) {
							tablaRevison.agregar(revision);
						}
						revision = tablaRevison.buscar(revision);

						for (int f = 0; f < modelo.getRowCount(); f++) {
							boolean calificado = tabla.getValueAt(f, c).toString().length() <= 3;
							if (calificado) {
								RenglonRevision rr = new RenglonRevision();
								rr.setCveAlum(tabla.getValueAt(f, 1).toString());
								rr.setCalfRenRev(tabla.getValueAt(f, c).toString());
								rr.setFechaRenRev(LocalDate.now());
								rr.setCveRev(String.valueOf(revision.getCveRev()));
								if (!tablaRenglonRevision.existe(rr)) {
									tablaRenglonRevision.agregar(rr);
								} else if (tabla.getValueAt(f, c).toString().length() > 3) {
									rr.setNumRengRev(tablaRenglonRevision.buscar(rr).getNumRengRev());
									tablaRenglonRevision.eliminar(rr);
								} else {
									rr.setNumRengRev(tablaRenglonRevision.buscar(rr).getNumRengRev());
									tablaRenglonRevision.modificar(rr);
								}
							}
						}
					}
					sePuedeCerrar = true;
					contenedorPrincipal.removeAll();
					contenedorPrincipal.repaint();
					cancel.setText("Salir");
					JOptionPane.showMessageDialog(null, "Calificacon Asignada");

				} else
					JOptionPane.showMessageDialog(null, "No se Asignaron Calificaciones", "Err",
							JOptionPane.ERROR_MESSAGE);

			}
		});
		cancel = new JButton("Cancelar");
		cancel.setFont(new Font("Tahoma", Font.PLAIN, 15));

		panelInferior.add(acpt);
		panelInferior.add(cancel);

		add(panelInferior, BorderLayout.SOUTH);

		setSize(1100, 800);
		initComponents();

	}

	private void setComb() {
		comb = new JComboBox<>();
		comb.addItem("No Calificado");
		for (int i = 0; i < 101; i++) {
			comb.addItem(i);
		}

		tabla.setModel(modelo);
		comboBox.removeAllItems();
		comboBox.addItem("--Seleccione--");
		comb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {

				int valor;
				try {
					valor = Integer
							.parseInt(tabla.getValueAt(tabla.getSelectedRow(), tabla.getSelectedColumn()).toString());
					valor++;
				} catch (Exception e1) {
					valor = 0;
				}
//				System.out.println("combo= " + comb.getSelectedIndex() + " valor: " + valor);
				if (comb.getSelectedIndex() != valor) {
					comb.setSelectedIndex(valor);
				}

			}
		});
		comb.setFont(new Font("Tahoma", Font.PLAIN, 15));
		for (int i = 3; i < modelo.getColumnCount(); i++) {
			TableColumn col = tabla.getColumnModel().getColumn(i);
			col.setCellEditor(new DefaultCellEditor(comb));
			comboBox.addItem(modelo.getColumnName(i));
		}

	}

	private int getCalificacionesAsignadas() {
		int n = 0;
		for (int c = 3; c < modelo.getColumnCount(); c++) {
			for (int f = 0; f < modelo.getRowCount(); f++) {
				try {
					if (Integer.parseInt(tabla.getValueAt(f, c).toString()) > 0) {
						n++;
					}
				} catch (Exception e) {
				}

			}
		}

		return n;
	}

	private void borrarColumna(int col) {
		String[] cabecera = new String[modelo.getColumnCount() - 1];
		Object[][] tableData = new Object[modelo.getRowCount()][modelo.getColumnCount() - 1];
		boolean encontrada = false;
		for (int i = 0; i < tableData.length; i++) {
			for (int j = 0; j < modelo.getColumnCount() - 1; j++) {
				if (j != col && !encontrada) {
					tableData[i][j] = tabla.getValueAt(i, j);
				} else {
					tableData[i][j] = tabla.getValueAt(i, j + 1);
					encontrada = true;
				}
				encontrada = false;
			}
		}
		encontrada = false;
		for (int i = 0; i < modelo.getColumnCount() - 1; i++) {
			if (i != col && !encontrada) {
				cabecera[i] = modelo.getColumnName(i);
			} else {
				cabecera[i] = modelo.getColumnName(i + 1);
				encontrada = true;
			}
		}

		modelo.setDataVector(tableData, cabecera);
		setColums();
	}

	private void crearModelo() {
		String[] cabecera = { "#", "Numero de Control", "Nombre del Alumno" };
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
				return c > 2;
			}

		};

		modelo.setColumnIdentifiers(cabecera);
		tabla.setModel(modelo);
		trs = new TableRowSorter<DefaultTableModel>(modelo);
		tabla.setRowSorter(trs);
	}

	private void initComponents() {

		tabla = new JTable();
		tabla.setBackground(new Color(204, 204, 204));
		tabla.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tabla.setRowHeight(25);
		scroll = new JScrollPane(tabla);
		scroll.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				setColums();
			}
		});
		tabla.setFillsViewportHeight(true);
		crearModelo();
		bajaAlumnos = tablaPaseLista.getAlumnosConFaltas(gpo);
		RenderTabla miRender = new RenderTabla(bajaAlumnos);
		tabla.setDefaultRenderer(Integer.class, miRender);
		tabla.setDefaultRenderer(Object.class, miRender);
		add(scroll, BorderLayout.CENTER);

		añadirLista(g);
		for (Revision string : revisiones) {
			añadirRevision(string);
		}
		eventoTecla(textFieldCriterio);
		eventoTecla(tabla);
		eventoTecla(comboBox);
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

	private void añadirRevision(Revision rev) {

		sql = "select ifnull(y.calf_renRev,-1) from (select * from alumno where cve_alum in(select cve_alum from grupomatealum where cve_grumat = "
				+ gpo.getClaveGrupoMateria()
				+ ")) as x left join (select cve_alum, calf_renRev from renglonrevision rr where rr.cve_rev = '"
				+ rev.getCveRev() + "') as y on y.cve_alum = x.cve_alum order by x.nom_alum ";
		try {
			statement = conexion.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			Object[] lista = new Object[modelo.getRowCount()];
			for (int i = 0; result.next(); i++) {
				Object dato = result.getInt(1) == -1 ? "No Calificado" : result.getInt(1);
				lista[i] = dato;
			}

			modelo.addColumn(rev.getNomRev(), lista);
			setComb();
		} catch (

		SQLException e) {
			e.printStackTrace();
		}
	}

	private void añadirColumna(String nombre) {
		if (arrLimpio == null) {
			arrLimpio = new Object[modelo.getRowCount()];
			for (int i = 0; i < modelo.getRowCount(); i++) {
				arrLimpio[i] = "No Calificado";
			}
		}

		modelo.addColumn(nombre, arrLimpio);
		setComb();
		setColums();
	}

	public JButton getCancel() {
		return cancel;
	}

	public JButton getAcpt() {
		return acpt;
	}

	public boolean isSePuedeCerrar() {
		return sePuedeCerrar;
	}

	private void setColums() {

		TableColumn column = null;
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		int tamaño = scroll.getSize().width;
		column = tabla.getColumnModel().getColumn(0);
		column.setMinWidth(80);
		column.setPreferredWidth(80);
		column = tabla.getColumnModel().getColumn(1);
		column.setMinWidth(130);
		column.setPreferredWidth(130);
		column = tabla.getColumnModel().getColumn(2);
		column.setMinWidth(400);
		column.setPreferredWidth(400);
		tamaño = tamaño - 610;
		tamaño = tamaño / ((modelo.getColumnCount() - 3) != 0 ? (modelo.getColumnCount() - 3) : 1);
		if (modelo.getColumnCount() == 3) {
			tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		} else {
			for (int i = 3; i < modelo.getColumnCount(); i++) {
				column = tabla.getColumnModel().getColumn(i);
				column.setMinWidth(70);
				column.setPreferredWidth(tamaño);
			}

		}

	}

	private void añadirLista(GrupoMateria gpo) {

		sql = "select * from alumno where cve_alum in(select cve_alum from grupomatealum where cve_grumat = "
				+ gpo.getClaveGrupoMateria() + ") order by nom_alum";

		modelo.setRowCount(0);

		try {
			statement = conexion.prepareStatement(sql);
			ResultSet result = statement.executeQuery();

			for (int i = 1; result.next(); i++) {

				modelo.addRow(new Object[] { i, result.getString(1), result.getString(2) });
			}

		} catch (

		SQLException e) {
			e.printStackTrace();
		}
		setColums();
	}

}
