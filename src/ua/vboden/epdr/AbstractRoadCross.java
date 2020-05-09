package ua.vboden.epdr;

import java.util.HashSet;
import java.util.Set;

import com.jme3.math.Vector3f;

public class AbstractRoadCross {

	private Vector3f coordinates;
	private Set<Road> members;
	private AppContext context;

	public AbstractRoadCross(Vector3f coordinates, AppContext context) {
		this.coordinates = coordinates;
		this.context = context;
		members = new HashSet<>();
	}

	public Vector3f getCoordinates() {
		return coordinates;
	}

	public int getX() {
		return (int) coordinates.x;
	}

	public int getY() {
		return (int) coordinates.y;
	}

	public int getZ() {
		return (int) coordinates.z;
	}

	public void setCoordinates(Vector3f coordinates) {
		this.coordinates = coordinates;
	}
	
	public void addMember(Road road) {
		members.add(road);
	}

	public Set<Road> getMembers() {
		return members;
	}

	public void setMembers(Set<Road> members) {
		this.members = members;
	}

	public AppContext getContext() {
		return context;
	}

	public void setContext(AppContext context) {
		this.context = context;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractRoadCross other = (AbstractRoadCross) obj;
		if (coordinates == null) {
			if (other.coordinates != null)
				return false;
		} else if (!coordinates.equals(other.coordinates))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AbstractRoadCross [coordinates=" + coordinates + "]";
	}

}
