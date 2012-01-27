package butti.javalibs.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public class HorizontalLayout implements LayoutManager {
	public enum Position {
		LEFT, RIGHT
	}

	/**
	 * Gap between the elements
	 */
	private int margin = 0;

	/**
	 * Gap at the start and end of the layout
	 */
	private int padding = 0;
	private Position position = Position.LEFT;

	public HorizontalLayout() {
	}

	public HorizontalLayout(Position position) {
		this.position = position;
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
	}

	@Override
	public void removeLayoutComponent(Component comp) {
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Dimension preffered = innerDimension(parent);

		preffered.width += margin;

		Insets insets = parent.getInsets();
		System.out.println(insets);
		preffered.width += insets.left + insets.right;
		preffered.height += insets.top + insets.bottom;

		System.out.println(preffered);
		return preffered;
	}

	private Dimension innerDimension(Container parent) {
		Dimension inner = new Dimension(0, 0);

		int count = parent.getComponentCount();
		for (int i = 0; i < count; i++) {
			Component m = parent.getComponent(i);
			if (m.isVisible()) {
				Component comp = parent.getComponent(i);
				Dimension pref = comp.getPreferredSize();

				inner.height = Math.max(inner.height, pref.height);
				inner.width += pref.width;

				// last element
				if (i == count - 1) {
					inner.width += margin;
				} else {
					inner.width += padding;
				}
			}
		}

		return inner;
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return preferredLayoutSize(parent);
	}

	@Override
	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();
		Dimension size = parent.getSize();
		int height = size.height - insets.top - insets.bottom;
		int width = insets.left + margin;

		if (position == Position.RIGHT) {
			Dimension inner = innerDimension(parent);
			width = parent.getWidth() - (int) inner.getWidth();
		}

		int count = parent.getComponentCount();
		for (int i = 0; i < count; i++) {
			Component m = parent.getComponent(i);
			if (m.isVisible()) {
				m.setBounds(width, insets.top, m.getPreferredSize().width,
						height);
				width += m.getSize().width;
				// last element
				if (i == count - 1) {
					width += margin;
				} else {
					width += padding;
				}

			}
		}

	}

}