package ua.vboden.epdr.crosses;

import java.util.concurrent.Callable;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import ua.vboden.epdr.AppContext;
import ua.vboden.epdr.AppStart;

public class CarMover implements Runnable {

	private static final float CAR_SPEED = 0.1f;

	private AppStart mainApp;
	private AssetManager assetManager;
	private Car car;
	private Material turnOn;
	private Material turnOff;
	private boolean rightOn;
	private int counter;

	public CarMover(AppContext context, Car car) {
		this.car = car;
		this.mainApp = context.getMainApp();
		this.assetManager = context.getAssetManager();
		turnOn = assetManager.loadMaterial("Materials/Generated/car-car_signal_on.j3m");
		turnOff = assetManager.loadMaterial("Materials/Generated/car-car_signal_off.j3m");
	}

	@Override
	public void run() {
		int oneSec = 10;
		while (true) {
			try {
				counter++;
				Thread.sleep(oneSec);
				switchPosition();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void switchPosition() {
		mainApp.enqueue(new Callable<Spatial>() {

			@Override
			public Spatial call() throws Exception {
				Spatial carModel = car.getCarModel();
				Vector3f localTranslation = carModel.getLocalTranslation();
				localTranslation.z += CAR_SPEED;
				carModel.setLocalTranslation(localTranslation);
				Vector3f shift = new Vector3f(CAR_SPEED, 0, 0); // for collisions changed coordinates x and z
				CollisionShapeFactory.shiftCompoundShapeContents(car.getCubShape(), new Vector3f(shift));
				Material current = rightOn ? turnOn : turnOff;
				((Node) carModel).getChild("car-signal-fr").setMaterial(current);
				((Node) carModel).getChild("car-signal-br").setMaterial(current);
				if (counter > 50) {
					rightOn = !rightOn;
					counter = 0;
				}
				return carModel;
			}
		});
	}
}
