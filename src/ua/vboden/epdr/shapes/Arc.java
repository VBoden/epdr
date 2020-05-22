package ua.vboden.epdr.shapes;

import java.io.IOException;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;

public class Arc extends Mesh {

	private static final int CIRCLE_POINTS = 100;

	private float x;
	private float y;
	private float z;
	private float radius;
	private float lineWidth;
	private int circlePoints = CIRCLE_POINTS;
	private float startAngle = 0;
	private float angleSize = 90;

	/**
	 * Serialization only. Do not use.
	 */
	protected Arc() {
	}

	public Arc(float x, float y, float z, float radius, float lineWidth, float startAngle, float angleSize) {
		this(x, y, z, radius, lineWidth, startAngle, angleSize, CIRCLE_POINTS);
	}

	public Arc(float x, float y, float z, float radius, float lineWidth, float startAngle, float angleSize,
			int circlePoints) {
		this.startAngle = startAngle;
		this.angleSize = angleSize;
		this.circlePoints = circlePoints;
		init(x, y, z, radius, lineWidth);
	}

	protected void init(float radius, float lineWidth) {
		init(0, 0, 0, radius, lineWidth);
	}

	protected void init(float x, float y, float z, float radius, float lineWidth) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.radius = radius;
		this.lineWidth = lineWidth;
		setMode(getShapeMode());
	}

	public Arc updateGraphics() {
		int i = 0;
		int points = (int) (circlePoints * angleSize / 360);
		float[] coordinates = new float[6 * points + 6];
		short[] indexes = new short[getIndexesPerAngle() * (points + 1)];
		float innerRadius = radius - lineWidth;
		float angleStep = (float) (angleSize / points);
		float angle = startAngle;
		for (int k = 0; k < points + 1; k++) {
			float radians = (float) (angle * (Math.PI / 180.0));
			coordinates[6 * k] = x + (float) (innerRadius * Math.cos(radians));
			coordinates[6 * k + 1] = y;
			coordinates[6 * k + 2] = z + (float) (innerRadius * Math.sin(radians));
			coordinates[6 * k + 3] = x + (float) (radius * Math.cos(radians));
			coordinates[6 * k + 4] = y;
			coordinates[6 * k + 5] = z + (float) (radius * Math.sin(radians));
			i = addIndexes(k, indexes, i);
			angle -= angleStep;
		}

		setBuffer(Type.Position, 3, coordinates);
		setBuffer(Type.Index, 1, indexes);

		updateBound();
		setStatic();
		return this;
	}

	protected Mode getShapeMode() {
		return Mode.TriangleStrip;
	}

	protected int getIndexesPerAngle() {
		return 2;
	}

	protected int addIndexes(int k, short[] indexes, int i) {
		indexes[i] = (short) (2 * k);
		indexes[i + 1] = (short) (2 * k + 1);
		return i + 2;
	}

	protected void setCirclePoints(int circlePoints) {
		this.circlePoints = circlePoints;
	}

	@Override
	public void read(JmeImporter e) throws IOException {
		super.read(e);
		InputCapsule capsule = e.getCapsule(this);
		radius = capsule.readFloat("radius", 1);
		lineWidth = capsule.readFloat("lineWidth", 0.9f);
		circlePoints = capsule.readInt("circlePoints", CIRCLE_POINTS);
		x = capsule.readFloat("x", 0);
		y = capsule.readFloat("y", 0);
		z = capsule.readFloat("z", 0);
		startAngle = capsule.readFloat("startAngle", 0);
		angleSize = capsule.readFloat("angleSize", 90);
	}

	@Override
	public void write(JmeExporter e) throws IOException {
		super.write(e);
		OutputCapsule capsule = e.getCapsule(this);
		capsule.write(radius, "radius", 1);
		capsule.write(lineWidth, "lineWidth", 0.9f);
		capsule.write(circlePoints, "circlePoints", CIRCLE_POINTS);
		capsule.write(x, "x", 0);
		capsule.write(y, "y", 0);
		capsule.write(z, "z", 0);
		capsule.write(startAngle, "startAngle", 0);
		capsule.write(angleSize, "angleSize", 90);
	}

}
