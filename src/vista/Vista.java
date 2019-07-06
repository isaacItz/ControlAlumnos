package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import modelo.Alumno;
import modelo.BaseDatos;
import modelo.Docente;
import modelo.EstructuraLista;
import modelo.Materia;
import modelo.Periodo;
import modelo.TablaLista;
import modelo.Utileria;
import tablas.TablaDocente;
import tablas.TablaMateria;
import tablas.Tablaperiodo;

public class Vista extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Fondo fond;
	private BaseDatos bd;
	public static Font fuentePrincipal;
	public static Image icon;
	public static Docente docente;
	public static Periodo periodo;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Vista frame = new Vista();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Vista() {
		fuentePrincipal = crearFuete();
		fuentePrincipal = new Font("Arial Rounded MT Bold", Font.PLAIN, 15);
		icon = Toolkit.getDefaultToolkit().getImage(Vista.class.getResource("/recursos/petirrojos.png"));
		setIconImage(icon);

		try {
			setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		bd = new BaseDatos("escuela", "root", "maracana");
		bd.setDriver("com.mysql.jdbc.Driver");
		bd.setProtocolo("jdbc:mysql://localhost/");
		JPasswordField pf = new JPasswordField();
		int okCxl = -2;
		while (!bd.hacerConexion().equals("Exito")
				&& !(okCxl == JOptionPane.CANCEL_OPTION || okCxl == JOptionPane.CLOSED_OPTION)) {
			pf.requestFocus();
			System.out.println(bd.hacerConexion());

			okCxl = JOptionPane.showConfirmDialog(null, pf, "Contraseña del Manejador", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			pf.grabFocus();
			bd.setPassword(String.copyValueOf(pf.getPassword()));
		}
		if (okCxl == JOptionPane.CANCEL_OPTION || okCxl == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}

		Tablaperiodo tablaperiodo = new Tablaperiodo(bd.getConexion());
		periodo = tablaperiodo.getPeriodoActual();
		TablaDocente tablaDocente = new TablaDocente(bd.getConexion());
		docente = tablaDocente.getPrimero();

		fond = new Fondo("imgs/fon.jpg");
		fond.setToolTipText("Instituto Tecnologico de Zitacuaro");
		fond.setForeground(Color.WHITE);
		setContentPane(fond);
		fond.setLayout(new BorderLayout(0, 0));

		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("TecNM");
		JMenuBar menuBar = new JMenuBar();
		menuBar.setMargin(new Insets(5, 2, 10, 0));
		menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		setJMenuBar(menuBar);

		JMenu mnRegistrar = new JMenu("Alumnos");
		mnRegistrar.setFont(fuentePrincipal);
		mnRegistrar.setForeground(new Color(0, 0, 0));
		mnRegistrar.setBackground(new Color(240, 248, 255));
		menuBar.add(mnRegistrar);

		JMenu mnRegistrar_1 = new JMenu("Registrar");
		mnRegistrar_1.setFont(fuentePrincipal);
		mnRegistrar.add(mnRegistrar_1);

		JMenuItem mntmAlumno = new JMenuItem("Nuevo Alumno");
		mntmAlumno.setFont(fuentePrincipal);
		mnRegistrar_1.add(mntmAlumno);
		mntmAlumno.setForeground(Color.BLACK);
		mntmAlumno.setBackground(new Color(0, 0, 0));

		JMenuItem mntmMedianteArchivo = new JMenuItem("Mediante Archivo");
		mntmMedianteArchivo.setFont(fuentePrincipal);
		mnRegistrar_1.add(mntmMedianteArchivo);
		mntmMedianteArchivo.setForeground(Color.BLACK);
		mntmMedianteArchivo.setBackground(new Color(0, 0, 0));

		JMenuItem mntmBajaAlumno = new JMenuItem("Baja Alumno");
		mntmBajaAlumno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DialogBajaAlum(bd.getConexion());
			}
		});
		mntmBajaAlumno.setFont(fuentePrincipal);
		mnRegistrar.add(mntmBajaAlumno);
		mntmMedianteArchivo.addActionListener(x -> {
			if (registrarAlumnosMedianteArchivo())
				Utileria.escribir("Alumnos Registrados");
			else
				Utileria.escribir("Problemas al registrar los alumnos");
		});
		mntmAlumno.addActionListener(x -> new VistaRegistroAlumno(bd.getConexion()));

		JMenu mnCriterios = new JMenu("Criterios de Evaluacion");
		mnCriterios.setFont(fuentePrincipal);
		mnCriterios.setBackground(new Color(240, 248, 255));
		mnCriterios.setForeground(new Color(0, 0, 0));
		menuBar.add(mnCriterios);

		JMenuItem mntmUnidad = new JMenuItem("Agregar/Modificar");
		mntmUnidad.setFont(fuentePrincipal);
		mntmUnidad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpiarPantalla();
				new RegistroCriterios(bd.getConexion(), getContentPane());

			}
		});
		mntmUnidad.setForeground(Color.BLACK);
		mntmUnidad.setBackground(new Color(0, 0, 0));
		mnCriterios.add(mntmUnidad);

		JMenu mnEvaluacion = new JMenu("Evaluacion");
		mnEvaluacion.setFont(fuentePrincipal);
		mnEvaluacion.setForeground(Color.BLACK);
		menuBar.add(mnEvaluacion);

		JMenuItem mntmEvaluarCriterio = new JMenuItem("Evaluar/Continuar/Actualizar Criterio");
		mntmEvaluarCriterio.setFont(fuentePrincipal);
		mntmEvaluarCriterio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpiarPantalla();
				new SeleccionCalfCrit(bd.getConexion(), getContentPane());
			}
		});
		mnEvaluacion.add(mntmEvaluarCriterio);

		JMenuItem mntmConsultarEvaluacion = new JMenuItem("Consultar Evaluacion");
		mntmConsultarEvaluacion.setFont(fuentePrincipal);
		mntmConsultarEvaluacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpiarPantalla();
				BuscarEvaluaciones bE = new BuscarEvaluaciones(bd.getConexion());

				getContentPane().add(bE, BorderLayout.CENTER);
				repaint();
				setVisible(true);
				bE.getBotonSalir().addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						getContentPane().remove(bE);
						getContentPane().repaint();
					}
				});
			}
		});
		mnEvaluacion.add(mntmConsultarEvaluacion);

		JMenu mnUnidad = new JMenu("Unidades");
		mnUnidad.setFont(fuentePrincipal);
		mnUnidad.setForeground(Color.BLACK);
		menuBar.add(mnUnidad);

		JMenuItem mntmRegistrarUnidad = new JMenuItem("Registrar Unidad");
		mntmRegistrarUnidad.setFont(fuentePrincipal);
		mntmRegistrarUnidad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TablaMateria tablaMateria = new TablaMateria(bd.getConexion());

				Materia[] mat = tablaMateria.getMaterias();
				String[] strings = new String[mat.length];
				for (int i = 0; i < mat.length; i++) {
					strings[i] = (mat[i].getNombre());
				}

				Object opcion = JOptionPane.showInputDialog(null, "Selecciona la materia", "Elegir",
						JOptionPane.QUESTION_MESSAGE, null, strings, strings[0]);
				if (opcion != null) {
					new RegistroUnidades(bd.getConexion(), mat[linealSearch(strings, opcion)]);
				}
			}
		});
		mnUnidad.add(mntmRegistrarUnidad);

		JMenu mnTipoDeLista = new JMenu("Tipo de lista");
		mnTipoDeLista.setFont(fuentePrincipal);
		mnTipoDeLista.setForeground(new Color(0, 0, 0));
		mnTipoDeLista.setBackground(new Color(230, 230, 250));
		menuBar.add(mnTipoDeLista);

		JMenuItem mntmPaseDeLista = new JMenuItem("Pase De Lista");
		mntmPaseDeLista.setFont(fuentePrincipal);
		mntmPaseDeLista.setForeground(Color.BLACK);
		mntmPaseDeLista.setBackground(new Color(0, 0, 0));
		mnTipoDeLista.add(mntmPaseDeLista);
		mntmPaseDeLista.addActionListener(x -> {
			limpiarPantalla();
			PaseLista pase = new PaseLista(bd.getConexion(), getContentPane());
			getContentPane().add(pase);
			repaint();
			setVisible(true);

		});

		JMenuItem mntmBuscarPaseDe = new JMenuItem("Buscar Pase de Lista");
		mntmBuscarPaseDe.setFont(fuentePrincipal);
		mntmBuscarPaseDe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpiarPantalla();
				BuscarPaseLista busc = new BuscarPaseLista(bd.getConexion(), getContentPane());
				getContentPane().add(busc);
				repaint();
				setVisible(true);

			}
		});
		mntmBuscarPaseDe.setForeground(Color.BLACK);
		mntmBuscarPaseDe.setBackground(Color.BLACK);
		mnTipoDeLista.add(mntmBuscarPaseDe);

		JMenuItem mntmEvaluacionDeUnidad = new JMenuItem("Evaluacion de Unidad");
		mntmEvaluacionDeUnidad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		mntmEvaluacionDeUnidad.setBackground(new Color(0, 0, 0));
		mntmEvaluacionDeUnidad.setForeground(Color.BLACK);
		// mnTipoDeLista.add(mntmEvaluacionDeUnidad);
		JMenuItem mntmL = new JMenuItem("Buscar Lista");
		mntmL.setFont(fuentePrincipal);
		mntmL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpiarPantalla();

				VistaBuscarLista vis = new VistaBuscarLista(bd.getConexion());

				getContentPane().add(vis);
				vis.setFocusable(true);
				vis.requestFocusInWindow();
				vis.requestFocus();
				repaint();
				setVisible(true);
				vis.getCancel().addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						getContentPane().remove(vis);
						repaint();
						setVisible(true);

					}
				});

			}
		});
		mntmL.setBackground(new Color(0, 0, 0));
		mntmL.setForeground(Color.BLACK);
		mnTipoDeLista.add(mntmL);

		JMenuItem lblEstablecerFaltasPara = new JMenuItem("Establecer Faltas Para Baja");
		lblEstablecerFaltasPara.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new DialogFaltasAlum(bd.getConexion());
			}
		});
		lblEstablecerFaltasPara.setFont(fuentePrincipal);
		mnTipoDeLista.add(lblEstablecerFaltasPara);
		setSize(900, 600);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(95, 158, 160));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setVisible(true);
	}

	private boolean registrarAlumnosMedianteArchivo() {
		try {
			System.out.println(new File(".").getAbsolutePath());
			String ruta = ".\\src\\recursos\\lista.txt";
			File file = new File(ruta);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			File pdf = Utileria.getArchivo("./src/recursos");
			if (pdf != null) {
				String resultado = Utileria.leerPDF(pdf);
				bw.write(resultado);
				EstructuraLista cabecera = Utileria.geEstructuraLista(resultado);
				String lineas[] = resultado.split("\n");
				Alumno[] alumnos = new Alumno[cabecera.getAlumnos()];
				for (int i = 8; i < lineas.length - 1; i++) {
					String alum[] = Utileria.generarAlumno(lineas[i]);
					alumnos[i - 8] = Utileria.generarAlumno(alum);
				}

				TablaLista lista = new TablaLista(bd.getConexion());
				System.out.println(lista.insertar(cabecera, alumnos));
			}
			bw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void limpiarPantalla() {
		getContentPane().removeAll();
		repaint();
	}

	private Font crearFuete() {
		Font fuente = null;
		try {
			File archivo = new File("src\\fuentes/bahnschrift.ttf");
			fuente = Font.createFont(Font.TRUETYPE_FONT, archivo);
			fuente = fuente.deriveFont(Font.PLAIN, 15);
		} catch (Exception r) {
			r.printStackTrace();
		}
		return fuente;
	}

	public int linealSearch(Object[] arr, Object dato) {

		for (int i = 0; i < arr.length; i++)
			if (arr[i].equals(dato))
				return i;

		return -1;
	}

}
