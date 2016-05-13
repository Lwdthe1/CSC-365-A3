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

public class GraphA3B<V> {

    public static class Edge<V>{
        private V vertex;
        private double weight;

        public Edge(V v, int w){
            vertex = v; weight = w;
        }

        public V getVertex() {
            return vertex;
        }

        public double getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return "Edge {vertex = " + vertex + ", weight = " + weight + "}\n";
        }

    }

    /**
     * A Map is used to map each vertex to its list of adjacent vertices.
     */
    private Map<V, List<Edge<V>>> vertices = new HashMap<>();

    private int numEdges;

    

    /**
     * Add a vertex to the graph. Nothing happens if vertex is already in graph.
     */
    public void add(V vertex) {
        if (vertices.containsKey(vertex))
            return;
        vertices.put(vertex, new ArrayList<Edge<V>>());
    }

    public int getNumberOfEdges(){
        int sum = 0;
        for(List<Edge<V>> outBounds : vertices.values()){
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

    /**
     * Add an edge to the graph; if either vertex does not exist, it's added.
     * This implementation allows the creation of multi-edges and self-loops.
     * @param fromNode
     * @param toNode
     * @param weight
     */
    public void addEdge(V fromNode, V toNode, int weight) {
        this.add(fromNode);
        this.add(toNode);
        vertices.get(fromNode).add(new Edge<>(toNode, weight));
    }
    
    public void addEdges(Iterable<EdgeA3> edges) {
        for(EdgeA3 edge: edges){
            V fromNode = (V) (((EdgeA3) edge).getNodeA()).getValue();
            V toNode = (V) (((EdgeA3) edge).getNodeB()).getValue();
            int weight =  (int) ((EdgeA3) edge).getWeight();
            this.add(fromNode);
            this.add(toNode);
            vertices.get(fromNode).add(new Edge<>(toNode, weight));
        }
    }

    public int outDegree(V vertex) {
        return vertices.get(vertex).size();
    }
    

    public int inDegree(V vertex) {
       return inboundNeighbors(vertex).size();
    }

    public List<V> outboundNeighbors(V vertex) {
        List<V> list = new ArrayList<>();
        for(Edge<V> e: vertices.get(vertex))
            list.add(e.vertex);
        return list;
    }

    public List<V> inboundNeighbors(V inboundVertex) {
        List<V> inList = new ArrayList<>();
        for (V toNode : vertices.keySet()) {
            for (Edge edge : vertices.get(toNode))
                if (edge.vertex.equals(inboundVertex))
                    inList.add(toNode);
        }
        return inList;
    }

    public boolean isEdge(V from, V to) {
      for(Edge<V> e :  vertices.get(from)){
          if(e.vertex.equals(to))
              return true;
      }
      return false;
    }

    public double getWeight(V from, V to) {
        for(Edge<V> e :  vertices.get(from)){
            if(e.vertex.equals(to))
                return e.weight;
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
        vertices.keySet().stream().forEach((v) -> {
            s.append("\n    ").append(v).append(" -> ").append(vertices.get(v));
        });
        return s.toString();
    }
    
    public String outDegrees() {
        StringBuilder s = new StringBuilder();
        vertices.keySet().stream().forEach((v) -> {
            s.append("\n    ").append(v).append(" -> ").append(vertices.get(v).size());
        });
        return s.toString();
    }
    
    public String inDegrees() {
        StringBuilder s = new StringBuilder();
        vertices.keySet().stream().forEach((v) -> {
            s.append("\n    ").append(v).append(" -> ").append(inboundNeighbors(v).size());
        });
        return s.toString();
    }

    public static void main(String[] args) throws IOException {
        GraphA3B<String> graph = new GraphA3B<>();

        ArrayList<EdgeA3> edges = new ArrayList<>();
        edges.add(new EdgeA3(new NodeA3("google.com"), new NodeA3("all"), 1));
        edges.add(new EdgeA3(new NodeA3("all"), new NodeA3("cat"), 2));
        edges.add(new EdgeA3(new NodeA3("cat"), new NodeA3("dog"), 2));
        edges.add(new EdgeA3(new NodeA3("dog"), new NodeA3("google.com"), 2));
        edges.add(new EdgeA3(new NodeA3("all"), new NodeA3("dog"), 1));
        edges.add(new EdgeA3(new NodeA3("cat"), new NodeA3("all"), 5));
        graph.addEdges(edges);
//        graph.addEdge("google.com", "all", 1);
//        graph.addEdge("all", "cat", 2);
//        graph.addEdge("cat", "dog", 2);
//        graph.addEdge("dog", "google.com", 2);
//        graph.addEdge("all", "dog", 1);
//        graph.addEdge("cat", "all", 5);

        System.out.println("The no. of vertices is: " + graph.vertices.keySet().size());
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