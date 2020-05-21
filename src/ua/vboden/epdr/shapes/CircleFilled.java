package ua.vboden.epdr.shapes;

public class CircleFilled extends ArcFilled {

	/**
	 * Serialization only. Do not use.
	 */
	protected CircleFilled() {
	}

	public CircleFilled(float x, float y, float z, float radius) {
		super(x, y, z, radius, 0, 360);
	}

	public CircleFilled(float radius) {
		super(0, 0, 0, radius, 0, 360);
	}
}
