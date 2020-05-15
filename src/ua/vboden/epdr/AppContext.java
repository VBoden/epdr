package ua.vboden.epdr;

import java.util.List;
import java.util.Set;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

import ua.vboden.epdr.crosses.AbstractRoadCross;

public class AppContext {

	private AppStart mainApp;
	private Node rootNode;
	private AssetManager assetManager;
	private BulletAppState bulletAppState;
	private CharacterControl player;
	protected Camera cam;
	private List<Road> roads;
	private Set<AbstractRoadCross> roadCrosses;
	private float angle = 0;
	private float speed = 0f;
	private AbstractRoadCross passedCross;
	private String popupId;

	public AppStart getMainApp() {
		return mainApp;
	}

	public void setMainApp(AppStart mainApp) {
		this.mainApp = mainApp;
	}

	public Node getRootNode() {
		return rootNode;
	}

	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public void setAssetManager(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public BulletAppState getBulletAppState() {
		return bulletAppState;
	}

	public void setBulletAppState(BulletAppState bulletAppState) {
		this.bulletAppState = bulletAppState;
	}

	public List<Road> getRoads() {
		return roads;
	}

	public void setRoads(List<Road> roads) {
		this.roads = roads;
	}

	public float getAngle() {
		return angle;
	}

	public float getAngleDegress() {
		return Utils.correctAngleRange(Utils.toDegress(angle));
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public Set<AbstractRoadCross> getRoadCrosses() {
		return roadCrosses;
	}

	public void setRoadCrosses(Set<AbstractRoadCross> roadCrosses) {
		this.roadCrosses = roadCrosses;
	}

	public AbstractRoadCross getPassedCross() {
		return passedCross;
	}

	public void setPassedCross(AbstractRoadCross passedCross) {
		this.passedCross = passedCross;
	}

	public CharacterControl getPlayer() {
		return player;
	}

	public void setPlayer(CharacterControl player) {
		this.player = player;
	}

	public Camera getCam() {
		return cam;
	}

	public void setCam(Camera cam) {
		this.cam = cam;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setPopupId(String id) {
		popupId = id;
	}

	public String getPopupId() {
		return popupId;
	}

}
