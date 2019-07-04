package vista;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import modelo.Alumno;
import modelo.GrupoMateria;
import modelo.GrupoMateriaAlumno;
import tablas.TablaAlumno;
import tablas.TablaGrupoMateria;
import tablas.TablaGrupoMateriaAlumno;
import tablas.TablaMateria;

public class VistaRegistroAlumno extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5427051903668417515L;
	private JPanel contentPane;
	private JTextField editNoControl;
	private JLabel lblNombre;
	private JButton btnAceptar;
	private JButton botonCancelar;
	private JTextField editNombre;
	private JLabel lblSeleccioneElGrupo;
	private JComboBox<String> comboGrupo;
	private TablaMateria tablaMateria;
	private TablaGrupoMateria tablaGrupoMateria;
	private GrupoMateria[] grupos;
	private TablaAlumno tablaAlumno;
	private TablaGrupoMateriaAlumno tablaGrupoMateriaAlumno;
	private JPanel buttonPane;

	/**
	 * Create the frame.
	 */
	public VistaRegistroAlumno(Connection conexion) {
		setTitle("Instituto Tecnológico de Zitacuaro");
		setIconImage(Vista.icon);
		tablaMateria = new TablaMateria(conexion);
		tablaGrupoMateria = new TablaGrupoMateria(conexion);
		tablaGrupoMateriaAlumno = new TablaGrupoMateriaAlumno(conexion);
		tablaAlumno = new TablaAlumno(conexion);

		grupos = tablaGrupoMateria.getCurrentGroups();
		// docentesGruMat = tablaDocenteGruMat.buscar();

		setSize(520, 365);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(153, 204, 204));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setModal(true);
		editNoControl = new JTextField();
		contentPane.setLayout(new GridLayout(0, 1, 0, 10));

		JLabel lblControl = new JLabel("Ingrese El Numero De Control:");

		lblControl.setForeground(Color.BLACK);
		lblControl.setLabelFor(contentPane);
		lblControl.setFont(new Font("Arial", Font.PLAIN, 15));
		lblControl.setBackground(Color.WHITE);
		contentPane.add(lblControl);
		contentPane.add(editNoControl);
		editNoControl.setColumns(10);
		editNoControl.setFont(new Font("Arial", Font.PLAIN, 15));
		ValidacionTextField.soloNum(editNoControl);
		ValidacionTextField.limitarTextF(editNoControl, 8);
		lblNombre = new JLabel("Ingrese Su Nombre Completo:");
		lblNombre.setFont(new Font("Arial", Font.PLAIN, 15));
		lblNombre.setForeground(Color.BLACK);
		contentPane.add(lblNombre);

		editNombre = new JTextField();
		editNombre.setFont(new Font("Arial", Font.PLAIN, 15));
		contentPane.add(editNombre);
		editNombre.setColumns(10);

		lblSeleccioneElGrupo = new JLabel("Seleccione El Grupo y Materia:");
		lblSeleccioneElGrupo.setForeground(Color.BLACK);
		lblSeleccioneElGrupo.setFont(new Font("Arial", Font.PLAIN, 15));
		contentPane.add(lblSeleccioneElGrupo);

		comboGrupo = new JComboBox<>();
		comboGrupo.setFont(new Font("Arial", Font.PLAIN, 15));
		comboGrupo.addItem("--Seleccione--");
		for (GrupoMateria g : grupos) {
			comboGrupo.addItem(
					g.getNombreGrupo().concat(" ".concat(tablaMateria.getMateria(g.getCveMateria()).getNombre())));
		}

		contentPane.add(comboGrupo);

		buttonPane = new JPanel();
		buttonPane.setBackground(new Color(102, 153, 153));
		FlowLayout flowLayout = (FlowLayout) buttonPane.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		contentPane.add(buttonPane);
		// contentPane.add(lblDocente);
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setFont(new Font("Arial", Font.PLAIN, 15));
		buttonPane.add(btnAceptar);
		botonCancelar = new JButton("Cancelar");
		botonCancelar.setFont(new Font("Arial", Font.PLAIN, 15));
		buttonPane.add(botonCancelar);
		botonCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (validar()) {
					Alumno a = new Alumno();
					a.setNoControl(editNoControl.getText());
					a.setNombre(editNombre.getText());
					if (!tablaAlumno.existe(a)) {
						tablaAlumno.agregar(a);
					}
					GrupoMateriaAlumno g = new GrupoMateriaAlumno();
					g.setClaveAlumno(a.getNoControl());
					g.setClabeGpoMat(grupos[comboGrupo.getSelectedIndex() - 1].getClaveGrupoMateria());
					if (!tablaGrupoMateriaAlumno.existe(g)) {

						tablaGrupoMateriaAlumno.agregar(g);
						JOptionPane.showMessageDialog(null, "Alumno Registrado");
					} else
						JOptionPane.showMessageDialog(null, "El Alumno ya Existe");
				}

			}
		});
		setLocationRelativeTo(null);
		setVisible(true);

	}

	private boolean validar() {
		if (editNoControl.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Llene el Numero de Control");
			return false;
		}
		if (editNoControl.getText().length() != 8) {
			JOptionPane.showMessageDialog(null, "El Numero de Control debe Contener 8 Digitos");
			return false;
		}

		if (editNombre.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Llene el Nombre del Alumno");
			return false;
		}

		if (editNombre.getText().split(" ").length < 2) {
			JOptionPane.showMessageDialog(null, "Escriba por lo Menos un Nombre y Apellido");
			return false;
		}

		if (comboGrupo.getSelectedIndex() < 1) {
			JOptionPane.showMessageDialog(null, "Seleccione un Grupo");
			return false;
		}
		return true;

	}
}
