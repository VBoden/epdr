package ua.vboden.epdr.shapes;

public class CircleFilled extends ArcFilled {

	/**
	 * Serialization only. Do not use.
	 */
	protected CircleFilled() {
	}

	public CircleFilled(float x, float y, float z, float radius, int circlePoints) {
		super(x, y, z, radius, 0, 360, circlePoints);
	}
}
