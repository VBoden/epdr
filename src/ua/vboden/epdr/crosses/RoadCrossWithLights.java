package ua.vboden.epdr.crosses;

import static ua.vboden.epdr.enums.Color.YELLOW;
import static ua.vboden.epdr.Constants.DOUBLE_SCALE;
import static ua.vboden.epdr.Constants.SCALE;
import static ua.vboden.epdr.enums.Direction.E;
import static ua.vboden.epdr.enums.Direction.N;
import static ua.vboden.epdr.enums.Direction.S;
import static ua.vboden.epdr.enums.Direction.W;

import java.util.HashMap;
import java.util.Map;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import ua.vboden.epdr.AppContext;
import ua.vboden.epdr.Road;
import ua.vboden.epdr.Utils;
import ua.vboden.epdr.enums.Color;
import ua.vboden.epdr.enums.Direction;

public class RoadCrossWithLights extends AbstractRoadCross {

	private Map<Direction, TrafficLights> lights = new HashMap<>();
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

	private TrafficLights addLights(int xSign, int zSign, int dx, int dz, int degress) {
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
		TrafficLights traficLights = new TrafficLights(lights, YELLOW);
		Thread thread = new Thread(new LightSwitcher(getContext().getMainApp(), traficLights, getAssetManager()));
		thread.setDaemon(true);
		thread.start();
		return traficLights;
	}

	@Override
	public Boolean passedCross(Direction direction, Road rememberedRoad, int x, int z) {
		boolean passedLights = hasPassedStartControllPoint(x, z, direction, 1);
		float toLightDist = 1f;
		boolean passedLightsSeenPoint = hasPassedStartControllPoint(x, z, direction, toLightDist);
//		System.out.println("====================" + direction + "====" + passedLights + " " + passedLightsSeenPoint);
		if (!passedLights && !passedLightsSeenPoint)
			seenColor = lights.get(direction).getColor();
		if (passedLights) {
			if (!Color.GREEN.equals(seenColor)) {
				getContext().setBreakedRuleKey("8.7.3");
			}
			return Color.GREEN.equals(seenColor);
		}
		return null;
	}

	@Override
	public void resetCheckState() {
		seenColor = null;
	}

}
