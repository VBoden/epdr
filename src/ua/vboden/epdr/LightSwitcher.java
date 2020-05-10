package ua.vboden.epdr;

import static ua.vboden.epdr.enums.Color.GREEN;
import static ua.vboden.epdr.enums.Color.RED;
import static ua.vboden.epdr.enums.Color.YELLOW;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import ua.vboden.epdr.enums.Color;

public class LightSwitcher implements Runnable {

	private AppStart mainApp;
	private TrafficLishts traficLights;
	private Material greenOn;
	private Material yellowOn;
	private Material redOn;
	private Material greenOff;
	private Material yellowOff;
	private Material redOff;

	private Map<Color, Material> onMaterials;
	private Map<Color, Material> offMaterials;

	public LightSwitcher(AppStart mainApp, TrafficLishts traficLights, AssetManager assetManager) {
		this.mainApp = mainApp;
		this.traficLights = traficLights;
		greenOn = assetManager.loadMaterial("Materials/Generated/lights-green.j3m");
		yellowOn = assetManager.loadMaterial("Materials/Generated/lights-yellow.j3m");
		redOn = assetManager.loadMaterial("Materials/Generated/lights-red.j3m");
		greenOff = assetManager.loadMaterial("Materials/Generated/lights-green-off.j3m");
		yellowOff = assetManager.loadMaterial("Materials/Generated/lights-yellow-off.j3m");
		redOff = assetManager.loadMaterial("Materials/Generated/lights-red-off.j3m");
		onMaterials = new HashMap<Color, Material>() {
			{
				put(RED, redOn);
				put(GREEN, greenOn);
				put(YELLOW, yellowOn);
			}
		};
		offMaterials = new HashMap<Color, Material>() {
			{
				put(RED, redOff);
				put(GREEN, greenOff);
				put(YELLOW, yellowOff);
			}
		};
	}

	@Override
	public void run() {
		int oneSec = 1000;
		int greenTime = oneSec * ThreadLocalRandom.current().nextInt(20, 41);
		int redTime = oneSec * ThreadLocalRandom.current().nextInt(10, 21);
		int yellowTime = 5 * oneSec;
		while (true) {
			try {
				Thread.sleep(yellowTime);
				switchColor(YELLOW, GREEN);
				Thread.sleep(greenTime);
				switchColor(GREEN, YELLOW);
				Thread.sleep(yellowTime);
				switchColor(YELLOW, RED);
				Thread.sleep(redTime);
				switchColor(RED, YELLOW);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void switchColor(Color oldColor, Color newColor) {
		traficLights.setColor(newColor);
		mainApp.enqueue(new Callable<Spatial>() {

			@Override
			public Spatial call() throws Exception {
				Node lights = (Node) traficLights.getLights();
				lights.getChild(oldColor.getNodeName()).setMaterial(offMaterials.get(oldColor));
				lights.getChild(newColor.getNodeName()).setMaterial(onMaterials.get(newColor));
				return lights;
			}
		});
	}

}
