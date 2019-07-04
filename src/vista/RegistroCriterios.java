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
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import modelo.GrupoMateria;
import modelo.Materia;
import modelo.Unidad;
import tablas.TablaGrupoMateria;
import tablas.TablaMateria;

public class RegistroCriterios extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JComboBox<String> comboGrupoMat;
	private JComboBox<String> comboBoxUnidad;
	private Connection conexion;
	private TablaMateria tablaMateria;
	private GrupoMateria[] gpos;
	private GrupoMateria gpo;
	private Unidad[] unidades;
	private Unidad unidad;
	private JButton okButton;
	private JCheckBox chckbxRegistarCriteriosPara;
	private TablaGrupoMateria tablaGrupoMateria;
	private Materia mat;
	private RegistarCriterioUnidad panelCriterio;

	public RegistroCriterios(Connection conexion, Container panel) {
		setTitle("Instituto Tecnológico de Zitacuaro - Criterios de Evaluacion");
		setIconImage(Vista.icon);
		this.conexion = conexion;
		tablaGrupoMateria = new TablaGrupoMateria(conexion);
		tablaMateria = new TablaMateria(conexion);
		gpos = tablaGrupoMateria.getCurrentGroups();
		mat = new Materia();

		setSize(650, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(153, 204, 204));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		JLabel lblSeleccioneElGrupo = new JLabel("Seleccione el Grupo y Materia");
		lblSeleccioneElGrupo.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		contentPanel.add(lblSeleccioneElGrupo);
		comboGrupoMat = new JComboBox<>();
		comboGrupoMat.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		comboBoxUnidad = new JComboBox<>();
		comboBoxUnidad.setFont(new Font("Bahnschrift", Font.PLAIN, 15));

		comboGrupoMat.addItem("--Seleccione--");
		contentPanel.add(comboGrupoMat);
		for (GrupoMateria gpo : gpos) {
			comboGrupoMat
					.addItem(tablaMateria.getMateria(gpo.getCveMateria()).getNombre() + " " + gpo.getNombreGrupo());
		}
		comboGrupoMat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				comboBoxUnidad.removeAllItems();
				chckbxRegistarCriteriosPara.setSelected(false);
				if (comboGrupoMat.getSelectedIndex() > 0) {
					mat = tablaMateria.getMateria(gpos[comboGrupoMat.getSelectedIndex() - 1].getCveMateria());
					unidades = tablaMateria.getUnidades(mat);
					if (unidades.length > 0) {
						gpo = gpos[comboGrupoMat.getSelectedIndex() - 1];
						comboBoxUnidad.addItem("--Seleccione--");

						for (Unidad u : unidades) {
							comboBoxUnidad.addItem(u.getNumeroUnidad() + " " + u.getNombre());
						}
						if (unidades != null) {
							if (!chckbxRegistarCriteriosPara.isSelected()) {
								comboBoxUnidad.setEnabled(true);
							}
						} else {
							JOptionPane.showMessageDialog(null, "No Hay Unidades Registradas Para Esta Materia");
							comboBoxUnidad.setEnabled(false);
						}

						if (hayUnidadesAsignadas(gpo)) {
							chckbxRegistarCriteriosPara.setEnabled(false);
						} else
							chckbxRegistarCriteriosPara.setEnabled(true);
					} else {
						JOptionPane.showMessageDialog(null,
								"No Hay Unidades Registradas, Registre Unidades Para Continuar", "Err",
								JOptionPane.ERROR_MESSAGE);
						chckbxRegistarCriteriosPara.setEnabled(false);
						comboBoxUnidad.setEnabled(false);
					}

				} else {
					chckbxRegistarCriteriosPara.setEnabled(false);
					comboBoxUnidad.setEnabled(false);
				}

			}
		});
		chckbxRegistarCriteriosPara = new JCheckBox("Registar Criterios Para Todas las Unidades");
		chckbxRegistarCriteriosPara.setBackground(new Color(153, 204, 204));
		chckbxRegistarCriteriosPara.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		chckbxRegistarCriteriosPara.setEnabled(false);
		contentPanel.add(chckbxRegistarCriteriosPara);
		JLabel lblNewLabel = new JLabel("Unidad");
		lblNewLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		contentPanel.add(lblNewLabel);

		comboBoxUnidad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (comboBoxUnidad.getSelectedIndex() > 0) {
					unidad = unidades[comboBoxUnidad.getSelectedIndex() - 1];
					if (getTotalCrit(gpo, unidad) == 0) {
						okButton.setText("Agregar Criterios");
					} else
						okButton.setText("Modificar Criterios");
				} else
					unidad = null;

			}
		});

		contentPanel.add(comboBoxUnidad);
		comboBoxUnidad.setEnabled(false);

		chckbxRegistarCriteriosPara.addActionListener(x -> {
			if (comboGrupoMat.getSelectedIndex() > 0) {
				if (chckbxRegistarCriteriosPara.isSelected()) {
					okButton.setText("Agregar Criterios");
					comboBoxUnidad.setEnabled(false);
				} else if (comboBoxUnidad.getItemCount() > 1)
					comboBoxUnidad.setEnabled(true);
			}

		});
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(102, 153, 153));
		panelCriterio = null;
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		okButton = new JButton("OK");
		okButton.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (comboGrupoMat.getSelectedIndex() > 0) {

					if (chckbxRegistarCriteriosPara.isSelected()) {
						panelCriterio = new RegistarCriterioUnidad(conexion, gpo, panel, unidades);
						panel.add(panelCriterio, BorderLayout.CENTER);
						panel.setVisible(true);
						panel.repaint();
						panel.validate();
					} else {
						panelCriterio = new RegistarCriterioUnidad(conexion, gpo, panel, unidad);
						panel.add(panelCriterio, BorderLayout.CENTER);
						panel.setVisible(true);
						panel.repaint();
						panel.validate();
						panelCriterio.getBotonCancelar().addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								panel.remove(panelCriterio);
								panel.repaint();

							}
						});

					}
					dispose();
				}

			}
		});
		buttonPane.add(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(x -> dispose());
		buttonPane.add(cancelButton);
		setLocationRelativeTo(null);
		setModal(true);
		setVisible(true);

	}

	public GrupoMateria getGrupoMate() {
		return gpo;
	}

	public Unidad getUnidad() {
		return unidad;
	}

	public JButton getOkButton() {
		return okButton;
	}

	private int getTotalCrit(GrupoMateria gpo, Unidad u) {
		String sql = "select count(*) from (select u.cve_uni from criteriounidad cu join unidad u on u.cve_uni = cu.cve_uni where cu.cve_grumat = "
				+ gpo.getClaveGrupoMateria() + " and u.cve_uni = " + u.getClave() + ")as x";
		try {
			java.sql.Statement st = conexion.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next() && rs.getInt(1) > 0) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			System.err.println("problema con verificarcri " + e.getMessage());

		}
		return 0;

	}

	private boolean hayUnidadesAsignadas(GrupoMateria gpo) {
		Unidad[] units = tablaMateria.getUnidades(mat);
		for (Unidad unidad : units) {
			String sql = "select * from criterioUnidad where cve_uni = " + unidad.getClave() + " and cve_grumat = "
					+ gpo.getClaveGrupoMateria();
			try {
				java.sql.Statement st = conexion.createStatement();
				ResultSet rs = st.executeQuery(sql);
				if (rs.next()) {
					return true;
				}
			} catch (SQLException e) {
				System.err.println("problema con verificarcri " + e.getMessage());

			}
		}

		return false;

	}

//	private boolean hayUnidadesDisp(GrupoMateria gpo) {
//		Unidad[] units = tablaMateria.getUnidades(mat);
//		for (Unidad unidad : units) {
//			String sql = "select * from criterioUnidad where cve_uni = " + unidad.getClave() + " and cve_grumat = "
//					+ gpo.getClaveGrupoMateria();
//			try {
//				java.sql.Statement st = conexion.createStatement();
//				ResultSet rs = st.executeQuery(sql);
//				if (!rs.next()) {
//					return true;
//				}
//			} catch (SQLException e) {
//				System.err.println("problema con verificarcri " + e.getMessage());
//
//			}
//		}
//
//		return false;
//
//	}

}
