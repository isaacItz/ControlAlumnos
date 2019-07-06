package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import modelo.Alumno;
import modelo.BajaAlumno;
import modelo.GrupoMateria;
import modelo.GrupoMateriaAlumno;
import tablas.TablaAlumno;
import tablas.TablaBajaAlumno;
import tablas.TablaGrupoMateria;
import tablas.TablaGrupoMateriaAlumno;
import tablas.TablaMateria;

public class DialogBajaAlum extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JComboBox<String> comboGrupo;
	private String numCont;
	private TablaGrupoMateria tablaGrupoMateria;
	private GrupoMateria[] gpos;
	private GrupoMateria gpo;
	private TablaMateria tablaMateria;
	private JButton okButton;
	private JTextField textField;
	private TablaAlumno tablaAlumno;
	private Alumno[] alumnos;
	private JList<String> list;
	private DefaultListModel<String> modeloLista;
	private TablaBajaAlumno tablaBajaAlumno;
	private TablaGrupoMateriaAlumno tablaGrupoMateriaAlumno;

	/**
	 * Create the dialog.
	 */
	public DialogBajaAlum(Connection conexion) {
		tablaGrupoMateria = new TablaGrupoMateria(conexion);
		tablaAlumno = new TablaAlumno(conexion);
		tablaBajaAlumno = new TablaBajaAlumno(conexion);
		tablaGrupoMateriaAlumno = new TablaGrupoMateriaAlumno(conexion);
		tablaMateria = new TablaMateria(conexion);
		gpos = tablaGrupoMateria.getCurrentGroups();
		alumnos = tablaAlumno.getAlumnos(null);
		setTitle("Instituto Tecnológico de Zitacuaro - Criterios de Evaluacion");
		setIconImage(Vista.icon);
		setSize(450, 388);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(153, 204, 204));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		modeloLista = new DefaultListModel<String>();

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(153, 204, 204));
		contentPanel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblBuscarPor = new JLabel("Ingrese el Nombre o Numero de Control:");
		lblBuscarPor.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_1.add(lblBuscarPor);

		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (modeloLista.getSize() > 0) {
					list.setSelectedIndex(0);

					String cad = list.getSelectedValue();
					if (cad != null) {
						setGrupos(cad.substring(0, 8));
						textField.setText(cad);
					}

					comboGrupo.requestFocus();
				}

			}
		});
		textField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				llenarLista();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				llenarLista();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				llenarLista();
			}
		});

		textField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_1.add(textField);
		textField.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane);

		list = new JList<>();
//		list.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				if (e.getClickCount() == 2) {
//					setGrupos();
//					textField.setText(list.getSelectedValue());
//				}
//
//			}
//		});
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					String cad = list.getSelectedValue();
					if (cad != null) {
						setGrupos(cad.substring(0, 8));
						textField.setText(cad);
					}

				}

			}
		});

		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFont(new Font("Tahoma", Font.PLAIN, 13));
		scrollPane.setViewportView(list);
		list.setModel(modeloLista);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(153, 204, 204));
		contentPanel.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));
		JLabel lblSeleccioneGrupoY = new JLabel("Seleccione Grupo y Materia");
		panel_2.add(lblSeleccioneGrupoY);
		lblSeleccioneGrupoY.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboGrupo = new JComboBox<>();
		panel_2.add(comboGrupo);
		comboGrupo.setFont(new Font("Tahoma", Font.PLAIN, 15));

		comboGrupo.addActionListener(x -> {
			if (comboGrupo.getSelectedIndex() > 0) {
				gpo = gpos[comboGrupo.getSelectedIndex() - 1];
				okButton.setEnabled(true);
			} else {
				okButton.setEnabled(false);
			}

		});

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		okButton = new JButton("OK");
		okButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Alumno a = new Alumno();
				a.setNoControl(numCont);
				if (tablaAlumno.existe(a)) {
					GrupoMateriaAlumno gp = new GrupoMateriaAlumno();
					gp.setClabeGpoMat(gpo.getClaveGrupoMateria());
					gp.setClaveAlumno(numCont);
					gp = tablaGrupoMateriaAlumno.buscar(gp);
					if (gp != null) {
						BajaAlumno baja = new BajaAlumno();
						baja.setCveGruMatAlum(gp.getClaveGpoMatAlum());
						System.out.println(gp);
						System.out.println(baja);
						if (!tablaBajaAlumno.existe(baja)) {
							tablaBajaAlumno.agregar(baja);
						} else {
							int op = JOptionPane.showConfirmDialog(null, "Alumno Dado de Baja. ¿Desea Reintegrarlo?");
							if (op == JOptionPane.YES_OPTION) {
								baja = tablaBajaAlumno.buscar(baja);
								tablaBajaAlumno.eliminar(baja);
							}

						}
						dispose();
						JOptionPane.showMessageDialog(null, "Exito");
					} else {
						JOptionPane.showMessageDialog(null, "El Alumno No Esta Registrado En el Grupo Seleccionado");
					}

				} else {
					JOptionPane.showMessageDialog(null, "El Numero De Control Ingresado No Esta Registrado");
				}

			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		buttonPane.setBackground(new Color(102, 153, 153));
		setLocationRelativeTo(null);
		setModal(true);
		setVisible(true);
	}

	private void setGrupos(String cad) {
		Alumno a = new Alumno();
		numCont = cad;
		a.setNoControl(cad);
		gpos = tablaGrupoMateriaAlumno.getGruposAlum(a);
		comboGrupo.removeAllItems();
		comboGrupo.addItem("--Seleccione--");
		for (GrupoMateria gpo : gpos) {
			comboGrupo.addItem(
					tablaMateria.getMateria(gpo.getCveMateria()).getNombre().concat(" ".concat(gpo.getNombreGrupo())));
		}
		if (comboGrupo.getItemCount() == 2) {
			comboGrupo.setSelectedIndex(1);
		}
	}

	private void llenarLista() {
		modeloLista.removeAllElements();
		for (Alumno alumno : alumnos) {
			String cad = alumno.getNoControl().concat(" ").concat(alumno.getNombre());
			if (cad.toLowerCase().contains(textField.getText().toLowerCase())) {
				modeloLista.addElement(cad);
			}
		}
	}

}
