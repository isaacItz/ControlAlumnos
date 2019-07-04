package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import modelo.Criterio;
import modelo.CriterioUnidad;
import modelo.GrupoMateria;
import modelo.Materia;
import modelo.Unidad;
import tablas.TablaCriterio;
import tablas.TablaGrupoMateria;
import tablas.TablaMateria;
import tablas.TablaRevision;
import tablas.TablaUnidad;

public class SeleccionCalfCrit extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JComboBox<String> comboCriterio;
	private JComboBox<String> comboUnidad;
	private JComboBox<String> comboGrupoMateria;
	private JButton okButton;
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
	private int estadoDeEvaluacionCriterio;
	private TablaRevision tablaEvaluacion;
	private TablaRevision tablaRevision;

	/**
	 * Launch the application.
	 */
	public SeleccionCalfCrit(Connection conexion, Container contentP) {
		setTitle("Instituto Tecnológico de Zitacuaro - Calificacion de Criterios");
		setIconImage(Vista.icon);
		tablaGrupoMateria = new TablaGrupoMateria(conexion);
		tablaEvaluacion = new TablaRevision(conexion);
		tablaMateria = new TablaMateria(conexion);
		tablaCriterio = new TablaCriterio(conexion);
		tablaRevision = new TablaRevision(conexion);
		tablaUnidad = new TablaUnidad(conexion);
		gpos = tablaGrupoMateria.getCurrentGroups();

		setSize(550, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(153, 204, 204));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		JLabel lblSeleccioneLaMateria = new JLabel("Seleccione el Grupo y Materia");
		lblSeleccioneLaMateria.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		contentPanel.add(lblSeleccioneLaMateria);

		DefaultComboBoxModel<String> modeloCombo = new DefaultComboBoxModel<>();
		comboGrupoMateria = new JComboBox<>(modeloCombo);
		comboGrupoMateria.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		comboGrupoMateria.addItem("--Seleccione--");

		for (GrupoMateria g : gpos) {
			comboGrupoMateria.addItem(
					tablaMateria.getMateria(g.getCveMateria()).getNombre().concat(" ").concat(g.getNombreGrupo()));
		}

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
						okButton.setEnabled(false);
					} else {
						comboUnidad.setEnabled(true);
					}
				}
			}
		});
		contentPanel.add(comboGrupoMateria);
		JLabel lblSeleccioneLaUnidad = new JLabel("Seleccione la Unidad");
		lblSeleccioneLaUnidad.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		contentPanel.add(lblSeleccioneLaUnidad);
		comboUnidad = new JComboBox<String>();
		comboUnidad.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
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
						okButton.setEnabled(false);
						comboCriterio.setEnabled(false);
					} else {

						comboCriterio.setEnabled(true);
					}

				}

			}
		});
		contentPanel.add(comboUnidad);
		JLabel lblSeleccioneLaActividad = new JLabel("Seleccione la Actividad a Evaluar");
		lblSeleccioneLaActividad.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		contentPanel.add(lblSeleccioneLaActividad);
		comboCriterio = new JComboBox<>();
		comboCriterio.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		comboCriterio.addItem("--Seleccione--");

		comboCriterio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (comboCriterio.getSelectedIndex() > 0) {
					okButton.setEnabled(true);
					criterio = criterios[comboCriterio.getSelectedIndex() - 1];
					estadoDeEvaluacionCriterio = 1;
					if (tablaEvaluacion.alumnosEvaluados(criterio) > 0) {
						if (tablaGrupoMateria.getTotalAlumnos(grupoMateria.getClaveGrupoMateria()) > tablaEvaluacion
								.alumnosEvaluados(criterio)) {
							estadoDeEvaluacionCriterio = 2;
						} else
							estadoDeEvaluacionCriterio = 3;
					}

					switch (estadoDeEvaluacionCriterio) {
					case 2:
						okButton.setText("Continuar Evaluacion");
						break;
					case 3:
						okButton.setText("Actualizar Evaluacion");
						break;
					default:
						okButton.setText("Iniciar Evaluacion");
					}

				} else
					okButton.setEnabled(false);
			}
		});
		contentPanel.add(comboCriterio);
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(102, 153, 153));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		okButton = new JButton("OK");
		okButton.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Criterio c = new Criterio();
				c.setClave(criterio.getCvecri());

				if (comboCriterio.getSelectedItem().toString().toLowerCase().equals("asistencia")) {

					if (!tablaRevision.existeRevision(criterio)) {
						dispose();
						new VistaCalificarAistencia(conexion, unidad, criterio, grupoMateria);
					} else {
						JOptionPane.showMessageDialog(null, "Asistencia ya Registrada");
					}

				} else {
					CalificarCriterio calf = new CalificarCriterio(conexion, unidad, criterio, grupoMateria, materia,
							contentP);
					contentP.add(calf, BorderLayout.CENTER);
					contentP.repaint();
					contentP.validate();
					contentP.setVisible(true);
					dispose();
					calf.getCancel().addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							contentP.remove(calf);
							contentP.repaint();
						}
					});
				}

			}
		});
		okButton.setActionCommand("OK");
		okButton.setEnabled(false);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		setLocationRelativeTo(null);
		comboUnidad.setEnabled(false);
		comboCriterio.setEnabled(false);
		setModal(true);
		setVisible(true);

	}

	private void removerItems(JComboBox<String> combo) {
		combo.removeAllItems();
		combo.addItem("--Seleccione--");
	}

}
