package ua.vboden.epdr.shapes;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

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

	protected ArcFilled() {
	}

	public ArcFilled(float x, float y, float z, float radius, float startAngle, float angleSize) {
		this(x, y, z, radius, startAngle, angleSize, CIRCLE_POINTS);
	}

	public ArcFilled(float x, float y, float z, float radius, float startAngle, float angleSize, int circlePoints) {
		this.startAngle = startAngle;
		this.angleSize = angleSize;
		this.x = x;
		this.y = y;
		this.z = z;
		this.radius = radius;
		this.circlePoints = circlePoints;
		setMode(Mode.TriangleFan);
		updateGeometry();
	}

	public void updateGeometry() {

		int points = (int) (circlePoints * angleSize / 360);
		float[] coordinates = new float[3 * points + 6];
		float[] normals = new float[3 * points + 6];
		float[] textureCoords = new float[2 * points + 4];
		short[] indexes = new short[points + 2];
		setupCenterParams(coordinates, normals, textureCoords, indexes);
		float angleStep = (float) (angleSize / points);
		float angle = startAngle;
		for (int k = 1; k < points + 2; k++) {
			float radians = (float) toRadians(angle);
			coordinates[3 * k] = x + (float) (radius * cos(radians));
			coordinates[3 * k + 1] = y;
			coordinates[3 * k + 2] = z + (float) (radius * sin(radians));
			addTextureCoords(k, angle, textureCoords);
			indexes[k] = (short) (k);
			addNormals(k, normals);
			angle -= angleStep;
		}

		setBuffer(Type.Position, 3, coordinates);
		setBuffer(Type.Index, 1, indexes);
		setBuffer(Type.Normal, 3, normals);
		setBuffer(Type.TexCoord, 2, textureCoords);

		updateBound();
		setStatic();
	}

	private void setupCenterParams(float[] coordinates, float[] normals, float[] textureCoords, short[] indexes) {
		coordinates[0] = x;
		coordinates[1] = y;
		coordinates[2] = z;
		textureCoords[0] = 0.5f;
		textureCoords[1] = 0.5f;
		addNormals(0, normals);
		indexes[0] = 0;
	}

	private void addTextureCoords(int k, float angle, float[] textureCoords) {
		float angle2 = angle - startAngle;
		while (angle2 < 0) {
			angle2 += 360;
		}
		while (angle2 > 360) {
			angle2 -= 360;
		}
		textureCoords[2 * k] = (float) ((Math.cos(Math.toRadians(angle2)) + 1) / 2.0);
		textureCoords[2 * k + 1] = (float) ((Math.sin(Math.toRadians(angle2)) + 1) / 2.0);
	}

	private void addNormals(int k, float[] normals) {
		normals[3 * k] = 0;
		normals[3 * k + 1] = 1;
		normals[3 * k + 2] = 0;
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
