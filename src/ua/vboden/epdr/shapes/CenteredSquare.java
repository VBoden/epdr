package ua.vboden.epdr.shapes;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import java.io.IOException;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;

public class CenteredSquare extends Mesh {

	private float width;
	private float height;
	private float x;
	private float y;
	private float z;
	private float rotationDegress;

	protected CenteredSquare() {
	}

	protected CenteredSquare(float width, float height) {
		this.width = width;
		this.height = height;
	}

	protected CenteredSquare(float x, float y, float z, float width, float height) {
		this(width, height);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	protected CenteredSquare(float x, float y, float z, float width, float height, float rotationDegress) {
		this(x, y, z, width, height);
		while (rotationDegress < 0) {
			rotationDegress += 360;
		}
		this.rotationDegress = rotationDegress;
	}

	protected void updateGeometry() {
		float halfDiagonal = (float) (sqrt(height * height + width * width) / 2);
		float diagonalHalfAngle = (float) toDegrees(atan2(width, height));
		float[] positions = new float[12];

		for (int i = 0; i < 4; i++) {
			diagonalHalfAngle = -diagonalHalfAngle;
			float angle = (float) toRadians(diagonalHalfAngle + (i / 2 * 180) + rotationDegress);
			positions[3 * i] = (float) (x + round(sin(angle) * halfDiagonal * 100) / 100.0);
			positions[3 * i + 1] = y;
			positions[3 * i + 2] = (float) (z + round(cos(angle) * halfDiagonal * 100) / 100.0);
		}

		setBuffer(Type.Position, 3, positions);
		setBuffer(Type.Normal, 3, new float[] { 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0 });
		setBuffer(Type.Index, 3, new short[] { 0, 1, 2, 0, 2, 3 });
		setBuffer(Type.TexCoord, 2, new float[] { 0, 0, 1, 0, 1, 1, 0, 1 });

		updateBound();
		setStatic();
	}

	@Override
	public void read(JmeImporter e) throws IOException {
		super.read(e);
		InputCapsule capsule = e.getCapsule(this);
		width = capsule.readFloat("width", 0);
		height = capsule.readFloat("height", 0);
		x = capsule.readFloat("x", 0);
		y = capsule.readFloat("y", 0);
		z = capsule.readFloat("z", 0);
		rotationDegress = capsule.readFloat("rotationDegress", 0);
	}

	@Override
	public void write(JmeExporter e) throws IOException {
		super.write(e);
		OutputCapsule capsule = e.getCapsule(this);
		capsule.write(width, "width", 0);
		capsule.write(height, "height", 0);
		capsule.write(x, "x", 0);
		capsule.write(y, "y", 0);
		capsule.write(z, "z", 0);
		capsule.write(rotationDegress, "rotationDegress", 0);
	}

	public static class Builder {
		CenteredSquare instance = new CenteredSquare();

		public Builder withCenter(float x, float y, float z) {
			instance.x = x;
			instance.y = y;
			instance.z = z;
			return this;
		}

		public Builder withHeight(float height) {
			instance.height = height;
			return this;
		}

		public Builder withWidth(float width) {
			instance.width = width;
			return this;
		}

		public Builder withRotationt(float rotationDegress) {
			instance.rotationDegress = rotationDegress;
			return this;
		}

		public CenteredSquare build() {
			instance.updateGeometry();
			return instance;
		}
	}
}
