package butti.javalibs.util;

import java.awt.Point;
import java.awt.Rectangle;

public class ExtendableRange {
	private int x0;
	private int x1;
	private int y0;
	private int y1;

	private boolean initialized = false;

	public ExtendableRange() {
	}

	public void clear() {
		initialized = false;
	}

	/**
	 * Adds a point to the range, the range is extended to contain this point
	 * 
	 * @param x
	 * @param y
	 */
	public void addPoint(int x, int y) {
		if (initialized == false) {
			initialized = true;

			this.x0 = x;
			this.x1 = x;
			this.y0 = y;
			this.y1 = y;
		}

		if (this.x0 > x) {
			this.x0 = x;
		}
		if (this.x1 < x) {
			this.x1 = x;
		}

		if (this.y0 > y) {
			this.y0 = y;
		}
		if (this.y1 < y) {
			this.y1 = y;
		}
	}

	public void addPoint(Point point) {
		addPoint(point.x, point.y);
	}

	public void addRect(Rectangle rect) {
		addPoint(rect.x, rect.y);
		addPoint(rect.x + rect.width, rect.y + rect.height);
	}

	public int getX() {
		return x0;
	}

	public int getY() {
		return y0;
	}

	public int getWidth() {
		return this.x1 - this.x0;
	}

	public int getHeight() {
		return this.y1 - this.y0;
	}

	public Rectangle getRect() {
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

}
