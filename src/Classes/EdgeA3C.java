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
public class EdgeA3C implements Serializable{
    private String nodeA, nodeB;
    //the similarity between the two sites
    private double weight;
    
    public EdgeA3C(String nodeA, String nodeB, double weight){
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.weight = weight;
    }
    
    public EdgeA3C( String nodeB, double weight){
        this.nodeB = nodeB;
        this.weight = weight;
    }
    
    void setNodeA(String node) { this.nodeA = node; }
    void setNodeB(String node) { this.nodeB = node; }
    
    public String getNodeA(){ return nodeA; }
    public String getNodeB(){ return nodeB; }
    
    public double getWeight(){
        return weight;
    }
    
    @Override
    public String toString(){
        return LMeths.formatS("%s --"+weight+"--> %s", nodeA, nodeB);
    }

    public String getOther(String v) {
        if(nodeA.equals(v)) return nodeB;
        else return nodeA;
    }

    
    
}
