package butti.javalibs.gui;


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * Verwaltet das <code>GridBagLayout</code> eines Frames
 * 
 * @author Andreas Butti
 * @version 1.8, 26.12.07
 * 
 */
public class GridBagManager {
	/**
	 * GridBagLyout des aktullen Frames
	 */
	private GridBagLayout gbl;

	/**
	 * JFrame das verwaltet wird
	 */
	private JFrame frame = null;

	/**
	 * JDialog der verwaltet wird
	 */
	private JDialog dialog = null;

	/**
	 * Der Container der Verwaltet wird
	 */
	private Container panel;

	/**
	 * GridBagConstraints des aktuellen Komponenten
	 */
	private GridBagConstraints c;

	/**
	 * Die Grösse des Komponenten
	 */
	private Dimension dim;

	/**
	 * Der Komponent wird auf Scrollpanel geaddet
	 */
	private boolean scrollPanel;

	// ->Debug
	private ArrayList<DebugItem> items;
	private final boolean debug;
	// <-Debug

	/**
	 * Initialisiert den <code>GridBagManager</code>
	 * 
	 * @param panel
	 *            Das Panel (oder was auch immer) das verwaltet werden soll
	 */
	public GridBagManager(Container panel) {
		this(panel, false);
	}

	/**
	 * Initialisiert den <code>GridBagManager</code>
	 * 
	 * @param panel
	 *            Das Panel (oder was auch immer) das verwaltet werden soll
	 * @param debug
	 *            Debuggin einschalten
	 */
	public GridBagManager(Container panel, boolean debug) {
		this.debug = debug;
		
		if(debug) {
			new Exception().printStackTrace();
		}
		
		reset();
		this.panel = panel;
		gbl = new GridBagLayout();
		panel.setLayout(gbl);
		items = null;
		initDebug();
	}

	/**
	 * Initialisiert den <code>GridBagManager</code>
	 * 
	 * @param frame
	 *            Das JFrame das verwaltet werden soll
	 */
	public GridBagManager(JFrame frame) {
		this(frame, false);
	}

	/**
	 * Initialisiert den <code>GridBagManager</code>
	 * 
	 * @param frame
	 *            Das JFrame das verwaltet werden soll
	 * @param debug
	 *            Debuggin einschalten
	 */
	public GridBagManager(JFrame frame, boolean debug) {
		this.debug = debug;
		reset();
		this.frame = frame;
		panel = frame.getContentPane();
		gbl = new GridBagLayout();
		frame.getContentPane().setLayout(gbl);
		items = null;
		initDebug();
	}

	/**
	 * Initialisiert den <code>GridBagManager</code>
	 * 
	 * @param dialog
	 *            Der Dialog der verwaltet werden soll
	 */
	public GridBagManager(JDialog dialog) {
		this(dialog, false);
	}

	/**
	 * Initialisiert den <code>GridBagManager</code>
	 * 
	 * @param dialog
	 *            Der Dialog der verwaltet werden soll
	 * @param debug
	 *            Debuggin einschalten
	 */
	public GridBagManager(JDialog dialog, boolean debug) {
		this.debug = debug;
		reset();
		this.dialog = dialog;
		panel = dialog.getContentPane();
		gbl = new GridBagLayout();
		dialog.getContentPane().setLayout(gbl);
		items = null;
		initDebug();
	}

	/**
	 * Setzt die aktuelle Instanz zurck.
	 * 
	 */
	public void reset() {
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 1;
		c.weighty = 1;
		dim = null;
		scrollPanel = false;
	}

	/**
	 * 
	 * @return das aktuelle <code>GridBagLayout</code>
	 */
	public GridBagLayout getGridBagLayout() {
		return gbl;
	}

	/**
	 * 
	 * @return Gibt das Aktuelle <code>GridBagConstraints</code> zurck
	 */

	public GridBagConstraints getGridBagConstraints() {
		return c;
	}

	/**
	 * Setzt die X Startkoordinate des aktuellen <code>Component</code>
	 * 
	 * @param x
	 *            X Startkoordinate
	 * 
	 * @return <code>GridBagManager this</code>
	 */
	public GridBagManager setX(int x) {
		c.gridx = x;
		return this;
	}

	/**
	 * Setzt die Y Startkoordinate des aktuellen <code>Component</code>
	 * 
	 * @param y
	 *            Y Startkoordinate
	 * 
	 * @return <code>GridBagManager this</code>
	 */
	public GridBagManager setY(int y) {
		c.gridy = y;
		return this;
	}

	/**
	 * Setzt das Resizverhalten. Möglichkeiten:
	 * <code>GridBagConstraints.NONE</code>
	 * <code>GridBagConstraints.HORIZONTAL</code>
	 * <code>GridBagConstraints.VERTICAL</code>
	 * 
	 * @param fill
	 *            Resizverhalten
	 * 
	 * @return <code>GridBagManager this</code>
	 */
	public GridBagManager setFill(int fill) {
		c.fill = fill;
		return this;
	}

	/**
	 * Platzierung des Controls in einer zu grossen Zelle
	 * <code>GridBagConstraints.
	 * -------------------------------------------------
	 * |FIRST_LINE_START   PAGE_START     FIRST_LINE_END|
	 * |                                                |
	 * |                                                |
	 * |LINE_START           CENTER             LINE_END|
	 * |                                                |
	 * |                                                |
	 * |LAST_LINE_START     PAGE_END       LAST_LINE_END|
	 * -------------------------------------------------
	 * </code>
	 * 
	 * @param anchor
	 *            Anker
	 * @return <code>GridBagManager this</code>
	 */
	public GridBagManager setAnchor(int anchor) {
		c.anchor = anchor;
		return this;
	}

	/**
	 * Anzahl Zellen die horizontal belegt werden
	 * 
	 * @param width
	 *            Zellen
	 * @return <code>GridBagManager this</code>
	 */
	public GridBagManager setWidth(int width) {
		c.gridwidth = width;
		return this;
	}

	/**
	 * Anzahl Zellen die vertiakl belegt werden
	 * 
	 * @param height
	 *            Zellen
	 * @return <code>GridBagManager this</code>
	 */
	public GridBagManager setHeight(int height) {
		c.gridheight = height;
		return this;
	}

	/**
	 * Setzt die Grösse des Controls
	 * 
	 * @param width
	 *            Breite in px
	 * @param height
	 *            Höhe in px
	 * @return <code>GridBagManager this</code>
	 */
	public GridBagManager setPreferedSize(int width, int height) {
		setPreferedSize(new Dimension(width, height));
		return this;
	}

	/**
	 * Setzt die Grösse des Controls
	 * 
	 * @param dimension
	 *            Grösse
	 * @return <code>GridBagManager this</code>
	 */
	public GridBagManager setPreferedSize(Dimension dimension) {
		this.dim = dimension;
		return this;
	}

	/**
	 * Setzt den Komponent auf ein JScrollPanel bevor er geaddet wird, Vorsicht!
	 * Es muss das zurückgegeben JPanel gelöscht werden, nicht den Komponent
	 * löschen! Dieser wird nicht gefunden!
	 * 
	 * @return <code>GridBagManager this</code>
	 */
	public GridBagManager setScrollPanel() {
		scrollPanel = true;
		return this;
	}

	/**
	 * Setzt das gewichten beim resizen (0.0 ist standard)
	 * 
	 * @param x
	 *            Gewicht X
	 * @return <code>GridBagManager this</code>
	 */
	public GridBagManager setWeightX(double x) {
		c.weightx = x;
		return this;
	}

	/**
	 * Setzt das gewichten beim resizen (0.0 ist standard)
	 * 
	 * @param y
	 *            Gewicht Y
	 * @return <code>GridBagManager this</code>
	 */
	public GridBagManager setWeightY(double y) {
		c.weighty = y;
		return this;
	}

	/**
	 * Setzt den Abstand zu denn Zellen (standard 5px rundherum)
	 * 
	 * @param insets
	 *            Abstand
	 * @return <code>GridBagManager this</code>
	 */
	public GridBagManager setInsets(Insets insets) {
		c.insets = insets;
		return this;
	}

	/**
	 * Setzt den Component und added den Component auf das Frame. Die Aktion ist
	 * damit abgeschlossen.
	 * 
	 * @param component
	 *            Component der geaddet werden soll
	 * @return <code>Component</code> der geaddet wurde
	 */
	public Component setComp(Component component) {
		if (scrollPanel) {
			JScrollPane pane = new JScrollPane(component);
			gbl.setConstraints(pane, c);
			panel.add(pane);
			component = pane;
		} else {
			gbl.setConstraints(component, c);
			panel.add(component);
		}
		// Erst am Schluss setzen, damit ggf. die Grösse des JScrollPane gesetzt
		// wird
		if (dim != null)
			component.setPreferredSize(dim);

		addDebugItem(component);
		reset();

		return component;
	}

	/**
	 * Löscht ein Komponet aus dem <code>GridBagLayout</code>
	 * 
	 * @param component
	 *            zu entfernender <code>Component</code>
	 */
	public void remove(Component component) {
		gbl.removeLayoutComponent(component);
		panel.remove(component);
		removeDebugItem(component);
		if (frame != null)
			frame.pack();
		if (dialog != null)
			dialog.pack();
	}

	/**
	 * Löscht alle Komponet aus dem <code>GridBagLayout</code>
	 * 
	 */
	public void removeAll() {
		for (Component component : panel.getComponents()) {
			gbl.removeLayoutComponent(component);
			panel.remove(component);
			removeDebugItem(component);
		}
	}

	/**
	 * Ersetzt ein <code>Component</code> mit einem neuen
	 * <code>Component</code>
	 * 
	 * @param c1
	 *            Der alte <code>Component</code>
	 * @param c2
	 *            Der neue <code>Component</code>
	 * @return <code>GridBagManager this</code>
	 */
	public GridBagManager replace(Component c1, Component c2) {
		c = gbl.getConstraints(c1);
		remove(c1);
		setComp(c2);
		return this;
	}

	// Debug-----------------------------
	private class DebugItem {
		private int x;

		private int y;

		private int width;

		private int height;

		private Component component;

		/**
		 * Konstruktor, setzt alle Werte
		 * 
		 * @param x
		 *            X Koordinate
		 * @param y
		 *            Y Koordinate
		 * @param width
		 *            Breite
		 * @param height
		 *            Hhe
		 * @param component
		 *            Komponent
		 */
		public DebugItem(int x, int y, int width, int height,
				Component component) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.component = component;
		}

		/**
		 * Prüft ob der angegebene Platz schon belegt ist.
		 * 
		 * @param x
		 *            X Koordinate
		 * @param y
		 *            Y Koordinate
		 * @param width
		 *            Breite
		 * @param height
		 *            Höhe
		 * @return true wenn belegt, sonst false
		 */
		public boolean isUsed(int x, int y, int width, int height) {
			// Horizontal prfen, links

			System.out.println("\nPrüfe: this.x = " + this.x + ", this.y = "
					+ this.y + ", this.width = " + this.width
					+ ", this.height = " + this.height + "\nx = " + x
					+ ", y = " + y + ", width = " + width + ", height = "
					+ height);

			if (this.x < x && this.x + this.width - 1 < x) {
				System.out.println("Links OK");
				return false;
			} else {
				if (x < this.x && x + width - 1 < this.x) {
					System.out.println("Rechts OK");
					return false;
				} else if (this.y < y && this.y + this.height - 1 < y) {
					System.out.println("Oben OK");
					return false;
				} else if (y < this.y && y + height - 1 < this.y) {
					System.out.println("Unten OK");
					return false;
				}
			}
			return true;
		}

		/**
		 * Prüft ob diese Instanz den Komponenten enthlt
		 * 
		 * @param component
		 *            zu suchender Komponent
		 * @return true wenn diese Klasse diesen Komponent enthlt
		 */
		public boolean isComponent(Component component) {
			return (this.component == component);
		}
	}

	/**
	 * Initialisiert den Debugteil
	 * 
	 */
	private void initDebug() {
		if (!debug)
			return;
		items = new ArrayList<DebugItem>();
	}

	/**
	 * Fügt ein Komponent in die DebugArrayList und prüft ob der Platz schon
	 * belegt ist, und printet im Fehlerfall den Call Stack.
	 * 
	 * @param component
	 *            Komponent
	 */
	private void addDebugItem(Component component) {
		if (!debug)
			return;
		System.out
				.println("-----------------------------------------------------");
		for (DebugItem d : items)
			if (d.isUsed(c.gridx, c.gridy, c.gridwidth, c.gridheight)) {
				new Exception("Diese Koordinate ist schon belegt!")
						.printStackTrace();
				System.exit(1);
			}
		items.add(new DebugItem(c.gridx, c.gridy, c.gridwidth, c.gridheight,
				component));
	}

	/**
	 * Löschte ein Komponent aus der DebugArrayList
	 * 
	 * @param component
	 *            Komponent
	 */
	private void removeDebugItem(Component component) {
		if (!debug)
			return;
		for (DebugItem d : items)
			if (d.isComponent(component)) {
				items.remove(d);
				return;
			}
		new Exception("Fehler: Dieser Komponent ist nicht vorhanden!")
				.printStackTrace();
		System.exit(1);
	}

}
