package butti.javalibs.controls.searchcontrol;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import butti.javalibs.util.DrawHelper;

/**
 * Ein Suchfeld mit Suchicon
 * 
 * @author Andreas Butti
 * 
 */
public class SearchField extends JTextField {
	private static final long serialVersionUID = 1L;

	private static Image icon;
	private static int iconWidth;
	private static int iconHeight;
	private static final Color INFO_COLOR = new Color(0xaa, 0xaa, 0xaa);
	private String name;

	private boolean armed = false;
	private boolean showingPlaceholderText = false;

	private static final Border CANCEL_BORDER = new CancelBorder();

	/**
	 * Bild laden
	 */
	static {
		try {
			icon = ImageIO.read(SearchField.class.getResource("img/lupe.png"));
			iconWidth = icon.getWidth(null);
			iconHeight = icon.getHeight(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Erstellt ein Suchfeld
	 * 
	 * @param name
	 *            Der Name / Nach was gesucht wird
	 */
	public SearchField(String name) {
		this.name = name;

		addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				repaint();
			}

			@Override
			public void focusLost(FocusEvent e) {
				repaint();
			}

		});

		initBorder();
		initKeyListener();
	}

	private void initBorder() {
		setBorder(new CompoundBorder(getBorder(), CANCEL_BORDER));
		MouseInputListener mouseInputListener = new CancelListener();
		addMouseListener(mouseInputListener);
		addMouseMotionListener(mouseInputListener);
	}

	private void initKeyListener() {
		addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cancel();
				}
			}
		});
	}

	private void cancel() {
		setText("");
		postActionEvent();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		DrawHelper.antialisingOn(g);
		
		/**
		 * Platzhalter Text darstellen wenn kein Text eingegeben
		 */
		if (getText().equals("") && !hasFocus()) {
			g.drawImage(icon, 5, (getHeight() - iconHeight) / 2, this);
			g.setColor(INFO_COLOR);
			g.setClip(0, 0, getWidth() - 5, getHeight());
			g.drawString(name, 10 + iconWidth, 15);
		}
	}

	/**
	 * 
	 * Draws the cancel button as a gray circle with a white cross inside.
	 * 
	 */
	static class CancelBorder extends EmptyBorder {
		private static final long serialVersionUID = 1L;

		private static final Color GRAY = new Color(0.7f, 0.7f, 0.7f);

		CancelBorder() {
			super(0, 0, 0, 15);
		}

		public void paintBorder(Component c, Graphics oldGraphics, int x, int y, int width, int height) {
			SearchField field = (SearchField) c;
			if (field.showingPlaceholderText || field.getText().length() == 0) {
				return;
			}

			Graphics2D g = (Graphics2D) oldGraphics;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			final int circleL = 14;
			final int circleX = x + width - circleL;
			final int circleY = y + (height - 1 - circleL) / 2;
			g.setColor(field.armed ? Color.GRAY : GRAY);
			g.fillOval(circleX, circleY, circleL, circleL);
			final int lineL = circleL - 8;
			final int lineX = circleX + 4;
			final int lineY = circleY + 4;
			g.setColor(Color.WHITE);
			g.drawLine(lineX, lineY, lineX + lineL, lineY + lineL);
			g.drawLine(lineX, lineY + lineL, lineX + lineL, lineY);
		}
	}

	/**
	 * 
	 * Handles a click on the cancel button by clearing the text and notifying
	 * 
	 * any ActionListeners.
	 * 
	 */
	class CancelListener extends MouseInputAdapter {

		/**
		 * Default Cursor: über dem Löschen Button
		 */
		private final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);

		/**
		 * Cursor für den Text
		 */
		private final Cursor TEXT_CURSOR = new Cursor(Cursor.TEXT_CURSOR);

		private boolean isOverButton(MouseEvent e) {
			// If the button is down, we might be outside the component
			// without having had mouseExited invoked.
			if (contains(e.getPoint()) == false) {
				return false;
			}

			// In lieu of proper hit-testing for the circle, check that
			// the mouse is somewhere in the border.
			Rectangle innerArea = SwingUtilities.calculateInnerArea(SearchField.this, null);
			return (innerArea.contains(e.getPoint()) == false);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (isOverButton(e)) {
				setCursor(DEFAULT_CURSOR);
			} else {
				setCursor(TEXT_CURSOR);
			}
		}

		public void mouseDragged(MouseEvent e) {
			arm(e);
		}

		public void mouseEntered(MouseEvent e) {
			arm(e);
		}

		public void mouseExited(MouseEvent e) {
			disarm();
		}

		public void mousePressed(MouseEvent e) {
			arm(e);
		}

		public void mouseReleased(MouseEvent e) {
			if (armed) {
				cancel();
			}
			disarm();
		}

		private void arm(MouseEvent e) {
			armed = (isOverButton(e) && SwingUtilities.isLeftMouseButton(e));
			repaint();
		}

		private void disarm() {
			armed = false;
			repaint();
		}
	}
}
