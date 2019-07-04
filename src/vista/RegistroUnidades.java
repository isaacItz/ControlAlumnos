package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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

import modelo.Materia;
import modelo.Periodo;
import modelo.Unidad;
import tablas.TablaUnidad;
import tablas.Tablaperiodo;
import java.awt.Color;
import java.awt.Font;

public class RegistroUnidades extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JComboBox<String> comboUnidad;
	private JComboBox<String> comboPeriodo;
	private Tablaperiodo tablaperiodo;
	private TablaUnidad tablaUnidad;
	// private Connection conexion;
	private Periodo[] periodos;
	private JButton okButton;
	private Unidad unitUp;

	public RegistroUnidades(Connection conexion, Materia materia) {
		// this.conexion = conexion;
		setTitle("Instituto Tecnológico de Zitacuaro - Unidades");
		setIconImage(Vista.icon);
		tablaperiodo = new Tablaperiodo(conexion);
		tablaUnidad = new TablaUnidad(conexion);
		periodos = tablaperiodo.getPeriodos();
		setSize(601, 399);
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setVgap(5);
		getContentPane().setLayout(borderLayout);
		contentPanel.setBackground(new Color(153, 204, 204));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(7, 1, 0, 0));
		{
			JLabel lblMateria = new JLabel("Materia: " + materia.getNombre());
			lblMateria.setFont(new Font("Tahoma", Font.PLAIN, 15));
			contentPanel.add(lblMateria);
		}
		{
			JLabel lblSeleccioneLaUnidad = new JLabel("Seleccione la Unidad");
			lblSeleccioneLaUnidad.setFont(new Font("Tahoma", Font.PLAIN, 15));
			contentPanel.add(lblSeleccioneLaUnidad);
		}
		{
			comboUnidad = new JComboBox<>();
			comboUnidad.setFont(new Font("Tahoma", Font.PLAIN, 15));
			comboUnidad.addItem("--Seleccione--");
			for (int i = 1; i <= 10; i++)
				comboUnidad.addItem(String.valueOf(i));
			contentPanel.add(comboUnidad);

			comboUnidad.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (comboUnidad.getSelectedIndex() > 0) {
						Unidad u = new Unidad();
						u.setClaveMateria(materia.getClave());
						u.setNumeroUnidad(comboUnidad.getSelectedItem().toString());
						u = tablaUnidad.buscar(u);
						if (u != null) {
							textField.setText(u.getNombre());
							okButton.setText("Actualizar");
							unitUp = u;
							System.out.println(u);
						}

						else {
							textField.setText(null);
							okButton.setText("OK");
						}

					}
				}
			});
		}
		{
			JLabel lblNombreDeLa = new JLabel("Nombre de la Unidad");
			lblNombreDeLa.setFont(new Font("Tahoma", Font.PLAIN, 15));
			contentPanel.add(lblNombreDeLa);
		}
		{
			textField = new JTextField();
			textField.setFont(new Font("Tahoma", Font.PLAIN, 15));
			contentPanel.add(textField);
			textField.setColumns(10);
		}
		{
			JLabel lblSeleccioneElPeriodo = new JLabel("Seleccione el Periodo");
			lblSeleccioneElPeriodo.setFont(new Font("Tahoma", Font.PLAIN, 15));
			contentPanel.add(lblSeleccioneElPeriodo);
		}
		{
			comboPeriodo = new JComboBox<>();
			comboPeriodo.setFont(new Font("Tahoma", Font.PLAIN, 15));
			for (Periodo periodo : periodos) {

				comboPeriodo.addItem(periodo.getPeridoFormateado());
			}
			contentPanel.add(comboPeriodo);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(102, 153, 153));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
				okButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {

						if (!textField.getText().isEmpty()) {
							if (okButton.getText().equals("Actualizar")) {
								unitUp.setNombre(textField.getText());
								System.out.println(tablaUnidad.modificar(unitUp));
								JOptionPane.showMessageDialog(null, "Acualizado");
								comboUnidad.setSelectedIndex(0);
								textField.setText("");
								okButton.setText("OK");

							} else {
								Unidad unidad = new Unidad();
								unidad.setNumeroUnidad(comboUnidad.getSelectedItem().toString());
								unidad.setClaveMateria(materia.getClave());
								unidad.setNombre(textField.getText());
								if (!tablaUnidad.existe(unidad)) {
									tablaUnidad.agregar(unidad);
									JOptionPane.showMessageDialog(null, "Registrado!!");
									textField.setText("");
								} else
									JOptionPane.showMessageDialog(null, "La Unidad Ya esta Registrada");
							}

						} else
							JOptionPane.showMessageDialog(null, "Registre un Nombre de Unidad");

					}
				});

				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
				cancelButton.addActionListener(x -> dispose());
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setLocationRelativeTo(null);
		setModal(true);
		setVisible(true);
	}

}
