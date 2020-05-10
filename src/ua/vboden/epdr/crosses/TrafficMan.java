package ua.vboden.epdr.crosses;

import com.jme3.scene.Spatial;

import ua.vboden.epdr.enums.Direction;
import ua.vboden.epdr.enums.StickPosition;

public class TrafficMan {

	private Spatial manModel;
	private Direction direction;
	private StickPosition stickPosition;

	public TrafficMan(Spatial manModel, Direction direction, StickPosition stickPosition) {
		this.manModel = manModel;
		this.direction = direction;
		this.stickPosition = stickPosition;
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
