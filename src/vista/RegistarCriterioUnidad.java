package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import modelo.Criterio;
import modelo.CriterioUnidad;
import modelo.GrupoMateria;
import modelo.Unidad;
import tablas.TablaCriterio;
import tablas.TablaCriterioUnidad;
import tablas.TablaGrupoMateria;
import tablas.TablaMateria;
import tablas.TablaRevision;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;

public class RegistarCriterioUnidad extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelSupA;
	private JPanel panelInf;
	private JButton btnAsignarCriterios;
	private JButton btnCancelar;
	private JScrollPane scrollTablaC;
	private JTable tablaCri;
	private DefaultTableModel dm;
	private boolean tama;
	private JLabel lblPDisp;
	private JLabel lblPUsado;
	private int puntajeUsado;
	private int puntajeDisp;
	private JComboBox<Integer> comb;
	private JButton btnRemoverC;
	private Unidad[] u;
	private List<Integer> listaValores;
	private TablaCriterio tablaCriterio;
	private TablaMateria tablaMateria;
	private JPanel panelSup;
	private JPanel panelSupI;
	private JPanel panel_1;
	private JLabel lblIngreseElNombre;
	private JTextField textFieldNomCri;
	private JScrollPane scrollPane;
	private JList<String> list;
	private DefaultListModel<String> dmNomCri;
	private Criterio[] criterios;
	private JButton btnAgregarNuevoCriterio;
	private TablaGrupoMateria tablaGrupoMateria;
	private TablaRevision tablaRevision;
	private GrupoMateria grupo;
	private modelo.CriterioUnidad[] criteriosU;
	private JButton btnAgregar;
	private JPanel panel;
	private JLabel lblNewLabel;
	private JComboBox<String> comboBoxNomCri;
	private JComboBox<Integer> comboBoxCantCri;
	private JLabel lblValor;
	private JLabel label_3;
	private List<CriterioUnidad> criteriosEliminados;

	/**
	 * Create the panel.
	 */
	public RegistarCriterioUnidad(Connection conexion, GrupoMateria gpo, Container com, Unidad... u) {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));

		grupo = gpo;
		listaValores = new ArrayList<Integer>();
		criteriosEliminados = new ArrayList<CriterioUnidad>();
		dmNomCri = new DefaultListModel<>();
		tablaMateria = new TablaMateria(conexion);
		tablaRevision = new TablaRevision(conexion);
		tablaGrupoMateria = new TablaGrupoMateria(conexion);
		this.u = u;
		puntajeDisp = 100;
		tablaCriterio = new TablaCriterio(conexion);
		criterios = tablaCriterio.getCriterios();
		puntajeUsado = 0;

		panelInf = new JPanel();
		panelInf.setBackground(new Color(102, 153, 153));
		add(panelInf, BorderLayout.SOUTH);
		panelInf.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnAsignarCriterios = new JButton();
		btnAsignarCriterios.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		btnAsignarCriterios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validarCols()) {

					TablaCriterioUnidad tablaCrit = new TablaCriterioUnidad(conexion);
					Unidad[] unidades = null;
					if (u.length > 1) {
						unidades = tablaMateria.getUnidades(tablaMateria.getMateria(gpo.getCveMateria()));

						for (int i = 0; i < unidades.length; i++) {
							for (int i1 = 0; i1 < dm.getRowCount(); i1++) {
								modelo.CriterioUnidad c = new modelo.CriterioUnidad();
								c.setClaveUnidad(unidades[i].getClave());
								c.setGrupoMateria(gpo.getClaveGrupoMateria());
								Criterio cri = new Criterio();
								cri.setNombre(dm.getValueAt(i1, 1).toString());
								if (!tablaCriterio.existe(cri))
									tablaCriterio.agregar(cri);

								cri = tablaCriterio.buscar(cri);
								c.setCvecri(cri.getClave());
								c.setValor(dm.getValueAt(i1, 2).toString());
								System.out.println(tablaCrit.agregar(c));
							}
						}

					} else {

						if (criteriosEliminados.size() > 0) {
							for (CriterioUnidad cri : criteriosEliminados) {
								if (tablaRevision.existeRevision(cri)) {
									tablaRevision.borrarRevisiones(cri);
								}

								tablaCrit.eliminar(cri);
							}
						}

						for (modelo.CriterioUnidad cri : criteriosU) {
							System.out.println(cri);
							tablaCrit.modificar(cri);
						}
						for (int i = criteriosU.length; i < dm.getRowCount(); i++) {
							modelo.CriterioUnidad c = new modelo.CriterioUnidad();
							c.setClaveUnidad(RegistarCriterioUnidad.this.u[0].getClave());
							c.setGrupoMateria(gpo.getClaveGrupoMateria());
							Criterio cri = new Criterio();
							cri.setNombre(dm.getValueAt(i, 1).toString());
							if (!tablaCriterio.existe(cri))
								tablaCriterio.agregar(cri);

							cri = tablaCriterio.buscar(cri);
							c.setCvecri(cri.getClave());
							c.setValor(dm.getValueAt(i, 2).toString());
							System.out.println(tablaCrit.agregar(c));
						}
					}

					JOptionPane.showMessageDialog(null, "Criterios Registrados");
					com.removeAll();
					com.repaint();

				}
			}
		});
		btnAsignarCriterios.setText("Asignar Criterios");
		panelInf.add(btnAsignarCriterios);

		btnCancelar = new JButton();
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Hola soy nuebo");
			}
		});
		btnCancelar.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		btnCancelar.setText("Cancelar");
		panelInf.add(btnCancelar);

		tablaCri = new JTable();
		tablaCri.setBackground(new Color(204, 204, 204));
		tablaCri.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tablaCri.setRowHeight(25);
		tablaCri.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (tablaCri.rowAtPoint(e.getPoint()) > -1) {
					añadirDatosCombCri(tablaCri.getValueAt(tablaCri.getSelectedRow(), 1).toString());
					comboBoxCantCri.setEnabled(true);
					comboBoxNomCri.setEnabled(true);
				} else {
					comboBoxCantCri.setEnabled(true);
					comboBoxNomCri.setEnabled(true);
				}

			}
		});
		tablaCri.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// tablaCri.setDefaultRenderer(Object.class, new RenderTablaCriterioUnidad());

		String[] titles = { "Num Criterio", "Nombre", "Valor" };
		dm = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			// Class<?>[] types = new Class[] { String.class, String.class, JComboBox.class
			// };
			//
			// public Class<?> getColumnClass(int columnIndex) {
			// return types[columnIndex];
			// }

			@Override
			public boolean isCellEditable(int f, int c) {
//				if (c < tablaCri.getColumnCount() - 1)
				return false;
//				return true;
			}

		};

		dm.setColumnIdentifiers(titles);

		tablaCri.setFillsViewportHeight(true);
		scrollTablaC = new JScrollPane(tablaCri);
		setComb();
		add(scrollTablaC, BorderLayout.CENTER);

		panelSup = new JPanel();
		panelSup.setBackground(new Color(153, 204, 204));
		add(panelSup, BorderLayout.NORTH);
		panelSup.setLayout(new BorderLayout(0, 0));
		panelSupA = new JPanel();
		panelSupA.setBackground(new Color(153, 204, 204));
		panelSup.add(panelSupA, BorderLayout.NORTH);
		panelSupA.setLayout(new GridLayout(2, 3, 0, 0));

		JLabel label_1 = new JLabel("Materia: " + tablaMateria.getMateria(gpo.getCveMateria()).getNombre());
		label_1.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		panelSupA.add(label_1);

		JLabel label_2 = new JLabel("Grupo: " + gpo.getNombreGrupo());
		label_2.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		panelSupA.add(label_2);
		String title = u.length > 1 ? "Todas Las Unidades" : u[0].getNombre();
		JLabel label = new JLabel("Unidad: " + title);
		label.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		panelSupA.add(label);

		lblPDisp = new JLabel();
		lblPDisp.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		panelSupA.add(lblPDisp);
		lblPUsado = new JLabel();
		lblPUsado.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		panelSupA.add(lblPUsado);

		panelSupI = new JPanel();
		panelSupI.setBackground(new Color(153, 204, 204));
		panelSupI.setBorder(
				new TitledBorder(null, "Buscador de Criterios", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelSup.add(panelSupI, BorderLayout.CENTER);
		panelSupI.setLayout(new BorderLayout(0, 0));

		panel_1 = new JPanel();
		panel_1.setBackground(new Color(153, 204, 204));
		panelSupI.add(panel_1, BorderLayout.NORTH);

		lblIngreseElNombre = new JLabel("Ingrese el Nombre del Criterio: ");
		lblIngreseElNombre.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		panel_1.add(lblIngreseElNombre);

		textFieldNomCri = new JTextField();
		textFieldNomCri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (list.getSelectedIndex() > -1) {
					if (puntajeDisp > 0) {
						String fila = list.getSelectedValue();
						if (fila != null && !existeFila(fila)) {
							agregarFila(fila);

						} else
							JOptionPane.showMessageDialog(null, "Criterio ya Agregado");
					} else {

						JOptionPane.showMessageDialog(null, "Puntaje Maximo Alcanzado");
					}
					textFieldNomCri.setText("");

				}
			}
		});
		textFieldNomCri.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		panel_1.add(textFieldNomCri);
		textFieldNomCri.setColumns(30);

		textFieldNomCri.getDocument().addDocumentListener(new DocumentListener() {

			private void methodz() {

				if (!textFieldNomCri.getText().isEmpty()) {
					int lon = 0;
					String[] criter = getCriterios(textFieldNomCri.getText());
					dmNomCri.removeAllElements();
					lon = criter.length;

					for (String s : criter)
						dmNomCri.addElement(s);
					Dimension d = new Dimension(0, lon * 20);
					if (d.getHeight() < 160)
						scrollPane.setPreferredSize(d);
					else if (d.getHeight() >= 160)
						scrollPane.setPreferredSize(new Dimension(0, 160));

					validate();
				}

				else {
					agregarTodosCriterios();
				}
				list.setSelectedIndex(0);
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

		btnAgregarNuevoCriterio = new JButton("Agregar Nuevo Criterio");
		btnAgregarNuevoCriterio.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		btnAgregarNuevoCriterio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String criterioNuevo = JOptionPane.showInputDialog(null, "", "Nuevo Criterio",
						JOptionPane.QUESTION_MESSAGE);
				criterioNuevo = hacerMayus(criterioNuevo);
				System.out.println(criterioNuevo);
				if (criterioNuevo != null) {
					String posibleCriterio = buscarCoincidencias(criterioNuevo);
					if (posibleCriterio != null) {
						JOptionPane.showMessageDialog(null, "Criterio ya Agregado");
					} else {
						Criterio cr = new Criterio();
						cr.setNombre(criterioNuevo);
						if (!tablaCriterio.existe(cr)) {
							tablaCriterio.agregar(cr);
						}
						JOptionPane.showMessageDialog(null, "Agregado");
						criterios = tablaCriterio.getCriterios();
						list.setSelectedValue(posibleCriterio, true);
					}
					textFieldNomCri.setText(posibleCriterio);
					textFieldNomCri.requestFocus();
				} else
					JOptionPane.showMessageDialog(null, "Cancelado");

			}
		});

		btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (puntajeDisp > 0) {
					String fila = list.getSelectedValue();
					if (fila != null && !existeFila(fila)) {
						agregarFila(fila);
					} else
						JOptionPane.showMessageDialog(null, "Criterio ya Agregado");
					textFieldNomCri.requestFocus();
				} else {

					JOptionPane.showMessageDialog(null, "Puntaje Maximo Alcanzado");
				}

			}
		});
		btnAgregar.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		panel_1.add(btnAgregar);
		panel_1.add(btnAgregarNuevoCriterio);

		btnRemoverC = new JButton();
		btnRemoverC.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		panel_1.add(btnRemoverC);
		btnRemoverC.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				int fila = tablaCri.getSelectedRow();
				if (fila > -1) {
					if (fila < criteriosU.length) {
						if (tablaRevision.existeRevision(criteriosU[fila])) {
							Criterio c = new Criterio();
							c.setClave(criteriosU[fila].getCvecri());
							c = tablaCriterio.getCriterio(c);
							int op = JOptionPane.showConfirmDialog(null, "Criterio " + c.getNombre()
									+ " Ya Cuenta con Revisiones \n¿Seguro que Desea Eliminarlo?");
							if (op == JOptionPane.YES_OPTION) {
								criteriosEliminados.add(criteriosU[fila]);

								for (int i = fila; i < criteriosU.length - 1; i++) {
									criteriosU[i] = criteriosU[i + 1];
								}
								criteriosU = Arrays.copyOf(criteriosU, criteriosU.length - 1);
								dm.removeRow(fila);
								listaValores.remove(fila);
								establecerCantidades();
							}

						} else {
							criteriosEliminados.add(criteriosU[fila]);
							for (int i = fila; i < criteriosU.length - 1; i++) {
								criteriosU[i] = criteriosU[i + 1];
							}
							criteriosU = Arrays.copyOf(criteriosU, criteriosU.length - 1);
							dm.removeRow(fila);
							listaValores.remove(fila);
							establecerCantidades();
						}

					} else {
						dm.removeRow(fila);
						listaValores.remove(fila);
						establecerCantidades();
					}

					comboBoxCantCri.removeAllItems();
					comboBoxNomCri.removeAllItems();
					if (tablaCri.getRowCount() > 0) {
						tablaCri.changeSelection(tablaCri.getRowCount() - 1, 1, false, false);
						añadirDatosCombCri(tablaCri.getValueAt(tablaCri.getSelectedRow(), 1).toString());
					} else {
						comboBoxCantCri.setEnabled(false);
						comboBoxNomCri.setEnabled(false);
					}

				} else {
					JOptionPane.showMessageDialog(null, "Seleccione Una Fila Para Eliminar");
				}

			}
		});
		btnRemoverC.setText("Remover Criterio");
		list = new JList<>(dmNomCri);
		list.setBackground(new Color(204, 204, 255));
		list.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(list);
		scrollPane.setPreferredSize(new Dimension(0, 0));
		panelSupI.add(scrollPane, BorderLayout.CENTER);

		panel = new JPanel();
		panel.setBorder(new EmptyBorder(11, 0, 11, 0));
		panel.setBackground(new Color(102, 153, 153));
		panelSup.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(0, 6, 0, 0));

		label_3 = new JLabel("");
		panel.add(label_3);

		lblNewLabel = new JLabel("Criterio:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Yu Gothic Medium", Font.PLAIN, 15));
		panel.add(lblNewLabel);

		comboBoxNomCri = new JComboBox<>();
		comboBoxNomCri.setFont(new Font("Arial", Font.PLAIN, 14));
		panel.add(comboBoxNomCri);

		lblValor = new JLabel("Valor:");
		lblValor.setHorizontalAlignment(SwingConstants.CENTER);
		lblValor.setFont(new Font("Yu Gothic Medium", Font.PLAIN, 15));
		panel.add(lblValor);

		comboBoxCantCri = new JComboBox<>();
		comboBoxCantCri.setFont(new Font("Arial", Font.PLAIN, 14));
		panel.add(comboBoxCantCri);

		comboBoxCantCri.addActionListener(new OyenteCombCant());
		comboBoxNomCri.addActionListener(new OyenteCombNom());

		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {

					if (puntajeDisp > 0) {
						String fila = list.getSelectedValue();
						if (fila != null && !existeFila(fila)) {
							agregarFila(fila);

						} else
							JOptionPane.showMessageDialog(null, "Criterio ya Agregado");
						textFieldNomCri.requestFocus();
					} else {

						JOptionPane.showMessageDialog(null, "Puntaje Maximo Alcanzado");
					}
				}
			}
		});
		textFieldNomCri.requestFocus();
		comboBoxCantCri.setEnabled(false);
		comboBoxNomCri.setEnabled(false);
		cargarCriterios();
		establecerCantidades();
		setTamañoCols();
		agregarTodosCriterios();
		textFieldNomCri.requestFocus();

	}

	private String hacerMayus(String cad) {
		cad = cad.trim();
		String retorno = "";
		String arr[] = cad.split(" ");
		for (String cad2 : arr) {
			if (cad2.length() > 0 && Character.isAlphabetic(cad2.charAt(0))) {
				retorno += cad2.substring(0, 1).toUpperCase().concat(cad2).substring(1).concat(" ");
			}

		}
		return retorno.trim();
	}

	private String buscarCoincidencias(String p) {
		for (Criterio criterio : criterios) {
			if (criterio.getNombre().equalsIgnoreCase(p)) {
				return criterio.getNombre();
			}
		}
		return null;
	}

	private void agregarTodosCriterios() {
		int lon = 0;
		dmNomCri.removeAllElements();
		for (Criterio criterio : criterios) {
			dmNomCri.addElement(criterio.getNombre());
		}
		lon = dmNomCri.getSize();
		Dimension d = new Dimension(0, lon * 20);
		if (d.getHeight() < 160)
			scrollPane.setPreferredSize(d);
		else if (d.getHeight() >= 160)
			scrollPane.setPreferredSize(new Dimension(0, 160));

		validate();
	}

	private boolean existeFila(String criterio) {
		for (int i = 0; i < dm.getRowCount(); i++) {
			if (tablaCri.getValueAt(i, 1).equals(criterio)) {
				return true;
			}
		}
		return false;
	}

	private void agregarFila(String criterio) {
		dm.addRow(new Object[] { dm.getRowCount() + 1, criterio, 0 });
		listaValores.add(0);
		tablaCri.changeSelection(tablaCri.getRowCount() - 1, 1, false, false);
		añadirDatosCombCri(tablaCri.getValueAt(tablaCri.getSelectedRow(), 1).toString());
		comboBoxCantCri.setEnabled(true);
		comboBoxNomCri.setEnabled(true);
	}

	private String[] getCriterios(String pattern) {
		List<String> lis = new ArrayList<>();
		for (Criterio c : criterios) {
			if (c.getNombre().toLowerCase().startsWith(pattern.toLowerCase())) {
				lis.add(c.getNombre());
			}
		}
		return lis.toArray(new String[lis.size()]);
	}

	private void cargarCriterios() {
		criteriosU = tablaGrupoMateria.getCriteriosUnidad(grupo, u[0]);
		for (modelo.CriterioUnidad crit : criteriosU) {
			Criterio crite = new Criterio();
			crite.setClave(crit.getCvecri());
			dm.addRow(new Object[] { dm.getRowCount() + 1, tablaCriterio.getCriterio(crite).getNombre(),
					crit.getValor() });
			int val = Integer.parseInt(crit.getValor());
			listaValores.add(val);
		}

	}

	private void setTamañoCols() {
		TableColumn column = null;

		int total = scrollTablaC.getWidth();

		column = tablaCri.getColumnModel().getColumn(0);
		column.setPreferredWidth((int) (total * .15));
		column = tablaCri.getColumnModel().getColumn(1);
		column.setPreferredWidth((int) (total * .70));
		column = tablaCri.getColumnModel().getColumn(2);
		column.setPreferredWidth((int) (total * .15));

		if (!tama) {
			tama = true;
		}
	}

	private int sumaArr(List<Integer> lista) {
		int res = 0;
		for (Integer integer : lista) {
			res += integer;
		}
		return res;
	}

	private void establecerCantidades() {

		puntajeUsado = sumaArr(listaValores);
		puntajeDisp = 100 - puntajeUsado;
		lblPUsado.setText("Puntaje Usado: " + String.valueOf(puntajeUsado));
		lblPDisp.setText("Puntaje Disponible: " + String.valueOf(puntajeDisp));
	}

	private void setComb() {
		comb = new JComboBox<>();
		Integer[] ar = new Integer[101];
		for (int i = 0; i < ar.length; i++) {
			comb.addItem(i);
		}
		comb.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				int val = Integer.parseInt(tablaCri.getValueAt(tablaCri.getSelectedRow(), 2).toString());
				if (val != comb.getSelectedIndex()) {
					comb.setSelectedItem(comb.getItemAt(val));
				}
			}
		});
		comb.addActionListener(new oyenteCombo());
		tablaCri.setModel(dm);
		tablaCri.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comb));
		tablaCri.setDefaultRenderer(Object.class, new RenderTablaCriterio());

	}

	private class oyenteCombo implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (comb.getSelectedIndex() > -1 && tablaCri.getSelectedRow() > -1) {
				int valor = Integer.parseInt(tablaCri.getValueAt(tablaCri.getSelectedRow(), 2).toString());
				// int valor = comb.getSelectedIndex();

				if ((int) comb.getSelectedItem() > puntajeDisp
						&& ((sumaArr(listaValores) + valor - listaValores.get(tablaCri.getSelectedRow())) > 100)) {
					if (puntajeUsado == 100) {
						listaValores.set(tablaCri.getSelectedRow(), listaValores.get(tablaCri.getSelectedRow()));
						tablaCri.setValueAt(listaValores.get(tablaCri.getSelectedRow()), tablaCri.getSelectedRow(), 2);
					} else {
						int val = puntajeDisp + listaValores.get(tablaCri.getSelectedRow());
						listaValores.set(tablaCri.getSelectedRow(), val);
						tablaCri.setValueAt(val, tablaCri.getSelectedRow(), 2);
					}

				} else {
					listaValores.set(tablaCri.getSelectedRow(), valor);
				}
				establecerCantidades();

			}

		}

	}

	private void añadirDatosCombCri(String valor) {
		comboBoxNomCri.removeAllItems();
		comboBoxCantCri.removeAllItems();
		for (Criterio criterio : criterios) {
			int i = 0;
			while (i < tablaCri.getRowCount() && !criterio.getNombre().equals(tablaCri.getValueAt(i, 1).toString())) {
				i++;
			}
			if (i == tablaCri.getRowCount()) {
				comboBoxNomCri.addItem(criterio.getNombre());
			}
		}
		comboBoxNomCri.addItem(valor);
		comboBoxNomCri.setSelectedItem(valor);

		int valorActual = Integer.parseInt(tablaCri.getValueAt(tablaCri.getSelectedRow(), 2).toString());
		int limite = valorActual + puntajeDisp;
		for (int i = 1; i <= limite; i++) {
			comboBoxCantCri.addItem(i);
		}
		comboBoxCantCri.setSelectedItem(valorActual);

	}

	private class OyenteCombNom implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (tablaCri.getSelectedRow() > -1) {
				if (comboBoxNomCri.getItemCount() > 0) {
					tablaCri.setValueAt(comboBoxNomCri.getSelectedItem(), tablaCri.getSelectedRow(), 1);
					if (tablaCri.getSelectedRow() < criteriosU.length) {
						Criterio cri = new Criterio();
						cri.setNombre(comboBoxNomCri.getSelectedItem().toString());
						cri = tablaCriterio.buscar(cri);
						criteriosU[tablaCri.getSelectedRow()].setCvecri(cri.getClave().toString());
					}
				}
			}
		}

	}

	private class OyenteCombCant implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (tablaCri.getSelectedRow() > -1) {
				if (comboBoxCantCri.getItemCount() > 0) {
					tablaCri.setValueAt(comboBoxCantCri.getSelectedItem(), tablaCri.getSelectedRow(), 2);
					if (tablaCri.getSelectedRow() < criteriosU.length) {
						criteriosU[tablaCri.getSelectedRow()].setValor(comboBoxCantCri.getSelectedItem().toString());
					}
					listaValores.set(tablaCri.getSelectedRow(),
							Integer.valueOf(comboBoxCantCri.getSelectedItem().toString()));
					establecerCantidades();
				}

			}
		}

	}

	public JButton getBotonCancelar() {
		return btnCancelar;
	}

	private boolean validarCols() {
		for (int i = 0; i < dm.getRowCount(); i++) {

			if (dm.getValueAt(i, 2).equals(0)) {
				JOptionPane.showMessageDialog(null, "Asigne un Valor Correcto a los Criterios");
				return false;
			}

			if (puntajeUsado < 100) {
				JOptionPane.showMessageDialog(null, "El Puntaje Total No es 100");
				return false;
			}

		}
		return true;
	}

}