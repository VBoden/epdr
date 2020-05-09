package ua.vboden.epdr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jme3.math.Vector3f;

public class RoadsCreator {

	private AppContext context;

	public RoadsCreator(AppContext context) {
		this.context = context;
	}

	public void createRoads() {
		List<Road> roads = getRoads();
		context.setRoads(roads);
		createCrosses();
	}

	private List<Road> getRoads() {
		List<Road> roads = new ArrayList<>();
		Road road = new Road(new Vector3f(1, 0, 1), new Vector3f(98, 0, 1));
		road.setId(0);
		roads.add(road);
		road = new Road(new Vector3f(1, 0, 40), new Vector3f(98, 0, 40));
		road.setId(1);
		roads.add(road);
		road = new Road(new Vector3f(1, 0, 1), new Vector3f(1, 0, 98));
		road.setId(2);
		roads.add(road);
		road = new Road(new Vector3f(20, 0, 1), new Vector3f(20, 0, 98));
		road.setId(3);
		roads.add(road);
		return roads;
	}

	private void createCrosses() {
		Set<AbstractRoadCross> roadCrosses = new HashSet<>();
		for (Road road : context.getRoads()) {
			for (int i = (int) road.getStart().x; i < road.getEnd().x + 1; i++) {
				for (int j = (int) road.getStart().z; j < road.getEnd().z + 1; j++) {
					for (Road road2 : context.getRoads()) {
						if (road.isDirectedByZ() == road2.isDirectedByZ())
							continue;
						if (road2.hasPoint(i, j)) {
							int crossPoint = road.toRoadPoint(i, j);
							road.addCrossPoint(crossPoint);
							AbstractRoadCross roadCross = new RoadCrossWithLights(new Vector3f(i, 0, j), context);
							for (AbstractRoadCross cross : roadCrosses)
								if (cross.equals(roadCross)) {
									roadCross = cross;
									break;
								}
							roadCrosses.add(roadCross);
							roadCross.addMember(road);
							road.getCrosses().put(crossPoint, roadCross);
						}
					}
				}
			}
		}
		context.setRoadCrosses(roadCrosses);
	}
}
