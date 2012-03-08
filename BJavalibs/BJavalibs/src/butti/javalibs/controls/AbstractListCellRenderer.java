package butti.javalibs.controls;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import butti.javalibs.controls.listcontrol.FilteredRenderer;

/**
 * Renderer für die SortableTable
 * 
 * @author Andreas Butti
 * 
 * @param Typ
 *            der angezeigt werden soll
 */
public abstract class AbstractListCellRenderer<E extends Object> extends JComponent implements ListCellRenderer, FilteredRenderer {
	private static final long serialVersionUID = 1L;

	/**
	 * Ob der Eintrag selektiert ist
	 */
	protected boolean selected;

	/**
	 * Der Wert der angezeigt werden soll
	 */
	protected E value;

	/**
	 * Die Höhe des Eintrages
	 */
	protected final int HEIGHT;

	/**
	 * Die Breite des Eintrages
	 */
	private int width = 0;

	/**
	 * Der Graphics wärend dem <code>paint</code>
	 */
	private Graphics2D graphics;

	/**
	 * Die standard Schriftart
	 */
	private Font defaultFont;

	/**
	 * Die Texte die nach denen gesucht wird
	 */
	private String[] filter;

	/**
	 * Das Hintergrundbild (z.B. Verlauf)
	 */
	private BufferedImage background;

	/**
	 * Konstruktor
	 * 
	 * @param height
	 *            Die höhe der Listeneinträge
	 */
	public AbstractListCellRenderer(int height) {
		HEIGHT = height + 1;
		setBackground(Color.WHITE);
	}

	/**
	 * Setzt die Filter der Suche
	 * 
	 * @param filter
	 */
	public void setFilter(String[] filter) {
		this.filter = filter;
	}

	/**
	 * Gibt den Suchtext zurück
	 * 
	 * @param index
	 *            Der Index
	 * @return Der Text oder null wenn keiner gesetzt ist
	 */
	protected String getFilter(int index) {
		if (filter == null) {
			return null;
		}
		return filter[index];
	}

	@SuppressWarnings("unchecked")
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

		this.selected = isSelected;

		try {
			// Check zur Laufzeit nicht möglich
			this.value = (E) value;
		} catch (Exception e) {
			this.value = null;
		}

		int height = HEIGHT;

		if (this.value != null) {
			// Beim ersten Rendern ist ggf. keine Schrift verfügbar, sofern der
			// erste Text nicht der längste ist kein Problem.
			// Bei Grössenveränderungen wird der Komponent dann richtig
			// berechnet...
			if (defaultFont != null || getFont() != null) {
				calculateMinWidth(this.value);
			}
			height = calcHeight(this.value);
		}

		setPreferredSize(new Dimension(width, height));

		return this;
	}

	/**
	 * Berechnet die Höhe des Eintrages
	 * 
	 * @param value
	 *            Die Daten
	 * @return Die Höhe in PX
	 */
	protected int calcHeight(E data) {
		return HEIGHT;
	}

	/**
	 * Berechnet die minimale Breite des Komponenten
	 */
	protected abstract void calculateMinWidth(E data);

	/**
	 * Wird aufgerufen mit dem Text der dargestellt werden muss
	 * 
	 * @param padding
	 *            Padding, links und rechts
	 * @param text
	 *            Der Text
	 * @param font
	 *            Die Schrift
	 * @return Die Grösse
	 */
	protected int setMinWidth(int padding, String text, Font font) {
		FontMetrics metrics = getFontMetrics(font);
		int width = padding + metrics.stringWidth(text);
		setMinWidth(width);
		return width;
	}

	/**
	 * Wird aufgerufen mit dem Text der dargestellt werden muss
	 * 
	 * @param padding
	 *            Padding, links und rechts
	 * @param text
	 *            Der Text
	 * @param title
	 *            true für ein Titel Text
	 * @return Die Grösse
	 */
	protected int setMinWidth(int padding, String text, boolean title) {
		Font font = defaultFont;
		if (defaultFont == null) {
			font = getFont();
		}
		if (title) {
			font = font.deriveFont(Font.BOLD);
		}
		return setMinWidth(padding, text, font);
	}

	/**
	 * Wird aufgerufen mit dem Text der dargestellt werden muss (Default Font)
	 * 
	 * @param padding
	 *            Padding, links und rechts
	 * @param text
	 *            Der Text
	 * @return Die Grösse
	 */
	protected int setMinWidth(int padding, String text) {
		return setMinWidth(padding, text, false);
	}

	/**
	 * Setzt die Mindestbreite
	 * 
	 * @param width
	 *            Breite in px
	 */
	protected void setMinWidth(int width) {
		this.width = Math.max(this.width, width);
	}

	/**
	 * Sets the minimum with, overrides the old value
	 */
	protected void setMinWidthAbsolut(int width) {
		this.width = width;
	}

	/**
	 * @return The minimum width currently set
	 */
	public int getMinWidth() {
		return this.width;
	}

	/**
	 * Gibt das Objekt zurück
	 * 
	 * @return Das Objekt das dargestellt werden soll
	 */
	public E getValue() {
		return value;
	}

	@Override
	public void paint(Graphics g) {
		if (getHeight() == 0) {
			return;
		}

		graphics = (Graphics2D) g;
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		this.defaultFont = g.getFont();

		background = new BufferedImage(1, getHeight(), BufferedImage.TYPE_INT_ARGB);

		printBackground((Graphics2D) background.getGraphics(), 1);
		printBackground(graphics, getWidth());

		if (this.value != null) {
			paintData(graphics, this.value);
		}
		graphics = null;
	}

	/**
	 * Setzt die Titel Schrift
	 */
	protected void setTitleFont() {
		graphics.setFont(defaultFont.deriveFont(Font.BOLD));
	}

	/**
	 * Setzt die standard Schrift
	 */
	protected void setDefaultFont() {
		graphics.setFont(defaultFont);
	}

	/**
	 * Setzt die Schriftfarbe
	 */
	protected void setFontColor() {
		if (selected) {
			graphics.setColor(Color.WHITE);
		} else {
			graphics.setColor(Color.BLACK);
		}
	}

	/**
	 * Zeichnet die Daten
	 * 
	 * @param g
	 *            Der Graphics
	 */
	protected abstract void paintData(Graphics2D g, E data);

	/**
	 * Zeichnet den Hintergrund
	 * 
	 * @param g
	 *            Der Graphics
	 * @param width
	 *            Breite die gezeichnet werden soll
	 */
	private void printBackground(Graphics2D g, int width) {
		if (selected) {
			Color color1 = new Color(0x5591ec);
			Color color2 = new Color(0x202aef);
			// Paint a gradient from top to bottom
			GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
			g.setPaint(gp);
			g.fillRect(0, 0, width, getHeight());
		} else {
			// super.paint(g);
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight() - 1);
		}
		g.setColor(Color.LIGHT_GRAY);

		int height = calcHeight(this.value);

		g.drawLine(0, height - 1, width, height - 1);
		setFontColor();
	}

	/**
	 * Gibt den Text aus und markiert die Gesuchten stellen
	 * 
	 * @param str
	 *            Der Text
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 * @param maxWidth
	 *            Wie breit der Text maximal sein darf
	 * 
	 * @param filterId
	 *            ID des gesucten Textes
	 */
	protected void drawStringFilter(String str, int x, int y, int filterId) {
		drawStringFilter(str, x, y, 0, getFilter(filterId));
	}

	/**
	 * Gibt den Text aus und markiert die Gesuchten stellen
	 * 
	 * @param str
	 *            Der Text
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 * @param maxWidth
	 *            Wie breit der Text maximal sein darf
	 * 
	 * @param filterId
	 *            ID des gesucten Textes
	 */
	protected void drawStringFilter(String str, int x, int y, int maxWidth, int filterId) {
		drawStringFilter(str, x, y, maxWidth, getFilter(filterId));
	}

	/**
	 * Gibt den Text aus und markiert die gesuchten Stellen
	 * 
	 * @param str
	 *            Der Text
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 * @param filter
	 *            Gesucter Text
	 */
	protected void drawStringFilter(String str, int x, int y, String filter) {
		drawStringFilter(str, x, y, 0, filter);
	}

	/**
	 * Gibt den Text aus und markiert die gesuchten Stellen
	 * 
	 * @param str
	 *            Der Text
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 * @param maxWidth
	 *            Wie breit der Text maximal sein darf
	 * @param filter
	 *            Gesucter Text
	 */
	protected void drawStringFilter(String str, int x, int y, int maxWidth, String filter) {
		Shape clipBackup = null;

		if (maxWidth != 0) {
			clipBackup = graphics.getClip();

			if (clipBackup instanceof Rectangle) {
				Rectangle r = (Rectangle) clipBackup;

				graphics.setClip((int) r.getX(), (int) r.getY(), (int) r.getX() + x + maxWidth, (int) r.getHeight());
			}
		}

		if (filter == null) {
			graphics.drawString(str, x, y);
			if (clipBackup != null) {
				graphics.setClip(clipBackup);
			}
			return;
		}

		String strl = str.toLowerCase();
		String filterl = filter.toLowerCase();

		int start = strl.indexOf(filterl);

		if (start == -1) {
			graphics.drawString(str, x, y);
			return;
		}

		int last = 0;

		String part;

		while ((start = strl.indexOf(filterl, last)) != -1) {
			setFontColor();
			part = str.substring(last, start);
			graphics.drawString(part, x, y);
			x += graphics.getFontMetrics().stringWidth(part);

			graphics.setColor(Color.RED);

			part = str.substring(start, start + filterl.length());

			graphics.drawString(part, x, y);
			x += graphics.getFontMetrics().stringWidth(part);

			last = start + filterl.length();
		}

		if (last < strl.length()) {
			setFontColor();
			start = strl.length();

			part = str.substring(last, start);
			graphics.drawString(part, x, y);
		}

		setFontColor();

		if (clipBackup != null) {
			graphics.setClip(clipBackup);
		}
	}

	/**
	 * Blendet den letzten Teil des Strings langsam aus, anstatt hart
	 * abgeschnitten (oder ...)
	 */
	protected void fadeOutString(int y) {
		fadeOutString(getWidth(), y);
	}

	/**
	 * Blendet den letzten Teil des Strings langsam aus, anstatt hart
	 * abgeschnitten (oder ...)
	 */
	protected void fadeOutString(int x, int y) {
		final int width = 40;
		final int fsize = getFont().getSize();
		final int maxDescent = getFontMetrics(getFont()).getMaxDescent();

		Composite comp = graphics.getComposite();

		for (int w = 0; w < width; w++) {
			float alpha = 1 - (float) (w + 1) / (float) (width + 1);

			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

			graphics.drawImage(background, x - w, // Wohin wird das Bild kopiert (start X)
					y - fsize, // Wohin wird das Bild kopiert (start Y)
					x - w + 1, // Wohin wird das Bild kopiert (end X)
					y + maxDescent, // Wohin wird das Bild kopiert (end Y)
					0, // Was wird kopiert (start X)
					y - fsize, // Was wird kopiert (start Y)
					1, // Was wird kopiert (stop X)
					y + maxDescent, // Was wird kopiert (stop Y)
					this);
		}

		graphics.setComposite(comp);
	}
}
