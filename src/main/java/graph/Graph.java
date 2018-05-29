package graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class Graph{

	/** Contains all vertices of the graph */
	public static ArrayList<Vertex> vertices = new ArrayList<>();

	/** Contains all the edges of the graph */
	public static LinkedList<Edge> edges = new LinkedList<>();

	/** List used for Dijkstra algorithm */
	private static ArrayList<Vertex> listVertexVisited = new ArrayList<>();

	/** List used for found a graph with selected road */
	private static ArrayList <Edge> graph = new ArrayList<>();

	/**
	 * Read a xml file and create vertex and edges
	 * @param file Found file in package Raw in android
	 * @throws IOException if there is no file
	 */
	public static void readXMLFile(InputStream file) throws IOException{
//		String line;
//		String csvSplitBy = ",";
//		boolean writeVertex = true;
//		BufferedReader br = new BufferedReader(new InputStreamReader(file));
//
//		while((line = br.readLine()) != null){
//			line.replaceAll(" ", ",");
//			String[] lineRead = line.split(csvSplitBy);
//			if(lineRead[0].equals("95")){
//				writeVertex = false;
//			}
//			if(lineRead.length >= 2 && writeVertex){
//				vertices.add(new Vertex(lineRead[0],lineRead[1],Integer.parseInt(lineRead[2])));
//			} else if(lineRead.length >= 2 && !writeVertex){
//				Vertex start = vertices.get(Integer.parseInt(lineRead[3])-1);
//				Vertex end = vertices.get(Integer.parseInt(lineRead[4])-1);
//				edges.add(new Edge(lineRead[1],lineRead[2],start,end));
//				start.setSucc(end,edges.getLast());
//			}
//		}

	}

	/**
	 * Init dijkstra algorithm
	 * @param indice start of the algorithm
	 */
	private static void initDijkstra(int indice){
		listVertexVisited.clear();
		listVertexVisited.addAll(vertices);
		listVertexVisited.get(indice).setMark(0);
	}

	/**
	 * Search minimum for the next step of Dijkstra algorithm
	 * @return vertexMin the Vertex with minimum weigth
	 * @throws NoPathException if no Vertex raise this exception
	 */
	private static Vertex found_min() throws NoPathException {
		int min = Integer.MAX_VALUE; // infinity
		int weigth;
		Vertex vertexMin = null;

		for(int i=0;i<listVertexVisited.size();i++){
			weigth = listVertexVisited.get(i).getMark();
			if(weigth < min){
				min = weigth;
				vertexMin = listVertexVisited.get(i);
			}
		}
		if(vertexMin == null){
			throw new NoPathException("No path found");
		}

		return vertexMin;
	}

	/** Get the weigth of one edge with this Vertex
	 * @param start Vertex start of the edge
	 * @param end Vertex end of the edge
	 * @return weigth of the edge
	 */
	private static int weigth(Vertex start, Vertex end){
		return start.getLink(end).getWeigth();
	}

	/**
	 * Update the distance on the Vertex Mark
	 * @param v1 Vertex start of the edge
	 * @param v2 Vertex end of the edge
	 * @return weigth of the edge
	 */
	private static void distance_update(Vertex v1, Vertex v2) throws NoPathException {

		if((v2.getMark() > v1.getMark() + weigth(v1,v2))){
			v2.setMark(v1.getMark() + weigth(v1,v2));
			v2.setPred(v1);
		}

	}

	/** Algorithm Dijkstra
	 * @param start number of the start vertex
	 * @throws NoPathException raise exception if there is a problem
	 */
	private static void Dijkstra(int start) throws NoPathException {
		initDijkstra(start-1);
		Vertex v;
		while(!listVertexVisited.isEmpty()){
			v = found_min();
			listVertexVisited.remove(v);
			ArrayList<Vertex> listSucc = v.getSuccessors();
			for (int i = 0; i < listSucc.size(); i++) {
				distance_update(v, listSucc.get(i));
			}
		}

	}

	/**
	 * Run the shortest path with Dijkstra algorithm
	 * @param startN start number vertex
	 * @param end end number vertex
	 * @return shortest with all vertex in the shortest path
	 * @throws NoPathException raise exception if there is a problem
	 */
	public static LinkedList<Vertex> ShortestPath(int startN, int end) throws NoPathException {
		Dijkstra(startN);
		Vertex verify = vertices.get(end-1);
		Vertex start = vertices.get(startN-1);
		LinkedList<Vertex> shortest = new LinkedList<>();
		while (!start.equals(verify)){
			shortest.add(verify);
			verify = verify.getPredecessors();
		}

		shortest.addLast(start);
		listVertexVisited.clear();
		return shortest;

	}

	/** Init the research of a graph with selected road
	 * @param start Vertex who start the graph
	 * @param levelChoose Tab who contains type of road choose by the user
	 * @return All edge in the graph found
	 */
	public static ArrayList<Edge> findGraphInit(Vertex start, String[] levelChoose){
		findGraph(start,levelChoose);
		return graph;
	}

	/** Function recusive for find graph with type road choose
	 * @param start Vertex who start the graph
	 * @param levelChoose Tab who contains type of road choose by the user
	 */
	private static void findGraph(Vertex start, String[] levelChoose){
		ArrayList<Vertex> succ = start.getSuccessors();
		if(succ.size() == 0){
		} else {
			for(int i = 0;i<succ.size();i++){
				Edge edge = start.getLink(succ.get(i));
				if(roadisOk(edge.getType(),levelChoose)
						&& ! graph.contains(edge)){
					//add on the list
					graph.add(edge);
					findGraph(start.getLink(succ.get(i)).getEndEdge(),levelChoose);
				}
			}
		}
	}

	/** Chek if we can travel on the road
	 * @param road in question for example "TS"
	 * @param levelChoose All the type of road
	 * @return true if we can use it, and false if we can't
	 */
	private static boolean roadisOk(String road,String[] levelChoose) {
		for(int i=0;i<levelChoose.length;i++) {
			if(road.equals(levelChoose[i])){
				return true;
			}
		}
		return false;
	}

	/**
	 * Found edge with this name
	 * @param name name of this Edge
	 * @return Edge with this name
	 */
	public static Edge foundEdge(String name){
		for(int i = 0; i<edges.size();i++){
			if(edges.get(i).getName().equals(name)){
				return edges.get(i);
			}
		}
		return null;
	}

}
