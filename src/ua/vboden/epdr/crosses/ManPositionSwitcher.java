package ua.vboden.epdr.crosses;

import static ua.vboden.epdr.enums.StickPosition.BEFORE;
import static ua.vboden.epdr.enums.StickPosition.DOWN;
import static ua.vboden.epdr.enums.StickPosition.FORWARD;
import static ua.vboden.epdr.enums.StickPosition.SIDE;
import static ua.vboden.epdr.enums.StickPosition.UP;
import static ua.vboden.epdr.Utils.toRadians;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import ua.vboden.epdr.AppStart;
import ua.vboden.epdr.enums.Direction;
import ua.vboden.epdr.enums.StickPosition;

public class ManPositionSwitcher implements Runnable {

	private AppStart mainApp;
	private TrafficMan man;
	private Direction[] directions = Direction.values();
	private StickPosition[] samePosition = new StickPosition[] { DOWN, SIDE, BEFORE };
	private Map<StickPosition, float[]> stickRotation;
	private Map<StickPosition, Vector3f> stickTranslation;
	private int prevDirectionIndex = -1;

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

	@Override
	public void run() {
		int oneSec = 1000;
		int greenTime = oneSec * ThreadLocalRandom.current().nextInt(15, 35);
		int redTime = oneSec * ThreadLocalRandom.current().nextInt(10, 21);
		int yellowTime = 5 * oneSec;
		while (true) {
			try {
				Direction direction = directions[getDirectionIndex()];
				rotateMan(direction);
				man.setDirection(direction);
				Thread.sleep(greenTime);
				switchPosition(UP); // 8.8 в)
				Thread.sleep(yellowTime);
				switchPosition(FORWARD); // 8.8 б)
				Thread.sleep(redTime);
				switchPosition(UP); // 8.8 в)
				Thread.sleep(yellowTime);
				int position = ThreadLocalRandom.current().nextInt(0, 3);
				switchPosition(samePosition[position]); // 8.8 а)
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private int getDirectionIndex() {
		int index;
		do {
			index = ThreadLocalRandom.current().nextInt(0, directions.length);
		} while (index == prevDirectionIndex);
		prevDirectionIndex = index;
		System.out.println(index);
		return index;
	}

	private void rotateMan(Direction direction) {
		mainApp.enqueue(new Callable<Spatial>() {

			@Override
			public Spatial call() throws Exception {
				man.getManModel()
						.setLocalRotation(new Quaternion().fromAngles(0, toRadians(direction.getDegress()), 0));
				return man.getManModel();
			}
		});
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
