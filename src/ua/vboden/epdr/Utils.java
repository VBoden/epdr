package ua.vboden.epdr;

import com.jme3.math.Quaternion;

public class Utils {

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

}