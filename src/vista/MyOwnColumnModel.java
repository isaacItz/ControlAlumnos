package vista;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

public class MyOwnColumnModel extends DefaultTableColumnModel {

	private static final long serialVersionUID = 1L;
	private Integer[] arr;

	public MyOwnColumnModel() {
		llenarArr();
	}

	@Override
	public void addColumn(TableColumn tableColumn) {
		// Obtenemos el numero de la columna.

		switch (getColumnCount()) {
		case 1:
			break;
		case 2:
			tableColumn.setPreferredWidth(80);
			tableColumn.setCellEditor(new DefaultCellEditor(getComboSexo()));
			break;
		}

		super.addColumn(tableColumn);
	}

	private void llenarArr() {
		arr = new Integer[100];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = i + 1;
		}
	}

	private JComboBox<Integer> getComboSexo() {
		JComboBox<Integer> combo = new JComboBox<Integer>(arr);

		return combo;
	}
}