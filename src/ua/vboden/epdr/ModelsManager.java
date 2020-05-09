package ua.vboden.epdr;

import static ua.vboden.epdr.Constants.DOUBLE_SCALE;
import static ua.vboden.epdr.Constants.SCALE;

import java.util.Random;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;

public class ModelsManager {

	private Node rootNode;
	private AssetManager assetManager;
	private BulletAppState bulletAppState;
	private AppContext context;
	private Random rand = new Random();

	public ModelsManager(Node rootNode, AssetManager assetManager, BulletAppState bulletAppState, AppContext context) {
		this.rootNode = rootNode;
		this.assetManager = assetManager;
		this.bulletAppState = bulletAppState;
		this.context = context;
	}

	public void addModels() {
		createRoads();
	}

	public void createRoads() {
		RoadsCreator creator = new RoadsCreator(context);
		creator.createRoads();

		Material roadWithoutMarking = assetManager.loadMaterial("Materials/green.j3m");
		roadWithoutMarking.setTexture("DiffuseMap", assetManager.loadTexture("Materials/RoadEmpty.png"));
		Material roadWithSide = assetManager.loadMaterial("Materials/green.j3m");
		roadWithSide.setTexture("DiffuseMap", assetManager.loadTexture("Materials/RoadSide.png"));
		Material roadWithCrossSide = assetManager.loadMaterial("Materials/green.j3m");
		roadWithCrossSide.setTexture("DiffuseMap", assetManager.loadTexture("Materials/RoadCrossSide.png"));
		Material roadWithDashedMarking = assetManager.loadMaterial("Materials/green.j3m");
		roadWithDashedMarking.setTexture("DiffuseMap", assetManager.loadTexture("Materials/RoadDashed.png"));

		for (Road road : context.getRoads()) {
			int rotateDegrees = road.getXMult() * 90;
			for (int i = (int) road.getStart().x; i < road.getEnd().x + 1; i++) {
				for (int j = (int) road.getStart().z; j < road.getEnd().z + 1; j++) {
					int sideRotateDegrees0 = 180;
					Material roadMaterial;
					Material roadSideMaterial = null;
					if (isRoadCrossPoint(i, j, road)) {
						roadMaterial = roadWithoutMarking;
						roadSideMaterial = roadWithCrossSide;
						addRoadSquare((i + road.getXMult() + road.getZMult()) * DOUBLE_SCALE,
								(j + road.getZMult() - road.getXMult()) * DOUBLE_SCALE, roadSideMaterial,
								rotateDegrees + 180);
						addRoadSquare((i + road.getXMult() - road.getZMult()) * DOUBLE_SCALE,
								(j + road.getZMult() + road.getXMult()) * DOUBLE_SCALE, roadSideMaterial,
								rotateDegrees + 90);
						addRoadSquare((i - road.getXMult() + road.getZMult()) * DOUBLE_SCALE,
								(j - road.getZMult() - road.getXMult()) * DOUBLE_SCALE, roadSideMaterial,
								rotateDegrees - 90);
						addRoadSquare((i - road.getXMult() - road.getZMult()) * DOUBLE_SCALE,
								(j - road.getZMult() + road.getXMult()) * DOUBLE_SCALE, roadSideMaterial,
								rotateDegrees);
					} else if (isRoadCrossPoint(i + road.getXMult(), j + road.getZMult(), road)) {
						roadMaterial = roadWithoutMarking;
					} else if (isRoadCrossPoint(i - road.getXMult(), j - road.getZMult(), road)) {
						roadMaterial = roadWithoutMarking;
					} else {
						roadMaterial = roadWithDashedMarking;
						roadSideMaterial = roadWithSide;
						addRoadSquare((i + road.getZMult()) * DOUBLE_SCALE, (j - road.getXMult()) * DOUBLE_SCALE,
								roadSideMaterial, rotateDegrees - sideRotateDegrees0);
						addRoadSquare((i - road.getZMult()) * DOUBLE_SCALE, (j + road.getXMult()) * DOUBLE_SCALE,
								roadSideMaterial, rotateDegrees);
						addBuildingOrTree((i + road.getZMult() * 2) * DOUBLE_SCALE,
								(j + road.getXMult() * 2) * DOUBLE_SCALE);
						addBuildingOrTree((i - road.getZMult() * 2) * DOUBLE_SCALE,
								(j - road.getXMult() * 2) * DOUBLE_SCALE);
					}
					addRoadSquare(i * DOUBLE_SCALE, j * DOUBLE_SCALE, roadMaterial, rotateDegrees);
				}
			}
		}
	}

	private void addRoadSquare(int x, int z, Material material, int rotate) {
		Quad quad = new Quad(1, 1);
		Geometry blue = new Geometry("Quad", quad);
		blue.setLocalScale(10, 10, 1);
		blue.setLocalRotation(new Quaternion().fromAngles(-FastMath.HALF_PI, Utils.toRadians(rotate), 0));
		blue.setLocalTranslation(x, 0.01f, z);
		correctAfterRotation(blue, rotate);
		blue.setMaterial(material);
		rootNode.attachChild(blue);
	}

	private void correctAfterRotation(Geometry blue, int rotate) {
		int quaterMiddle = -rotate + 45;
		float x = (float) Math.min(0, Math.signum(Math.sin(Utils.toRadians(quaterMiddle))));
		float z = (float) Math.min(0, Math.signum(Math.cos(Utils.toRadians(quaterMiddle))));
		Vector3f scale = blue.getLocalScale();
		blue.getLocalTranslation().addLocal(-x * scale.x, 0, z * scale.y);
	}

	private boolean isRoadCrossPoint(int x, int z, Road road) {
		return road.getCrossPoints().contains(road.toRoadPoint(x, z));
	}

	public void addModels0() {
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 2; j++)
				addTree(i * 10, 0, j * 10);

		for (int i = 10; i < 12; i++)
			for (int j = 10; j < 12; j++)
				addLights(i * 10, 0, j * 10);

		Box box1 = new Box(1, 1, 1);
		Geometry blue = new Geometry("Box", box1);
		blue.setLocalTranslation(new Vector3f(-15, 0, -15));
		blue.setLocalScale(10, 10, 10);

		CollisionShape cubShape = CollisionShapeFactory.createMeshShape(blue);
		RigidBodyControl landscape2 = new RigidBodyControl(cubShape, 0);
		blue.addControl(landscape2);
		bulletAppState.getPhysicsSpace().add(landscape2);

//        Material mat1 = new Material(assetManager,
//                "Common/MatDefs/Misc/Unshaded.j3md");
//        mat1.setColor("Color", ColorRGBA.Blue);
//        mat1.setTexture("DiffuseMap", assetManager.loadTexture("Materials/grass.png"));
//        blue.setMaterial(mat1);

		Material mat1 = assetManager.loadMaterial("Materials/green.j3m");
//        mat1.setColor("Color", ColorRGBA.Blue);
		mat1.setTexture("DiffuseMap", assetManager.loadTexture("Materials/estrada.jpg"));
		blue.setMaterial(mat1);
		rootNode.attachChild(blue);
	}

	private void addBuildingOrTree(int x, int z) {
		if (rand.nextInt(10) > 6)
			addTree(x, 0, z);
		else
			addBuilding(x, 0, z);
	}

	private void addBuilding(int x, int y, int z) {
		Spatial building = assetManager.loadModel("Models/building.j3o");
		int height = 5 + rand.nextInt(20);
		Vector3f scale = new Vector3f(SCALE, height, SCALE);
		Vector3f translation = new Vector3f(x, y, z);
		addModel(building, scale, translation);
	}

	private void addTree(int x, int y, int z) {
		Spatial tree = assetManager.loadModel("Models/tree-1.j3o");
		Vector3f scale = new Vector3f(1.05f, 2.05f, 1.05f);
		x += -SCALE + 2 + rand.nextInt(2 * SCALE - 2);
		z += -SCALE + 2 + rand.nextInt(2 * SCALE - 2);
		Vector3f translation = new Vector3f(x, y, z);
		addModel(tree, scale, translation);
	}

	private void addModel(Spatial model, Vector3f scale, Vector3f translation) {
		model.setLocalScale(scale);
		translation.x += SCALE;
		translation.z -= SCALE;
		model.setLocalTranslation(translation);
		addCollisionShape(model);

		rootNode.attachChild(model);
	}

	private void addCollisionShape(Spatial object) {
		CollisionShape cubShape = CollisionShapeFactory.createMeshShape(object);
		RigidBodyControl landscape2 = new RigidBodyControl(cubShape, 0);
		object.addControl(landscape2);
		bulletAppState.getPhysicsSpace().add(landscape2);
	}

	private void addLights(int x, int y, int z) {
//		Spatial ninja = assetManager.loadModel("Models/tree.j3o");
		Spatial lights = assetManager.loadModel("Models/lights.j3o");
		Material greenOn = assetManager.loadMaterial("Materials/Generated/lights-green.j3m");
//		Material mat2 = assetManager.loadMaterial("Materials/Generated/tree1-Cone1.j3m");
		((com.jme3.scene.Node) lights).getChild("green").setMaterial(greenOn);
//		((com.jme3.scene.Node)ninja).getChild("Cone1").setMaterial(mat2);
//		Spatial ninja = assetManager.loadModel("Models/tree2.blend");
		lights.scale(1.0f, 1.0f, 1.0f);
//      ninja.rotate(0.0f, -3.0f, 0.0f);
		lights.setLocalTranslation(x, y, z);
		addCollisionShape(lights);
		rootNode.attachChild(lights);
	}

}
