package org.krysalis.barcode4j.compat;

public abstract class Rectangle2D {

	public static final int OUT_BOTTOM = 8;
	public static final int OUT_LEFT   = 1;
	public static final int OUT_RIGHT  = 4;
	public static final int OUT_TOP    = 2;

	protected Rectangle2D() {
	}

	public void add(double newx, double newy) {
	}

	public void add(Point2D pt) {
	}

	public void add(Rectangle2D r) {
	}

	public boolean contains(double x, double y) {
	}

	public boolean contains(double x, double y, double w, double h) {
	}

	public abstract Rectangle2D createIntersection(Rectangle2D r);

	public abstract Rectangle2D createUnion(Rectangle2D r);

	public boolean equals(Object obj) {
	}

	public Rectangle2D getBounds2D() {
	}

	public PathIterator getPathIterator(AffineTransform at) {
	}

	public PathIterator getPathIterator(AffineTransform at, double flatness) {
	}

	public int hashCode() {
	}

	public static void intersect(Rectangle2D src1, Rectangle2D src2, Rectangle2D dest) {
	}

	public boolean intersects(double x, double y, double w, double h) {
	}

	public boolean intersectsLine(double x1, double y1, double x2, double y2) {
	}

	public boolean intersectsLine(Line2D l) {
	}

	public abstract int outcode(double x, double y);

	public int outcode(Point2D p) {
	}

	public void setFrame(double x, double y, double w, double h) {
	}

	public abstract void setRect(double x, double y, double w, double h);

	public void setRect(Rectangle2D r) {
	}

	public static void union(Rectangle2D src1, Rectangle2D src2, Rectangle2D dest) {
	}


	public static class Double extends Rectangle2D {
		double height = 0;
		double width = 0;
		double x = 0;
		double y = 0;

		public Double() {
		}

		public Double(double x, double y, double w, double h) {
			this.x = x;
			this.y = y;
			this.width = w;
			this.height = h;
		}

		public Rectangle2D createIntersection(Rectangle2D r) {
		}

		public Rectangle2D createUnion(Rectangle2D r) {
		}

		public int outcode(double x, double y) {
		}

		public void setRect(double x, double y, double w, double h) {
		}

	}
}
