package ua.vboden.epdr;

import static ua.vboden.epdr.enums.Direction.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme3.math.Vector3f;

import ua.vboden.epdr.crosses.AbstractRoadCross;
import ua.vboden.epdr.enums.Direction;

public class Road {

	private int id;
	private Vector3f start;
	private Vector3f end;
	private List<Integer> crossPoints;
	private Map<Integer, AbstractRoadCross> crosses;

	public Road(Vector3f start, Vector3f end) {
		this.start = start;
		this.end = end;
		this.crossPoints = new ArrayList<>();
		this.crosses = new HashMap<>();
	}

	public boolean isDirectedByZ() {
		return start.x == end.x;
	}

	public int getXMult() {
		return !isDirectedByZ() ? 1 : 0;
	}

	public int getZMult() {
		return isDirectedByZ() ? 1 : 0;
	}

	public boolean hasPoint(float x, float z) {
		return !isDirectedByZ() && z == start.z && isBetween(x, start.x, end.x)
				|| isDirectedByZ() && x == start.x && isBetween(z, start.z, end.z);
	}

	private boolean isBetween(float value, float start, float end) {
		start = Math.min(start, end);
		end = Math.max(start, end);
		return value <= end && value >= start;
	}

	public int toRoadPoint(int x, int z) {
		return isDirectedByZ() ? z : x;
	}

	public AbstractRoadCross getNearestCross(int x, int z, Direction direction) {
		int point = toRoadPoint(x, z);
		int index = 0;
		int inc = 1;
		int endIndex = crossPoints.size();
		if (S == direction || E == direction) {
			index = crossPoints.size() - 1;
			inc = -1;
			endIndex = -1;
		}
		while (index != endIndex) {
			Integer crossPosition = crossPoints.get(index);
			if (-inc * (point - crossPosition) > 0) {
				return crosses.get(crossPosition);
			}
			index += inc;
		}
		return null;
	}

	public void addCrossPoint(int coordinate) {
		crossPoints.add(coordinate);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Vector3f getStart() {
		return start;
	}

	public void setStart(Vector3f start) {
		this.start = start;
	}

	public Vector3f getEnd() {
		return end;
	}

	public void setEnd(Vector3f end) {
		this.end = end;
	}

	public List<Integer> getCrossPoints() {
		return crossPoints;
	}

	public void setCrossPoints(List<Integer> crossPoints) {
		this.crossPoints = crossPoints;
	}

	public Map<Integer, AbstractRoadCross> getCrosses() {
		return crosses;
	}

	public void setCrosses(Map<Integer, AbstractRoadCross> crosses) {
		this.crosses = crosses;
	}

	@Override
	public String toString() {
		return "Road [id=" + id + ", start=" + start + ", end=" + end + "]";
	}

}
