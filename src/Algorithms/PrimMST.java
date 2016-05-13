/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import Classes.EdgeA3C;
import Classes.GraphA3C;
import Classes.UF;
import Queues.PriorityQueueA3;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author Lwdthe1
 */
public class PrimMST {
    private ArrayList<EdgeA3C> edgeTo;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private Map<String, Double> distTo;      // distTo[v] = weight of shortest such edge
    private ArrayList<String> marked;     // marked[v] = true if v on tree, false otherwise
    private PriorityQueueA3 queue;

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     * @param G the edge-weighted graph
     */
    public PrimMST(GraphA3C G) {
        edgeTo = new ArrayList<>();
        distTo = new HashMap<>();
        marked = new ArrayList<>();
        queue = new PriorityQueueA3(G.V());
        
        for(Object e: G.edgeList()){
            EdgeA3C edge = (EdgeA3C) e;
            String v = edge.getNodeA();
            String w = edge.getOther(v);
            if(!distTo.containsKey(v)) distTo.put(v, Double.MAX_VALUE);
            if(!distTo.containsKey(w)) distTo.put(w, Double.MAX_VALUE);
        }
        
        for (Object v: G.getVerticesKeys()){
            // run from each vertex to find minimum spanning forest
            if (!marked.contains(v.toString())) prim(G, (String) v);      
        }

        // check optimality conditions
        assert check(G);
    }

    // run Prim's algorithm in graph G, starting from vertex s
    private void prim(GraphA3C G, String s) {
        distTo.put(s,0.0);
        queue.insert(s, distTo.get(s));
        while (!queue.isEmpty()) {
            String vertex = queue.removeMin();
            scan(G, vertex);
        }
    }

    // scan vertex v
    private void scan(GraphA3C G, String vertex) {
        marked.add(vertex);
        for (Object e : G.getAdjacencies(vertex)) {
            EdgeA3C edge = (EdgeA3C) e;
            String w = edge.getOther(vertex);
            if ( marked.contains(w)) continue; // v->w is already marked
            if (getWeight(edge) < distTo.get(w)) {
                distTo.put(w, getWeight(edge));
                edgeTo.add(edge);
                if (queue.contains(w)) 
                    queue.decreasePriority(w, distTo.get(w));
                else queue.insert(w, distTo.get(w));
            }
        }
    }

    /**
     * Returns the edges in a minimum spanning tree (or forest).
     * @return the edges in a minimum spanning tree (or forest) as
     *    an Iterable of edges
     */
    public Iterable<EdgeA3C> edges() {
        ArrayList<EdgeA3C> mst = new ArrayList<>();
        for (int v = 0; v < edgeTo.size(); v++) {
            EdgeA3C e = edgeTo.get(v);
            if (e != null) {
                mst.add(e);
            }
        }
        return mst;
    }

    /**
     * Returns the sum of the edge weights in a minimum spanning tree (or forest).
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double weight() {
        double weight = 0.0;
        for (EdgeA3C e : edges())
            weight += getWeight(e);
        return weight;
    }


    // check optimality conditions (takes time proportional to E V lg* V)
    public boolean check(GraphA3C G) {

        // check that it is acyclic
        UF uf = new UF(G.V());
        
        // check weight
        /*double totalWeight = 0.0;
        for (EdgeA3C e : edges()) {
            totalWeight += getWeight(e);
        }
        double EPSILON = 1E-12;
        if (Math.abs(totalWeight - weight()) > EPSILON) {
            System.out.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weight());
            return false;
        }

        
        for (EdgeA3C e : edges()) {
            String v = e.getNodeA(), w = e.getOther(v);
            if (uf.connected(v, w)) {
                System.out.println("Not a forest");
                return false;
            }
            
            uf.union(v, w);
        }

        // check that it is a spanning forest
        for (EdgeA3C e :edges()) {
            EdgeA3C edge = (EdgeA3C) e;
            String v = edge.getNodeA(), w = edge.getOther(v);
            if (!uf.connected(v, w)) {
                System.out.println("Not a spanning forest");
                return false;
            }
        }*/

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (EdgeA3C e : edges()) {

            // all edges in MST except e
            uf = new UF(G.V());
            for (EdgeA3C f : edges()) {
                String x = f.getNodeA(), y = f.getOther(x);
                if (f != e) uf.union(x, y);
            }

            // check that e is min weight edge in crossing cut
            for (EdgeA3C f : edges()) {
                String x = f.getNodeA(), y = f.getOther(x);
                if (!uf.connected(x, y)) {
                    if (getWeight(f) < getWeight(e)) {
                        System.out.println("Edge " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }

        return true;
    }

    public static double getWeight(EdgeA3C e) {
        //System.out.println(e +" \n"+(1/e.getWeight()));
        return 1/e.getWeight();
    }
}

