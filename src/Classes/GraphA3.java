/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.ArrayList;
import util.LMeths;
import util.LSystem;

/**
 *
 * @author Lwdthe1
 */
/**
 * The <tt>GraphA3</tt> class represents an edge-weighted graph of vertices
 * named 0 through <em>maxVertices</em> - 1, where each undirected edge is of type
 * {@link Edge} and has a real-valued weight. It supports the following two
 * primary operations: add an edge to the graph, iterate over all of the edges
 * incident to a vertex. It also provides methods for returning the number of
 * vertices <em>maxVertices</em> and the number of edges <em>numEdges</em>. Parallel edges and
 * self-loops are permitted.
 * <p>
 * This implementation uses an adjacency-lists representation, which is a
 * vertex-indexed array of @link{Bag} objects. All operations take constant time
 * (in the worst case) except iterating over the edges incident to a given
 * vertex, which takes time proportional to the number of such edges.
 * <p>
 * For additional documentation, see
 * <a href="http://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class GraphA3 {

    private final int maxVertices;
    private int numEdges;
    private NodeA3[] adj;

    /**
     * Initializes an empty edge-weighted graph with <tt>maxVertices</tt> vertices and 0
     * edges if init is true
     *
     * @param init is true
     * @param V the number of vertices
     * @throws java.lang.IllegalArgumentException if <tt>maxVertices</tt> < 0
     */
    public GraphA3(int V, boolean init) {
        if (V < 0) {
            throw new IllegalArgumentException("Number of vertices must be nonnegative");
        }
        this.maxVertices = V;
        this.numEdges = 0;

        adj = (NodeA3[]) new NodeA3[V];
        if (init) {
            for (int v = 0; v < V; v++) {
                adj[v] = new NodeA3("");
            }
        }
    }

    /**
     * Returns the number of vertices in the edge-weighted graph.
     *
     * @return the number of vertices in the edge-weighted graph
     */
    public int maxVertices() {
        return maxVertices;
    }

    /**
     * Returns the number of edges in the edge-weighted graph.
     *
     * @return the number of edges in the edge-weighted graph
     */
    public int numEdges() {
        return numEdges;
    }

    // throw an IndexOutOfBoundsException unless 0 <= v < maxVertices
    private void validateVertex(int v) {
        if (v < 0 || v >= maxVertices) {
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (maxVertices - 1));
        }
    }

    /**
     * Adds the undirected edge <tt>e</tt> to the edge-weighted graph.
     *
     * @param edge the edge
     * @throws java.lang.IndexOutOfBoundsException unless both endpoints are
 between 0 and maxVertices-1
     */
    public void addEdge(EdgeA3 edge) {
        NodeA3 nodeA = edge.getNodeA();
        NodeA3 nodeB = edge.getNodeB();

        for (int j = 0; j < maxVertices; j++) {
            NodeA3 vertex = adj[j];

            if (vertex.getValue().equalsIgnoreCase(nodeA.getValue())) {
                vertex.getEdges().add(edge);
            } else if (vertex.getValue().equalsIgnoreCase(nodeB.getValue())) {
                vertex.getEdges().add(edge);
            }
        }

        numEdges++;
    }

    public void addEdges(ArrayList<EdgeA3> edges) {
        for (int i = 0; i < 177; i++) {
            EdgeA3 edge = edges.get(i);
            double weight = edge.getWeight();

            NodeA3 nodeA = edge.getNodeA();
            NodeA3 nodeB = edge.getNodeB();

            if (nodeA != null && nodeB != null) {
                validateVertex(i);
                adj[i] = nodeA;
                for (int p = i+1; p < maxVertices; p++) {
                    if(adj[p]== null){
                        adj[p] = nodeB;
                        break;
                    }
                }
                for (int j = 0; j < maxVertices; j++) {
                    NodeA3 vertex = adj[j];

                    if (vertex != null) {
                        if (vertex.getValue()
                                .equalsIgnoreCase(nodeA.getValue())) {
                            vertex.addEdge(nodeB.getValue(), weight);
                        } else if (vertex.getValue().equalsIgnoreCase(nodeB.getValue())) {
                            vertex.addEdge(nodeA.getValue(), nodeB.getValue(), weight);
                        }
                    }
                }
                numEdges++;
            }
        }

    }

    /**
     * Returns the edges incident on vertex <tt>v</tt>.
     *
     * @return the edges incident on vertex <tt>v</tt> as an Iterable
     * @param v the vertex
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < maxVertices
     */
    public Iterable<EdgeA3> adj(int v) {
        validateVertex(v);
        return adj[v].getEdges();
    }

    /**
     * Returns the degree of vertex <tt>v</tt>.
     *
     * @return the degree of vertex <tt>v</tt>
     * @param v the vertex
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < maxVertices
     */
    public int degree(int v) {
        validateVertex(v);
        return adj[v].getEdges().size();
    }

    /**
     * Returns all edges in the edge-weighted graph. To iterate over the edges
     * in the edge-weighted graph, use foreach notation:
     * <tt>for (Edge e : G.edges())</tt>.
     *
     * @return all edges in the edge-weighted graph as an Iterable.
     */
    public Iterable<EdgeA3> edges() {
        Bag<EdgeA3> list = new Bag<EdgeA3>();
        for (int v = 0; v < maxVertices; v++) {
            int selfLoops = 0;
            for (EdgeA3 e : adj(v)) {
                if (LMeths.stringGreaterThan(e.getNodeB().getValue(), v)) {
                    list.add(e);
                } // only add one copy of each self loop (self loops will be consecutive)
                else if (LMeths.stringsEqual(e.getNodeB().getValue(), v)) {
                    if (selfLoops % 2 == 0) {
                        list.add(e);
                    }
                    selfLoops++;
                }
            }
        }
        return list;
    }

    /**
     * Returns a string representation of the edge-weighted graph. This method
     * takes time proportional to <em>numEdges</em> + <em>maxVertices</em>.
     *
     * @return the number of vertices <em>maxVertices</em>, followed by the number of
     * edges <em>numEdges</em>, followed by the <em>maxVertices</em> adjacency lists of edges
     */
    public String toString() {
        
        StringBuilder s = new StringBuilder();
        
        s.append("Vertices ").append(numEdges/2).append("\n\n");
        for (int i = 0; i < 177; i++) {
            s.append("for ").append(i).append("*").append(adj[i]).append(LSystem.NEW_LINE);
            for (EdgeA3 e : adj[i].getEdges()) {
                s.append(e.toString());
            }
            s.append(LSystem.NEW_LINE);
        }
        return s.toString();
    }

}
