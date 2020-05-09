package ua.vboden.epdr;

import static ua.vboden.epdr.Color.*;

import java.util.HashMap;
import java.util.Map;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Node;

public class LightSwitcher implements Runnable {

	private TrafficLishts traficLights;
	private Material greenOn;
	private Material yellowOn;
	private Material redOn;
	private Material greenOff;
	private Material yellowOff;
	private Material redOff;

	private Map<Color, Material> onMaterials;
	private Map<Color, Material> offMaterials;

	public LightSwitcher(TrafficLishts traficLights, AssetManager assetManager) {
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
		while (true) {
			try {
				Thread.sleep(3000);
				switchColor(YELLOW, GREEN);
				Thread.sleep(3000);
				switchColor(GREEN, YELLOW);
				Thread.sleep(3000);
				switchColor(YELLOW, RED);
				Thread.sleep(3000);
				switchColor(RED, YELLOW);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void switchColor(Color oldColor, Color newColor) {
		traficLights.setColor(newColor);
		Node lights = (Node) traficLights.getLights();
		lights.getChild(oldColor.getNodeName()).setMaterial(offMaterials.get(oldColor));
		lights.getChild(newColor.getNodeName()).setMaterial(onMaterials.get(newColor));
	}

}
