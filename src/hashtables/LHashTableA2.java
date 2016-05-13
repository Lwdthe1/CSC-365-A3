/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashtables;

import Objects.LWebsite;
import frames.SystemActivityFrame;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import util.LMeths;

/**
 * A linear probing Hash table
 * @author Lwdthe1
 */
public class LHashTableA2<K,V> implements Map {
    private static final int MIN_CAPACITY = 11;//the minimum size of the key/value pair array. Array won't downsize when smaller than this

    private Entry[] elements; // the array holding all the key/value pairs
    private int size; // the current number of elements
    private int capacity; // the current capacity of the array
    
    private double max; // determines how full the array can get before resizing. default 1/2
    private double min; // determines how empty the array can get before resizing. default 3/4
    private double fullSize; // determines how full the array should be made when resizing; default 1/4
    
    /**
     * The primary constructor
     * @param max how full the array can get before resizing. default 1/2
     * @param min how empty the array can get before resizing. default 3/4
     * @param fullSize how full the array should be made when resizing; default 1/4
     */
    public LHashTableA2( double max, double min, double fullSize ) {
        this.size = 0;
        this.capacity = MIN_CAPACITY;
        this.max = max;
        this.min = min;
        this.fullSize = fullSize;
        
        this.elements = (Entry[]) Array.newInstance(Entry.class, this.capacity); // make the new array to hold key/value pairs
    }
    
    /**
     * Secondary constructor that defaults {@code fullSize} to .5
     * {@code fullSize} is how full the array should be made when resizing to;
     * @param max how full the array can get before resizing. default 1/2
     * @param min how empty the array can get before resizing. default 3/4
     */
    public LHashTableA2 ( double max, double min ) {
        this(max, min, .5);
    } 
    
    /**
     * tertiary constructor
     * defaults the max and min to .75 and .25 respectively
     */
    public LHashTableA2 () {        
        this(.75, .25);
    }
    
    
    /**
     * Primary hash function
     * @param key the key being passed in in the key/value pair Entry
     * @return the absolute value of the {@code key} Object 
     * in order to avoid negative indices when accessing data storage
     */
    public int hash(Object key){
        return modCapacity( Math.abs(key.hashCode()) );
    }
    
    /**
     * Secondary hash function
     * @param key the key being passed in in the key/value pair Entry
     * @param newCapacity the new capacity of the data storage which is being resized
     * @return the absolute value of the {@code key} Object 
     * in order to avoid negative indices when accessing data storage
     */
    public int resizeHash(Object key, int newCapacity){
        return modNewCapacity( Math.abs(key.hashCode()), newCapacity );
    }
    
    public int modCapacity(int keyHashCode){
        return keyHashCode % capacity;
    }
    
    public int modNewCapacity(int keyHashCode, int newCapacity){
        return keyHashCode % newCapacity;
    }
    
    public double maxSize(){
        return capacity*max;
    }
    
    public double minSize(){
        return capacity*min;
    }
    
    public boolean resizeNeeded(){
        return ( size < minSize() ) || size > maxSize();
    }

    
    @Override
    public Object put(Object key, Object value) {       
        int i = hash(key);
        //get the index of the object {@code Entry} to the key
        i = getKeyIndex(i, key);
        
        if ( elements[i] == null ) {
            /* if new object is being added to the data storage
             * increase the size variable of the data storage
             */
            size++;
        }
        
        //store the new object at the computed index in the data storage
        elements[i] = new Entry(key, value);
        
        resize(); // if needed, resize the data storage
        return null;
    }
    
    /**
     * Puts the key/value pair into the data storage as an Entry.
     * This is best for keeping track of {@code key} frequency
     * by incrementing {@code value}
     * @param key
     * @param value is the amount you want to increment the frequency by
     * @return 
     */
    public Object putAdditional(Object key, int value) {
        //frequency of insertion of this key
        int frequency = 0;
        
        int i = hash(key);
        //get the index of the object {@code Entry} to the key
        i = getKeyIndex(i, key);
        
        if ( elements[i] == null ) {
            /* this key is not currently in the data storage, so it's new
             * if new object is being added to the data storage
             * increase the size variable of the data storage
             */
            size++;
        } else {
            frequency = (int)elements[i].frequency + 1;
        }
        
        //store the new object at the computed index in the data storage
        elements[i] = new Entry(key, value, frequency);
        
        resize(); // if needed, resize the data storage
        return null;
    }
    
    public void resize(){
        if( !resizeNeeded() ){ return; } 
        
        // determin size of the new datat storage
        int newCapacity = (int) (size/fullSize); 

        //make the new data storage
        @SuppressWarnings("uncheck")
        Entry[] newElements = (Entry[]) Array.newInstance(Entry.class, newCapacity);

        addAllElementsToNewDataStorage(newCapacity, newElements);
        
        /* finalize the resize
         * by reinitializing 
         * the elements data storage
         * and the data storage capacity
        */
        this.elements = newElements;
        this.capacity = newCapacity;
    }
    
    private int getKeyIndex(int i, Object key) {
        while ( elements[i]!=null && !key.equals(elements[i].key) ) {
            /* while the search key does not equal the current key at index i
            * increase the index of which to search from
            */
            i = modCapacity( i + 1 );
        }
        return i;
    }

    private void addAllElementsToNewDataStorage(int newCapacity,
            Entry[] newElements) {
        /* iterate over the elements of the current data storage
        and add each one to the new data storage
        */
        for (int j = 0; j<capacity; j++){
            Entry currentElement = elements[j];

            if( currentElement == null ){
                /* if the currentElement is null
                * the rest of this loop iteration will be skipped
                * and the loop's boolean expression will be evaluated
                */
                continue; // as explained above, no code below this line will be executed   
            }

            //the new hash of the current element for the new data storage
            int i = resizeHash(currentElement.key, newCapacity); 
            i = getResizeKeyIndex(newElements, i, currentElement, newCapacity);
            //insert the currentElement into the new data storage at the correct index
            newElements[i] = currentElement;
        }
    }

    /**
     *
     * @param newElements
     * @param i
     * @param currentElement
     * @param newCapacity
     * @return
     */
    private int getResizeKeyIndex(Entry[] newElements,
            int i, Entry currentElement, int newCapacity) {
        while ( newElements[i] != null && 
                !currentElement.key.equals(newElements[i].key) ) {
            //get the next index to access the new data storage
            i = modNewCapacity( i+1, newCapacity );
        }
        return i;
    }
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size<1;
    }

    @Override
    public boolean containsKey(Object key) {
        return this.get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return this.values().contains(value);
    }
    
    @Override
    public void putAll(Map map) {
        map.entrySet();
        
        for(int i = 0; i<map.size(); i++){
            Object objectKey = map.keySet().toArray()[i];
            Object objectValue = map.values().toArray()[i];
            this.put(objectKey, objectValue);
        }
    }

    @Override
    public void clear() {
        elements = (Entry[]) Array.newInstance(Entry.class, this.capacity);
    }

    @Override
    public Set keySet() {
        Set<Object> keySet = new HashSet<>(size);
        for ( Entry e: elements ) {
            if( e!= null ){
                keySet.add(e.key);
            }
        }
        
        return keySet;
    }

    @Override
    public Collection values() {
        LinkedList<Object> valueCollection = new LinkedList<>();
        for ( Entry e: elements) {
            if ( e != null ) {
                valueCollection.add(e.value);
            }
        }
        return valueCollection;
    }

    @Override
    public Set entrySet() {
        Set<Entry> entrySet = new HashSet<>();
        for(Entry e: elements){
            entrySet.add(e);
        }
        return entrySet;
    }

    @Override
    public Object get(Object key) {
        //find the key's hash
        int i = hash(key);
        //get the index of the object {@code Entry} to the key
        i = getKeyIndex(i, key);
        
        /* check if the element at the index at which the 
         * search key would be held is null; if it is null, return null,
         * if it's not null, return the value of the element at the index
         */
        return elements[i] == null? null : elements[i].value;
    }

    

    @Override
    public Object remove(Object key) {
        ArrayList<Entry> entries = new ArrayList<>();
        
        int i = hash(key);
        //get the index of the object {@code Entry} to the key
        i = getKeyIndex(i, key);
           
        //remove all the keys that may have been "forced over" by this key
        while ( elements[i] != null ) {
            entries.add(elements[i]); // add current element to the entries list
            elements[i] = null; // set the element at index i of the data storage to null
            size--; // decrement the size of the data storage
            i = modCapacity( i+1 ); // get next index
        }
        
        //remove the element we set out to
        Entry entry = entries.remove(0);
        
        /* put the rest of the elements back into the datastorage
         * using it's own put method
        */
        for(Entry e: entries){
            this.put(e.key, e.value);
        }
        
        return entry;
    }
    
    public void scanInWords(String allWebSiteText) {
        Scanner wordReader = new Scanner(allWebSiteText);
        while (wordReader.hasNext()) {
            String word = wordReader.next().replaceAll("\\s", "");
            word = LMeths.filterWord(word);
            if ( LMeths.isAWord(word) ) this.putAdditional(word, 1);
        }
    }
    
    public static LWebsite compare(LHashTable baseHashTable, LWebsite compareToWebsite, SystemActivityFrame searchSystemActivityFrame) {
        
        searchSystemActivityFrame.appendActivity("\n***PARSING " +
                compareToWebsite.getUrlString() + "***\n");
        int wordsSimilarity = 0;
        int mostFrequentSimilarWordFrequency = 0;
        String mostFrequentSimilarWord = "";
        ArrayList<String> compareToWords = compareToWebsite.getWords();
        for(String compareToWord: compareToWords){
            if( baseHashTable.get(compareToWord) != null ){
                int wordFrequency = (int) baseHashTable.get(compareToWord);
                wordsSimilarity+= wordFrequency;
                searchSystemActivityFrame.appendActivity(
                    "\n#MATCH    [ " + compareToWord + ": " + wordFrequency +  " ]    " + wordsSimilarity);
            }
        }

        searchSystemActivityFrame.prependActivity("{\n" +
                compareToWebsite.getUrlString() + " \n" +
                " similarity points: " + wordsSimilarity +
                "\n}");
        
        LWebsite returnComparedWebsite = new LWebsite(
            compareToWebsite.getWordsHistogram(),
            compareToWebsite.getUrlString(), 
            wordsSimilarity,
            mostFrequentSimilarWord,
            mostFrequentSimilarWordFrequency);
                
        return returnComparedWebsite;
    }
    
    @Override
    public String toString(){
        return this.values().toString();
    }
    
    private class Entry {
        Object key;
        Object value;
        int frequency = 0;
        
        public Entry( Object key, Object value, int frequency) {
            this.key = key;
            this.value = value;
            this.frequency = frequency;
        }

        private Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }
    }    
}