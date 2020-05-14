package ua.vboden.epdr.crosses;

import static ua.vboden.epdr.Constants.DOUBLE_SCALE;
import static ua.vboden.epdr.Constants.SCALE;
import static ua.vboden.epdr.enums.Direction.E;
import static ua.vboden.epdr.enums.Direction.N;
import static ua.vboden.epdr.enums.Direction.S;
import static ua.vboden.epdr.enums.Direction.W;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import ua.vboden.epdr.AppContext;
import ua.vboden.epdr.Road;
import ua.vboden.epdr.Utils;
import ua.vboden.epdr.enums.Direction;

public abstract class AbstractRoadCross {

	private Vector3f coordinates;
	private Set<Road> members;
	private AppContext context;
	private Node rootNode;
	private AssetManager assetManager;
	private BulletAppState bulletAppState;
	private Map<Direction, Vector3f> pointsOnMap;

	public AbstractRoadCross(Vector3f coordinates, AppContext context) {
		this.coordinates = coordinates;
		this.context = context;
		this.rootNode = context.getRootNode();
		this.assetManager = context.getAssetManager();
		this.bulletAppState = context.getBulletAppState();
		this.members = new HashSet<>();
		this.pointsOnMap = new HashMap<>();
	}

	public abstract void addControls();

	public abstract Boolean passedCross(Direction direction, Road rememberedRoad, int x, int z);

	public abstract void resetCheckState();

	public void setupReturnPoints() {
		int roadMove = 2 * DOUBLE_SCALE;
		int sideMove = -SCALE;
		calculatePointOnMap(1, 1, sideMove, roadMove);
		pointsOnMap.put(S, calculatePointOnMap(1, 1, sideMove, roadMove));
		pointsOnMap.put(E, calculatePointOnMap(1, -1, roadMove, sideMove));
		pointsOnMap.put(N, calculatePointOnMap(-1, -1, sideMove, roadMove));
		pointsOnMap.put(W, calculatePointOnMap(-1, 1, roadMove, sideMove));
	}

	private Vector3f calculatePointOnMap(int xSign, int zSign, int dx, int dz) {
		int x = ((int) getCoordinates().x + xSign) * DOUBLE_SCALE + SCALE + xSign * dx;
		int z = ((int) getCoordinates().z + zSign) * DOUBLE_SCALE - SCALE + zSign * dz;
		return new Vector3f(x, 0, z);
	}

	protected boolean hasPassedStartControllPoint(float x, float z, Direction direction, float toLightDist) {
		int xSign = (int) Utils.getXMoveMultDeg(direction.getDegress());
		int zSign = (int) Utils.getZMoveMultDeg(direction.getDegress());
		Vector3f position = getPointOnMap(direction);
		int roadX = (int) (position.x / DOUBLE_SCALE);
		int roadZ = (int) (position.z / DOUBLE_SCALE);
		x += xSign * toLightDist;
		z += zSign * toLightDist;
		return xSign * (x - roadX) + zSign * (z - roadZ) > 0;
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

	public Vector3f getCoordinates() {
		return coordinates;
	}

	public int getX() {
		return (int) coordinates.x;
	}

	public int getY() {
		return (int) coordinates.y;
	}

	public int getZ() {
		return (int) coordinates.z;
	}

	public void setCoordinates(Vector3f coordinates) {
		this.coordinates = coordinates;
	}

	public void addMember(Road road) {
		members.add(road);
	}

	public Set<Road> getMembers() {
		return members;
	}

	public void setMembers(Set<Road> members) {
		this.members = members;
	}

	public AppContext getContext() {
		return context;
	}

	public void setContext(AppContext context) {
		this.context = context;
	}

	public Vector3f getPointOnMap(Direction direction) {
		return pointsOnMap.get(direction);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		AbstractRoadCross other = (AbstractRoadCross) obj;
		if (coordinates == null) {
			if (other.coordinates != null)
				return false;
		} else if (!coordinates.equals(other.coordinates))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String membersString = members.stream().map(m -> m.getId()).collect(Collectors.toList()).toString();
		return "AbstractRoadCross [members=" + membersString + " coordinates=" + coordinates + "]";
	}
}
