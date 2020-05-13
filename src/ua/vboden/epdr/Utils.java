package ua.vboden.epdr;

import static ua.vboden.epdr.Constants.CROSS_PASSED_RADIUS;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class Utils {

	public static void addCollisionShape(Spatial object, BulletAppState bulletAppState) {
		CollisionShape cubShape = CollisionShapeFactory.createMeshShape(object);
		RigidBodyControl landscape2 = new RigidBodyControl(cubShape, 0);
		object.addControl(landscape2);
		bulletAppState.getPhysicsSpace().add(landscape2);
	}

	public static float[] rotate(Quaternion currentRotation, float[] degress) {
		float[] angles = currentRotation.toAngles(null);
		for (int i = 0; i < 3; i++)
			angles[i] += toRadians(degress[i]);
		currentRotation.fromAngles(angles);
		return angles;
	}

	public static float rotateBy(Quaternion currentRotation, float degress, int pos) {
		float[] angles = currentRotation.toAngles(null);
		float angleRadians = toRadians(degress);
		angles[pos] += angleRadians;
		currentRotation.fromAngles(angles);
		return angles[pos];
	}

	public static float rotateByY(Quaternion currentRotation, float degress) {
		return rotateBy(currentRotation, degress, 1);
	}

	public static float toRadians(float degress) {
		return (float) (degress * (Math.PI / 180.0));
	}

	public static float toDegress(float radians) {
		return (float) (radians * 180.0 / Math.PI);
	}

	public static float getXMoveMult(float radians) {
		return (float) Math.round(Math.sin(radians));
	}

	public static float getZMoveMult(float radians) {
		return (float) Math.round(Math.cos(radians));
	}

	public static float getZMoveMultDeg(float degress) {
		return getZMoveMult(toRadians(degress));
	}

	public static float getXMoveMultDeg(float degress) {
		return getXMoveMult(toRadians(degress));
	}

	public static float correctAngleRange(float currentDegress) {
		if (currentDegress >= 360)
			currentDegress -= 360;
		else if (currentDegress < 0)
			currentDegress += 360;
		return currentDegress;
	}

	public static boolean isOutOfSquare(Vector3f center, Vector3f position) {
		return isDistanceGreaterThan(center, position, CROSS_PASSED_RADIUS);
	}

	public static boolean isDistanceGreaterThan(Vector3f center, Vector3f position, int distance) {
		return Math.abs(position.x - center.x) + Math.abs(position.z - center.z) >= distance;
	}
}
