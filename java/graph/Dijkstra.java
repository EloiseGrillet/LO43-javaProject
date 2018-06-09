package graph;

import java.util.ArrayList;

public class Dijkstra {

	/** Graph to work on */
	private SetOfStreets wholeGraph;
	
	/** List of the points of the graph (algo work on it) */
	private ArrayList<Point> points = new ArrayList<>();
	
	/** Points and roads of the final path */
	public ArrayList<Point> pathPoints;
	public ArrayList<String> pathRoads;
	
	/** List used for Dijkstra algorithm */
//	private ArrayList<Integer> listPointVisited;

	/** List used to find a graph with selected road */
//	private ArrayList<String> graph;
	
	/**
	 * Init dijkstra algorithm with a copy of a specified SetOfStreets
	 */
	public Dijkstra(SetOfStreets wholeGraph) {
		this.wholeGraph = new SetOfStreets(wholeGraph);

		points = this.wholeGraph.getPoints();
		pathPoints = new ArrayList<Point>();
		pathRoads = new ArrayList<String>();
//		listPointVisited = new ArrayList<Integer>();
//		graph = new ArrayList<String>();
//		
//		wholeGraph.getPoints().forEach(
//				(numPoint, point) -> listPointVisited.add(numPoint));
//		listPointVisited.get(start).setMark(0);
		initSuccessors();
	}

	private void reset(){
		points = this.wholeGraph.getPoints();
		pathPoints.clear();
		pathRoads.clear();
	}

	/**
	 * Init the successors of all the points of the graph
	 */
	private void initSuccessors() {
		// for each point
		wholeGraph.getPoints().forEach(point -> {
			
			// for each road linked to this point
			String[] roads = point.getRoads();
			for (String roadName : roads) {
				
				// search the successor of this point on this road
				Road road = wholeGraph.getRoad(roadName);
				int posPointInRoad = road.getPos(point.getId());
				
				// successor placed just before this point on the road
				if (posPointInRoad > 0) {
					point.addSucc(road.getPoint(posPointInRoad - 1), roadName);
				}

				// successor placed just after this point on the road
				if (posPointInRoad < road.nbPoints() - 1) {
					point.addSucc(road.getPoint(posPointInRoad + 1), roadName);
				}
			}
		});
	}
	
	/**
	 * Search minimum for the next step of Dijkstra algorithm
	 * @return the Point with minimum weigth
	 * @throws NoPathException if no Point, raise this exception
	 */
	private Point searchMin() throws NoPathException {
		double min = Double.MAX_VALUE;
		double weigth;
		Point pointMin = null;

		for (int i = 0; i < points.size(); i++) {
			weigth = points.get(i).getMark();
			if (weigth < min) {
				min = weigth;
				pointMin = points.get(i);
			}
		}
		
		if (pointMin == null) {
			throw new NoPathException("No path found");
		} // else

		return pointMin;
	}

	/** 
	 * Get the weigth of one edge with this Point
	 * @param start Point start of the edge
	 * @param end Point end of the edge
	 * @return weigth of the edge
	 */
	private static double weigth(Point start, Point end){
		return start.distanceFrom(end);
	}

	/**
	 * Update the distance on the Point Mark
	 * @param v1 Point start of the edge
	 * @param v2 Point end of the edge
	 * @return weigth of the edge
	 */
	private static void distance_update(Point v1, Point v2) throws NoPathException {
		if((v2.getMark() > v1.getMark() + weigth(v1,v2))){
			v2.setMark(v1.getMark() + weigth(v1,v2));
			v2.setPred(v1.getId());
		}
	}

	/** 
	 * Algorithm Dijkstra
	 * @param start number of the starting point
	 * @throws NoPathException raise exception if there is a problem
	 */
	private void dijkstraAlgo(int start) throws NoPathException {
//		wholeGraph.getPoint(start).setMark(0);
		wholeGraph.getPoint(start).setMark(0);
		Point v;
//		while (wholeGraph.nbPoints() > 0) {
		while (points.size() > 0) {
			v = searchMin();
//			wholeGraph.delPoint(v);
			points.remove(v);
			ArrayList<Integer> listSucc = v.getSuccessors();
			for (int i = 0; i < listSucc.size(); i++) {
				distance_update(v, wholeGraph.getPoint(listSucc.get(i)));
			}
		}
	}

	/**
	 * Run the shortest path with Dijkstra algorithm
	 * @param startN start number vertex
	 * @param end end number vertex
	 * @throws NoPathException raise exception if there is a problem
	 */
	public void shortestPath(int startN, int end) throws NoPathException {
		reset();
		dijkstraAlgo(startN);
		Point previous = wholeGraph.getPoint(end);
		Point verify = wholeGraph.getPoint(previous.getPredecessor());
		Point start = wholeGraph.getPoint(startN);
		
		pathPoints.add(previous);
//		LinkedList<Point> shortest = new LinkedList<>();
		while (!start.equals(verify)){
//			shortest.add(verify);
			pathPoints.add(0, verify);
			pathRoads.add(0, previous.getLink(verify.getId()));
			previous = verify;
			verify = wholeGraph.getPoint(verify.getPredecessor());
		}

//		shortest.addLast(start);
		pathPoints.add(0, start);
		pathRoads.add(0, previous.getLink(start.getId()));
		wholeGraph = new SetOfStreets();
	}
}