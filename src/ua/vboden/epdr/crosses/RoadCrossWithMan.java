package ua.vboden.epdr.crosses;

import static ua.vboden.epdr.Constants.DOUBLE_SCALE;
import static ua.vboden.epdr.Constants.SCALE;
import static ua.vboden.epdr.enums.Direction.N;

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

	public RoadCrossWithMan(Vector3f coordinates, AppContext context) {
		super(coordinates, context);
	}

	@Override
	public void addControls() {
		trafficMan = addLights(N.getDegress());
		setupReturnPoints();
	}

	private TrafficMan addLights(int degress) {
		int x = (int) getCoordinates().x * DOUBLE_SCALE + SCALE;
		int z = (int) getCoordinates().z * DOUBLE_SCALE - SCALE;
		Spatial man = getAssetManager().loadModel("Models/man.j3o");
		man.scale(1.0f, 1.0f, 1.0f);
		man.setLocalRotation(new Quaternion().fromAngles(0, Utils.toRadians(degress), 0));
		man.setLocalTranslation(x, 0, z);
		Utils.addCollisionShape(man, getBulletAppState());
		getRootNode().attachChild(man);
		man.setLocalRotation(new Quaternion().fromAngles(0, 0, 0));
		((Node) man).getChild("stick").setLocalRotation(new Quaternion(new float[] { 0, 0, 0 }));
		trafficMan = new TrafficMan(man, N, StickPosition.DOWN);
		Thread thread = new Thread(new ManPositionSwitcher(getContext().getMainApp(), trafficMan));
		thread.setDaemon(true);
		thread.start();
		return trafficMan;
	}

	@Override
	public Boolean passedCross(Direction direction, Road rememberedRoad, int x, int z) {
		if (this.equals(getContext().getPassedCross()))
			return null;
		int xSign = (int) Utils.getXMoveMultDeg(direction.getDegress());
		int zSign = (int) Utils.getZMoveMultDeg(direction.getDegress());
		Vector3f position = getPointOnMap(direction);
		int roadX = (int) (position.x / DOUBLE_SCALE);
		int roadZ = (int) (position.z / DOUBLE_SCALE);
		float toLightDist = 0f;
		boolean passedLightsSeenPoint = hasPassedLights(x + xSign * toLightDist, z + zSign * toLightDist, xSign, zSign,
				roadX, roadZ);
		if (!passedLightsSeenPoint) {
			seenStickPos = trafficMan.getStickPosition();
			seenDirection = trafficMan.getDirection();
		} else if (seenDirection != null) {
			if (StickPosition.UP.equals(seenStickPos) || seenDirection.equals(direction))
				return false;
			int diff = seenDirection.getDegress() - direction.getDegress();
			if (diff == 90 || diff == -270) {
				System.out.println("moving from left");
				if (StickPosition.FORWARD.equals(seenStickPos))
					return true;
			}
			if (diff == -90 || diff == 270) {
				if (StickPosition.FORWARD.equals(seenStickPos))
					return false;
			}
		}

		return null;
	}

	private boolean hasPassedLights(float x, float z, int xSign, int zSign, int roadX, int roadZ) {
		return xSign * (x - roadX) + zSign * (z - roadZ) > 0;
	}

	@Override
	public void resetCheckState() {
		seenDirection = null;
		seenStickPos = null;
	}

}