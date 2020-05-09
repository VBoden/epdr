package ua.vboden.epdr;

import com.jme3.scene.Spatial;

public class TrafficLishts {

	private Spatial lights;
	private Color color;
	private int roadPoint;

	public TrafficLishts(Spatial lights) {
		this.lights = lights;
	}

	public Spatial getLights() {
		return lights;
	}

	public void setLights(Spatial lights) {
		this.lights = lights;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getRoadPoint() {
		return roadPoint;
	}

	public void setRoadPoint(int roadPoint) {
		this.roadPoint = roadPoint;
	}

}
