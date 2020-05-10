package ua.vboden.epdr;

import static ua.vboden.epdr.Color.YELLOW;
import static ua.vboden.epdr.Constants.DOUBLE_SCALE;
import static ua.vboden.epdr.Constants.SCALE;
import static ua.vboden.epdr.Direction.E;
import static ua.vboden.epdr.Direction.N;
import static ua.vboden.epdr.Direction.S;
import static ua.vboden.epdr.Direction.W;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class RoadCrossWithMan extends AbstractRoadCross {

	private TrafficMan trafficMan;

	public RoadCrossWithMan(Vector3f coordinates, AppContext context) {
		super(coordinates, context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addControls() {
		trafficMan = addLights(N.getDegress());
//		lights.put(S, addLights(1, 1, sideMove, roadMove, N.getDegress()));
//		lights.put(E, addLights(1, -1, roadMove, sideMove, W.getDegress()));
//		lights.put(N, addLights(-1, -1, sideMove, roadMove, S.getDegress()));
//		lights.put(W, addLights(-1, 1, roadMove, sideMove, E.getDegress()));
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
		trafficMan = new TrafficMan(man);
		Thread thread = new Thread(new ManPositionSwitcher(trafficMan));
		thread.setDaemon(true);
		thread.start();
		return trafficMan;
	}

	// Down:
	// Rot: (0,0,0) Trans: (-1.0, 0.5, 0.0)
	// Up:
	// Rot: (0,0,0) Trans: (-1.0, 4.5, 0.0)
	// Forward:
	// Rot: (90,0,0) Trans: (-1.0, 2.5, 0.0)
	// Side:
	// Rot: (0,0,90) Trans: (-1.0, 2.5, 0.0)
	// Before:
	// Rot: (0.0, 0.0, -90.0) Trans: (-1.0, 2.5, 0.5)

	@Override
	public Boolean passedCross(Direction direction, Road rememberedRoad, int x, int z) {
		// TODO Auto-generated method stub
		return true;
	}

}
