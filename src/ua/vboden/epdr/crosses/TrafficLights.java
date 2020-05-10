package ua.vboden.epdr.crosses;

import com.jme3.scene.Spatial;

import ua.vboden.epdr.enums.Color;

public class TrafficLights {

	private Spatial lights;
	private Color color;
//	private int roadPoint;

	public TrafficLights(Spatial lights, Color color) {
		this.lights = lights;
		this.color = color;
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

//	public int getRoadPoint() {
//		return roadPoint;
//	}
//
//	public void setRoadPoint(int roadPoint) {
//		this.roadPoint = roadPoint;
//	}

}
