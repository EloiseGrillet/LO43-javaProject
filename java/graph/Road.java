package graph;

import java.util.ArrayList;

public class Road {

	/** Name of the road */
	private String name;
	
	/** Points linked by the road */
	private ArrayList<Integer> points;

	public Road(){
		points = new ArrayList<Integer>();
	}
	/**
	 * Make a road with its name
	 * @param name the name of the road
	 */
	public Road(String name){
		this.name = name;
		points = new ArrayList<Integer>();
	}

	/**
	 * Make a road with its name and the points it's passing by
	 * @param name the name of the road.
	 * @param points the points it's passing by
	 */
	public Road(String name, ArrayList<Integer> points){
		this.name = name;
		this.points = new ArrayList<Integer>(points);
	}

	/**
	 * Set the name of the road
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the name of the road	
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Add a point to the list at the end of the list
	 * @param point the new point
	 */
	public void addPoint(int point) {
		points.add(point);
	}
	
	/**
	 * Add a point to the list at a given position
	 * @param point the new point
	 * @param pos the position of the point
	 */
	public void addPoint(int point, int pos) {
		points.add(pos, point);
	}
	
	/** 
	 * Delete a given point
	 * @param point the point to delete
	 */
	public void delPoint(int point) {
		points.remove(point);
	}
	
	/**
	 * Get the id of th point in a given position
	 * @param pos the position of the point to find
	 * @return the point in the given position
	 */
	public int getPoint(int pos) {
		return points.get(pos);
	}
	
	/**
	 * Get the position of a given point in the list of points of the road
	 * @param point the point to find in the list
	 * @return -1 if the point doesn't exist in the liste,
	 * 		   the pos of the point in the list if it exists
	 */
	public int getPos(int point) {
		return points.indexOf(point);
	}
	
	/**
	 * @return the number of points linked by this road
	 */
	public int nbPoints() {
		return points.size();
	}
	
	/**
	 * @return the points this road is passing by
	 */
	public ArrayList<Integer> getPoints() {
		return new ArrayList<Integer>(points);
	}
}
