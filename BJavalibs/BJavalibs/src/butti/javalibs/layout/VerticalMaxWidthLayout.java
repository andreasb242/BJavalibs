package butti.javalibs.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * 
 * @author Andreas Butti
 */
public class VerticalMaxWidthLayout implements LayoutManager {
	private int width;
	private int margin;

	public VerticalMaxWidthLayout(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public void addLayoutComponent(String name, Component c) {
	}

	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();
		Dimension size = parent.getSize();
		int width = size.width - insets.left - insets.right;

		if (width > this.width) {
			width = this.width;
		}

		int height = insets.top;

		for (int i = 0, c = parent.getComponentCount(); i < c; i++) {
			Component m = parent.getComponent(i);
			if (m.isVisible()) {
				m.setBounds(insets.left, height, width, m.getPreferredSize().height);
				height += m.getSize().height + margin;
			}
		}
	}

	public Dimension minimumLayoutSize(Container parent) {
		return preferredLayoutSize(parent);
	}

	public Dimension preferredLayoutSize(Container parent) {
		Insets insets = parent.getInsets();
		Dimension pref = new Dimension(0, 0);

		for (int i = 0, c = parent.getComponentCount(); i < c; i++) {
			Component m = parent.getComponent(i);
			if (m.isVisible()) {
				Dimension componentPreferredSize = parent.getComponent(i).getPreferredSize();
				pref.height += componentPreferredSize.height + margin;
				pref.width = Math.max(pref.width, componentPreferredSize.width);
			}
		}

		pref.width += insets.left + insets.right;
		pref.height += insets.top + insets.bottom;

		if (pref.width > this.width) {
			pref.width = this.width;
		}

		return pref;
	}

	public void removeLayoutComponent(Component c) {
	}

}
