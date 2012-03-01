package butti.javalibs.controls.listcontrol;

import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterEvent.Type;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import butti.javalibs.controls.listcontrol.searchmodules.NoSearch;
import butti.javalibs.controls.listcontrol.searchmodules.SearchModul;

/**
 * Eine sortierbare Tabelle mit einem Suchfeld pro Spalte
 * 
 * @author Andreas Butti
 * 
 */
public class SortableTable extends JPanel implements SearchListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Die dummy Tabelle mit dem Header zum sortieren
	 */
	private JTable tblHeader;

	/**
	 * Die Liste mit den eigentlichen Daten
	 */
	private JList list;

	/**
	 * Das Scrollpanel für die Liste
	 */
	private JScrollPane listScroll;

	/**
	 * Das Model mit den Daten
	 */
	private SortableTableModel model;

	/**
	 * Zum sortieren und filtern der Tabellle
	 */
	private TableRowSorter<TableModel> rowSorter;

	/**
	 * Das List model (das das schlussendlich angezeigt wird)
	 */
	private SortListModel listModel;

	/**
	 * Ob die Suchfelder angezeigt werden
	 */
	private boolean searchEnabled = false;

	/**
	 * Die Werte nach denen gesucht wird
	 */
	private String[] searchValues;

	/**
	 * Die Suchfelder
	 */
	private Vector<SearchModul> searchFields = new Vector<SearchModul>();

	/**
	 * Das Panel mit dem Tableheader und die Suche
	 */
	private JPanel tableHead;

	/**
	 * Die Höhe der Suchfelder
	 */
	private int searchFieldHeight;

	/**
	 * Erstellt eine neue Sortierbare Tabelle
	 * 
	 * @param model
	 *            Das Model mit den Daten
	 */
	public SortableTable(AbstractSortableTableModel model) {
		this.model = model;

		if (model == null) {
			throw new NullPointerException("model == null");
		}

		tblHeader = new JTable(model);
		listModel = new SortListModel();
		listModel.addListeners();

		tableHead = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void doLayout() {
				doPanelLayout();
			}

		};

		rowSorter = new TableRowSorter<TableModel>(model);

		tblHeader.setRowSorter(rowSorter);

		rowSorter.addRowSorterListener(new RowSorterListener() {

			public void sorterChanged(RowSorterEvent ev) {
				if (ev.getType() == Type.SORTED) {
					sortList();
				}
			}

		});

		tableHead.add(tblHeader.getTableHeader());

		// Grösse wird in <code>doLayout</code> berechet
		tblHeader.getTableHeader().setResizingAllowed(false);
		// Macht keinen Sinn
		tblHeader.getTableHeader().setReorderingAllowed(false);

		RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {

			@Override
			public boolean include(javax.swing.RowFilter.Entry<? extends Object, ? extends Object> entry) {

				for (int i = 0; i < entry.getValueCount(); i++) {
					if (!filter(i, entry.getValue(i))) {
						return false;
					}
				}

				return true;
			}

		};

		rowSorter.setRowFilter(filter);

		// force update
		searchEnabled = true;
		setSearchEnabled(false);

		list = new JList(listModel);

		list.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_END) {
					e.consume();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_END) {
					e.consume();
				}
			}

		});

		listScroll = new JScrollPane(list);

		listScroll.setColumnHeaderView(tableHead);

		add(listScroll);

		int width = (int) Math.max(tblHeader.getPreferredSize().getWidth(), list.getPreferredSize().getWidth());
		int height = (int) (tblHeader.getPreferredSize().getHeight() + list.getPreferredSize().getHeight());
		setPreferredSize(new Dimension(width, height));

		setFocusOrder();
	}

	/**
	 * Header layouten
	 */
	private void doPanelLayout() {
		JTableHeader tableHeader = tblHeader.getTableHeader();
		int header = tableHeader.getPreferredSize().height;
		int headerWidth = getWidth() - 7;

		if (listScroll.getVerticalScrollBar().isVisible()) {
			headerWidth -= listScroll.getVerticalScrollBar().getWidth();
		}

		tableHeader.setBounds(0, 0, headerWidth, header);

		Enumeration<TableColumn> elements = tblHeader.getColumnModel().getColumns();

		int width = headerWidth / tblHeader.getColumnModel().getColumnCount();
		int restWidth = headerWidth % tblHeader.getColumnModel().getColumnCount();

		while (elements.hasMoreElements()) {
			TableColumn t = elements.nextElement();

			// Nicht aufteilbare Pixel verteilen...
			if (restWidth > 0) {
				restWidth--;
				t.setWidth(width + 1);
			} else {
				t.setWidth(width);
			}
		}

		restWidth = headerWidth % tblHeader.getColumnModel().getColumnCount();

		if (searchEnabled) {
			int x = 0;
			int w;
			for (SearchModul mod : searchFields) {
				w = width;
				if (restWidth > 0) {
					restWidth--;
					w++;
				}
				mod.getComponent().setBounds(x, header, w, this.searchFieldHeight);
				x += w;
			}
		}
	}

	@Override
	public void doLayout() {
		listScroll.setBounds(0, 0, getWidth(), getHeight());
	}

	/**
	 * Fokus von links nach rechts setzen
	 */
	private void setFocusOrder() {
		Vector<JComponent> focusableComponent = new Vector<JComponent>();

		focusableComponent.add(list);

		for (int i = 0; i < model.getColumnCount(); i++) {
			if (model.getSearchModul(i) == null || model.getSearchModul(i).getComponent() == null) {
				continue;
			}
			focusableComponent.add(model.getSearchModul(i).getComponent());
		}

		// Setzt die richtige Tabulatorenreihenfolge
		FocusTraversalPolicy policy = getFocusTraversal(focusableComponent.toArray(new JComponent[focusableComponent.size()]));

		setFocusTraversalPolicy(policy); // setzen
		setFocusCycleRoot(true);// enablen
	}

	/**
	 * Gibt die Reihenfolge der in order[] gespeicherten Componenten als
	 * FocusTraversalPolicy Objekt zurück
	 * 
	 * Quelle:
	 * http://java-forum.org.server659-han.de-nserver.de/java-faq-beitraege
	 * /12511-focus-setzen-und-reihenfolge-festlegen.html
	 * 
	 * @param order
	 *            sind die Componenten in richtiger Reihenfolge und notfalls
	 *            muss eine Typkonvertierung mittels (JComponent) durchgeführt
	 *            werden
	 * @return das Objekt mit dem man mit
	 *         setFocusTraversalPolicy(FocsTraersalPolicy) und aktivieren mit
	 *         setFocusCycleRoot(true)
	 */
	public static FocusTraversalPolicy getFocusTraversal(final JComponent order[]) {
		FocusTraversalPolicy policy = new FocusTraversalPolicy() {
			java.util.List<JComponent> list = java.util.Arrays.asList(order);

			public java.awt.Component getFirstComponent(java.awt.Container focusCycleRoot) {
				return order[0];
			}

			public java.awt.Component getLastComponent(java.awt.Container focusCycleRoot) {
				return order[order.length - 1];
			}

			public java.awt.Component getComponentAfter(java.awt.Container focusCycleRoot, java.awt.Component aComponent) {
				int index = 0, x = -1;
				index = list.indexOf(aComponent);
				index++; // automatisch erhöht, sodaß er unten nichts
				// wegzeiehn muß
				// er geht rein entweder wenn es disabled ist oder wenn es nicht
				// angezeigt wird
				if (!order[index % order.length].isEnabled() || !order[index % order.length].isVisible()) {
					x = index;
					index = -1;
					// zuerst die Schleife nach hinten
					for (; x != order.length; x++) {
						if (order[x].isEnabled() && order[x].isVisible()) {
							index = x;
							break;
						}
					}
					if (index == -1) {
						x = list.indexOf(aComponent);
						for (int y = 0; y <= x; y++) {
							if (order[y].isEnabled() && order[x].isVisible()) {
								index = y;
								break;
							}
						}
					}
				}
				return order[index % order.length];
			}

			public java.awt.Component getComponentBefore(java.awt.Container focusCycleRoot, java.awt.Component aComponent) {
				int index = list.indexOf(aComponent);
				int x = -1;
				index--;
				if (!order[(index + order.length) % order.length].isEnabled() || !order[(index + order.length) % order.length].isVisible()) {
					x = index;
					index = -1;
					for (; x >= 0; x--) {
						if (order[x].isEnabled() && order[x].isVisible()) {
							index = x;
							break;
						}
					}
					// wenn sich nichts getan hat
					if (index == -1) {
						x = list.indexOf(aComponent);
						for (int y = order.length - 1; y >= x; y--) {
							if (order[y].isEnabled() && order[x].isVisible()) {
								index = y;
								break;
							}
						}
					}

				}
				return order[(index + order.length) % order.length];
			}

			public java.awt.Component getDefaultComponent(java.awt.Container focusCycleRoot) {
				return order[0];
			}

			public java.awt.Component getInitialComponent(java.awt.Window window) {
				return order[0];
			}
		};
		return policy;
	}

	/**
	 * Suche
	 * 
	 * @param column
	 *            Spalte
	 * @param object
	 *            Wert
	 * @return true => anzeigen, false => ausblenden
	 */
	private boolean filter(int column, Object object) {
		if (!searchEnabled) {
			return true;
		}

		return searchFields.get(column).showField(object);
	}

	/**
	 * Setzt den Renderer der Tabelle
	 * 
	 * @param cellRenderer
	 *            Der ListCellRenderer
	 */
	public void setCellRenderer(ListCellRenderer cellRenderer) {
		list.setCellRenderer(cellRenderer);
	}

	/**
	 * Liste neu laden
	 */
	private void sortList() {
		listModel.updateList();
	}

	/**
	 * Suche aktivieren
	 * 
	 * @param searchEnabled
	 *            true wenn aktiviert
	 */
	public void setSearchEnabled(boolean searchEnabled) {
		if (this.searchEnabled == searchEnabled) {
			return;
		}
		this.searchEnabled = searchEnabled;

		int thHeight = tblHeader.getTableHeader().getPreferredSize().height;

		this.searchFieldHeight = 0;

		if (searchEnabled) {
			generateSearchFileds();
			for (int i = 0; i < model.getColumnCount(); i++) {
				JComponent c = model.getSearchModul(i).getComponent();
				if (model.getSearchModul(i) == null || c == null) {
					continue;
				}

				searchFieldHeight = Math.max(c.getPreferredSize().height, searchFieldHeight);
			}
			tableHead.setPreferredSize(new Dimension(20, thHeight + searchFieldHeight));
		} else {
			deleteSearchFields();
			tableHead.setPreferredSize(new Dimension(20, thHeight));
		}
	}

	/**
	 * Suchfelder erstellen
	 */
	private void generateSearchFileds() {
		for (int i = 0; i < model.getColumnCount(); i++) {
			SearchModul modul = model.getSearchModul(i);
			if (modul == null) {
				modul = new NoSearch();
			}

			modul.addSearchListener(this);
			searchFields.add(modul);
			tableHead.add(modul.getComponent());
		}

		searchValues = new String[model.getColumnCount()];
	}

	/**
	 * Neu filtern
	 */
	public void filterChanged(SearchModul modul) {
		int id = searchFields.indexOf(modul);

		if (modul == null || modul.getValue() == null) {
			searchValues[id] = null;

		} else {
			searchValues[id] = modul.getValue().toString();
			if (searchValues[id].equals("")) {
				searchValues[id] = null;
			}
		}

		setFilterToUI();
	}

	/**
	 * Liste nach ändern des Filters neu laden
	 */
	private void setFilterToUI() {
		rowSorter.sort();
		if (list.getCellRenderer() instanceof FilteredRenderer) {
			((FilteredRenderer) list.getCellRenderer()).setFilter(searchValues);
		}
		sortList();
	}

	/**
	 * Suchfelder löschen
	 */
	private void deleteSearchFields() {
		for (SearchModul mod : searchFields) {
			tableHead.remove(mod.getComponent());
			mod.removeSearchListener(this);
		}
		searchFields.clear();
		invalidate();
	}

	/**
	 * Gibt zurück ob die Suche aktiviert ist
	 * 
	 * @return true wenn aktiviert
	 */
	public boolean isSearchEnabled() {
		return searchEnabled;
	}

	/**
	 * Setzt das Model
	 * 
	 * @param model
	 *            Das Model
	 */
	public void setModel(SortableTableModel model) {
		listModel.removeListeners();

		this.model = model;
		tblHeader.setModel(model);
		rowSorter.setModel(model);
		list.setModel(listModel);

		listModel.addListeners();
		setFocusOrder();
	}

	/**
	 * Gibt das Model zurück
	 * 
	 * @return Das Model
	 */
	public SortableTableModel getModel() {
		return model;
	}

	/**
	 * Das Listmodel zur Darstellung der Daten in der JList
	 */
	class SortListModel extends AbstractListModel implements TableModelListener {
		private static final long serialVersionUID = 1L;

		public void addListeners() {
			model.addTableModelListener(this);
		}

		public void removeListeners() {
			model.removeTableModelListener(this);
		}

		private void updateList() {
			fireContentsChanged(this, 0, getSize());
		}

		public Object getElementAt(int index) {
			try {
				return model.getElementAt(rowSorter.convertRowIndexToModel(index));
			} catch (Exception e) {
				return null;
			}
		}

		public int getSize() {
			return rowSorter.getViewRowCount();
		}

		@Override
		public void tableChanged(TableModelEvent e) {
			try {
				fireContentsChanged(this, rowSorter.convertRowIndexToView(e.getFirstRow()), rowSorter.convertRowIndexToView(e.getLastRow()));

				// Probleme mit dem Sortieren / Filter, model nicht aktuell beim
				// Daten laden
			} catch (IndexOutOfBoundsException ex) {
				updateList();
			}
		}

	}

	public void addListSelectionListener(ListSelectionListener listener) {
		list.addListSelectionListener(listener);
	}

	public void removeListSelectionListener(ListSelectionListener listener) {
		list.removeListSelectionListener(listener);
	}

	public Object getSelectedValue() {
		return list.getSelectedValue();
	}

	public void setSelectedValue(Object anObject) {
		list.setSelectedValue(anObject, true);
	}

	/**
	 * Gibt die JList die die Daten darstellt zurück
	 * 
	 * @return Die JList
	 */
	public JList getList() {
		return list;
	}

	public void selectScrolTo(Object item) {
		// Maske wählen
		list.setSelectedValue(item, false);
		// Nachdem Maskversion geladen wurden noch Scrollen...

		int id = list.getSelectedIndex() + 2;

		id = Math.min(id, model.getRowCount() - 1);

		list.ensureIndexIsVisible(id);
	}

	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}

	public TableRowSorter<TableModel> getRowSorter() {
		return rowSorter;
	}

	public void setFilter(int index, Object filter) {
		searchFields.get(index).setFilter(filter);
	}
}
