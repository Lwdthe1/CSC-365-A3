/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.io.Serializable;
import java.util.ArrayList;
import util.LMeths;

/**
 *
 * @author Lwdthe1
 */
public class NodeA3 implements Serializable{
    private String urlString;
    private ArrayList<EdgeA3> edges;
    private double distance;
    
    long visitMarker;
    static long lastVisited;
    
    public NodeA3(String urlString){
        this.urlString = urlString;
        this.edges = new ArrayList<>();
    }

    public NodeA3(String source, double distance) {
        this.urlString = source;
        this.edges = new ArrayList<>();
        this.distance = distance;
    }
    public void addEdge(String urlStringB, double weight){
        NodeA3 nodeA = this;
        NodeA3 nodeB = new NodeA3(urlStringB);
        edges.add(new EdgeA3(nodeA, nodeB, weight)); 
    }
    
    void addEdge(String urlStringA, String urlStringB, double weight) {
        NodeA3 nodeA = new NodeA3(urlStringA);
        NodeA3 nodeB = new NodeA3(urlStringB);
        edges.add(new EdgeA3(nodeA, nodeB, weight)); 
    }
    
    void setEdges(ArrayList<EdgeA3> arrayList) {
        this.edges = arrayList;
    }
    
    public void setDistance(int distance){ this.distance = distance;}
    public String getValue(){ return urlString;}
    public double getDistance(){ return distance;}
    public ArrayList<EdgeA3> getEdges(){ return edges;}
    
    @Override
    public String toString(){
        StringBuilder edgesS = new StringBuilder();
        if(edges!=null){
            for(EdgeA3 edge: edges) 
                edgesS.append("   ").append(edge.toString()).append("\n");
        }
        return LMeths.formatS("[\n %s\n%s \n]", urlString, edgesS);
    }

    

    
}
