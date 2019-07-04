package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.ibm.icu.text.SimpleDateFormat;
import com.toedter.calendar.JDateChooser;

import modelo.Criterio;
import modelo.CriterioUnidad;
import modelo.GrupoMateria;
import modelo.Materia;
import modelo.RenglonRevision;
import modelo.Revision;
import modelo.Unidad;
import tablas.TablaCriterio;
import tablas.TablaMateria;
import tablas.TablaPaseLista;
import tablas.TablaRenglonRevision;
import tablas.TablaRevision;

public class VistaCalificarAistencia extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel labelFech;
	private TablaMateria tablaMateria;
	private Unidad unidad;
	private CriterioUnidad criterioU;
	private Criterio criterio;
	private GrupoMateria grupoMateria;
	private TablaCriterio tablaCriterio;
	private JDateChooser dateChooser;
	private String sql;
	private PreparedStatement statement;
	private JDateChooser dateChooser_1;
	private Connection conexion;
	private DateTimeFormatter dTF;
	private LocalDate fechaInicial;
	private LocalDate primerPaseLista;
	private TablaPaseLista tablaPaseLista;
	private JCheckBox chckbxNewCheckBox;
	private TablaRenglonRevision tablaRenglonRevision;
	private TablaRevision tablaRevision;

	/**
	 * Create the dialog.
	 */
	public VistaCalificarAistencia(Connection conexion, Unidad u, modelo.CriterioUnidad cU, GrupoMateria g) {
		setIconImage(Vista.icon);
		tablaMateria = new TablaMateria(conexion);
		tablaCriterio = new TablaCriterio(conexion);
		tablaRenglonRevision = new TablaRenglonRevision(conexion);
		tablaPaseLista = new TablaPaseLista(conexion);
		tablaRevision = new TablaRevision(conexion);
		criterioU = cU;
		grupoMateria = g;
		unidad = u;
		dTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.conexion = conexion;
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		criterio = new Criterio();
		criterio.setClave(cU.getCvecri());
		criterio = tablaCriterio.getCriterio(criterio);
		Materia mat = new Materia();
		mat = tablaMateria.getMateria(u.getClaveMateria());
		fechaInicial = getFechaInicioEv();
		primerPaseLista = getPrimerPaseLista();
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(VistaCalificarAistencia.class.getResource("/recursos/petirrojos.png")));
		setTitle("Calificacion de Asistencia");
		setBounds(100, 100, 597, 422);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(153, 204, 204));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBackground(new Color(153, 204, 204));
		contentPanel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setBackground(new Color(0, 153, 153));
		panel.setLayout(new GridLayout(0, 1, 20, 8));
		panel.setBorder(new TitledBorder(null, "Datos de Evaluacion", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(153, 204, 204)));

		JLabel lblNewLabel = new JLabel(mat.getNombre() + " " + g.getNombreGrupo());
		lblNewLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 17));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

		panel.add(lblNewLabel);
		JLabel labelU = new JLabel("Unidad: ".concat(u.getNumeroUnidad()) + " " + u.getNombre());
		labelU.setFont(new Font("Bahnschrift", Font.PLAIN, 17));
		labelU.setHorizontalAlignment(SwingConstants.LEFT);
		labelFech = new JLabel("Evaluacion de " + criterio.getNombre() + " Valor: " + cU.getValor());
		labelFech.setFont(new Font("Bahnschrift", Font.PLAIN, 17));
		labelFech.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(labelU);
		panel.add(labelFech);
		JLabel lblF = new JLabel("Fecha de Evaluacion: " + sd.format(new Date()));
		lblF.setFont(new Font("Bahnschrift", Font.PLAIN, 17));
		lblF.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblF);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(102, 153, 153));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton("Registar");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (dateChooser.getDate() != null) {
					if (dateChooser_1.getDate() != null) {
						LocalDate fechI = LocalDate.parse(sd.format(dateChooser.getDate()), dTF);
						LocalDate fechF = LocalDate.parse(sd.format(dateChooser_1.getDate()), dTF);
						LocalDate[] fechas = tablaPaseLista.getFechasPasesLista(g, fechI, fechF);
						if (fechas.length > 0) {
							Revision r = new Revision();
							r.setCveCriUni(criterioU.getClave());
							r.setNomRev("Pase de Lista");
							r.setNumRev((short) 1);
							tablaRevision.agregar(r);
							r = tablaRevision.buscar(r);
							if (chckbxNewCheckBox.isSelected()) {
								crearRevision(grupoMateria, unidad, r);
							} else {
								crearRevision(r, fechas);
							}
							JOptionPane.showMessageDialog(null, "Registrada");
							dispose();
						} else if (chckbxNewCheckBox.isSelected()) {
							JOptionPane.showMessageDialog(null, "No Hay Pases de Lista Registrados en el Periodo");
						} else
							JOptionPane.showMessageDialog(null,
									"No Hay Pases de Lista Registrados en las Fechas Seleccionadas");
					} else {
						JOptionPane.showMessageDialog(null, "Asigne una Fecha Final");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Asigne una Fecha Inicial");
				}

			}
		});
		okButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("Cancelar");
		cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(153, 204, 204));
		panel_1.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblComienzoDeEvaluacion = new JLabel(
				"La Evaluacion de: " + criterio.getNombre() + " Comenzo a Evaluarse Desde: " + fechaInicial);
		panel_2.add(lblComienzoDeEvaluacion);
		lblComienzoDeEvaluacion.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JLabel lblSeComenzoA = new JLabel("Se Comenzo a Pasar Lista Desde: " + primerPaseLista);
		panel_2.add(lblSeComenzoA);
		lblSeComenzoA.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(
				new TitledBorder(null, "Fecha de Evaluacion", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBackground(new Color(153, 204, 204));
		panel_1.add(panel_3);
		panel_3.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(153, 204, 204));
		panel_3.add(panel_4);

		chckbxNewCheckBox = new JCheckBox("Establecer Periodo Automaticamente");
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxNewCheckBox.isSelected()) {
					try {
						dateChooser.setDate(sd.parse(fechaInicial.toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					dateChooser_1.setDate(new Date());
					dateChooser.setEnabled(false);
					dateChooser_1.setEnabled(false);
				} else {
					dateChooser.setEnabled(true);
					dateChooser_1.setEnabled(true);
				}

			}
		});
		chckbxNewCheckBox.setBackground(new Color(153, 204, 204));
		chckbxNewCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_4.add(chckbxNewCheckBox);

		JPanel panel_5 = new JPanel();
		panel_5.setBackground(new Color(153, 204, 204));
		panel_3.add(panel_5);
		panel_5.setLayout(new GridLayout(0, 4, 0, 0));

		JLabel lblFechaDeInicio = new JLabel("Fecha de Inicio: ");
		lblFechaDeInicio.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_5.add(lblFechaDeInicio);

		dateChooser = new JDateChooser();
		dateChooser.setMaxSelectableDate(new Date());
		dateChooser.setDateFormatString("yyyy-MM-dd");
		panel_5.add(dateChooser);
		JLabel lblFechaDeFinal = new JLabel("Fecha de Final");
		lblFechaDeFinal.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_5.add(lblFechaDeFinal);

		dateChooser_1 = new JDateChooser();
		dateChooser_1.getDateEditor().setMaxSelectableDate(new Date());
		try {
			dateChooser.getDateEditor().setMinSelectableDate(sd.parse(primerPaseLista.toString()));
			dateChooser_1.getDateEditor().setMinSelectableDate(sd.parse(primerPaseLista.toString()));
		} catch (Exception e) {
			// TODO: handle exception
		}

		dateChooser_1.setDateFormatString("yyyy-MM-dd");
		dateChooser.setFont(new Font("Tahoma", Font.PLAIN, 15));
		dateChooser_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_5.add(dateChooser_1);
		chckbxNewCheckBox.setSelected(true);
		try {
			dateChooser.setDate(sd.parse(fechaInicial.toString()));
			dateChooser.setDate(sd.parse(fechaInicial.toString()));

		} catch (Exception e) {
		}

		dateChooser_1.setDate(new Date());
		dateChooser.setEnabled(false);
		dateChooser_1.setEnabled(false);
		cancelButton.setActionCommand("Cancel");
		setLocationRelativeTo(null);
		buttonPane.add(cancelButton);
		setModal(true);
		setVisible(true);
	}

	private LocalDate getPrimerPaseLista() {
		sql = "select min(fecha_palis) from paselista where cve_grumat =" + grupoMateria.getClaveGrupoMateria();

		try {
			statement = conexion.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				return LocalDate.parse(rs.getString(1), dTF);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void crearRevision(Revision r, LocalDate... fechas) {

		String cad = "";
		for (LocalDate localDate : fechas) {
			cad += "'" + localDate + "',";
		}
		cad = cad.substring(0, cad.length() - 1);
		sql = "select gpa.cve_alum, ifnull(suma,-1) from (select cve_alum, sum(estatus_palisalu) suma from paselista pl join paselistaalumno pla on pla.cve_palis = pl.cve_palis where cve_grumat = "
				+ grupoMateria.getClaveGrupoMateria() + " and pl.fecha_palis in (" + cad
				+ ") group by cve_alum having suma = (select count(*) from paselista where cve_grumat = "
				+ grupoMateria.getClaveGrupoMateria() + " and fecha_palis in (" + cad
				+ "))) as x right join grupomatealum gpa on gpa.cve_alum = x.cve_alum where gpa.cve_grumat = "
				+ grupoMateria.getClaveGrupoMateria() + " order by gpa.cve_alum";

		try {
			System.out.println(sql);
			statement = conexion.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			List<RenglonRevision> lista = new ArrayList<>();

			while (rs.next()) {
				RenglonRevision re = new RenglonRevision();
				re.setCalfRenRev(rs.getInt(2) > 0 ? "100" : "0");
				re.setCveAlum(rs.getString(1));
				re.setFechaRenRev(LocalDate.now());
				re.setCveRev(String.valueOf(r.getCveRev()));
				lista.add(re);
				tablaRenglonRevision.agregar(re);
			}

		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	private void crearRevision(GrupoMateria gpo, Unidad u, Revision r) {
		sql = "select gpa.cve_alum, ifnull(suma,-1) from (select cve_alum, sum(estatus_palisalu) suma from paselista pl join paselistaalumno pla on pla.cve_palis = pl.cve_palis where cve_grumat = "
				+ gpo.getClaveGrupoMateria()
				+ " and pl.fecha_palis >= ( select min(fecha_renRev ) from criteriounidad cu join revision re on re.cve_criuni = cu.cve_criuni join renglonrevision rr on rr.cve_rev = re.cve_rev where cve_uni = "
				+ u.getClave() + " and cve_grumat = " + gpo.getClaveGrupoMateria()
				+ ") and pl.fecha_palis <= curdate() group by cve_alum having suma = (select count(*) from paselista where cve_grumat = "
				+ gpo.getClaveGrupoMateria()
				+ " and fecha_palis >= (select min(fecha_renRev ) from criteriounidad cu join revision re on re.cve_criuni = cu.cve_criuni join renglonrevision rr on rr.cve_rev = re.cve_rev where cve_uni = "
				+ u.getClave() + " and cve_grumat = " + gpo.getClaveGrupoMateria()
				+ ") and fecha_palis <= curdate())) as x right join grupomatealum gpa on gpa.cve_alum = x.cve_alum where gpa.cve_grumat = "
				+ gpo.getClaveGrupoMateria() + " order by gpa.cve_alum";

		try {
			statement = conexion.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			List<RenglonRevision> lista = new ArrayList<>();

			while (rs.next()) {
				RenglonRevision re = new RenglonRevision();
				re.setCalfRenRev(rs.getInt(2) > 0 ? "100" : "0");
				re.setCveAlum(rs.getString(1));
				re.setFechaRenRev(LocalDate.now());
				re.setCveRev(String.valueOf(r.getCveRev()));
				lista.add(re);
				tablaRenglonRevision.agregar(re);
			}

		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	private LocalDate getFechaInicioEv() {
		sql = "select min(fecha_renRev ) from criteriounidad cu join revision re on re.cve_criuni = cu.cve_criuni join renglonrevision rr on rr.cve_rev = re.cve_rev where cve_uni = "
				+ unidad.getClave() + " and cve_grumat = " + grupoMateria.getClaveGrupoMateria();

		try {
			System.out.println(sql);
			statement = conexion.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				if (rs.getString(1) != null) {
					return LocalDate.parse(rs.getString(1), dTF);
				}
				return null;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
