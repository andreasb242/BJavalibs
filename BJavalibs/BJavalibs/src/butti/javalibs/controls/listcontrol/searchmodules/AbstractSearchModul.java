package butti.javalibs.controls.listcontrol.searchmodules;

import java.util.Vector;

import butti.javalibs.controls.listcontrol.SearchListener;




/**
 * Abstraktes Searchmodul (nur Listener implementiert)
 * 
 * @author Andreas Butti
 * 
 */
public abstract class AbstractSearchModul implements SearchModul {
	private Vector<SearchListener> listeners = new Vector<SearchListener>();

	@Override
	public void addSearchListener(SearchListener searchListener) {
		listeners.add(searchListener);
	}

	@Override
	public void removeSearchListener(SearchListener searchListener) {
		listeners.remove(searchListener);
	}

	/**
	 * Etwas an der Suche hat sich verändert, Daten aktualisieren
	 */
	protected void fireSearchChanged() {
		for (SearchListener s : listeners) {
			s.filterChanged(this);
		}
	}

}
