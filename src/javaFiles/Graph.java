package javaFiles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Graph represents a mutable, directed labled multigraph that holds Strings in its nodes and edges.
 * Repeated edges (edges with the same lable that point from and to the same node) are not supported.
 * Repeat nodes (nodes represented by objects considered to be equal) are not supported.
 */
public class Graph<N, E> {

	// disables/enables the more expensive part of checkRep() to improve performance
	private final boolean runExpensiveCheckRep = false;

	private Map<N, GraphNode<N>> nodes;
	private Map<N, HashMap<N, HashSet<E>>> nodesToEdges;

	// Abstraction Function:
	// The Graph g, is represented by two fields. nodes stores all of the nodes that are currently in 
	// the graph. nodesToEdges maps the nodes that have outedges to the nodes that those outedges point to.
	// Those nodes that the outedges point to are then mapped to the outedges.
	//
	// Representation Invariant for every Graph g:
	// nodes != null &&
	// nodesToEdges != null &&
	// no String key in nodesToEdges maps to an empty HashMap (in other words, a node that 
	// does not have any outgoing edges should not be in the keyset for nodesToEdges)

	/**
	 * @effects Constructs an empty Graph with no nodes or edges.
	 */
	public Graph() {
		nodes = new HashMap<N, GraphNode<N>>();
		nodesToEdges = new HashMap<N, HashMap<N, HashSet<E>>>();
		checkRep();
	}

	/**
	 * @param node The object that represents the node to be inserted
	 * @return true if the insertion succeeded and false otherwise. It will fail if the node 
	 * 		   represented by 'node' is already contained in the graph.
	 * @throws IllegalArgumentException if: node == null
	 * @modifies this
	 * @effects inserts an unconnected node represented by 'node' into 'this'.
	 */
	public boolean insertNode(N node) throws IllegalArgumentException {
		if (node == null) {
			throw new IllegalArgumentException();
		}

		if (nodes.containsKey(node)) { // graph already contains n1, failure
			return false;
		}

		// the graph doesn't contain n1 yet, so add it in
		nodes.put(node, new GraphNode<N>(node));

		checkRep();
		return true;
	}

	/**
	 * @param nodeTo The object that represents the node that the edge to be inserted points to
	 * @param nodeFrom The object that represents the node that the edge to be inserted points from
	 * @param label The label of the edge to be inserted
	 * @return true if the insertion succeeded and false otherwise. It will fail if there already exists an edge
	 * 		   from the node represented by nodeFrom to the node represented by nodeTo with the given label label
	 * 		   (in order words, duplicate edges are not supported).
	 * @throws IllegalArgumentException if: label != null || nodeTo != null || nodeFrom != null
	 * @modifies this
	 * @effects inserts an edge with the given label label that points from the node represented by nodeFrom to the
	 * 			node represented by nodeTo
	 */
	public boolean insertEdge(N nodeFrom, N nodeTo, E label) throws IllegalArgumentException {
		if (nodeFrom == null || nodeTo == null || label == null) {
			throw new IllegalArgumentException();
		}

		// if the graph doesn't contain one or both of the given nodes, first insert them
		if (!nodes.containsKey(nodeTo)) {
			insertNode(nodeTo);
		}
		if (!nodes.containsKey(nodeFrom)) {
			insertNode(nodeFrom);
		}

		// check if the edge already exists; if not, insert it
		HashMap<N, HashSet<E>> hm = nodesToEdges.get(nodeFrom);
		if (hm == null) { // nodeFrom does not point to any other nodes
			// add a new edge between the nodeFrom and nodeTo
			hm = new HashMap<N, HashSet<E>>();
			HashSet<E> ts = new HashSet<E>();
			ts.add(label);
			hm.put(nodeTo, ts);
			nodesToEdges.put(nodeFrom, hm);
		} else { // nodeFrom points to some/a node(s)
			if (hm.containsKey(nodeTo)) { // nodeFrom has existing edges to nodeTo
				if (hm.get(nodeTo).contains(label)) {
					return false;
				} else {
					hm.get(nodeTo).add(label);
				}
			} else { // add a mapping for nodeTo
				HashSet<E> ts = new HashSet<E>();
				ts.add(label);
				hm.put(nodeTo, ts);
			}
		}
		// update nodeTo to have nodeFrom pointing to it
		nodes.get(nodeTo).addToPointsToMe(nodeFrom);

		checkRep();
		return true;
	}

	/**
	 * @param node The object representing the node to be removed
	 * @return true if the removal succeeded and false otherwise. It will fail if the node does not
	 * 		   already exist in the graph or if this.isEmpty().
	 * @throws IllegalArgumentException if: node == null
	 * @modifies this
	 * @effects removes from the graph the node represented by node and all of its outgoing and incoming edges
	 */
	public boolean removeNode(N node) throws IllegalArgumentException {
		if (node == null) {
			throw new IllegalArgumentException();
		}

		if (this.isEmpty() || !nodes.containsKey(node)) {
			return false;
		}

		// disconnect the nodes that n points to if there are any
		if (nodesToEdges.containsKey(node)) { // n has edges pointing to other nodes
			// for each node that n points to, update its pointsToMe field
			Iterator<N> itr = nodesToEdges.get(node).keySet().iterator();
			while (itr.hasNext()) {
				N elem = itr.next();
				nodes.get(elem).removeFromPointsToMe(node);
			}

			// remove the mapping for n in nodesToEdges because it no longer points to anything
			// after it's removed
			nodesToEdges.remove(node);
		}

		// disconnect the nodes that point to n if there are any
		if (!nodes.get(node).pointsToMeIsEmpty()) {
			Iterator<N> itr = nodes.get(node).getPointsToMe().iterator();
			while (itr.hasNext()) { // for every node that points to n, remove its connection to n
				N elem = itr.next();
				nodesToEdges.get(elem).remove(node);

				// if a node that points to n doens't have any edges that point to other nodes, remove 
				// it from nodesToEdges
				if (nodesToEdges.get(elem).isEmpty()) {
					nodesToEdges.remove(elem);
				}
			}
		}

		// remove the mapping for n
		nodes.remove(node);

		checkRep();
		return true;
	}

	/**
	 * @param nodeTo The object that represents the node that the edge to be removed points to
	 * @param nodeFrom The object that represents the node that the edge to be removed points from
	 * @param label The label of the edge to be removed
	 * @return true if the removal succeeded and false otherwise. It will fail if: 
	 * 		   - there is no edge with the label label pointing from the node represented by nodeFrom to the 
	 * 			 node represented by nodeTo in the graph
	 * 		   - either of the given nodes are not in the graph
	 * 		   - this.isEmpty() is true
	 * @throws IllegalArgumentException if: nodeTo == null || nodeFrom == null || label == null
	 * @effects removes the edge with the given label label that points from the node represented by nodeFrom 
	 * 			to the node represented by nodeTo from the graph
	 */
	public boolean removeEdge(N nodeFrom, N nodeTo, E label) throws IllegalArgumentException {
		if (nodeTo == null || nodeFrom == null || label == null) {
			throw new IllegalArgumentException();
		}

		if (this.isEmpty() || !nodes.containsKey(nodeTo) || !nodes.containsKey(nodeFrom)) {
			return false;
		}

		HashMap<N, HashSet<E>> hm = nodesToEdges.get(nodeFrom);
		if (hm == null) { // nodeFrom does not point to any nodes
			return false;
		} else { // nodeFrom points to some/a node(s)
			if (hm.containsKey(nodeTo)) { // nodeFrom has existing edges to nodeTo
				if (hm.get(nodeTo).contains(label)) { // the specific edge exists
					hm.get(nodeTo).remove(label);

					// if the removed edge was the only edge between nodeFrom and nodeTo,
					// remove the mapping from nodeFrom to nodeTo
					if (nodesToEdges.get(nodeFrom).get(nodeTo).isEmpty()) {
						nodesToEdges.get(nodeFrom).remove(nodeTo);

						// if nodeFrom now has edges to nothing, remove its mapping in nodesToEdges
						if (nodesToEdges.get(nodeFrom).isEmpty()) {
							nodesToEdges.remove(nodeFrom);
						}
					}

					nodes.get(nodeTo).removeFromPointsToMe(nodeFrom);
				} else {
					return false;
				}
			} else { // nodeFrom doesn't have any edges to nodeTo
				return false;
			}
		}

		checkRep();
		return true;
	}

	/**
	 * @param node The object that represents the node for which the children will be returned
	 * @return a set of the names of the children of the GraphNode with the name 'node'. The set is in 
	 * 		   alphabetical order when traversed with an iterator. If the node represented by 'node' has 
	 * 	       no children or is not in 'this', return null.
	 * @throws IllegalArgumentException if: node == null
	 */
	public Set<N> getChildren(N node) throws IllegalArgumentException {
		if (node == null) {
			throw new IllegalArgumentException();
		}
		if (!nodesToEdges.containsKey(node)) {
			return null;
		}

		return new HashSet<N>(nodesToEdges.get(node).keySet());
	}

	/**
	 * @param nodeFrom The name of the node with an edge that points to nodeTo
	 * @param nodeTo The name of the node with an edge pointing to it from nodeFrom
	 * @return a TreeSet of the labels of the edges going from the node with with nodeFrom to the node with 
	 * 		   name nodeTo sorted by alphabetical order of the labels. Returns null if:  
	 * 		   - 'this' is empty
	 * 		   - there are no outgoing edges from the node represented by nodeFrom to the node represented by nodeTo
	 * 		   - the node represented by nodeFrom is not in 'this'
	 * 	       - the node represented by nodeTo is not in the 'this'
	 * @throws IllegalArgumentException if: nodeFrom == null || nodeTo == null
	 */
	public Set<E> getEdges(N nodeFrom, N nodeTo) throws IllegalArgumentException {
		if (nodeFrom == null || nodeTo == null) {
			throw new IllegalArgumentException();
		}
		if (!nodes.containsKey(nodeFrom) || 
				!nodes.containsKey(nodeTo) || 
				!nodesToEdges.containsKey(nodeFrom) ||
				!nodesToEdges.get(nodeFrom).containsKey(nodeTo) ||
				isEmpty()) {
			return null;
		}

		return new HashSet<E>(nodesToEdges.get(nodeFrom).get(nodeTo));
	}

	/**
	 * @effects Clears 'this' so that it has no nodes or edges.
	 */
	public void clear() {
		nodes.clear();
		nodesToEdges.clear();
		checkRep();
	}

	/**
	 * @return true if 'this' is empty and false otherwise
	 */
	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	/**
	 * @return a set of the objects that represent of all the nodes in 'this'. Returns 
	 *         null if 'this' is empty. The set returned is not guaranteed to be in any
	 *         particular order.
	 */
	public Set<N> getAllNodes() {
		if (isEmpty()) {
			return null;
		}
		return new HashSet<N>(nodes.keySet());
	}

	/**
	 * @return a String representation of 'this'. For every node in the 'this' represented by n, return a string of the form:
	 * 		   (n is pointed to by: name1(edge(s): [edge1, edge2, ...]), name2(edges(s): [edge1, edge2, ...]), ...)\n
	 * 		   (n points to: nam1(edges(s): [edge1, edge2, ...]), nam2(edge(s): [edge1, edge2, ...]), ...)
	 * 		   where name1, name2, ... are the Strings that represent the nodes that have outgoing edges to n and where
	 * 	       nam1, nam2, ... are the Strings that represent the nodes that n has outgoing edges to.
	 *         that n points to. The nodes n, name and nam are not guaranteed to be in any particular order
	 */
	public String toString() {
		if (isEmpty()) {
			return "";
		}

		String result = "";
		Set<N> allNodes = getAllNodes();
		Iterator<N> itr = allNodes.iterator();
		while (itr.hasNext()) {
			N nextNode= itr.next();

			// this node has no edges
			if (!nodesToEdges.containsKey(nextNode) && nodes.get(nextNode).getPointsToMe().isEmpty()) {
				result += "(" + nextNode + " has no edges to or from it)\n";
				continue;
			}

			// this node has other nodes pointing to it
			if (!nodes.get(nextNode).getPointsToMe().isEmpty()) {
				result += "(" + nextNode + " is pointed to by: ";
				Iterator<N> itr2 = nodes.get(nextNode).getPointsToMe().iterator();
				if (itr2.hasNext()) {
					N nextNode2 = itr2.next();
					result += nextNode2 + "(edge(s): " + nodesToEdges.get(nextNode2).get(nextNode).toString() + ")";
				} else {

				}
				while (itr2.hasNext()) {
					N nextNode2 = itr2.next();
					result += ", " + nextNode2 + "(edge(s): " + nodesToEdges.get(nextNode2).get(nextNode).toString() + ")";
				}
				result += ")\n";
			} else {
				result += "(" + nextNode + " has no edges to it)\n";
			}

			// this node has edges to other nodes
			if (nodesToEdges.containsKey(nextNode)) {
				result += "(" + nextNode + " points to: ";

				Set<N> pointsToNodes = nodesToEdges.get(nextNode).keySet();
				Iterator<N> itr2 = pointsToNodes.iterator();
				if (itr2.hasNext()) {
					N nextNode2 = itr2.next();
					result += nextNode2 + "(edge(s): " + nodesToEdges.get(nextNode).get(nextNode2).toString() + ")";
				}
				while (itr2.hasNext()) {
					N str2 = itr2.next();
					result += ", " + str2 + "(edge(s): " + nodesToEdges.get(nextNode).get(str2).toString() + ")";
				}
				result += ")\n";
			} else {
				result += "(" + nextNode + " has no edges from it)\n";
			}
		}	

		return result;
	}

	/**
	 * Standard equality operation.
	 *
	 * @param o The object to be compared for equality.
	 * @return true if and only if 'o' is an instance of a Graph, and 'this' and 'o' represent graphs with
	 * 		   the same set of nodes and edges.
	 */ 
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Graph<?, ?>)) {
			return false;
		}
		Graph<?, ?> g = (Graph<?, ?>) o;
		return this.nodes.equals(g.nodes) && this.nodesToEdges.equals(g.nodesToEdges);
	}

	/**
	 * Used for testing only.
	 * @return nodes
	 */
	public Map<N, GraphNode<N>> getNodes() {
		return new HashMap<N, GraphNode<N>>(nodes);
	}

	/**
	 * Used for testing only.
	 * @return nodesToEdges
	 */
	public Map<N, HashMap<N, HashSet<E>>> getNodesToEdges() {
		return new HashMap<N, HashMap<N, HashSet<E>>>(nodesToEdges);
	}

	/**
	 * Checks that the representation invariant holds.
	 */
	private void checkRep() {
		assert (nodes != null) : "nodes is null";
		assert (nodesToEdges != null) : "nodesToEdges is null";

		if (runExpensiveCheckRep) {
			Set<N> temp = nodesToEdges.keySet();
			Iterator<N> itr = temp.iterator();
			while (itr.hasNext()) {
				N nextNode = itr.next();
				assert (!nodesToEdges.get(nextNode).isEmpty()) : "HashMap for key " + nextNode + " is empty.";
			}
		}
	}
}
