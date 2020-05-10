package ua.vboden.epdr;

import static ua.vboden.epdr.Constants.DOUBLE_SCALE;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class AppStart extends SimpleApplication implements ActionListener, ScreenController {

	private static final String KEY_D = "d";
	private static final String KEY_A = "a";
	private static final String KEY_S = "s";
	private static final String KEY_W = "w";
	private static final String KEY_R = "r";
	private static final String KEY_DOWN = "arrow_down";
	private static final String KEY_UP = "arrow_up";
	private static final String KEY_LEFT = "arrow_left";
	private static final String KEY_RIGHT = "arrow_right";
	private static final String KEY_BACKSPACE = "backspace";
	private static final float TURN_SPEED_MULTIPLIER = 10;

	private Map<String, Integer> keyNames = new HashMap<String, Integer>() {
		{
			put(KEY_D, KeyInput.KEY_D);
			put(KEY_A, KeyInput.KEY_A);
			put(KEY_S, KeyInput.KEY_S);
			put(KEY_W, KeyInput.KEY_W);
			put(KEY_R, KeyInput.KEY_R);
			put(KEY_DOWN, KeyInput.KEY_DOWN);
			put(KEY_UP, KeyInput.KEY_UP);
			put(KEY_LEFT, KeyInput.KEY_LEFT);
			put(KEY_RIGHT, KeyInput.KEY_RIGHT);
			put(KEY_BACKSPACE, KeyInput.KEY_BACK);
		}
	};

	private Map<String, Float> keyCoeficients = new HashMap<String, Float>() {
		{
			put(KEY_D, 0.1f);
			put(KEY_A, 0.1f);
			put(KEY_S, 0.2f);
			put(KEY_W, 0.2f);
			put(KEY_R, 1.0f);
			put(KEY_DOWN, 1.0f);
			put(KEY_UP, 1.0f);
			put(KEY_LEFT, 1.0f);
			put(KEY_RIGHT, 1.0f);
			put(KEY_BACKSPACE, 1.0f);
		}
	};

	private Map<String, Float> keyValues = new HashMap<>();
	private AppContext context = new AppContext();

	private Spatial sceneModel;
	private RigidBodyControl landscape;
	private CharacterControl player;
	private BulletAppState bulletAppState;
	private BitmapText hudText;

	private Vector3f walkDirection = new Vector3f();
	private Vector3f camDir = new Vector3f();

	private ModelsManager modelsManager;
	private MovingManager movingManager;
	private float speed = 0f;
	private boolean correctedDirection;
	private int moveManagTriggerCounter = 0;

	private Nifty nifty;

	public static void main(String[] args) {
		AppStart app = new AppStart();
		app.setShowSettings(false);
		AppSettings settings2 = new AppSettings(true);
		settings2.put("Width", 1024);
		settings2.put("Height", 768);
		settings2.put("Title", "eeee");
		app.setSettings(settings2);
		app.setDisplayStatView(false);
		app.setDisplayFps(false);
		app.start();
	}

	@Override
	public void simpleInitApp() {
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);

		flyCam.setMoveSpeed(100);
		flyCam.setEnabled(false);

		setUpKeys();
		setUpLight();
		setUpScene();
		setUpPlayer();
		rotateCamera(-180);

		createScreenText();

		context.setAssetManager(assetManager);
		context.setRootNode(rootNode);
		context.setBulletAppState(bulletAppState);
		context.setPlayer(player);
		context.setCam(cam);
		modelsManager = new ModelsManager(context);
		modelsManager.addModels();
		movingManager = new MovingManager(context);
	}

	private void setUpPlayer() {
		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
		player = new CharacterControl(capsuleShape, 0.05f);
		player.setJumpSpeed(20);
		player.setFallSpeed(30);

		bulletAppState.getPhysicsSpace().add(landscape);
		bulletAppState.getPhysicsSpace().add(player);

		player.setGravity(new Vector3f(0, -30f, 0));
		player.setPhysicsLocation(new Vector3f(DOUBLE_SCALE, 10, 0));
//		player.setPhysicsLocation(new Vector3f(15 * DOUBLE_SCALE, 10, 20 * DOUBLE_SCALE));
//		rotateCamera(90);

		cam.setLocation(player.getPhysicsLocation());
	}

	private void setUpScene() {
		sceneModel = assetManager.loadModel("Scenes/main.j3o");
		sceneModel.setLocalScale(20f);
		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(sceneModel);
		landscape = new RigidBodyControl(sceneShape, 0);
		sceneModel.addControl(landscape);
		sceneModel.setLocalScale(100);
		rootNode.attachChild(sceneModel);
	}

	private void createScreenText() {
		BitmapFont myFont = assetManager.loadFont("Interface/Fonts/Academy3.fnt");
		hudText = new BitmapText(myFont, false);
		hudText.setSize(myFont.getCharSet().getRenderedSize());
		hudText.setColor(ColorRGBA.Blue);
		hudText.setText("Швидкість: " + speed + " км/год");
		hudText.setLocalTranslation(300, hudText.getLineHeight() + 10, 0);
		guiNode.attachChild(hudText);
	}

	private void setUpLight() {
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(0.7f));
		rootNode.addLight(al);

		DirectionalLight dl = new DirectionalLight();
		dl.setColor(ColorRGBA.White);
		dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
		rootNode.addLight(dl);
	}

	private void setUpKeys() {
		for (Map.Entry<String, Integer> key : keyNames.entrySet()) {
			String keyName = key.getKey();
			inputManager.addMapping(keyName, new KeyTrigger(key.getValue()));
			inputManager.addListener(this, keyName);
			keyValues.put(keyName, 0f);
		}
	}

	public void onAction(String keyName, boolean isPressed, float tpf) {
		float value = isPressed ? keyCoeficients.get(keyName) : 0f;
		keyValues.put(keyName, value);
	}

	@Override
	public void simpleUpdate(float tpf) {
		if (keyValues.get(KEY_BACKSPACE) > 0) {
			restartMove();
			return;
		}
		speed = (float) (context.getSpeed() + 0.02 * (keyValues.get(KEY_UP) - keyValues.get(KEY_DOWN)));
		if (speed < 0)
			speed = 0;
		if (keyValues.get(KEY_S) > 0) {
			speed = new BigDecimal(speed * 0.9f).setScale(2, BigDecimal.ROUND_DOWN).floatValue();
		}
		context.setSpeed(speed);

		DecimalFormat df = new DecimalFormat("#.#");
		df.setRoundingMode(RoundingMode.CEILING);
		hudText.setText("Швидкість: " + df.format(100 * speed) + " км/год");

		float angleDegrees = keyValues.get(KEY_LEFT) - keyValues.get(KEY_RIGHT);
		if (angleDegrees != 0) {
			rotateCamera(TURN_SPEED_MULTIPLIER * speed * angleDegrees);
			correctedDirection = false;
		} else if (!correctedDirection) {
			correcteMovingAngle();
			correctedDirection = true;
		}

		camDir.set(cam.getDirection()).multLocal(speed);
		walkDirection.set(0, -20, 0);
		if (keyValues.get(KEY_R) > 0) {
			walkDirection.addLocal(camDir.negate());
		} else
			walkDirection.addLocal(camDir);

		player.setWalkDirection(walkDirection);
		cam.setLocation(player.getPhysicsLocation());
		if (moveManagTriggerCounter % 10 == 0)
			movingManager.manage(player.getPhysicsLocation());
		moveManagTriggerCounter++;
	}

	private void restartMove() {
		player.setPhysicsLocation(new Vector3f(DOUBLE_SCALE, 10, 0));
		cam.getRotation().fromAngles(0, 0, 0);
		speed = 0;
		context.setSpeed(speed);
		context.setAngle(0);
	}

	private void rotateCamera(float degress) {
		Quaternion rotation = cam.getRotation();
		float angle = Utils.rotateByY(rotation, degress);
		context.setAngle(angle);
	}

	private void correcteMovingAngle() {
		float currentDegress = context.getAngleDegress();
		currentDegress = Utils.correctAngleRange(currentDegress);
		float turnKeyValues = keyValues.get(KEY_LEFT) + keyValues.get(KEY_RIGHT);
		if (turnKeyValues == 0) {
			float diff = currentDegress % 90;
			float directionDelta = TURN_SPEED_MULTIPLIER / 2;
			if (diff > 0 && diff < directionDelta) {
				rotateCamera(-diff);
			} else if (diff > 90 - directionDelta && diff < 90) {
				rotateCamera(90 - diff);
			}
		}
	}

//    @Override
//    public void simpleRender(RenderManager rm) {
//        //TODO: add render code
//    }

	@Override
	public void bind(Nifty nifty, Screen screen) {
		System.out.println("bind( " + screen.getScreenId() + ")");
	}

	@Override
	public void onStartScreen() {
		System.out.println("onStartScreen");
	}

	@Override
	public void onEndScreen() {
		System.out.println("onEndScreen");
	}

	public void quit() {
		nifty.gotoScreen("end");
	}
}
