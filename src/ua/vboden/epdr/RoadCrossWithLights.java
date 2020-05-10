package ua.vboden.epdr;

import static ua.vboden.epdr.Color.YELLOW;
import static ua.vboden.epdr.Constants.DOUBLE_SCALE;
import static ua.vboden.epdr.Constants.SCALE;
import static ua.vboden.epdr.Direction.E;
import static ua.vboden.epdr.Direction.N;
import static ua.vboden.epdr.Direction.S;
import static ua.vboden.epdr.Direction.W;

import java.util.HashMap;
import java.util.Map;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class RoadCrossWithLights extends AbstractRoadCross {

	private Map<Direction, TrafficLishts> lights = new HashMap<>();
	private Color seenColor;

	public RoadCrossWithLights(Vector3f coordinates, AppContext context) {
		super(coordinates, context);
	}

	@Override
	public void addControls() {
		int roadMove = DOUBLE_SCALE;
		int sideMove = 2;
		lights.put(S, addLights(1, 1, sideMove, roadMove, N.getDegress()));
		lights.put(E, addLights(1, -1, roadMove, sideMove, W.getDegress()));
		lights.put(N, addLights(-1, -1, sideMove, roadMove, S.getDegress()));
		lights.put(W, addLights(-1, 1, roadMove, sideMove, E.getDegress()));
		setupReturnPoints();
	}

	private TrafficLishts addLights(int xSign, int zSign, int dx, int dz, int degress) {
		int x = ((int) getCoordinates().x + xSign) * DOUBLE_SCALE + SCALE + xSign * dx;
		int z = ((int) getCoordinates().z + zSign) * DOUBLE_SCALE - SCALE + zSign * dz;
		Spatial lights = getAssetManager().loadModel("Models/lights.j3o");
		lights.scale(1.0f, 1.0f, 1.0f);
		lights.setLocalRotation(new Quaternion().fromAngles(0, Utils.toRadians(degress), 0));
		lights.setLocalTranslation(x, 0, z);
		Utils.addCollisionShape(lights, getBulletAppState());
		getRootNode().attachChild(lights);
		Material yellowOn = getAssetManager().loadMaterial("Materials/Generated/lights-yellow.j3m");
		((Node) lights).getChild(YELLOW.getNodeName()).setMaterial(yellowOn);
		TrafficLishts traficLights = new TrafficLishts(lights);
		Thread thread = new Thread(new LightSwitcher(traficLights, getAssetManager()));
		thread.setDaemon(true);
		thread.start();
		return traficLights;
	}

	@Override
	public Boolean passedCross(Direction direction, Road rememberedRoad, int x, int z) {
		if (this.equals(getContext().getPassedCross()))
			return null;
		int xSign = (int) Utils.getXMoveMultDeg(direction.getDegress());
		int zSign = (int) Utils.getZMoveMultDeg(direction.getDegress());
		Vector3f position = lights.get(direction).getLights().getLocalTranslation();
		int roadX = (int) (position.x / DOUBLE_SCALE);
		int roadZ = (int) (position.z / DOUBLE_SCALE);
		boolean passedLights = hasPassedLights(x + xSign, z + zSign, xSign, zSign, roadX, roadZ);
		float toLightDist = 2;
		boolean passedLightsSeenPoint = hasPassedLights(x + xSign * toLightDist, z + zSign * toLightDist, xSign, zSign,
				roadX, roadZ);
		if (!passedLights && !passedLightsSeenPoint)
			seenColor = lights.get(direction).getColor();
		if (passedLights)
			return Color.GREEN.equals(seenColor);
		return null;
	}

	private boolean hasPassedLights(float x, float z, int xSign, int zSign, int roadX, int roadZ) {
		return xSign * (x - roadX) + zSign * (z - roadZ) > 0;
	}

}
