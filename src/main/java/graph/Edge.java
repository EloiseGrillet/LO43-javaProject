package graph;

public class Edge {

	/** Vertex start of this */
	private Vertex startEdge;

	/** Vertex end of this */
	private Vertex endEdge;

	/** Name of this */
	private String name;

	/** Type of this */
	private String type;

	/** Weigth of this */
	private int weigth;

	/** Define the weigth of this with the type */
	private void defineWeigth(){

	}

	public Edge(String name, String type, Vertex start, Vertex end){
		this.name = name;
		this.type = type;
		this.startEdge = start;
		this.endEdge = end;
		defineWeigth();
	}

	public Vertex getStartEdge() {
		return startEdge;
	}

	public Vertex getEndEdge() {
		return endEdge;
	}

	public int getWeigth() {
		return weigth;
	}

	public String getType(){
		return type;
	}

	@Override
	public String toString() {
		return name + " Start V " + startEdge + " End V " + endEdge + " Weigth " + weigth;
	}

	public String getName(){
		return name;
	}
}
