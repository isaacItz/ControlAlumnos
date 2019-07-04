package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.placeholder.PlaceHolder;

import modelo.GrupoMateria;
import tablas.TablaGrupoMateria;
import tablas.TablaMateria;
import tablas.TablaPaseLista;

public class VistaBuscarLista extends JPanel {

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
	private TableRowSorter<TableModel> trs;
	private JButton cancel;
	private TablaGrupoMateria tablaGrupoMateria;
	private GrupoMateria gpo;
	private GrupoMateria[] gpos;
	private TablaPaseLista tablaPaseLista;
	private JTextField textFieldBusq;
	private JPanel panel;
	private JPanel panel_1;
	private JButton button;

	public VistaBuscarLista(Connection conexion) {
		this.conexion = conexion;
		tablaPaseLista = new TablaPaseLista(conexion);
		setLayout(new BorderLayout());
		JPanel panelSuperior = new JPanel();
		panelSuperior.setBackground(new Color(153, 204, 204));
		add(panelSuperior, BorderLayout.NORTH);
		tablaGrupoMateria = new TablaGrupoMateria(conexion);
		TablaMateria tablaMateria = new TablaMateria(conexion);
		gpos = tablaGrupoMateria.getCurrentGroups();

		panelSuperior.setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		panel.setBackground(new Color(153, 204, 204));
		panelSuperior.add(panel, BorderLayout.CENTER);

		combo = new JComboBox<>();
		combo.addItem("--Seleccione--");
		for (GrupoMateria gpo : gpos) {
			combo.addItem(tablaMateria.getMateria(gpo.getCveMateria()).getNombre() + " " + gpo.getNombreGrupo());
		}
		panel.add(combo);
		combo.setFont(new Font("Tahoma", Font.PLAIN, 15));

		panel_1 = new JPanel();
		panel_1.setBackground(new Color(153, 204, 204));
		panelSuperior.add(panel_1, BorderLayout.EAST);

		textFieldBusq = new JTextField();
		panel_1.add(textFieldBusq);
		textFieldBusq.setFont(new Font("Tahoma", Font.PLAIN, 15));
		new PlaceHolder(textFieldBusq, "Numero de Control o Nombre");
		textFieldBusq.setColumns(25);

		button = new JButton("");
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
		panel_1.add(button);
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
				Object o = e.getSource();
				if (o instanceof JTextField) {
					JTextField text = (JTextField) o;
					text.setSelectionStart(0);
					text.setSelectionEnd(text.getText().length());
				}
			}
		});

		combo.addItemListener(x -> {

			int seleccion = combo.getSelectedIndex() - 1;
			if (seleccion > -1) {
				gpo = gpos[seleccion];
				tabla.requestFocus();
				setColums();
				añadirLista(gpo);
				textFieldBusq.setEnabled(true);
			} else
				textFieldBusq.setEnabled(false);

		});

		JPanel panelInferior = new JPanel();
		panelInferior.setBackground(new Color(102, 153, 153));
		cancel = new JButton("Salir");
		cancel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelInferior.add(cancel);

		add(panelInferior, BorderLayout.SOUTH);

		initComponents();
		eventoTecla(tabla);
		eventoTecla(combo);
		eventoTecla(button);
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
		tabla.setFillsViewportHeight(true);
		modelo = new DefaultTableModel() {

			private static final long serialVersionUID = 1L;
			Class<?> tipos[] = { Integer.class, Integer.class, String.class };

			@Override
			public Class<?> getColumnClass(int c) {
				if (c < tipos.length) {
					return tipos[c];
				}
				if (c == modelo.getColumnCount() - 1) {
					return String.class;
				}
				return boolean.class;
			}

			@Override
			public boolean isCellEditable(int f, int c) {
				return false;
			}

		};

		trs = new TableRowSorter<TableModel>(modelo);

		modelo.setColumnIdentifiers(cabecera);
		tabla.setModel(modelo);
		tabla.setRowSorter(trs);

		add(scroll, BorderLayout.CENTER);
		scroll.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if (modelo.getRowCount() > 0) {
					setColums();

				}
			}
		});
		textFieldBusq.setEnabled(false);
	}

	private void setColums() {
		tabla.setDefaultRenderer(Object.class, new RenderTabla(tablaPaseLista.getAlumnosConFaltas(gpo)));
		tabla.setDefaultRenderer(Integer.class, new RenderTabla(tablaPaseLista.getAlumnosConFaltas(gpo)));
		TableColumn column = null;
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		int tamaño = scroll.getSize().width;
		column = tabla.getColumnModel().getColumn(0);
		column.setMinWidth(80);
		column.setPreferredWidth((int) (tamaño * .20));
		column = tabla.getColumnModel().getColumn(1);
		column.setMinWidth(130);
		column.setPreferredWidth((int) (tamaño * .30));
		column = tabla.getColumnModel().getColumn(2);
		column.setMinWidth(400);
		column.setPreferredWidth((int) (tamaño * .50));

	}

	public JTextField getText() {
		return textFieldBusq;
	}

	public JButton getCancel() {
		return cancel;
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
