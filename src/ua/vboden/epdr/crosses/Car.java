package ua.vboden.epdr.crosses;

import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.scene.Spatial;

public class Car {
	private Spatial carModel;
	private CompoundCollisionShape cubShape;

	public Car(Spatial carModel, CompoundCollisionShape cubShape) {
		this.carModel = carModel;
		this.cubShape = cubShape;
	}

	protected Spatial getCarModel() {
		return carModel;
	}

	protected void setCarModel(Spatial carModel) {
		this.carModel = carModel;
	}

	protected CompoundCollisionShape getCubShape() {
		return cubShape;
	}

	protected void setCubShape(CompoundCollisionShape cubShape) {
		this.cubShape = cubShape;
	}

}
