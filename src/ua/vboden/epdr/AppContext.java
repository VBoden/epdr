package ua.vboden.epdr;

import java.util.List;
import java.util.Set;

public class AppContext {

	private List<Road> roads;
	private Set<AbstractRoadCross> roadCrosses;
	private float angle = 0;

	public List<Road> getRoads() {
		return roads;
	}

	public void setRoads(List<Road> roads) {
		this.roads = roads;
	}

	public float getAngle() {
		return angle;
	}

	public int getAngleDegres() {
		return (int) (angle * 180 / Math.PI);
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public Set<AbstractRoadCross> getRoadCrosses() {
		return roadCrosses;
	}

	public void setRoadCrosses(Set<AbstractRoadCross> roadCrosses) {
		this.roadCrosses = roadCrosses;
	}

}
