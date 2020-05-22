package ua.vboden.epdr.shapes;

public class CircleDashed extends ArcDashed {

	/**
	 * Serialization only. Do not use.
	 */
	protected CircleDashed() {
	}

	public CircleDashed(float x, float y, float z, float radius, float lineWidth, int dashSize) {
		super(x, y, z, radius, lineWidth, 0, 360, dashSize);
	}
}
