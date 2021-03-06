package ua.vboden.epdr;

import static ua.vboden.epdr.Constants.DOUBLE_SCALE;
import static ua.vboden.epdr.Constants.SCALE;

import java.util.Random;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

import ua.vboden.epdr.crosses.AbstractRoadCross;
import ua.vboden.epdr.crosses.Car;
import ua.vboden.epdr.crosses.CarMover;

public class ModelsManager {

	private Node rootNode;
	private AssetManager assetManager;
	private BulletAppState bulletAppState;
	private AppContext context;
	private Random rand = new Random();

	public ModelsManager(AppContext context) {
		this.rootNode = context.getRootNode();
		this.assetManager = context.getAssetManager();
		this.bulletAppState = context.getBulletAppState();
		this.context = context;
	}

	public void addModels() {
		createRoads();
		addControlsToRoadCrosses();
		Thread thread = new Thread(new CarMover(context, addCar(15, 1, 40)));
		thread.setDaemon(true);
		thread.start();
	}

	private void createRoads() {
		RoadsCreator creator = new RoadsCreator(context);
		creator.createRoads();

		Material roadWithoutMarking = assetManager.loadMaterial("Materials/green.j3m");
		roadWithoutMarking.setTexture("DiffuseMap", assetManager.loadTexture("Materials/RoadEmpty.png"));
		Material roadWithSide = assetManager.loadMaterial("Materials/green.j3m");
		roadWithSide.setTexture("DiffuseMap", assetManager.loadTexture("Materials/RoadSide.png"));
		Material roadWithCrossSide = assetManager.loadMaterial("Materials/green.j3m");
		roadWithCrossSide.setTexture("DiffuseMap", assetManager.loadTexture("Materials/RoadCrossSide2.png"));
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
					} else if (isRoadCrossPoint(i + road.getXMult(), j + road.getZMult(), road)
							|| isRoadCrossPoint(i - road.getXMult(), j - road.getZMult(), road)) {
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

	private void addBuildingOrTree(int x, int z) {
		if (rand.nextInt(10) > 6)
			addTree(x, 0, z);
		else
			addBuilding(x, 0, z);
	}

	private void addBuilding(int x, int y, int z) {
		Material material = assetManager.loadMaterial("Materials/green.j3m");
		Texture texture = assetManager.loadTexture("Materials/building.png");
		texture.setWrap(WrapMode.Repeat);
		material.setTexture("DiffuseMap", texture);

		int height = 2 + rand.nextInt(10);
		Box box = new Box(1, 1, 1);
		Geometry building = new Geometry("Building", box);
		building.setMaterial(material);
		building.getMesh().scaleTextureCoordinates(new Vector2f(1, height));

		Vector3f scale = new Vector3f(SCALE, height * SCALE, SCALE);
		Vector3f translation = new Vector3f(x, y + height * SCALE, z);
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
		Utils.addCollisionShape(model, bulletAppState);

		rootNode.attachChild(model);
	}

	private Car addCar(int x, int y, int z) {
		Spatial car = assetManager.loadModel("Models/car.j3o");
		car.setLocalRotation(new Quaternion().fromAngles(0, Utils.toRadians(-90), 0));
		Vector3f scale = new Vector3f(1, 1, 1);
		Vector3f translation = new Vector3f(x, y, z);
		car.setLocalScale(scale);
		translation.x += SCALE;
		translation.z -= SCALE;
		car.setLocalTranslation(translation);
		CollisionShape carShape = CollisionShapeFactory.createDynamicMeshShape(car);
		VehicleControl control = new VehicleControl(carShape, 0);
		car.addControl(control);
		bulletAppState.getPhysicsSpace().add(control);

		rootNode.attachChild(car);
		return new Car(car, (CompoundCollisionShape) carShape);
	}

	private void addControlsToRoadCrosses() {
		for (AbstractRoadCross roadCross : context.getRoadCrosses())
			roadCross.addControls();
	}
}
