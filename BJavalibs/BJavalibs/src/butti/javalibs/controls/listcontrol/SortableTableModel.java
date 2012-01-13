package butti.javalibs.controls.listcontrol;

import javax.swing.table.TableModel;

import butti.javalibs.controls.listcontrol.searchmodules.SearchModul;



public interface SortableTableModel extends TableModel {
	public Object[] getHeader();
	public Object getElementAt(int index);
	public SearchModul getSearchModul(int index);
}
