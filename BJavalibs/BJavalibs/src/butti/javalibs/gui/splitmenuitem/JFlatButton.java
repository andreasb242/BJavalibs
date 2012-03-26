package butti.javalibs.gui.splitmenuitem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.metal.MetalButtonUI;

/**
 * Source http://listen2gopi.blogspot.com/2008/01/flat-button-using-swing.html
 * 
 * Adapted for Simulation
 * 
 * @author Andreas Butti
 */
public class JFlatButton extends JButton {
	private static final long serialVersionUID = 1L;

	protected PaintProperties paintProp;

	public JFlatButton(String text) {
		super(text);
	}

	public JFlatButton(Action action) {
		super(action);
	}

	public JFlatButton(Icon icon) {
		super(icon);
	}

	public JFlatButton(String text, Icon icon) {
		super(text, icon);
	}

	public void setUI(ButtonUI ui) {
		// defaults
		if (paintProp == null) {
			paintProp = new PaintProperties();
			paintProp.borderColor = Color.GRAY;
			paintProp.rollOver1 = Color.WHITE;
			paintProp.rollOver2 = Color.LIGHT_GRAY;

			paintProp.pressed1 = paintProp.rollOver2;
			paintProp.pressed2 = paintProp.rollOver1;
		}
		// override the UI
		// always set our UI instead of platform based LAF!
		super.setUI(new MetalButtonUI() {
			protected void paintButtonPressed(Graphics g, AbstractButton b) {
				// if back group paint required
				if (b.isContentAreaFilled()) {
					Dimension size = b.getSize();
					Graphics2D g2d = (Graphics2D) g;

					Paint paint = g2d.getPaint(); // save to restore

					GradientPaint pt = new GradientPaint(new Point2D.Float(0, 0), paintProp.pressed1, new Point2D.Float(0, b.getHeight()), paintProp.pressed2);
					g2d.setPaint(pt);
					g.fillRect(0, 0, size.width, size.height);

					g.setColor(paintProp.borderColor);
					g.drawRect(0, 0, size.width - 1, size.height - 1);

					g2d.setPaint(paint);
				}
			}

			public void update(Graphics g, JComponent c) {
				// under normal condition, don't render the background
				// on roll over render my custom color
				if (model.isRollover()) {
					drawBG(g, c);
				}
				paint(g, c);
			}

			protected void drawBG(Graphics g, JComponent b) {
				Graphics2D g2d = (Graphics2D) g;
				Paint paint = g2d.getPaint(); // save to restore
				GradientPaint pt = new GradientPaint(new Point2D.Float(0, 0), paintProp.rollOver1, new Point2D.Float(0, b.getHeight()), paintProp.rollOver2);
				g2d.setPaint(pt);
				// fill button
				g.fillRect(0, 0, b.getWidth() - 1, b.getHeight() - 1);

				// draw border line
				g.setColor(paintProp.borderColor);
				g.drawRect(0, 0, b.getWidth() - 1, b.getHeight() - 1);

				g2d.setPaint(paint); // restore
			}

			protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
				// nothing, focus not rendered!
			}
		});
		// override some properties
		this.setBorderPainted(false);
		this.setOpaque(false);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	public PaintProperties getPaintProp() {
		return paintProp;
	}

	public void setPaintProp(PaintProperties paintProp) {
		this.paintProp = paintProp;
	}

	public static class PaintProperties {

		public Color pressed2;
		public Color pressed1;
		public Color rollOver2;
		public Color rollOver1;
		public Color borderColor;

	}
}