package ua.vboden.epdr;

import static ua.vboden.epdr.Constants.DOUBLE_SCALE;

import com.jme3.math.Vector3f;

import ua.vboden.epdr.crosses.AbstractRoadCross;
import ua.vboden.epdr.enums.Direction;

public class MovingManager {

	private AppContext context;
	private Direction rememberedDir;
	private Direction lastNotNullDirection;
	private Road lastNotNullRoad;
	private AbstractRoadCross rememberedCross;

	public MovingManager(AppContext context) {
		this.context = context;
	}

	public void manage(Vector3f currentPos) {
		Direction currentDirection = findDirection();
		if (currentDirection != null) {
			lastNotNullDirection = currentDirection;
		}
		int x = Math.round(currentPos.x / DOUBLE_SCALE);
		int z = Math.round(currentPos.z / DOUBLE_SCALE);
		if (Direction.E.equals(lastNotNullDirection))
			z += 1;
		if (Direction.S.equals(lastNotNullDirection))
			x -= 1;
		Road road = findRoad(x, z);
		if (road != null && currentDirection != null)
			lastNotNullRoad = road;
		if (lastNotNullRoad == null)
			return;
		if (context.getPassedCross() != null || x > 5 || z > 5) {
			AbstractRoadCross cross = lastNotNullRoad.getNearestCross(x, z, lastNotNullDirection);
			if (rememberedCross == null && cross != null || rememberedCross.equals(context.getPassedCross())) {
				rememberedCross = cross;
				rememberedDir = lastNotNullDirection;
			}
//			System.out.println(rememberedCross);
			if (rememberedCross != null) {
				Boolean passed = rememberedCross.passedCross(lastNotNullDirection, lastNotNullRoad, x, z);
				if (passed == null)
					return;
				rememberedCross.resetCheckState();
//				System.out.println("passed");
				if (passed) {
					context.setPassedCross(rememberedCross);
					rememberedCross = null;
					System.out.println("passed2");
				} else {
					context.setSpeed(0);
					float radians = Utils.toRadians(rememberedDir.getDegress());
					context.cam.getRotation().fromAngles(0, radians, 0);
					Vector3f onMap = rememberedCross.getPointOnMap(rememberedDir);
					float x0 = onMap.x - Utils.getXMoveMult(radians) * Constants.RETURN_DISTANCE;
					float z0 = onMap.z - Utils.getZMoveMult(radians) * Constants.RETURN_DISTANCE;
					context.getPlayer().setPhysicsLocation(new Vector3f(x0, 1, z0));
				}
			}
		}
	}

	private Road findRoad(int x, int z) {
		if (lastNotNullRoad != null && lastNotNullRoad.hasPoint(x, z)) {
			return lastNotNullRoad;
		}
		for (Road road : context.getRoads()) {
			if (road.hasPoint(x, z))
				return road;
		}
		return null;
	}

	private Direction findDirection() {
		float currentAngle = context.getAngleDegress();
		for (Direction direction : Direction.values()) {
			if (Math.abs(currentAngle - direction.getDegress()) < 20)
				return direction;
		}
		if (Math.abs(currentAngle - 360) < 20)
			return Direction.N;
		return null;
	}
}
