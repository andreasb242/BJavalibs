package butti.javalibs.controls.listcontrol.searchmodules;

import javax.swing.JComponent;

import butti.javalibs.controls.listcontrol.SearchListener;




/**
 * Verantwortlich für die Suche und die Darstellung des zugehörigen JComponentes
 * 
 * @author Andreas Butti
 */
public interface SearchModul {
	/**
	 * Gibt den JComponent zurück der in der Liste dargestellt wird
	 * 
	 * @return Der Suchkomponent
	 */
	public JComponent getComponent();

	/**
	 * Fügt einen Searchlistener hinzu
	 * 
	 * @param searchListener
	 *            Der Listener
	 */
	public void addSearchListener(SearchListener searchListener);

	/**
	 * Entfernt einen Searchlistener
	 * 
	 * @param searchListener
	 *            Der Listener
	 */
	public void removeSearchListener(SearchListener searchListener);

	/**
	 * Gibt den Wert zurück nach dem gesucht werden soll
	 * 
	 * @return Suchwert
	 */
	public Object getValue();

	/**
	 * Ob das Feld angezeigt werden soll oder durch den Filter ausgeschlosse
	 * wurde
	 * 
	 * @param object
	 *            Der Wert
	 * @return true wenn es angezeigt werden soll
	 */
	public boolean showField(Object object);

	/**
	 * Setzt den Filter auf den angegeben Parameter
	 * 
	 * @param filter
	 *            Der Filter
	 */
	public void setFilter(Object filter);
}
