package vista;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderTabla extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long[] celdas;

	public RenderTabla(Long[] bajaAlumnos) {
		this.celdas = bajaAlumnos;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		Color c = getBackground();
		Long dato;
		try {
			dato = Long.parseLong(table.getValueAt(row, 1).toString());
		} catch (Exception e) {
			System.out.println("te cahce");
			dato = -1l;
		}
		if (linealSearch(dato) > -1) {
			this.setBackground(Color.RED);
			this.setForeground(Color.white);
		} else {
			if (row % 2 == 0) {
				this.setBackground(new Color(204, 234, 231));
			} else {
				this.setBackground(new Color(171, 185, 184));

			}

			this.setForeground(Color.black);

		}
		if (isSelected) {
			this.setBackground(c);
			this.setForeground(Color.white);
		}

		this.setOpaque(true);
		this.setHorizontalAlignment(SwingConstants.CENTER);

		return this;

	}

	private int linealSearch(Long dato) {
		for (int i = 0; i < celdas.length; i++)
			if (celdas[i].compareTo(dato) == 0)
				return i;
		return -1;
	}

}