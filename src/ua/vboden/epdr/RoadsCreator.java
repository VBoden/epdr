package ua.vboden.epdr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.jme3.math.Vector3f;

public class RoadsCreator {

	private static final int START_X = 0;
	private static final int START_Y = 1;
	private static final int END_X = 2;
	private static final int END_Y = 3;

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
		Road road;
		int id = 0;
		String filePath = "data/roads.csv";
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

			String line;
			boolean firstLine = true;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#") || firstLine) {
					firstLine = false;
					continue;
				}
				String[] roadData = line.split(",");
				int[] data = new int[roadData.length];
				for (int i = 0; i < roadData.length; i++)
					data[i] = Integer.parseInt(roadData[i]);
				road = new Road(new Vector3f(data[START_X], 0, data[START_Y]),
						new Vector3f(data[END_X], 0, data[END_Y]));
				road.setId(id++);
				roads.add(road);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
							AbstractRoadCross roadCross;
							if (new Random().nextInt(10) > 5)
								roadCross = new RoadCrossWithMan(new Vector3f(i, 0, j), context);
							else
								roadCross = new RoadCrossWithLights(new Vector3f(i, 0, j), context);
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
