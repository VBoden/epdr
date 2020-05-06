package ua.vboden.epdr;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeVersion;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * 
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener {

	private Spatial sceneModel;
	private RigidBodyControl landscape;
	private CharacterControl player;
	private BulletAppState bulletAppState;

	private Vector3f walkDirection = new Vector3f();
	private boolean left = false, right = false, up = false, down = false, turnLeft = false, turnRight = false;

	private Vector3f camDir = new Vector3f();
	private Vector3f camLeft = new Vector3f();

	private float angle = 0;
	private ModelsManager modelsManager;

	public static void main(String[] args) {
		Main app = new Main();
		app.setShowSettings(false);
		AppSettings settings2 = new AppSettings(true);
		settings2.put("Width", 1024);
		settings2.put("Height", 768);
		settings2.put("Title", "eeee");
		app.setSettings(settings2);
		app.start();
	}

	@Override
	public void simpleInitApp() {
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		modelsManager = new ModelsManager(rootNode, assetManager, bulletAppState);

		flyCam.setMoveSpeed(100);
		flyCam.setEnabled(false);
//        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
		setUpKeys();
		setUpLight();
//        System.out.println(flyCam.getMoveSpeed());
//        
//        sceneModel = assetManager.loadModel("Scenes/newScene2.j3o");
		sceneModel = assetManager.loadModel("Scenes/main.j3o");
		sceneModel.setLocalScale(20f);

		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(sceneModel);
		landscape = new RigidBodyControl(sceneShape, 0);
		sceneModel.addControl(landscape);
		sceneModel.setLocalScale(100);

		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
		player = new CharacterControl(capsuleShape, 0.05f);
		player.setJumpSpeed(20);
		player.setFallSpeed(30);

		rootNode.attachChild(sceneModel);
//        System.out.println(sceneModel.getLocalTranslation());
//        rootNode.setLocalTranslation(-1000, -200000, 0);

		bulletAppState.getPhysicsSpace().add(landscape);
		bulletAppState.getPhysicsSpace().add(player);

		player.setGravity(new Vector3f(0, -30f, 0));
		player.setPhysicsLocation(new Vector3f(10, 10, 10));
//        player.set

		cam.setLocation(player.getPhysicsLocation());
		
		modelsManager.addModels();

		
	}





	private void setUpLight() {
		// We add light so we see the scene
//		AmbientLight al = new AmbientLight();
//		al.setColor(ColorRGBA.White.mult(1.1f));
//		rootNode.addLight(al);
//		
      DirectionalLight dl = new DirectionalLight();
      dl.setColor(ColorRGBA.White);
      dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
      rootNode.addLight(dl);
	}

	private void setUpKeys() {
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(this, "Left");
		inputManager.addListener(this, "Right");
		inputManager.addListener(this, "Up");
		inputManager.addListener(this, "Down");
		inputManager.addListener(this, "Jump");
		inputManager.addMapping("TurnLeft", new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping("TurnRight", new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addListener(this, "TurnLeft");
		inputManager.addListener(this, "TurnRight");
	}

	public void onAction(String binding, boolean isPressed, float tpf) {
		if (binding.equals("Left")) {
			left = isPressed;
		} else if (binding.equals("Right")) {
			right = isPressed;
		} else if (binding.equals("Up")) {
			up = isPressed;
		} else if (binding.equals("Down")) {
			down = isPressed;
		} else if (binding.equals("Jump")) {
			if (isPressed) {
				player.jump(new Vector3f(0, -20f, 0));
			}
		} else if (binding.equals("TurnLeft")) {
			turnLeft = isPressed;
		} else if (binding.equals("TurnRight")) {
			turnRight = isPressed;
		}
	}

	private void rotateCamera(float degress) {
		Quaternion rotation = cam.getRotation();
		float[] angles = rotation.toAngles(null);
		float angleRadians = (float) (degress * (Math.PI / 180.0));
		angles[1] += angleRadians;
		rotation.fromAngles(angles);
	}

	@Override
	public void simpleUpdate(float tpf) {
		camDir.set(cam.getDirection()).multLocal(0.6f);
		camLeft.set(cam.getLeft()).multLocal(0.4f);
		walkDirection.set(0, -10, 0);
		if (left) {
			walkDirection.addLocal(camLeft);
		}
		if (right) {
			walkDirection.addLocal(camLeft.negate());
		}
		if (up) {
			walkDirection.addLocal(camDir);
		}
		if (down) {
			walkDirection.addLocal(camDir.negate());
		}
		if (turnLeft) {
			rotateCamera(10);
		}
		if (turnRight) {
			rotateCamera(-10);
		}
		player.setWalkDirection(walkDirection);
		cam.setLocation(player.getPhysicsLocation());
//        System.out.println(walkDirection);
	}

//    @Override
//    public void simpleRender(RenderManager rm) {
//        //TODO: add render code
//    }
}
