package ua.vboden.epdr.shapes;

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

	/**
	 * Serialization only. Do not use.
	 */
	protected CenteredSquare() {
	}

	public CenteredSquare(float width, float height) {
		this.width = width;
		this.height = height;
		init();
	}

	public CenteredSquare(float x, float y, float z, float width, float height) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.z = z;
		init();
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}

	public void init() {
		setBuffer(Type.Position, 3, new float[] { x - width / 2, y, z + height / 2, //
				x + width / 2, y, z + height / 2, //
				x + width / 2, y, z - height / 2, //
				x - width / 2, y, z - height / 2 });

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
	}
}
