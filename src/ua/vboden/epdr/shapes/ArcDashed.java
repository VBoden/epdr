package ua.vboden.epdr.shapes;

import java.io.IOException;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;

public class ArcDashed extends Arc {

	private int dashSize = 1;

	/**
	 * Serialization only. Do not use.
	 */
	protected ArcDashed() {
	}

	public ArcDashed(float x, float y, float z, float radius, float lineWidth, float startAngle, float angleSize,
			int dashSize) {
		super(x, y, z, radius, lineWidth, startAngle, angleSize);
		this.dashSize = dashSize;
	}

	public ArcDashed(float x, float y, float z, float radius, float lineWidth, float startAngle, float angleSize,
			int circlePoints, int dashSize) {
		super(x, y, z, radius, lineWidth, startAngle, angleSize, circlePoints);
		this.dashSize = dashSize;
	}

	@Override
	protected Mode getShapeMode() {
		return Mode.Triangles;
	}

	@Override
	protected int getIndexesPerAngle() {
		return 6;
	}

	@Override
	protected int addIndexes(int k, short[] indexes, int i) {
		if (k > 0 && k % (2 * dashSize) < dashSize) {
			indexes[i] = (short) (2 * k - 2);
			indexes[i + 1] = (short) (2 * k - 1);
			indexes[i + 2] = (short) (2 * k);
			indexes[i + 3] = (short) (2 * k);
			indexes[i + 4] = (short) (2 * k - 1);
			indexes[i + 5] = (short) (2 * k + 1);
			return i + 6;
		}
		return i;
	}

	@Override
	public void read(JmeImporter e) throws IOException {
		super.read(e);
		InputCapsule capsule = e.getCapsule(this);
		dashSize = capsule.readInt("dashSize", 1);
	}

	@Override
	public void write(JmeExporter e) throws IOException {
		super.write(e);
		OutputCapsule capsule = e.getCapsule(this);
		capsule.write(dashSize, "dashSize", 1);
	}

}
