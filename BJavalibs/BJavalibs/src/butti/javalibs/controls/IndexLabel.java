package butti.javalibs.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

public class IndexLabel extends JComponent {
	private static final long serialVersionUID = 1L;
	private String index;

	public IndexLabel(String index) {
		this.index = index;
		setPreferredSize(new Dimension(50, 50));
		setMinimumSize(getPreferredSize());
		setMaximumSize(getPreferredSize());
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getIndex() {
		return index;
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color color1 = new Color(0x5591ec);
		Color color2 = new Color(0x202aef);
		// Paint a gradient from top to bottom
		GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
		graphics.setPaint(gp);
		graphics.fillOval(0, 0, getWidth(), getHeight());

		g.setColor(Color.WHITE);

		int x = getWidth() / 2;
		int y = getHeight();
		g.setFont(new Font("Serif", Font.BOLD, 40));

		FontMetrics metric = g.getFontMetrics();
		x -= metric.stringWidth(this.index) / 2;
		y -= 13;

		g.drawString(this.index, x, y);
	}
}
