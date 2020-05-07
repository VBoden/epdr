package ua.vboden.epdr;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class ModelsManager {

	private Node rootNode;
	private AssetManager assetManager;
	private BulletAppState bulletAppState;

	public ModelsManager(Node rootNode, AssetManager assetManager, BulletAppState bulletAppState) {
		this.rootNode = rootNode;
		this.assetManager = assetManager;
		this.bulletAppState = bulletAppState;
	}

	public void addModels() {
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
				addBox(i * 10, 0, j * 10);
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
		((com.jme3.scene.Node)lights).getChild("green").setMaterial(greenOn);
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
		((com.jme3.scene.Node)tree).getChild("leafs").setMaterial(mat1);
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

	private void addBox(int x, int y, int z) {
		Box box1 = new Box(1, 1, 1);
		Geometry blue = new Geometry("Box", box1);
		blue.setLocalTranslation(new Vector3f(x, y, z));
		blue.setLocalScale(1, 0.001f, 1);
		blue.setLocalRotation(Quaternion.DIRECTION_Z);
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

}
