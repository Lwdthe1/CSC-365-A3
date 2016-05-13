/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.io.Serializable;
import util.LMeths;

/**
 *
 * @author Lwdthe1
 */
public class EdgeA3<V> implements Serializable{
    private NodeA3 nodeA, nodeB;
    //the similarity between the two sites
    private double weight;
    private double vertexWeight;
    
    public EdgeA3(NodeA3 nodeA, NodeA3 nodeB, double weight){
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.weight = 1/weight;
        this.vertexWeight = Double.POSITIVE_INFINITY;
    }
    
    public EdgeA3( NodeA3 nodeB, double weight){
        this.nodeB = nodeB;
        this.weight = weight;
    }
    
    void setNodeA(NodeA3 node) { this.nodeA = node; }
    void setNodeB(NodeA3 node) { this.nodeB = node; }
    
    public NodeA3 getNodeA(){ return nodeA; }
    public NodeA3 getNodeB(){ return nodeB; }
    
    public double getWeight(){
        return weight;
    }
    
    @Override
    public String toString(){
        return LMeths.formatS("%s --%d--> %s", nodeA.getValue(), weight, nodeB.getValue());
    }

    
    
}
