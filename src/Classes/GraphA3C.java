/*
 * Directed graph implementation for CSC 365 Assignment 3
 */
package Classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Lwdthe1
 */
import java.io.IOException;
import java.util.*;

public class GraphA3C<V> {

    /**
     * A Map is used to map each vertex to its list of adjacent vertices.
     */
    private Map<V, List<EdgeA3C>> vertices = new HashMap<>();

    /**
     * Add a vertex to the graph. Nothing happens if vertex is already in graph.
     */
    public void add(V vertex) {
        if (vertices.containsKey(vertex))
            return;
        vertices.put(vertex, new ArrayList<>());
    }

    public int getNumberOfEdges(){
        int sum = 0;
        for(List<EdgeA3C> outBounds : vertices.values()){
            sum += outBounds.size();
        }
        return sum;
    }

    /**
     * True if graph contains vertex.
     */
    public boolean contains(V vertex) {
        return vertices.containsKey(vertex);
    }
    
    public int V(){
        return this.getVerticesKeys().size();
    }

    /**
     * Add an edge to the graph; if either vertex does not exist, it's added.
     * This implementation allows the creation of multi-edges and self-loops.
     * @param source
     * @param destination
     * @param weight
     */
    public void addEdge(V source, V destination, int weight) {
        this.add(source);
        this.add(destination);
        getAdjacencies(source).add(new EdgeA3C((String) source,(String) destination, weight));
    }
    
    public void addEdges(Iterable<EdgeA3C> edges) {
        for(EdgeA3C edge: edges){
            V source = (V) (((EdgeA3C) edge).getNodeA());
            V destination = (V) (((EdgeA3C) edge).getNodeB());
            int weight =  (int) ((EdgeA3C) edge).getWeight();
            this.add(source);
            this.add(destination);
            getAdjacencies(source).add(new EdgeA3C((String) source,(String) destination, weight));
        }
    }
    
    /**
     * @param vertex
     * @return the adjacent edge connected vertices to a vertex 
     */
    public List<EdgeA3C> getAdjacencies(V vertex){
        return vertices.get(vertex);
    }
    
    public Set<V> getVerticesKeys(){
        return vertices.keySet();
    }
    
    public List<EdgeA3C> edgeList(){
        List<EdgeA3C> edges = new ArrayList<>();
        for (V vertex: getVerticesKeys()) {
            List<EdgeA3C> vEdges = getAdjacencies(vertex);
            for(EdgeA3C vEdge: vEdges) edges.add(vEdge);
        }
        return edges;
    }

    public int outDegree(V vertex) {
        return getAdjacencies(vertex).size();
    }
    
    public int inDegree(V vertex) {
       return inboundNeighbors(vertex).size();
    }

    public List<V> outboundNeighbors(V vertex) {
        List<V> list = new ArrayList<>();
        for(EdgeA3C e: getAdjacencies(vertex))
            list.add((V) e.getNodeB());
        return list;
    }

    public List<V> inboundNeighbors(V vertex) {
        List<V> inList = new ArrayList<>();
        //for each vertex in the graph
        for (V destination : getVerticesKeys()) {
            //for each of the vertex's edges
            for (EdgeA3C edge : getAdjacencies(destination))
                // if its edge's outbound vertex is the same as the provided vertex
                // this is an inbound vertex (inbound neighbor) of the provided vertex
                if (edge.getNodeB().equals(vertex))
                    inList.add(destination);
        }
        return inList;
    }

    public boolean isEdge(V from, V to) {
      for(EdgeA3C e :  getAdjacencies(from)){
          if(e.getNodeB().equals(to))
              return true;
      }
      return false;
    }

    public double getWeight(V from, V to) {
        for(EdgeA3C e :  getAdjacencies(from)){
            if(e.getNodeB().equals(to))
                return e.getWeight();
        }
        return -1;
    }
    
    /**
     * String representation of graph.
     * @return 
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        getVerticesKeys().stream().forEach((v) -> {
            s.append("\n    ").append(v).append(" -> ").append(getAdjacencies(v));
        });
        return s.toString();
    }
    
    public String outDegrees() {
        StringBuilder s = new StringBuilder();
        getVerticesKeys().stream().forEach((v) -> {
            s.append("\n    ").append(v).append(" -> ").append(getAdjacencies(v).size());
        });
        return s.toString();
    }
    
    public String inDegrees() {
        StringBuilder s = new StringBuilder();
        getVerticesKeys().stream().forEach((v) -> {
            s.append("\n    ").append(v).append(" -> ").append(inboundNeighbors(v).size());
        });
        return s.toString();
    }

    public static void main(String[] args) throws IOException {
        GraphA3C<String> graph = new GraphA3C<>();

        ArrayList<EdgeA3C> edges = new ArrayList<>();
        /*edges.add(new EdgeA3C(new NodeA3("google.com"), new NodeA3("all"), 1));
        edges.add(new EdgeA3C(new NodeA3("all"), new NodeA3("cat"), 2));
        edges.add(new EdgeA3C(new NodeA3("cat"), new NodeA3("dog"), 2));
        edges.add(new EdgeA3C(new NodeA3("dog"), new NodeA3("google.com"), 2));
        edges.add(new EdgeA3C(new NodeA3("all"), new NodeA3("dog"), 1));
        edges.add(new EdgeA3C(new NodeA3("cat"), new NodeA3("all"), 5));*/
        graph.addEdges(edges);
//        graph.addEdge("google.com", "all", 1);
//        graph.addEdge("all", "cat", 2);
//        graph.addEdge("cat", "dog", 2);
//        graph.addEdge("dog", "google.com", 2);
//        graph.addEdge("all", "dog", 1);
//        graph.addEdge("cat", "all", 5);

        System.out.println("The no. of vertices is: " + graph.getVerticesKeys().size());
        System.out.println("The no. of edges is: " + graph.getNumberOfEdges()); // to be fixed
        System.out.println("The current graph: " + graph);
        System.out.println("In-degrees for google.com: " + graph.inDegree("google.com"));
        System.out.println("Out-degrees for google.com: " + graph.outDegree("google.com"));
        System.out.println("In-degrees for all: " + graph.inDegree("all"));
        System.out.println("Out-degrees for all: " + graph.outDegree("all"));
        System.out.println("In-degrees for dog: " + graph.inDegree("dog"));
        System.out.println("Out-degrees for dog: " + graph.outDegree("dog"));
        System.out.println("Outbounds for all: "+ graph.outboundNeighbors("all"));
        System.out.println("Inbounds for all: "+ graph.inboundNeighbors("all"));
        System.out.println("(google.com,cat)? " + (graph.isEdge("google.com", "cat") ? "It's an edge" : "It's not an edge"));
        System.out.println("(dog,all)? " + (graph.isEdge("dog", "all") ? "It's an edge" : "It's not an edge"));

        System.out.println("Weight for (cat,all)? "+ graph.getWeight("cat", "all"));
        
        System.out.println("Graph:\n" + graph.toString());
    }
}