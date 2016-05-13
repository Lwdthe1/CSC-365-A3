/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Collection;

/**
 *
 * @author Lwdthe1
 */
public class LIterator {
    
    public static void print(Collection c){
        for(Object o: c){
            System.out.println(o);
        }
    }
    
}
