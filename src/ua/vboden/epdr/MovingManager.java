package ua.vboden.epdr;

import static ua.vboden.epdr.Constants.CROSS_PASSED_RADIUS;
import static ua.vboden.epdr.Constants.DOUBLE_SCALE;

import com.jme3.math.Vector3f;

import ua.vboden.epdr.crosses.AbstractRoadCross;
import ua.vboden.epdr.enums.Direction;

public class MovingManager {

	private AppContext context;
	private Direction dirAtStartCrossing;
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
		Vector3f currentPosition = new Vector3f(x, 0, z);
		Road road = findRoad(x, z);
		if (road != null && currentDirection != null)
			lastNotNullRoad = road;
		if (lastNotNullRoad == null)
			return;
		if (context.getPassedCross() != null) {
			Vector3f passedCoord = context.getPassedCross().getCoordinates();
			if (!Utils.isOutOfSquare(passedCoord, currentPosition)) {
				return;
			}
		}
		AbstractRoadCross cross = lastNotNullRoad.getNearestCross(x, z, lastNotNullDirection);
		if ((rememberedCross == null || Utils.isOutOfSquare(rememberedCross.getCoordinates(), currentPosition))
				&& cross != null
				&& !Utils.isDistanceGreaterThan(cross.getCoordinates(), currentPosition, 2 * CROSS_PASSED_RADIUS)) {
			rememberedCross = cross;
			dirAtStartCrossing = lastNotNullDirection;
		}
		System.out.println(rememberedCross);
		if (rememberedCross != null) {
			Boolean passed = rememberedCross.passedCross(lastNotNullDirection, lastNotNullRoad, x, z);
			if (passed == null) {
				System.out.println("                  NULL " + rememberedCross);
				System.out.println("                       " + dirAtStartCrossing + " " + x + " " + z);
				return;
			}
			rememberedCross.resetCheckState();
//				System.out.println("passed");
			if (passed) {
				System.out.println("PASSED___ " + rememberedCross);
				System.out.println(dirAtStartCrossing + " " + x + " " + z);
				context.setPassedCross(rememberedCross);
				rememberedCross = null;
			} else {
				System.out.println(rememberedCross);
				System.out.println(dirAtStartCrossing + " " + x + " " + z);
				context.setSpeed(0);
				float radians = Utils.toRadians(dirAtStartCrossing.getDegress());
				context.setAngle(radians);
				context.cam.getRotation().fromAngles(0, radians, 0);
				lastNotNullDirection = dirAtStartCrossing;
				Vector3f onMap = rememberedCross.getPointOnMap(dirAtStartCrossing);
				float x0 = onMap.x - Utils.getXMoveMult(radians) * Constants.RETURN_DISTANCE;
				float z0 = onMap.z - Utils.getZMoveMult(radians) * Constants.RETURN_DISTANCE;
				context.getPlayer().setPhysicsLocation(new Vector3f(x0, 1, z0));
				context.getNiftyManager().showPopup();
			}
		}
	}

//	passed2
//	AbstractRoadCross [members=[0, 6] coordinates=(1.0, 0.0, 1.0)]
//	E 3 1
//	AbstractRoadCross [members=[0, 6] coordinates=(1.0, 0.0, 1.0)]
//	E 3 1
//	AbstractRoadCross [members=[0, 6] coordinates=(1.0, 0.0, 1.0)]
//	E 3 1
//	passed2
//	AbstractRoadCross [members=[2, 6] coordinates=(20.0, 0.0, 1.0)]
//	N 1 1
//	passed2
//	AbstractRoadCross [members=[3, 6] coordinates=(40.0, 0.0, 1.0)]
//	N 20 -1
//	passed2
//	AbstractRoadCross [members=[6, 4] coordinates=(60.0, 0.0, 1.0)]
//	N 40 -1
//	passed2
//	AbstractRoadCross [members=[5, 6] coordinates=(80.0, 0.0, 1.0)]
//	N 60 -1

	private Road findRoad(int x, int z) {
		if (lastNotNullRoad != null && roadCorrespondsDirection(lastNotNullRoad) && lastNotNullRoad.hasPoint(x, z)) {
			return lastNotNullRoad;
		}
		for (Road road : context.getRoads()) {
			if (roadCorrespondsDirection(road) && road.hasPoint(x, z))
				return road;
		}
		return null;
	}

	private boolean roadCorrespondsDirection(Road road) {
		return lastNotNullDirection != null && (road.isDirectedByZ() != (lastNotNullDirection.getDegress() % 180 != 0));
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
