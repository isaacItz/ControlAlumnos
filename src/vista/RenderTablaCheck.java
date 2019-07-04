package vista;

import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderTablaCheck extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long[] celdas;

	public RenderTablaCheck(Long[] bajaAlumnos) {
		this.celdas = bajaAlumnos;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JCheckBox c = new JCheckBox();
		c.setOpaque(true);
		c.setHorizontalAlignment(SwingConstants.CENTER);

		if (value.toString().equals("true") || value.toString().equals("false"))
			try {
				Boolean b = Boolean.parseBoolean(value.toString());
				c.setSelected(b);

			} catch (Exception e) {
				System.err.println(e);
			}

		if (Arrays.binarySearch(celdas, Long.parseLong(table.getValueAt(row, 1).toString())) > -1) {
			c.setBackground(Color.RED);
		} else {
			if (row % 2 == 0) {
				c.setBackground(new Color(204, 234, 231));
			} else {
				c.setBackground(new Color(171, 185, 184));
			}

			c.setForeground(Color.black);

		}
		if (isSelected) {
			c.setBackground(new Color(0, 120, 215));
		}

		return c;

	}

}