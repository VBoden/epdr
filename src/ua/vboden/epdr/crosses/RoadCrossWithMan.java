package ua.vboden.epdr.crosses;

import static ua.vboden.epdr.Constants.DOUBLE_SCALE;
import static ua.vboden.epdr.Constants.SCALE;
import static ua.vboden.epdr.enums.Direction.N;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import ua.vboden.epdr.AppContext;
import ua.vboden.epdr.Road;
import ua.vboden.epdr.Utils;
import ua.vboden.epdr.enums.Direction;
import ua.vboden.epdr.enums.StickPosition;

public class RoadCrossWithMan extends AbstractRoadCross {

	private TrafficMan trafficMan;
	private Direction seenDirection;
	private StickPosition seenStickPos;
	private Direction directionAtSeenPoint;
	private Direction[] directions = Direction.values();

	public RoadCrossWithMan(Vector3f coordinates, AppContext context) {
		super(coordinates, context);
	}

	@Override
	public void addControls() {
		int index = ThreadLocalRandom.current().nextInt(0, directions.length);
		Direction startDirection = directions[index];
		trafficMan = addTrafficMan(startDirection);
		setupReturnPoints();
	}

	private TrafficMan addTrafficMan(Direction startDirection) {
		int x = (int) getCoordinates().x * DOUBLE_SCALE + SCALE;
		int z = (int) getCoordinates().z * DOUBLE_SCALE - SCALE;
		Spatial man = getAssetManager().loadModel("Models/man.j3o");
		man.scale(1.0f, 1.0f, 1.0f);
		man.setLocalRotation(new Quaternion().fromAngles(0, Utils.toRadians(startDirection.getDegress()), 0));
		man.setLocalTranslation(x, 0, z);
		Utils.addCollisionShape(man, getBulletAppState());
		getRootNode().attachChild(man);
		man.setLocalRotation(new Quaternion().fromAngles(0, 0, 0));
		((Node) man).getChild("stick").setLocalRotation(new Quaternion(new float[] { 0, 0, 0 }));
		trafficMan = new TrafficMan(man, startDirection, StickPosition.DOWN);
		Thread thread = new Thread(new ManPositionSwitcher(getContext().getMainApp(), trafficMan));
		thread.setDaemon(true);
		thread.start();
		return trafficMan;
	}

	@Override
	public Boolean passedCross(Direction direction, Road rememberedRoad, int x, int z) {
//		if (this.equals(getContext().getPassedCross()))
//			return null;
		if (directionAtSeenPoint == null) {
			directionAtSeenPoint = direction;
		}
		int xSign = (int) Utils.getXMoveMultDeg(directionAtSeenPoint.getDegress());
		int zSign = (int) Utils.getZMoveMultDeg(directionAtSeenPoint.getDegress());
		Vector3f position = getPointOnMap(directionAtSeenPoint);
		int roadX = (int) (position.x / DOUBLE_SCALE);
		int roadZ = (int) (position.z / DOUBLE_SCALE);
		float toLightDist = 0f;
		boolean passedLightsSeenPoint = hasPassedLights(x + xSign * toLightDist, z + zSign * toLightDist, xSign, zSign,
				roadX, roadZ);
		int diffMyDirection = direction.getDegress() - directionAtSeenPoint.getDegress();
		System.out.println("           ----------" + x + " " + z + " " + direction);
		if (!passedLightsSeenPoint && Math.abs(diffMyDirection) != 180) {
			seenStickPos = trafficMan.getStickPosition();
			seenDirection = trafficMan.getDirection();
			directionAtSeenPoint = direction;
			System.out.println("           ----------" + this);
			System.out.println("           ----------" + seenStickPos + " " + seenDirection + " " + directionAtSeenPoint
					+ " " + direction);
		} else if (seenDirection != null) {
			if (StickPosition.UP.equals(seenStickPos) || seenDirection.equals(directionAtSeenPoint))
				return false;
			int diff = seenDirection.getDegress() - directionAtSeenPoint.getDegress();
			if (diff == 90 || diff == -270) { // move from left
				if (StickPosition.FORWARD.equals(seenStickPos))
					return true;
			}
			if (diff == -90 || diff == 270) { // move from right
				if (StickPosition.FORWARD.equals(seenStickPos))
					return false;
			}
			if (Utils.isOutOfSquare(getCoordinates(), new Vector3f(x, 0, z)) || Math.abs(diffMyDirection) == 180) {
				if (StickPosition.DOWN.equals(seenStickPos) || StickPosition.SIDE.equals(seenStickPos)
						|| StickPosition.BEFORE.equals(seenStickPos)) {
					List<Integer> turnRightOrForward = Arrays.asList(0, -90, 270);
					if ((diff != 0 && Math.abs(diff) != 180) && turnRightOrForward.contains(diffMyDirection)) {
						return true;
					} else {
						return false;
					}
				}
				if (StickPosition.FORWARD.equals(seenStickPos)) {// only move to man left not considered
					return diffMyDirection == -90 || diffMyDirection == 270;
				}
			}
		}
		return null;
	}

	private boolean hasPassedLights(float x, float z, int xSign, int zSign, int roadX, int roadZ) {
		return xSign * (x - roadX) + zSign * (z - roadZ) > 0;
	}

	@Override
	public void resetCheckState() {
		System.out.println(seenStickPos + " " + seenDirection + " " + directionAtSeenPoint);
		seenDirection = null;
		seenStickPos = null;
		directionAtSeenPoint = null;
	}

	protected void setTrafficMan(TrafficMan trafficMan) {
		this.trafficMan = trafficMan;
	}

}
