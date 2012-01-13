package butti.javalibs.controls.listcontrol;

import butti.javalibs.controls.listcontrol.searchmodules.SearchModul;

/**
 * Listener der auf Suchanfragen reagiert
 * 
 * @author Andreas Butti
 * 
 */
public interface SearchListener {
	/**
	 * Suche wurde geändert
	 * 
	 * @param modul
	 *            Das Modul dessen Werte geändert wurden
	 */
	public void filterChanged(SearchModul modul);
}
