package ua.vboden.epdr;

import com.jme3.scene.Spatial;

public class TrafficMan {

	private Spatial manModel;
	private Direction direction;
	private StickPosition stickPosition;

	public TrafficMan(Spatial manModel) {
		this.manModel = manModel;
	}

	public Spatial getManModel() {
		return manModel;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public StickPosition getStickPosition() {
		return stickPosition;
	}

	public void setStickPosition(StickPosition stickPosition) {
		this.stickPosition = stickPosition;
	}

}
