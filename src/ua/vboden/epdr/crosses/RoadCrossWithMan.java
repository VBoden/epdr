package ua.vboden.epdr.crosses;

import static ua.vboden.epdr.Constants.DOUBLE_SCALE;
import static ua.vboden.epdr.Constants.SCALE;

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
		if (directionAtSeenPoint == null) {
			directionAtSeenPoint = direction;
		}
		float toControllPointDistance = 0f;
		boolean passedLightsSeenPoint = hasPassedStartControllPoint(x, z, directionAtSeenPoint,
				toControllPointDistance);
		int diffMyDirection = direction.getDegress() - directionAtSeenPoint.getDegress();
//		System.out.println("           ----------" + x + " " + z + " " + direction);
		if (!passedLightsSeenPoint && Math.abs(diffMyDirection) != 180) {
			seenStickPos = trafficMan.getStickPosition();
			seenDirection = trafficMan.getDirection();
			directionAtSeenPoint = direction;
//			System.out.println("           ----------" + this);
//			System.out.println("           ----------" + seenStickPos + " " + seenDirection + " " + directionAtSeenPoint
//					+ " " + direction);
		} else if (seenDirection != null) {
			if (seenDirection.equals(directionAtSeenPoint)) {
				getContext().setBreakedRuleKey("8.8.all_back");
				return false;
			}
			if (StickPosition.UP.equals(seenStickPos)) {
				getContext().setBreakedRuleKey("8.8.c");
				return false;
			}
			int diff = seenDirection.getDegress() - directionAtSeenPoint.getDegress();
			if (diff == 90 || diff == -270) { // move from left
				if (StickPosition.FORWARD.equals(seenStickPos))
					return true;
			}
			if (diff == -90 || diff == 270) { // move from right
				if (StickPosition.FORWARD.equals(seenStickPos)) {
					getContext().setBreakedRuleKey("8.8.b_right");
					return false;
				}
			}
			if (Utils.isOutOfSquare(getCoordinates(), new Vector3f(x, 0, z)) || Math.abs(diffMyDirection) == 180) {
				if (StickPosition.DOWN.equals(seenStickPos) || StickPosition.SIDE.equals(seenStickPos)
						|| StickPosition.BEFORE.equals(seenStickPos)) {
					List<Integer> turnRightOrForward = Arrays.asList(0, -90, 270);
					if ((diff != 0 && Math.abs(diff) != 180) && turnRightOrForward.contains(diffMyDirection)) {
						return true;
					} else {
						if (diff == 0 || Math.abs(diff) == 180) {
							getContext().setBreakedRuleKey("8.8.a_2");
						} else {
							getContext().setBreakedRuleKey("8.8.a_1");
						}
						return false;
					}
				}
				if (StickPosition.FORWARD.equals(seenStickPos)) {// only move to man left not considered here
					boolean isTurningRight = diffMyDirection == -90 || diffMyDirection == 270;
					if (!isTurningRight) {
						getContext().setBreakedRuleKey("8.8.b");
					}
					return isTurningRight;
				}
			}
		}
		return null;
	}

	@Override
	public void resetCheckState() {
//		System.out.println(seenStickPos + " " + seenDirection + " " + directionAtSeenPoint);
		seenDirection = null;
		seenStickPos = null;
		directionAtSeenPoint = null;
	}

	protected void setTrafficMan(TrafficMan trafficMan) {
		this.trafficMan = trafficMan;
	}

}
