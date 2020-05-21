package ua.vboden.epdr.shapes;

import java.io.IOException;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;

public class ArcFilled extends Mesh {

	private static final int CIRCLE_POINTS = 100;

	private float x;
	private float y;
	private float z;
	private float radius;
	private int circlePoints = CIRCLE_POINTS;
	private float startAngle = 0;
	private float angleSize = 90;

	/**
	 * Serialization only. Do not use.
	 */
	protected ArcFilled() {
	}

	public ArcFilled(float x, float y, float z, float radius, float startAngle, float angleSize) {
		this.startAngle = startAngle;
		this.angleSize = angleSize;
		updateGeometry(x, y, z, radius);
	}

	public ArcFilled(float radius) {
		updateGeometry(radius);
	}

	public void updateGeometry(float radius) {
		updateGeometry(0, 0, 0, radius);
	}

	public void updateGeometry(float x, float y, float z, float radius) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.radius = radius;
		setMode(Mode.TriangleFan);

		int points = (int) (circlePoints * angleSize / 360);
		float[] coordinates = new float[3 * points + 6];
		short[] indexes = new short[points + 2];
		coordinates[0] = x;
		coordinates[1] = y;
		coordinates[2] = z;
		indexes[0] = 0;
		float angleStep = (float) (angleSize / points);
		float angle = startAngle;
		for (int k = 1; k < points + 2; k++) {
			float radians = (float) (angle * (Math.PI / 180.0));
			coordinates[3 * k] = x + (float) (radius * Math.cos(radians));
			coordinates[3 * k + 1] = y;
			coordinates[3 * k + 2] = z + (float) (radius * Math.sin(radians));
			indexes[k] = (short) (k);
			angle -= angleStep;
		}

		setBuffer(Type.Position, 3, coordinates);
		setBuffer(Type.Index, 1, indexes);

		updateBound();
		setStatic();
	}

	protected void setCirclePoints(int circlePoints) {
		this.circlePoints = circlePoints;
	}

	@Override
	public void read(JmeImporter e) throws IOException {
		super.read(e);
		InputCapsule capsule = e.getCapsule(this);
		radius = capsule.readFloat("radius", 1);
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
		capsule.write(circlePoints, "circlePoints", CIRCLE_POINTS);
		capsule.write(x, "x", 0);
		capsule.write(y, "y", 0);
		capsule.write(z, "z", 0);
		capsule.write(startAngle, "startAngle", 0);
		capsule.write(angleSize, "angleSize", 90);
	}
}
