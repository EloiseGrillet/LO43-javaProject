package graph;

import java.util.ArrayList;

public class Point {

	/** Point number, use for equals and other method */
	private int id;
	
	/** Coord of the point */
	private double[] coord;
	
	/** Roads passing through this point */
	private ArrayList<String> roads;

	/** List of the successors of this Point */
	private ArrayList<Integer> successors = new ArrayList<Integer>();
	private ArrayList<String> succLink = new ArrayList<String>();

	/** List with predecessor of this Point */
	private int predecessor;

	/** Mark of the point, MAX_VALUE = unmarked */
	private double mark = Double.MAX_VALUE;

	public Point() {
		this.coord = new double[2];
		this.roads = new ArrayList<String>();
	}
	
	/**
	 * Create a point with its id and its coords
	 * @param id the id of the point
	 * @param coord the coordinates of the points
	 */
	public Point(int id, double[] coord){
		this.id = id;
		
		this.coord = new double[2];
		this.coord[0] = coord[0];
		this.coord[1] = coord[1];
		
		this.roads = new ArrayList<String>();
	}
	
	/**
	 * Set the id of the point with a new given one
	 */
	public void setId(int newId) {
		id = newId;
	}
	
	/** 
	 * @return the id of the current point
	 */
	public int getId() {
		return id;
	}

	/**
	 * Change coord with new given data
	 */
	public void setX(double coordX) {
		coord[0] = coordX;
	}
	
	/**
	 * Change coord with new given data
	 */
	public void setY(double coordY) {
		coord[1] = coordY;
	}
	
	/**
	 * @return coord X
	 */
	public double getX() {
		return coord[0];
	}
	
	/**
	 * @return coord Y
	 */
	public double getY() {
		return coord[1];
	}
	
	/**
	 * Calc the distance as the crow flies
	 * from the current point from another given one
	 * @param point the other point
	 * @return the distance between the two points
	 */
	public double distanceFrom(Point point) {
		// sqrt((x0 - x1)² + (y0 - y1)²)
		double valX = Math.pow(coord[0] - point.coord[0], 2);
		double valY = Math.pow(coord[1] - point.coord[1], 2);
		return Math.sqrt(valX + valY);
	}
	
	/** Set the Point that marked this */
	public void setPred(int pred){
		this.predecessor = pred;
	}

	/** Add a successor and its link */
	public void addSucc(int succ, String link){
		successors.add(succ);
		succLink.add(link);
	}
		
	/** Remove a successor and its link */
	public void delSucc(int succ, String link) {
		Integer successor = new Integer(succ);
		successors.remove(successor);
		succLink.remove(link);
	}
	
	public ArrayList<Integer> getSuccessors(){
		return this.successors;
	}

	/** Find the link with the successor */
	public String getLink(int succ){
		for (int i = 0; i < successors.size(); i++) {
			if (successors.get(i) == succ) {
				return succLink.get(i);
			} // else
		}
		
		// No link found
		return null;
	}

	public int getPredecessor(){
		return this.predecessor;
	}

	public double getMark() {
		return mark;
	}

	public void setMark(double d) {
		this.mark = d;
	}

	@Override
	public boolean equals(Object obj) {
		return this.id == ((Point)obj).getId();
	}
	
	/**
	 * Add a road to the list of the ones passing by this point
	 * @param newRoad the road to add
	 */
	public void addRoad(String newRoad) {
		roads.add(newRoad);
	}
	
	/**
	 * Delete a given road of the list of the ones passing by this point
	 * @param road the road to delete
	 */
	public void delRoad(String road) {
		roads.remove(road);
	}
	
	/** 
	 * @return a table of the roads passing by this point
	 */
	public String[] getRoads() {
		return roads.toArray(new String[1]);
	}
}