package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import modelo.FaltasGruMat;
import modelo.GrupoMateria;
import tablas.TablaFaltasGruMat;
import tablas.TablaGrupoMateria;
import tablas.TablaMateria;

public class DialogFaltasAlum extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JComboBox<String> comboGrupo;
	private JComboBox<Integer> comboCant;
	private Connection conexion;
	private String sql;
	private PreparedStatement statement;
	private ResultSet rs;
	private TablaGrupoMateria tablaGrupoMateria;
	private GrupoMateria[] gpos;
	private GrupoMateria gpo;
	private TablaMateria tablaMateria;
	private TablaFaltasGruMat tablaFaltas;
	private FaltasGruMat faltas;
	private JButton okButton;

	/**
	 * Create the dialog.
	 */
	public DialogFaltasAlum(Connection conexion) {
		this.conexion = conexion;
		tablaGrupoMateria = new TablaGrupoMateria(conexion);
		tablaFaltas = new TablaFaltasGruMat(conexion);
		tablaMateria = new TablaMateria(conexion);
		gpos = tablaGrupoMateria.getCurrentGroups();
		setTitle("Instituto Tecnológico de Zitacuaro - Criterios de Evaluacion");
		setIconImage(Vista.icon);
		setSize(450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(153, 204, 204));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		JLabel lblSeleccioneGrupoY = new JLabel("Seleccione Grupo y Materia");
		lblSeleccioneGrupoY.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPanel.add(lblSeleccioneGrupoY);
		comboGrupo = new JComboBox<>();
		comboGrupo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboGrupo.addItem("--Seleccione--");
		for (GrupoMateria gpo : gpos) {
			comboGrupo.addItem(
					tablaMateria.getMateria(gpo.getCveMateria()).getNombre().concat(" ".concat(gpo.getNombreGrupo())));
		}

		comboGrupo.addActionListener(x -> {
			if (comboGrupo.getSelectedIndex() > 0) {
				gpo = gpos[comboGrupo.getSelectedIndex() - 1];
				comboCant.setEnabled(true);
				okButton.setEnabled(true);
				setDatos(gpo);
			} else {
				comboCant.setEnabled(false);
				okButton.setEnabled(false);
			}

		});

		contentPanel.add(comboGrupo);
		JLabel lblSeleccioneLasFaltas = new JLabel("Seleccione las Faltas Necesarias Para dar de Baja");
		lblSeleccioneLasFaltas.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPanel.add(lblSeleccioneLasFaltas);
		comboCant = new JComboBox<>();

		comboCant.setFont(new Font("Tahoma", Font.PLAIN, 15));
		for (int i = 1; i < 15; i++) {
			comboCant.addItem(i);
		}
		contentPanel.add(comboCant);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		okButton = new JButton("OK");
		okButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setFaltas(gpo);
				String st = "";
				if (tablaFaltas.existe(faltas)) {
					st = "Actualizado";
				} else
					st = "Agregado";
				JOptionPane.showMessageDialog(null, st);
				dispose();
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
		comboCant.setEnabled(false);
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		buttonPane.setBackground(new Color(102, 153, 153));
		setLocationRelativeTo(null);
		setModal(true);
		setVisible(true);
	}

	private void setFaltas(GrupoMateria gpo) {
		faltas = new FaltasGruMat();
		faltas.setCveGruMat(gpo.getClaveGrupoMateria());
		faltas.setNumFaltas(comboCant.getSelectedIndex() + 1);
		if (tablaFaltas.existe(faltas)) {
			faltas.setCveFaltasGrumat(tablaFaltas.buscar(faltas).getCveFaltasGrumat());
			tablaFaltas.modificar(faltas);
		} else
			tablaFaltas.agregar(faltas);
	}

	private void setDatos(GrupoMateria gpo) {
		sql = "select * from faltasGruMat where cve_grumat = " + gpo.getClaveGrupoMateria();

		try {
			statement = conexion.prepareStatement(sql);
			rs = statement.executeQuery();

			if (rs.next()) {
				comboCant.setSelectedIndex(rs.getInt(2) - 1);
				okButton.setText("Actualizar");
			} else {
				comboCant.setSelectedIndex(0);
				okButton.setText("Agregar");
			}

		} catch (Exception e) {
		}
	}
}
