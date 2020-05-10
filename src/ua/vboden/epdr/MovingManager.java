package ua.vboden.epdr;

import static ua.vboden.epdr.Constants.DOUBLE_SCALE;

import com.jme3.math.Vector3f;

public class MovingManager {

	private AppContext context;
	private Direction rememberedDir;
	private Road rememberedRoad;

	public MovingManager(AppContext context) {
		this.context = context;
	}

	public void manage(Vector3f currentPos) {
		Direction currentDirection = findDirection();
		System.out.printf("dir:%s\n", currentDirection);
		if (currentDirection != null) {
			rememberedDir = currentDirection;
		}
		Direction direction = rememberedDir;
		System.out.println(currentPos);
		int x = Math.round(currentPos.x / DOUBLE_SCALE);
		int z = Math.round(currentPos.z / DOUBLE_SCALE);
		if (Direction.E.equals(direction))
			z += 1;
		if (Direction.S.equals(direction))
			x -= 1;
		Road road = findRoad(x, z);
//		System.out.println("road=" + road + " direction=" + currentDirection);
		if (road != null && currentDirection != null)
			rememberedRoad = road;
		if (rememberedRoad == null)
			return;
		if (context.getPassedCross() != null || x > 5 || z > 5) {
			AbstractRoadCross cross = rememberedRoad.getNearestCross(x, z, direction);
			if (cross != null) {
				Boolean passed = cross.passedCross(direction, rememberedRoad, x, z);
				if (passed == null)
					return;
				if (passed) {
					context.setPassedCross(cross);
				} else {
					context.setSpeed(0);
					float radians = Utils.toRadians(direction.getDegress());
					context.cam.getRotation().fromAngles(0, radians, 0);
					Vector3f onMap = cross.getPointOnMap(direction);
					float x0 = onMap.x - Math.round(Math.sin(radians)) * Constants.RETURN_DISTANCE;
					float z0 = onMap.z - Math.round(Math.cos(radians)) * Constants.RETURN_DISTANCE;
					context.getPlayer().setPhysicsLocation(new Vector3f(x0, 0, z0));
				}
			}
		}
	}

	private Road findRoad(int x, int z) {
		System.out.printf("x:%d z:%d\n", x, z);
		if (rememberedRoad != null && rememberedRoad.hasPoint(x, z)) {
			return rememberedRoad;
		}
		for (Road road : context.getRoads()) {
			if (road.hasPoint(x, z))
				return road;
		}
		return null;
	}

	private Direction findDirection() {
		float currentAngle = context.getAngleDegress();
		System.out.printf("angle:%f\n", currentAngle);
		for (Direction direction : Direction.values()) {
			if (Math.abs(currentAngle - direction.getDegress()) < 20)
				return direction;
		}
		if (Math.abs(currentAngle - 360) < 20)
			return Direction.N;
		return null;
	}
}
