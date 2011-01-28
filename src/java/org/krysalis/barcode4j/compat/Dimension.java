package org.krysalis.barcode4j.compat;

public class Dimension {
	public int height=0;
	public int width=0;

	public Dimension() {
	}

	public Dimension(Dimension d) {
		setSize(d);
	}

	public Dimension(int width, int height) {
		setSize(width, height);
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public void setSize(Dimension d) {
		height = d.height;
		width = d.width;
	}

	public void setSize(double width, double height) {
		this.width = (int)width;
		this.height = (int)height;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

}
