package graph;

import java.io.Serializable;
import java.util.ArrayList;

public class Vertex implements Serializable{

	/** Vertex name, possibly a letter or number, here it's a number */
	private String name;

	/** Vertex number, use for equals and other method */
	private String number;

	/** List with successors of this Vertex */
	private ArrayList<Vertex> successors = new ArrayList<>();
	private ArrayList<Edge> succLink = new ArrayList<>();

	/** List with predecessor of this Vertex */
	private Vertex predecessor;


	/** Mark of the vertex, if it's max_value it's not mark */
	private int mark = Integer.MAX_VALUE;


	public Vertex(String number, String name,int altitude){
		this.number = number;
		this.name = name;
	}

	/** Set the Vertex who mark this */
	public void setPred(Vertex pred){
		this.predecessor = pred;
	}

	/** Set successor and the link with the other Vertex */
	public void setSucc(Vertex succ, Edge link){
		this.successors.add(succ);
		succLink.add(link);
	}

	public ArrayList<Vertex> getSuccessors(){
		return this.successors;
	}

	/** Found the link with the successor v */
	public Edge getLink(Vertex v){
		for(int i=0; i < successors.size();i++){
			if(successors.get(i).equals(v)){
				return succLink.get(i);
			}
		}
		return null;
	}

	public Vertex getPredecessors(){
		return this.predecessor;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public String toString(){
		return number;
	}

	@Override
	public boolean equals(Object obj) {
		return this.number.equals(((Vertex)obj).toString());
	}
}

