package ua.vboden.epdr.shapes;

public class CircledBuilder {

	private float x = 0;
	private float y = 0;
	private float z = 0;
	private float radius = 1;
	private float lineWidth = 0.1f;
	private int circlePoints = 100;
	private float startAngle = 0;
	private float angleSize = 90;
	private int dashSize = 1;

	public Arc buildArc() {
		Arc arc = new Arc(getX(), getY(), getZ(), getRadius(), getLineWidth(), getStartAngle(), getAngleSize(),
				getCirclePoints());
		arc.updateGraphics();
		return arc;
	}

	public ArcDashed buildArcDashed() {
		ArcDashed arc = new ArcDashed(getX(), getY(), getZ(), getRadius(), getLineWidth(), getStartAngle(),
				getAngleSize(), getCirclePoints(), getDashSize());
		arc.updateGraphics();
		return arc;
	}

	public Arc buildCircle() {
		this.withStartAngle(0);
		this.withAngleSize(360);
		Arc arc = new Arc(getX(), getY(), getZ(), getRadius(), getLineWidth(), getStartAngle(), getAngleSize(),
				getCirclePoints());
		arc.updateGraphics();
		return arc;
	}

	public ArcDashed buildCircleDashed() {
		this.withStartAngle(0);
		this.withAngleSize(360);
		ArcDashed arc = new ArcDashed(getX(), getY(), getZ(), getRadius(), getLineWidth(), getStartAngle(),
				getAngleSize(), getCirclePoints(), getDashSize());
		arc.updateGraphics();
		return arc;
	}

	public ArcFilled buildArcFilled() {
		ArcFilled arc = new ArcFilled(getX(), getY(), getZ(), getRadius(), getStartAngle(), getAngleSize(),
				getCirclePoints());
		return arc;
	}

	public ArcFilled buildCircleFilled() {
		this.withStartAngle(0);
		this.withAngleSize(360);
		ArcFilled arc = new ArcFilled(getX(), getY(), getZ(), getRadius(), getStartAngle(), getAngleSize(),
				getCirclePoints());
		return arc;
	}

	public CircledBuilder withCenter(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	private float getX() {
		return x;
	}

	public CircledBuilder withX(float x) {
		this.x = x;
		return this;
	}

	private float getY() {
		return y;
	}

	public CircledBuilder withY(float y) {
		this.y = y;
		return this;
	}

	private float getZ() {
		return z;
	}

	public CircledBuilder withZ(float z) {
		this.z = z;
		return this;
	}

	private float getRadius() {
		return radius;
	}

	public CircledBuilder withRadius(float radius) {
		this.radius = radius;
		return this;
	}

	private float getLineWidth() {
		return lineWidth;
	}

	public CircledBuilder withLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
		return this;
	}

	private int getCirclePoints() {
		return circlePoints;
	}

	public CircledBuilder withCirclePoints(int circlePoints) {
		this.circlePoints = circlePoints;
		return this;
	}

	private float getStartAngle() {
		return startAngle;
	}

	public CircledBuilder withStartAngle(float startAngle) {
		this.startAngle = startAngle;
		return this;
	}

	private float getAngleSize() {
		return angleSize;
	}

	public CircledBuilder withAngleSize(float angleSize) {
		this.angleSize = angleSize;
		return this;
	}

	private int getDashSize() {
		return dashSize;
	}

	public CircledBuilder withDashSize(int dashSize) {
		this.dashSize = dashSize;
		return this;
	}
}
