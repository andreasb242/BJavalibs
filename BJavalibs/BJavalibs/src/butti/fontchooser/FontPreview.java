package butti.fontchooser;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import butti.javalibs.util.DrawHelper;

public class FontPreview extends JComponent {
	private static final long serialVersionUID = 1L;

	private String previewText = "Franz jagt im komplett verwahrlosten Taxi quer durch Bayern";

	public FontPreview() {
		setPreferredSize(new Dimension(400, 80));
	}

	@Override
	protected void paintComponent(Graphics g1) {
		Graphics2D g = DrawHelper.antialisingOn(g1);
		g.setFont(getFont());

		FontMetrics metrics = g.getFontMetrics();

		int w = getWidth();
		int len;
		for (len = previewText.length(); len > 1 && metrics.stringWidth(previewText.substring(0, len)) > w; len--) {
		}

		String str = previewText.substring(0, len);
		int h = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
		int x = (getWidth() - metrics.stringWidth(str)) / 2;
		g.drawString(str, x, h);

	}

	public void setPreviewText(String previewText) {
		this.previewText = previewText;
	}

	public String getPreviewText() {
		return previewText;
	}
}
