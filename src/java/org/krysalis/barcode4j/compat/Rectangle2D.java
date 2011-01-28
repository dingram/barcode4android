package org.krysalis.barcode4j.compat;

public abstract class Rectangle2D {

	public static final int OUT_BOTTOM = 8;
	public static final int OUT_LEFT   = 1;
	public static final int OUT_RIGHT  = 4;
	public static final int OUT_TOP    = 2;

	protected Rectangle2D() {
	}

	public Rectangle2D getBounds2D() {
		return this;
	}

	public void setFrame(double x, double y, double w, double h) {
		setRect(x, y, w, h);
	}

	public abstract void setRect(double x, double y, double w, double h);


	public static class Double extends Rectangle2D {
		public double height = 0;
		public double width = 0;
		public double x = 0;
		public double y = 0;

		public Double() {
		}

		public Double(double x, double y, double w, double h) {
			setRect(x, y, w, h);
		}

		public void setRect(double x, double y, double w, double h) {
			this.x = x;
			this.y = y;
			this.width = w;
			this.height = h;
		}

		public double getHeight() {
			return height;
		}

		public double getWidth() {
			return width;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

	}
}
