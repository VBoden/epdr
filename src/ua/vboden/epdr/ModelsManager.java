package ua.vboden.epdr;

import static ua.vboden.epdr.Constants.DOUBLE_SCALE;

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

	private void addRoadSquare_0(int x, int z, Material material, int rotate) {
		Node node = new Node();
		node.setLocalRotation(new Quaternion().fromAngles(-FastMath.HALF_PI, 0, 0));
		node.getLocalTranslation().addLocal(0, 0.01f, 0);
		Quad quad = new Quad(1, 1);
		Geometry blue = new Geometry("Quad", quad);
		blue.setLocalScale(10, 10, 1);
		blue.getLocalRotation().addLocal(new Quaternion().fromAngles(0, 0, Utils.toRadians(2 * rotate)));
		correctAfterRotation_0(blue, rotate);
		blue.getLocalTranslation().addLocal(x, z, 0);
		blue.setMaterial(material);
		node.attachChild(blue);
		rootNode.attachChild(node);
	}

	private void correctAfterRotation_0(Geometry blue, int rotate) {
		int quaterMiddle = -rotate + 45;
		float x = (float) Math.min(0, Math.signum(Math.sin(Utils.toRadians(quaterMiddle))));
		float z = (float) Math.min(0, Math.signum(Math.cos(Utils.toRadians(quaterMiddle))));
		Vector3f scale = blue.getLocalScale();
		blue.getLocalTranslation().addLocal(-x * scale.x, -z * scale.y, 0);
	}

	public void createRoads0() {
		RoadsCreator creator = new RoadsCreator(context);
		creator.createRoads();

		Material roadWithoutMarking = assetManager.loadMaterial("Materials/green.j3m");
		roadWithoutMarking.setTexture("DiffuseMap", assetManager.loadTexture("Materials/RoadEmpty.png"));
		Material roadWithSide = assetManager.loadMaterial("Materials/green.j3m");
		roadWithSide.setTexture("DiffuseMap", assetManager.loadTexture("Materials/RoadSide.png"));
		Material roadWithCrossSideX = assetManager.loadMaterial("Materials/green.j3m");
		roadWithCrossSideX.setTexture("DiffuseMap", assetManager.loadTexture("Materials/RoadCrossSideX.png"));
		Material roadWithCrossSideZ = assetManager.loadMaterial("Materials/green.j3m");
		roadWithCrossSideZ.setTexture("DiffuseMap", assetManager.loadTexture("Materials/RoadCrossSideZ.png"));
		Material roadWithDashedMarking = assetManager.loadMaterial("Materials/green.j3m");
		roadWithDashedMarking.setTexture("DiffuseMap", assetManager.loadTexture("Materials/RoadDashed.png"));
		for (Road road : context.getRoads()) {
			if (road.isDirectedByZ())
				continue;
			int rotateDegrees = road.getXMult() * 90;
			int sideRotateDegrees = road.getXMult() * 180;
			int sideRotateDegrees2 = road.getZMult() * 180;
			for (int i = (int) road.getStart().x; i < road.getEnd().x + 1; i++) {
				for (int j = (int) road.getStart().z; j < road.getEnd().z + 1; j++) {
					Material roadMaterial;
					Material roadSideMaterial = null;
					if (isRoadCrossPoint(i, j, road)) {
						roadMaterial = roadWithoutMarking;
//						continue;
//						roadSideMaterial = roadWithCrossSide;
					} else if (isRoadCrossPoint(i + road.getXMult(), j + road.getZMult(), road)
							|| isRoadCrossPoint(i - road.getXMult(), j - road.getZMult(), road)) {
						roadMaterial = roadWithoutMarking;
//						continue;
//						if(road.isDirectedByZ()) {
//						roadSideMaterial = roadWithCrossSideZ;
//						sideRotateDegrees2 = -180;
//						}
//						else
						roadSideMaterial = roadWithCrossSideX;
						sideRotateDegrees2 = 90;
					} else {
						roadMaterial = roadWithDashedMarking;
						roadSideMaterial = roadWithSide;
					}
					addRoadSquare((i - road.getZMult()) * DOUBLE_SCALE, j * DOUBLE_SCALE, roadMaterial, rotateDegrees);
					if (roadSideMaterial == null)
						continue;
					addRoadSquare((i + road.getZMult()) * DOUBLE_SCALE,
							(j - road.getZMult() + road.getXMult()) * DOUBLE_SCALE, roadSideMaterial,
							rotateDegrees - sideRotateDegrees2);
					addRoadSquare((i - road.getXMult() - 2 * road.getZMult()) * DOUBLE_SCALE,
							(j - road.getXMult() - road.getXMult()) * DOUBLE_SCALE, roadSideMaterial,
							rotateDegrees + sideRotateDegrees);

//					if (isRoadCrossPoint(i, j, road) || isRoadCrossPoint(i + road.getXMult(), j + road.getZMult(), road)
//							|| isRoadCrossPoint(i - road.getXMult(), j - road.getZMult(), road)) {
//						addRoadSquare((i - road.getZMult()) * DOUBLE_SCALE, j * DOUBLE_SCALE, roadWithoutMarking,
//								rotateDegrees);
//						addRoadSquare((i + road.getZMult()) * DOUBLE_SCALE, (j + road.getXMult()) * DOUBLE_SCALE,
//								roadWithCrossSide, rotateDegrees - road.getZMult() * 180);
//						addRoadSquare((i - road.getZMult() - road.getZMult()) * DOUBLE_SCALE,
//								(j - road.getXMult() - road.getXMult()) * DOUBLE_SCALE, roadWithCrossSide,
//								rotateDegrees + road.getXMult() * 180);
//					} else {
//						addRoadSquare((i - road.getZMult()) * DOUBLE_SCALE, j * DOUBLE_SCALE, roadWithDashedMarking,
//								rotateDegrees);
//						addRoadSquare((i + road.getZMult()) * DOUBLE_SCALE, (j + road.getXMult()) * DOUBLE_SCALE,
//								roadWithSide, rotateDegrees - road.getZMult() * 180);
//						addRoadSquare((i - road.getZMult() - road.getZMult()) * DOUBLE_SCALE,
//								(j - road.getXMult() - road.getXMult()) * DOUBLE_SCALE, roadWithSide,
//								rotateDegrees + road.getXMult() * 180);
//					}
				}
			}
		}
//            y_rot_ind = road.get_y_bool()
//            x_rot_ind = road.get_x_bool()

//                    if self.is_road_cross_point(i, j, road) \
//                            or self.is_road_cross_point(i + x_rot_ind, j + y_rot_ind, road) \
//                            or self.is_road_cross_point(i - x_rot_ind, j - y_rot_ind, road):
//                        self.add(roads_cross_model, i * mult, j * mult)
//                    else:
//                        self.add(road_central_model, i * mult, j * mult, rotate)
//                        self.add_building_or_tree((i + (y_rot_ind * 2)) * mult, (j + (x_rot_ind * 2)) * mult,
//                                                  random.randint(0, 9) > 6)
//                        self.add_building_or_tree((i - (y_rot_ind * 2)) * mult, (j - (x_rot_ind * 2)) * mult,
//                                                  random.randint(0, 9) > 6)
//                    self.add(road_side_model, (i + y_rot_ind) * mult, (j + x_rot_ind) * mult, rotate - y_rot_ind * 180)
//                    self.add(road_side_model, (i - y_rot_ind) * mult, (j - x_rot_ind) * mult, rotate - x_rot_ind * 180)
	}

	private boolean isRoadCrossPoint(int x, int z, Road road) {
		return road.getCrossPoints().contains(road.toRoadPoint(x, z));
	}

	private void addRoadSquare0(int x, int z, Material material, int rotate) {
		Quad quad = new Quad(1, 1);
		Geometry blue = new Geometry("Quad", quad);
		blue.setLocalScale(10, 10, 1);
		blue.setLocalRotation(new Quaternion().fromAngles(-FastMath.HALF_PI, Utils.toRadians(rotate), 0));
		correctAfterRotation(blue, rotate);
		blue.getLocalTranslation().addLocal(x, 0.1f, z);
		blue.setMaterial(material);
		rootNode.attachChild(blue);
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
		for (int i = 0; i < 100; i++)
			for (int j = 0; j < 100; j++)
				addRoadSquare0(i * 10, j * 10);
	}

	private void addBuilding(int x, int y, int z) {
		Spatial lights = assetManager.loadModel("Models/building.j3o");
		lights.scale(1.0f, 15.0f, 1.0f);
//      ninja.rotate(0.0f, -3.0f, 0.0f);
		lights.setLocalTranslation(x, y, z);
		addCollisionShape(lights);
		rootNode.attachChild(lights);
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

	private void addTree(int x, int y, int z) {
		Spatial tree = assetManager.loadModel("Models/tree-1.j3o");
		Material mat1 = assetManager.loadMaterial("Materials/Generated/tree-leafs2.j3m");
//		Material mat2 = assetManager.loadMaterial("Materials/Generated/tree1-Cone1.j3m");
		((com.jme3.scene.Node) tree).getChild("leafs").setMaterial(mat1);
//		((com.jme3.scene.Node)ninja).getChild("Cone1").setMaterial(mat2);
//		Spatial ninja = assetManager.loadModel("Models/tree2.blend");
		tree.scale(1.05f, 2.05f, 1.05f);
//      ninja.rotate(0.0f, -3.0f, 0.0f);
		tree.setLocalTranslation(x, y, z);

		addCollisionShape(tree);

		rootNode.attachChild(tree);
	}

	private void addCollisionShape(Spatial ninja) {
		CollisionShape cubShape = CollisionShapeFactory.createMeshShape(ninja);
		RigidBodyControl landscape2 = new RigidBodyControl(cubShape, 0);
		ninja.addControl(landscape2);
		bulletAppState.getPhysicsSpace().add(landscape2);
	}

	private void addRoadSquare0(int x, int z) {
		Quad quad = new Quad(1, 1);
		Geometry blue = new Geometry("Quad", quad);
		blue.setLocalTranslation(new Vector3f(x, 0.1f, z));
		blue.setLocalScale(9, 10, 1);
//		blue.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
		blue.setLocalRotation(new Quaternion().fromAngles(-FastMath.HALF_PI, 0, 0));
		Material mat1 = assetManager.loadMaterial("Materials/green.j3m");
		mat1.setTexture("DiffuseMap", assetManager.loadTexture("Materials/estrada.jpg"));
		blue.setMaterial(mat1);
		rootNode.attachChild(blue);
	}

}
