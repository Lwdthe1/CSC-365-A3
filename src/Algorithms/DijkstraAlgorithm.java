/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

/**
 *
 * @author Lwdthe1
 */
import Classes.EdgeA3C;
import Classes.GraphA3C;
import Queues.PriorityQueueA3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DijkstraAlgorithm {

    private final List<String> nodes;
    private final List<EdgeA3C> edges;
    private Set<String> settledNodes;
    private Set<String> unSettledNodes;
    private Map<String, Double> distances;
    private Map<String, String> predecessors;

    public DijkstraAlgorithm(GraphA3C graph) {
        // create a copy of the array so that we can operate on this array
        this.nodes = new ArrayList<>(graph.getVerticesKeys());
        this.edges = new ArrayList<>(graph.edgeList());
        initDataSets();
    }

    public void initDataSets() {
        settledNodes = new HashSet<>();
        unSettledNodes = new HashSet<>();
        distances = new HashMap<>();
        predecessors = new HashMap<>();
    }

    /**
     * create distances from source to all vertices in graph
     *
     * @param source
     */
    public void execute(String source) {
        initDataSets();
        distances.put(source, 0.0);
        //initiate the unSettledNodes with the source
        unSettledNodes.add(source);
        //while the unsettledNodes is not empty
        while (unSettledNodes.size() > 0) {
            //get the minimum 
            String node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            String next = findMinimalDistances(node);
            if (next != null) {
                unSettledNodes.add(next);
            }
        }
    }

    public boolean perform(String source) {
        initDataSets();
        PriorityQueueA3 queue = new PriorityQueueA3(nodes.size());
        distances.put(source, 0.0);
        queue.insert(source, 0);

        while (queue.size() != 0) {
            String node = queue.removeMin();
            settledNodes.add(node);
            
            List<String> adjacentNodes = getNeighbors(node);
            for(String target: adjacentNodes){
                if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                    distances.put(target,
                            getShortestDistance(node) + getDistance(node, target));
                    predecessors.put(target, node);
                    if (queue.contains(target)) 
                        queue.decreasePriority(target, distances.get(target));
                    else queue.insert(target, getShortestDistance(node) + getDistance(node, target));
                    //unSettledNodes.add(target);
                }
            }
        }
        
        return distances.size()>1;
    }

    public Set<String> getSettledNodes() {
        return settledNodes;
    }

    public Map<String, String> getPreds() {
        return predecessors;
    }
    
    public Iterable<String> edges() {
        ArrayList<String> mst = new ArrayList<>();
        int count = 0;
        for (int v = 0; v < settledNodes.size(); v++) {
            String e = settledNodes.toArray()[v].toString();
            if (e != null) {
                mst.add(e);
            } else {
                count++;
                mst.add("########"+count+" trees#########");
            }
        }
        return mst;
    }

    /**
     * get the minimal distances from the node to its adjacent nodes add the
     * node's adjacent nodes to the unSettledNodes list if they aren't already
     * there
     *
     * @param node the source node to get distances from
     */
    private String findMinimalDistances(String node) {
        List<String> adjacentNodes = getNeighbors(node);
        for (String target : adjacentNodes) {
            if (getShortestDistance(target)
                    > getShortestDistance(node) + getDistance(node, target)) {
                distances.put(target,
                        getShortestDistance(node) + getDistance(node, target));
                predecessors.put(target, node);
                return target;
            }
        }
        return null;
    }

    /**
     *
     * @param source
     * @param destination
     * @return distance from source to the destination in weighted graph
     */
    private double getDistance(String source, String destination) {
        double distance = Integer.MAX_VALUE;
        for (EdgeA3C edge : edges) {
            if (edge.getNodeA().equals(source)
                    && edge.getNodeB().equals(destination)) {
                distance = 1/edge.getWeight();
            }
        }
        return distance;
    }

    /**
     *
     * @param vertex
     * @return the unsettled neighbors of the vertex
     */
    private List<String> getNeighbors(String vertex) {
        List<String> neighbors = new ArrayList<>();
        for (EdgeA3C edge : edges) {
            if (//if the edge's source vertex is the provided vertex    
                edge.getNodeA().equals(vertex) 
                //and if the edge's destination node is not settled 
                && ( !isSettled(edge.getNodeB())
                //or its distance is less than the current stored distance
                || distances.get(edge.getNodeB()) > 
                getShortestDistance(edge.getNodeB()) + getDistance(vertex, edge.getNodeB()) )
                ) {
                //add the node to the adjancency for the provided vertex
                neighbors.add(edge.getNodeB());
            }
        }
        return neighbors;
    }

    /**
     * @param vertexes
     * @return the node with the minimal edge weight from the
     */
    private String getMinimum(Set<String> vertexes) {
        String minimum = null;
        for (String vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    /**
     *
     * @param vertex
     * @return true if the vertex is in the list of settledNodes
     */
    private boolean isSettled(String vertex) {
        return settledNodes.contains(vertex);
    }

    /**
     *
     * @param destination
     * @return shortest distance in the precompiled list of distances from the
     * root of the graph
     */
    private double getShortestDistance(String destination) {
        Double d = distances.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

    /*
     * returns the path 
     * from the graph's predetermined source to the selected target
     * @return path from source to provided {@code destination} 
     * and null if no path
     */
    public LinkedList<String> getShortestPath(String destination) {
        LinkedList<String> path = new LinkedList<>();
        //this will be the end of the path after it's reversed
        String step = destination;
        // check if a path exists. if none, return null
        if (predecessors.get(step) == null) {
            return null;
        }

        //add the step to the path
        path.add(step);
        //while the vertex has predecessors
        while ((step = predecessors.get(step)) != null) {
            //step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }

}
