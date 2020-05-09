package ua.vboden.epdr;

import java.util.List;
import java.util.Set;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node;

public class AppContext {

	private Node rootNode;
	private AssetManager assetManager;
	private BulletAppState bulletAppState;
	private List<Road> roads;
	private Set<AbstractRoadCross> roadCrosses;
	private float angle = 0;

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

	public int getAngleDegres() {
		return (int) (angle * 180 / Math.PI);
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

}
