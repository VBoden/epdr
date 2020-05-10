package ua.vboden.epdr;

import static ua.vboden.epdr.StickPosition.BEFORE;
import static ua.vboden.epdr.StickPosition.DOWN;
import static ua.vboden.epdr.StickPosition.FORWARD;
import static ua.vboden.epdr.StickPosition.SIDE;
import static ua.vboden.epdr.StickPosition.UP;
import static ua.vboden.epdr.Utils.toRadians;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class ManPositionSwitcher implements Runnable {

	private AppStart mainApp;
	private TrafficMan man;
	private int directionCounter;
	private Direction[] directions = Direction.values();
	private StickPosition[] samePosition = new StickPosition[] { DOWN, SIDE, BEFORE };

	private Map<StickPosition, float[]> stickRotation;
	private Map<StickPosition, Vector3f> stickTranslation;

	public ManPositionSwitcher(AppStart mainApp, TrafficMan man) {
		this.man = man;
		this.mainApp = mainApp;

		stickRotation = new HashMap<StickPosition, float[]>() {
			{
				put(UP, new float[] { 0, 0, 0 });
				put(DOWN, new float[] { 0, 0, 0 });
				put(BEFORE, new float[] { 0, 0, -toRadians(90) });
				put(SIDE, new float[] { 0, 0, toRadians(90) });
				put(FORWARD, new float[] { toRadians(90), 0, 0 });
			}
		};
		stickTranslation = new HashMap<StickPosition, Vector3f>() {
			{
				put(UP, new Vector3f(-1.0f, 4.5f, 0.0f));
				put(DOWN, new Vector3f(-1.0f, 0.5f, 0.0f));
				put(BEFORE, new Vector3f(-1.0f, 2.5f, 0.5f));
				put(SIDE, new Vector3f(-1.0f, 2.5f, 0.0f));
				put(FORWARD, new Vector3f(-1.0f, 2.5f, 0.0f));
			}
		};
	}

	// Down:
	// Rot: (0,0,0) Trans: (-1.0, 0.5, 0.0)
	// Up:
	// Rot: (0,0,0) Trans: (-1.0, 4.5, 0.0)
	// Forward:
	// Rot: (90,0,0) Trans: (-1.0, 2.5, 0.0)
	// Side:
	// Rot: (0,0,90) Trans: (-1.0, 2.5, 0.0)
	// Before:
	// Rot: (0.0, 0.0, -90.0) Trans: (-1.0, 2.5, 0.5)
	@Override
	public void run() {
		int oneSec = 1000;
//		int greenTime = oneSec * ThreadLocalRandom.current().nextInt(20, 41);
//		int redTime = oneSec * ThreadLocalRandom.current().nextInt(10, 21);
//		int yellowTime = 5 * oneSec;
		int greenTime = oneSec * ThreadLocalRandom.current().nextInt(2, 4);
		int redTime = oneSec * ThreadLocalRandom.current().nextInt(1, 2);
		int yellowTime = 2 * oneSec;
		while (true) {
			try {
				Thread.sleep(yellowTime);
				switchPosition(UP);
				Thread.sleep(greenTime);
				switchPosition(FORWARD);
				Thread.sleep(yellowTime);
				switchPosition(UP);
				Thread.sleep(redTime);
				int position = ThreadLocalRandom.current().nextInt(0, 3);
				switchPosition(samePosition[position]);
				Thread.sleep(redTime);
				switchPosition(UP);
				Direction direction = directions[directionCounter % directions.length];
				directionCounter++;
				System.out.println(direction);
				mainApp.enqueue(new Callable<Spatial>() {

					@Override
					public Spatial call() throws Exception {
						man.getManModel()
								.setLocalRotation(new Quaternion().fromAngles(0, toRadians(direction.getDegress()), 0));
						return man.getManModel();
					}
				});
				man.setDirection(direction);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void switchPosition(StickPosition newPosition) {
		mainApp.enqueue(new Callable<Spatial>() {

			@Override
			public Spatial call() throws Exception {
				Spatial stick = ((Node) man.getManModel()).getChild("stick");
				stick.setLocalRotation(stick.getLocalRotation().fromAngles(stickRotation.get(newPosition)));
				stick.setLocalTranslation(stickTranslation.get(newPosition));
				return stick;
			}
		});
		man.setStickPosition(newPosition);
	}

}
