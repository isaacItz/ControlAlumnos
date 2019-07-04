package vista;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderTablaCriterio extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// if (column == 2) {
		// JComboBox<Integer> comboBox = new JComboBox<>();
		// comboBox.addItem((Integer) value);
		// return comboBox;
		// }
		// if ((float) row % 2 == 0) {
		// this.setBackground(Color.LIGHT_GRAY);
		// return this;
		// }
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	}
}
