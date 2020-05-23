package ua.vboden.epdr.shapes;

import java.io.IOException;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;

public class CenteredOrtSquare extends Mesh {

	private static final float[] TEXTURE_INIT_COORD = new float[] { 0, 0, 1, 0, 1, 1, 0, 1 };
	private static final int TEXTURE_POINTS_COUNT = TEXTURE_INIT_COORD.length;

	private float width;
	private float height;
	private float x;
	private float y;
	private float z;
	private int rotationDegress;

	protected CenteredOrtSquare() {
	}

	protected CenteredOrtSquare(float width, float height) {
		this.width = width;
		this.height = height;
	}

	protected CenteredOrtSquare(float x, float y, float z, float width, float height) {
		this(width, height);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	protected CenteredOrtSquare(float x, float y, float z, float width, float height, int rotationDegress) {
		this(x, y, z, width, height);
		while (rotationDegress < 0) {
			rotationDegress += 360;
		}
		this.rotationDegress = rotationDegress;
	}

	protected void updateGeometry() {
		setBuffer(Type.Position, 3, new float[] { x - width / 2, y, z + height / 2, //
				x + width / 2, y, z + height / 2, //
				x + width / 2, y, z - height / 2, //
				x - width / 2, y, z - height / 2 });

		setBuffer(Type.Normal, 3, new float[] { 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0 });
		setBuffer(Type.Index, 3, new short[] { 0, 1, 2, 0, 2, 3 });
		setBuffer(Type.TexCoord, 2, createTextureCoordinates());

		updateBound();
		setStatic();
	}

	private float[] createTextureCoordinates() {
		float[] textureCoord = new float[TEXTURE_POINTS_COUNT];
		int start = rotationDegress / 90;
		for (int i = 0; i < TEXTURE_POINTS_COUNT / 2; i++) {
			textureCoord[i * 2] = TEXTURE_INIT_COORD[(i + start) * 2 % TEXTURE_POINTS_COUNT];
			textureCoord[i * 2 + 1] = TEXTURE_INIT_COORD[(i + start) * 2 % TEXTURE_POINTS_COUNT + 1];
		}
		return textureCoord;
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
		rotationDegress = capsule.readInt("rotationDegress", 0);
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
		CenteredOrtSquare instance = new CenteredOrtSquare();

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

		public Builder withRotationt(int rotationDegress) {
			instance.rotationDegress = rotationDegress;
			return this;
		}

		public CenteredOrtSquare build() {
			instance.updateGeometry();
			return instance;
		}
	}
}
