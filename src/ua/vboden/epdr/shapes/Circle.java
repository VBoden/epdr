package ua.vboden.epdr.shapes;

public class Circle extends Arc {

	/**
	 * Serialization only. Do not use.
	 */
	protected Circle() {
	}

	public Circle(float x, float y, float z, float radius, float lineWidth) {
		super(x, y, z, radius, lineWidth, 0, 360);
	}

	public Circle(float radius, float lineWidth) {
		super(0, 0, 0, radius, lineWidth, 0, 360);
	}

	public Circle(float radius) {
		super(0, 0, 0, radius, 0.1f, 0, 360);
	}
}
