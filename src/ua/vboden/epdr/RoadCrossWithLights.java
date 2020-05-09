package ua.vboden.epdr;

import static ua.vboden.epdr.Constants.DOUBLE_SCALE;
import static ua.vboden.epdr.Constants.SCALE;
import static ua.vboden.epdr.Direction.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class RoadCrossWithLights extends AbstractRoadCross {

	private Map<Direction, TrafficLishts> lights = new HashMap<>();

	public RoadCrossWithLights(Vector3f coordinates, AppContext context) {
		super(coordinates, context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addControls() {
		int roadMove = 2 * DOUBLE_SCALE;
		int sideMove = 2;
//		road_move = 2 * ROAD_DBL_SCALE
//		        side_move = 2
//		        line_center_move = ROAD_SCALE
//		        						self.lights[Direction.N] = self.add_lights_directed(1, -1, side_move, road_move, Direction.N)
//		        self.lights[Direction.N].road_point = self.calculate_map_point(1, -1, -line_center_move, road_move)
//		        self.lights[Direction.S] = self.add_lights_directed(-1, 1, side_move, road_move, Direction.S)
//		        self.lights[Direction.S].road_point = self.calculate_map_point(-1, 1, -line_center_move, road_move)
//		        self.lights[Direction.W] = self.add_lights_directed(1, 1, road_move, side_move, Direction.W)
//		        self.lights[Direction.W].road_point = self.calculate_map_point(1, 1, road_move, -line_center_move)
//		        self.lights[Direction.E] = self.add_lights_directed(-1, -1, road_move, side_move, Direction.E)
//		        self.lights[Direction.E].road_point = self.calculate_map_point(-1, -1, road_move, -line_center_move)
		lights.put(N, addLights(1, 1, sideMove, roadMove, N.getDegress()));
		lights.put(W, addLights(1, -1, roadMove, sideMove, W.getDegress()));
		lights.put(S, addLights(-1, -1, sideMove, roadMove, S.getDegress()));
		lights.put(E, addLights(-1, 1, roadMove, sideMove, E.getDegress()));
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
		TrafficLishts traficLights = new TrafficLishts();
		return traficLights;
	}

	private void addLights0(int x, int y, int z) {
//		Spatial ninja = assetManager.loadModel("Models/tree.j3o");
		Spatial lights = getAssetManager().loadModel("Models/lights.j3o");
		Material greenOn = getAssetManager().loadMaterial("Materials/Generated/lights-green.j3m");
//		Material mat2 = assetManager.loadMaterial("Materials/Generated/tree1-Cone1.j3m");
		((com.jme3.scene.Node) lights).getChild("green").setMaterial(greenOn);
//		((com.jme3.scene.Node)ninja).getChild("Cone1").setMaterial(mat2);
//		Spatial ninja = assetManager.loadModel("Models/tree2.blend");
		lights.scale(1.0f, 1.0f, 1.0f);
//      ninja.rotate(0.0f, -3.0f, 0.0f);
		lights.setLocalTranslation(x, y, z);
		Utils.addCollisionShape(lights, getBulletAppState());
		getRootNode().attachChild(lights);
	}

}
