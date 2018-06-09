package graph;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class SetOfStreets {

	/** Contains the points of the graph */
	private HashMap<Integer, Point> points;

	/** Contains the roads of the graph */
	private HashMap<String, Road> roads;
	
	/** Limits of the map */
	private double[] nwPoint;	// north-west
	private double[] sePoint;	// south-east
	
	/** Size of the map */
	private static double[] MAP_SIZE = {9807, 6867};

	/** Constructor */
	public SetOfStreets() {
		points = new HashMap<Integer, Point>();
		roads = new HashMap<String, Road>();
		nwPoint = new double[2];
		sePoint = new double[2];
	}
	
	/** Constructor by copy */
	public SetOfStreets(SetOfStreets toCopy) {
		points = new HashMap<Integer, Point>(toCopy.points);
		roads = new HashMap<String, Road>(toCopy.roads);
		nwPoint = new double[2];
		sePoint = new double[2];
	}
	
	/**
	 * Set the limits of the map with 2 points : North-West and South-East
	 * @param nwPoint north-west point
	 * @param sePoint south-east point
	 */
	public void setCorners(double[] nwPoint, double[] sePoint) {
		this.nwPoint[0] = nwPoint[0];
		this.nwPoint[1] = nwPoint[1];
		this.sePoint[0] = sePoint[0];
		this.sePoint[1] = sePoint[1];
	}
	
	/**
	 * Convert the coords of all the points from pixel to the same coord unit as the corners
	 */
	public void pixelToCoord() {
		// size of the real map according to the corners
		double sizeX = sePoint[0] - nwPoint[0];
		double sizeY = sePoint[1] - nwPoint[1];
//		double sizeX = 7.2568606268 - 6.31946718153;
//		double sizeY = 47.347630014 - 47.847860155;

		// coeff between the size of the map in pixel and the one in the wanted coord
		double coeffX = sizeX / MAP_SIZE[0];
		double coeffY = sizeY / MAP_SIZE[1];
		
		// conversion using the previous coeff
		points.forEach((tmp, point) -> {point.setX(6.31946718153 + 0.01 + point.getX() * coeffX);
										point.setY(47.847860155 - 0.0058 + point.getY() * coeffY);});
	}
	
	/** 
	 * Add a point to the graph
	 */
	public void addPoint(Point point) {
		points.put(point.getId(), point);
	}
	
	/** 
	 * Add a point to the graph
	 * @param numPoint the id of the point
	 * @param coord the coordinates of the point
	 */
	public void addPoint(int numPoint, double[] coord) {
		points.put(numPoint, new Point(numPoint, coord));
	}
	
	/** 
	 * @param numPoint the id of the wanted point
	 * @return the point with the specified id 
	 */
	public Point getPoint(int numPoint) {
		return points.get(numPoint);
	}
	
	/**
	 * @return the list of all the points of the graph
	 */
	public ArrayList<Point> getPoints() {
		ArrayList<Point> toReturn = new ArrayList<Point>();
		
		// add each point o the ArrayList
		points.forEach((id, point) -> toReturn.add(point));
		
		return toReturn;
	}
	
	/**
	 * Delete a given point from the graph
	 * @param numPoint the point to delete
	 */
	public void delPoint(int numPoint) {
		points.remove(numPoint);
		// TODO remove from roads
	}
	
	/**
	 * Delete a given point from the graph
	 * @param point the point to delete
	 */
	public void delPoint(Point point) {
		points.remove(point.getId());
	}
	
	/**
	 * @return the number of points in the graph
	 */
	public int nbPoints() {
		return points.size();
	}
	
	/** 
	 * Add a road with its name and the points it's passing by
	 */
	public void addRoad(Road road) {
		roads.put(road.getName(), road);
	}
	
	/** 
	 * Add a road with its name and the points it's passing by
	 * @param name the name of the road
	 * @param pts the points it's passing by
	 */
	public void addRoad(String name, Point[] pts) {
		// TODO
	}
	
	/**
	 * Change the name of a road
	 * @param oldName the current name
	 * @param newName the new name
	 */
	public void renameRoad(String oldName, String newName) {
		// TODO
	}
	
	/**
	 * Delete a road
	 * @param name the name of the road to delete
	 */
	public void delRoad(String name) {
		// TODO
	}
	
	/**
	 * Delete a road
	 * @param road the road to delete
	 */
	public void delRoad(Road road) {
		// TODO
	}
	
	/**
	 * @param name the name of the road to get
	 * @return the road with the specified name
	 */
	public Road getRoad(String name) {
		return roads.get(name);
	}
	
	/**
	 */
	public ArrayList<String> getRoads() {
		ArrayList<String> toReturn = new ArrayList<String>();
		
		// add the name of each road to the ArrayList
		roads.forEach((name, road) -> toReturn.add(name));
		
		return toReturn;
	}
}
