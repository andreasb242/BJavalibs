package butti.javalibs.controls.listcontrol;

import javax.swing.table.AbstractTableModel;

/**
 * Das Model der Sortable Table, ein erweitertes TableModel
 * 
 * @author Andreas Butti
 * 
 */
public abstract class AbstractSortableTableModel extends AbstractTableModel
		implements SortableTableModel {
	private static final long serialVersionUID = 1L;

	@Override
	public String getColumnName(int column) {
		return getHeader()[column].toString();
	}

	public int getColumnCount() {
		return getHeader().length;
	}

}
