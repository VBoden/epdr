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
		trafficMan = new TrafficMan(man);
		Thread thread = new Thread(new ManPositionSwitcher(getContext().getMainApp(), trafficMan));
		thread.setDaemon(true);
		thread.start();
		return trafficMan;
	}

	@Override
	public Boolean passedCross(Direction direction, Road rememberedRoad, int x, int z) {
		// TODO Auto-generated method stub
		return true;
	}

}
