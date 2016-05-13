package btrees;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.LMeths;
import util.LRaf;

public class LBTreeA2<Key extends Comparable<Key>, Value> {

    //max amount of children per BTree node = {@code MAX} -1;

    public static final int MAX = 4;

    //the root of the BTree
    private Node root;
    //height of the BTree
    private int bTreeHeight;
    //number of key-value pairs in are in the BTree
    private int bTreeNumKeys;
    //arraylist of the keys in the BTree
    ArrayList<String> bTreeKeys = new ArrayList<>();

    /**
     * helper BTree node class
     */
    private static final class Node {
        //number of children the node has
        private int numElements;
        //array of children the node has
        private final Entry[] elements = new Entry[MAX];
        //points to next node in cache file
        private long pointer;

        //construct a node with k children

        private Node(int k) {
            numElements = k;
        }
    }

    /**
     * internal nodes: only use key and next external nodes: only use key and
     * value
     */
    private static class Entry {

        private Comparable key;
        private int frequency = 1;
        private final Object pageUrl;
        private Node next;// the next node after this one

        public Entry(Comparable key, Object value, Node next) {
            this.key = key;
            this.pageUrl = value;
            this.next = next;
        }
    }

    /**
     * BTree constructor
     */
    public LBTreeA2() {
        root = new Node(0);
    }
    
    /**
     * creates a BTree and initializes its random access file with that provided
     * @param raf 
     */
    public LBTreeA2(LRaf raf) {
        root = new Node(0);
    }
    public void setRaf(LRaf raf){
    }
    

    /**
     * return number of key-value pairs are in the BTree
     *
     * @return
     */
    public int size() {
        return bTreeNumKeys;
    }

    /**
     * return height of the BTree
     *
     * @return
     */
    public int height() {
        return bTreeHeight;
    }

    /**
     * return height of the BTree
     *
     * @return
     */
    public ArrayList<String> keys() {
        return bTreeKeys;
    }

    /**
     * *********************************************************************************************
     */
    /**
     * **************************FUNCTIONS FOR INSERTING INTO BTREE*********************************
     */
    /**
     * *********************************************************************************************
     */
    /**
     * public method to call insert() inserts key-value pair into tree TODO: add
     * check for duplicate key completes split operation if need be
     *
     * @param key
     * @param value
     * @return return true if the node already existed in tree
     */
    public boolean put(Key key, Value value) {
        /**
         * insert the key-value pair the return value, a node or null, will tell
         * us if we need to split the root insertion will return null if tree
         * does not need to be split: exit insertion will return the node where
         * the key-value was inserted if tree need s to be split
         */
        if (get(key) == null) {
            //the key doesn't already exist in the tree
            Node insertionResultNode = insert(root, key, value, bTreeHeight);
            bTreeKeys.add(key.toString());
            bTreeNumKeys++;
            if (insertionResultNode == null) {
                //no need to split. END INSERTION
                return false;
            }

            
            splitRootNode(insertionResultNode);
            return false;
        } else {
            //increment the frequency
            incrementFrequency(key);
            return true;
        }
    }

    /**
     * simply calls the actual put method with the passed in key and value to
     * insert the entry. then calls the setFrequecy method to set the frequency
     * of the entry we just inserted
     *
     * @param key
     * @param value
     * @param frequency
     */
    public void putFrequency(Key key, Value value, int frequency) {
        this.put(key, value);
        setFrequency(key, frequency);
    }

    /**
     * inserts a key-value pair into the tree starting at the head node
     */
    private Node insert(Node insertionNode, Key key, Value value, int treeHeight) {
        int insertionPosition = 0;
        Entry newEntry = new Entry(key, value, null);

        /**
         * if tree's height is 0, the insertion node (current node) has no
         * children the insertion node (current node) is an *external node*
         *
         * else, if the tree's height is greater than 0, the insertion node
         * (current node) has children the insertion node (current node) is an
         * *internal node*
         */
        if (treeHeight == 0) {
            /**
             * Node (tree) doesn't have children: only 1 level (height of 0)
             * start the insertion position at 0 for the following for loop
             * nodeNumChildren will be MAX-1. MAX = 4 as of 3/16/15 so 0 <=
             * nodeNumChildren <= 3. insrease insertion position until the
             * insertion new entry's key is less than the key of the entry at
             * the current insertion position of the node
             */
            for (insertionPosition = 0; insertionPosition < insertionNode.numElements; insertionPosition++) {
                /**
                 * check IF the new entry's key is less than the key at the
                 * current insertion position in the insertion node
                 *
                 * if so, break: stop incrementing insertion position
                 */
                if (LMeths.stringLessThan(key, insertionNode.elements[insertionPosition].key)) {
                    //stop incrementing insertion position
                    break;
                }
            }
        } else if (treeHeight > 0) {
            /**
             * Node (tree) has children: Internal Node start the insertion
             * position at 0
             *
             */
            for (insertionPosition = 0; insertionPosition < insertionNode.numElements; insertionPosition++) {
                /**
                 * check IF the insertion position is at the final child of the
                 * node (insertionPosition+1 == MAX-1) else if the new entry's
                 * key is less than the key of the entry at the current
                 * insertion position in the insertion node
                 *
                 * if so, go up a level in the tree (decrement tree's height by
                 * 1) and recursively call insert
                 */
                if ((insertionPosition + 1 == insertionNode.numElements
                        || LMeths.stringLessThan(key, insertionNode.elements[insertionPosition + 1].key))) {
                    /*
                     recursively insert the new entry's key and value at lower height 
                     with the insertionNode's next child's {@code next} node as the insertionNode
                     */
                    Node insertionResultNode = insert(insertionNode.elements[insertionPosition++].next, key, value, treeHeight - 1);

                    if (insertionResultNode == null) {
                        //the insertion process is complete. return the result
                        return null;
                    }
                    newEntry.key = insertionResultNode.elements[0].key;
                    newEntry.next = insertionResultNode;
                    break;
                }
            }
        }

        startInsertionAtFinalNodePosition(insertionNode, insertionPosition, newEntry);

        return splitNodeIfNeeded(insertionNode);
    }

    private void startInsertionAtFinalNodePosition(Node insertionNode, int insertionPosition, Entry newEntry) {
        shiftInsertionNodeElementsRight(insertionNode, insertionPosition);

        insertTheElement(insertionNode, insertionPosition, newEntry);
    }    

    private void shiftInsertionNodeElementsRight(Node insertionNode, int insertionPosition) {
        /**
         * shift the entries of the insertion node right i.e. if the insertion
         * node has 3 elements & insertion position is 2.  element 4 becomes element 3
         * (element at index 3 becomes element at index 2) in the insertion node
         * [a][b][d][] => [a][b][d][d]
         */
        for (int i = insertionNode.numElements; i > insertionPosition; i--) {
            insertionNode.elements[i] = insertionNode.elements[i - 1];
        }
    }
    
    private void insertTheElement(Node insertionNode, int insertionPosition, Entry newEntry) {
        /**
         * put the new entry in its final insertion position i.e. if the
         * insertion node has 3 children & insertion position is max/2 after the
         * shift [a][b][d][d] becomes [a][b][d][d] so put the entry at index max/2
         * in the insertion node i.g. if the new entry's key is {c} the
         * insertion node now looks like this [a][b][c][d] with {c} being
         * inserted at index 2
         */
        insertionNode.elements[insertionPosition] = newEntry;
        // increase the insertion node's elements count
        insertionNode.numElements++;
    }

    private Node splitNodeIfNeeded(Node insertionNode) {
        if (insertionNode.numElements < MAX) {
            /**
             * Insertion node still has room for more children DON'T SPLIT. END
             * INSERTION
             */
            return null;
        } else {
            /**
             * Insertion node has no more room for children SPLIT NODE
             */
            return splitInsertionNode(insertionNode);
        }
    }

    /**
     * split provided node in half
     */
    private Node splitInsertionNode(Node head) {
        Node newNode = new Node(MAX / 2);
        //head is insertion node. now has only 2 elements
        head.numElements = MAX / 2;
        for (int j = 0; j < MAX / 2; j++) {
            // this will be the right side of the new root node after we split the root node
            newNode.elements[j] = head.elements[MAX / 2 + j];
        }
        
        return newNode; //used in splitting root node
    }

    /**
     *
     * @param newNode
     */
    private void splitRootNode(Node newNode) {
        /**
         * we need to split the root! 
         * create a new root node of size 2
         * new root is entry at the index MAX/2 of the current root node
         */
        Node newRoot = new Node(2);
        //set the root's left children
        newRoot.elements[0] = new Entry(root.elements[0].key, null, root);
        //set the root's right children
        newRoot.elements[1] = new Entry(newNode.elements[0].key, null, newNode);
       
        root = newRoot;
        bTreeHeight++;
    }

    /**
     * *********************************************************************************************
     */
    /**
     * ***********************FUNCTIONS FOR SEARCHING & MANIPULATING BTREE *************************
     */
    /**
     * *********************************************************************************************
     */
    /**
     * search for given key
     *
     * @param key
     * @return associated value if key found or return null if key not found
     */
    public Value get(Key key) {
        return search(root, key, bTreeHeight);
    }

    private Value search(Node searchNode, Key key, int ht) {
        //create array of the search node's children
        Entry[] searchNodeChildren = searchNode.elements;

        //external node: because the tree only has 1 level, a height of 0
        if (ht == 0) {
            //go through each node of the single level tree
            for (int j = 0; j < searchNode.numElements; j++) {
                //check if the search key equals the key of child of the search node at index {@code j}
                if (LMeths.stringsEqual(key, searchNodeChildren[j].key)) {
                    /*
                     * if so, SUCCESFULL SEARCH!!!! return the value of the found node corresponding to the search key
                     */
                    return (Value) searchNodeChildren[j].pageUrl;
                }
            }
        } else {//internal node: because the tree has more levels, a height of 1 or more
            for (int searchPosition = 0; searchPosition < searchNode.numElements; searchPosition++) {
                /*
                 check IF the search position is at the final element of the node (insertionPosition+1 == MAX-1)
                 else if the search key is less than the key at 
                 the current insertion position in the search node
                
                 */
                if (searchPosition + 1 == searchNode.numElements || LMeths.stringLessThan(key, searchNodeChildren[searchPosition + 1].key)) {
                    //recursively search through the children of the search node
                    return search(searchNodeChildren[searchPosition].next, key, ht - 1);
                }
            }
        }
        return null;
    }

    private void incrementFrequency(Key key) {
        findAndIncreaseFrequency(root, key, bTreeHeight);
    }

    private void findAndIncreaseFrequency(Node searchNode, Key key, int ht) {

        //create array of the search node's children
        Entry[] searchNodeChildren = searchNode.elements;

        //external node: because the tree only has 1 level, a height of 0
        if (ht == 0) {
            //go through each node of the single level tree
            for (int j = 0; j < searchNode.numElements; j++) {
                //check if the search key equals the key of child of the search node at index {@code j}
                if (LMeths.stringsEqual(key, searchNodeChildren[j].key)) {
                    /*
                     * if so, SUCCESFULL SEARCH!!!! increament the frequency of the entry coresponding to the key
                     */
                    searchNodeChildren[j].frequency += 1;
                }
            }
        } else {//internal node: because the tree has more levels, a height of 1 or more
            for (int searchPosition = 0; searchPosition < searchNode.numElements; searchPosition++) {
                /*
                 check IF the search position is at the final child of the node (insertionPosition+1 == MAX-1)
                 else if the search key is less than the key at 
                 the current insertion position in the search node
                 */
                if (searchPosition + 1 == searchNode.numElements || LMeths.stringLessThan(key, searchNodeChildren[searchPosition + 1].key)) {
                    //recursively search through the children of the search node
                    findAndIncreaseFrequency(searchNodeChildren[searchPosition].next, key, ht - 1);
                }
            }
        }
    }

    private void setFrequency(Key key, int frequency) {
        findAndSetFrequency(root, key, frequency, bTreeHeight);
    }

    private void findAndSetFrequency(Node searchNode, Key key, int frequency, int ht) {
        //create array of the search node's children
        Entry[] searchNodeChildren = searchNode.elements;

        //external node: because the tree only has 1 level, a height of 0
        if (ht == 0) {
            //go through each node of the single level tree
            for (int j = 0; j < searchNode.numElements; j++) {
                //check if the search key equals the key of child of the search node at index {@code j}
                if (LMeths.stringsEqual(key, searchNodeChildren[j].key)) {
                    /*
                     * if so, SUCCESFULL SEARCH!!!! set the frequency of the entry coresponding to the key
                     */
                    searchNodeChildren[j].frequency = frequency;
                }
            }
        } else {//internal node: because the tree has more levels, a height of 1 or more
            for (int searchPosition = 0; searchPosition < searchNode.numElements; searchPosition++) {
                /*
                 check IF the search position is at the final child of the node (insertionPosition+1 == MAX-1)
                 else if the search key is less than the key at 
                 the current insertion position in the search node
                 */
                if (searchPosition + 1 == searchNode.numElements || LMeths.stringLessThan(key, searchNodeChildren[searchPosition + 1].key)) {
                    //recursively search through the children of the search node
                    findAndSetFrequency(searchNodeChildren[searchPosition].next, key, frequency, ht - 1);
                }
            }
        }
    }

    public int frequencyOf(Key key) {
        return findFrequencyOf(root, key, bTreeHeight);
    }

    private int findFrequencyOf(Node searchNode, Key key, int ht) {

        //create array of the search node's children
        Entry[] searchNodeChildren = searchNode.elements;

        //external node: because the tree only has 1 level, a height of 0
        if (ht == 0) {
            //go through each node of the single level tree
            for (int j = 0; j < searchNode.numElements; j++) {
                //check if the search key equals the key of child of the search node at index {@code j}
                if (LMeths.stringsEqual(key, searchNodeChildren[j].key)) {
                    /*
                     * if so, SUCCESFULL SEARCH!!!! return the frequency of the entry coresponding to the key
                     */
                    return searchNodeChildren[j].frequency;
                }
            }
        } else {//internal node: because the tree has more levels, a height of 1 or more
            for (int searchPosition = 0; searchPosition < searchNode.numElements; searchPosition++) {
                /*
                 check IF the search position is at the final child of the node (insertionPosition+1 == MAX-1)
                 else if the search key is less than the key at 
                 the current insertion position in the search node
                 */
                if (searchPosition + 1 == searchNode.numElements || LMeths.stringLessThan(key, searchNodeChildren[searchPosition + 1].key)) {
                    //recursively search through the children of the search node
                    return findFrequencyOf(searchNodeChildren[searchPosition].next, key, ht - 1);
                }
            }
        }
        return 0;
    }

    /**
     * adds up all the frequencies of each key in the table to get a total
     *
     * @return returns sum of all the frequencies of each key in the tree
     */
    int totalWordsFrequencies = 0;

    public int totalKeyFrequencies() {
        int totalKeyFrequencies = 0;
        for (String key : this.keys()) {
            totalKeyFrequencies += frequencyOf((Key) key);
        }
        return totalKeyFrequencies;

    }

    public void scanInWords(String allWebSiteText, String urlString) {
        Scanner wordReader = new Scanner(allWebSiteText);
        while (wordReader.hasNext()) {
            String word = wordReader.next().replaceAll("\\s", "");
            word = LMeths.filterWord(word);
            if (LMeths.isAWord(word)) {
                this.put((Key) word, (Value) urlString);
            }
        }
    }


    /**
     * *********************************************************************************************
     */
    /**
     * *******************************FUNCTIONS FOR WRITING BTREE TO FILE***************************
     */
    /**
     * *********************************************************************************************
     */
    /**
     * Calls the method to write each node of the BTree to the cache starting at
     * the root node.
     *
     * @param raf The random access file to write the BTree to cache
     * @throws java.io.IOException
     */
    public void writeToCacheRAF(LRaf raf) throws IOException {
            writeNodeToRaf(raf, root, bTreeHeight);

    }
    private void writeNodeToRaf(LRaf raf, Node node, int ht) throws IOException{
        Entry[] elements = node.elements;
        
        String keyDataBlock;
        String freqDataBlock;
        String pointerDataBlock;

        if (ht == 0) {
            
            node.pointer = raf.getFilePointer()+8;
            pointerDataBlock = LRaf.createDataBlock(node.pointer + "", LRaf.BTREE_NODE_POINTER_BLOCK_SIZE);
            raf.write(pointerDataBlock.getBytes());
            for (int j = 0; j < node.numElements; j++) {
                keyDataBlock = LRaf.createDataBlock(elements[j].key + "", LRaf.BTREE_KEY_BLOCK_SIZE);
                freqDataBlock = LRaf.createDataBlock(elements[j].frequency + "", LRaf.BTREE_KEY_FREQUENCY_BLOCK_SIZE);
                raf.write(keyDataBlock.getBytes());
                raf.write(freqDataBlock.getBytes());
            }
            
            createAndWriteNodePaddingToRafIfNeeded(raf, node);

        } else {
            for (int j = 0; j < node.numElements; j++) {
                writeNodeToRaf(raf, elements[j].next, ht - 1);
            }
        }
    }
    /**
     * seeks to the start of the provided random access file 
     * and puts the file pointer there
     * @param raf 
     */
    public void seekToStartOfRaf(LRaf raf) {
        try {
            raf.seek(0);
        } catch (IOException ex) {
            Logger.getLogger(LBTreeA2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     *
     * for the difference between the node's number of children and the max
     * number of children a node can have, create padding to fill the node's
     * space in the raf and write it to ensure that each node in th raf can be
     * the same size
     *
     * @param writeNode
     */
    private void createAndWriteNodePaddingToRafIfNeeded(LRaf raf, Node writeNode) {
        for (int i = 1; i < MAX - writeNode.numElements; i++) {
            //make an empty composite datablock for node padding
            //make the key data block
            String cacheWebsiteKeyDataBlock
                    = LRaf.createDataBlock(LRaf.padding,
                            LRaf.BTREE_KEY_BLOCK_SIZE);

            String cacheWebsiteKeyFrequencyDataBlock = LRaf.createDataBlock(LRaf.padding,
                    LRaf.BTREE_KEY_FREQUENCY_BLOCK_SIZE);

            //concatenate the key's block with it's frequency's block to make one composite data block
            String cacheWebsiteCompositeDataBlock = cacheWebsiteKeyDataBlock + cacheWebsiteKeyFrequencyDataBlock;
            try {
                raf.write(cacheWebsiteCompositeDataBlock.getBytes());
            } catch (IOException ex) {
                Logger.getLogger(LBTreeA2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * *********************************************************************
     */
    /**
     * *******************FUNCTIONS FOR PRINTING BTREE CONTENT*****************
     */
    /**
     * *********************************************************************
     */
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return toString(root, bTreeHeight, "") + "\n";
    }

    private String toString(Node h, int ht, String indent) {
        String s = "";
        Entry[] children = h.elements;

        if (ht == 0) {
            for (int j = 0; j < h.numElements; j++) {
                s += indent + children[j].key + " " + children[j].frequency + "\n";
            }
        } else {
            for (int j = 0; j < h.numElements; j++) {
                s += toString(children[j].next, ht - 1, indent + "     ");
            }
        }
        return s;
    }

    public String toStringClean() {
        Node h = root;
        int ht = bTreeHeight;

        String s = "";
        Entry[] children = h.elements;

        if (ht == 0) {
            for (int j = 0; j < h.numElements; j++) {
                s += "{" + children[j].key + " " + children[j].frequency + "}";
            }
        } else {
            for (int j = 0; j < h.numElements; j++) {
                s += toStringCleanS(children[j].next, ht - 1);
            }
        }
        return s;
    }

    private String toStringCleanS(Node h, int ht) {
        String s = "";
        Entry[] children = h.elements;

        if (ht == 0) {
            for (int j = 0; j < h.numElements; j++) {
                s += "{" + children[j].key + " " + children[j].frequency + "}";
            }
        } else {
            for (int j = 0; j < h.numElements; j++) {
                s += toStringCleanS(children[j].next, ht - 1);
            }
        }
        return s;
    }
}