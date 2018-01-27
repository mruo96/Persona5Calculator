package javaFiles;

import java.util.HashSet;
import java.util.Set;

/**
 * A GraphNode represents a node in a directed labeled multigraph Graph.
 */
public class GraphNode<N> {

	private N name;
	private Set<N> pointsToMe;

	// Abstraction Function:
	// The GraphNode, n, is represented by two fields. name is the String that represents n.
	// pointsToMe stores the nodes that have outgoing edges to n.
	//
	// Representation Invariant for every GraphNode n:
	// name != null &&
	// pointsToMe != null

	/**
	 * @param name The object/primitive that will represent 'this'
	 * @requires name != null
	 * @effects Constructs a new GraphNode that has no outgoing or incoming edges
	 */
	public GraphNode(N name) {
		this.name = name;
		this.pointsToMe = new HashSet<N>();
		checkRep();
	}

	/**
	 * @return the object/primitive that represents 'this'
	 */
	public N getName() {
		return name;
	}

	/**
	 * @return a set of the objects/primitives that represent the GraphNodes with outgoing edges to 'this'
	 */
	public Set<N> getPointsToMe() {
		return new HashSet<N>(pointsToMe);
	}

	/**
	 * @param n The object/primitive that represents the GraphNode that will have an outgoing edge to 'this'
	 * @requires n != null && pointsToMe doesn't already contain n
	 * @modifies 'this'
	 * @effects add the GraphNode represented by n as a node that has an edge to this. Does nothing 
	 * 			if a GraphNode represented by n already has an outgoing edge to 'this'.
	 */	
	public void addToPointsToMe(N n) {
		if (!pointsToMe.contains(n)) {
			pointsToMe.add(n);
		}
		checkRep();
	}

	/**
	 * @param n The object/primitive that represents the GraphNode that will no longer have an outgoing edge to 'this'
	 * @requires n != null
	 * @modifies 'this'
	 * @effects remove the GraphNode represented by n as a node that has an edge to this. If pointsToMe is 
	 * 		    empty, does nothing.
	 */	
	public void removeFromPointsToMe(N n) {
		if (!pointsToMeIsEmpty()) {
			pointsToMe.remove(n);
		}
		checkRep();
	}

	/**
	 * @return true if no GraphNodes have outgoing edges to 'this' and false otherwise.
	 */	
	public boolean pointsToMeIsEmpty() {
		return pointsToMe.isEmpty();
	}

	/**
	 * @return a String representation of 'this' in the form:
	 * 		   			name.toString(), pointed to by [n1, n2, ...]
	 * 		   where n1, n2, ... are the toStrings() of the objects/primitives that represent the GraphNodes 
	 * 	       that point to 'this' in no particular order
	 * 
	 * 		   If 'this' has no outgoing edges from other GraphNodes pointing to it, then this method returns:
	 * 	       name.toString(), pointed to by no edges.
	 */
	public String toString() {
		if (pointsToMeIsEmpty()) {
			return name.toString() + ", pointed to by no edges.";
		} else {
			return name.toString() + ", pointed to by " + pointsToMe.toString();
		}
	}

	/**
	 * Standard equality operation.
	 *
	 * @param o The object to be compared for equality.
	 * @return true if and only if 'o' is an instance of a GraphNode, 'this' and 'o'
	 * 		   are represented by the same object/primitive, and 'this' and 'o' have the same GraphNodes
	 *         pointing to it.
	 */ 
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof GraphNode<?>)) {
			return false;
		}
		GraphNode<N> n = (GraphNode<N>) o;
		return n.name.equals(this.name) && this.pointsToMe.equals(n.pointsToMe);
	}

	/**
	 * Standard hashCode function.
	 * 
	 * @return an int that all objects equal to this will also return.
	 */
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * Checks that the representation invariant holds.
	 */
	private void checkRep() {
		assert (name != null) : "name is null";
		assert (pointsToMe != null) : "pointsToMe is null";
	}
}
