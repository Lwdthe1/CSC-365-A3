/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.Set;

/**
 *
 * @author Lwdthe1
 */
public interface SymbolKey<K,V> {
    /**
     * 
     * @param key the key to search the data storage by
     * @return returns the value associated with the key
     */
    V get(K key);
    /**
     * adds the object to data storage with a key and a value
     * @param key the key to associate with the value.
     * @param value the value to associate with the key
     */
    void put(K key, V value);
    /**
     * Deletes the element in the data storage corresponding to the key
     * @param key 
     */
    void delete(K key);
    /**
     * 
     * @return a Set of the keys in the data storage
     */
    Set<K> keySet();
    /**
     * 
     * @return the size of the data storage
     */
    int size();
}
