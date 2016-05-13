package btrees;

import util.LMeths;
import util.LRaf;

public class LBTree<Key extends Comparable<Key>, Value> {
    //max amount of children per BTree node = {@code MAX} -1;
    private static final int MAX = 4;
    
    //the root of the BTree
    private Node root;
    //height of the BTree
    private int bTreeHeight;
    //number of key-value pairs in are in the BTree
    private int bTreeNumKeys;
    
    /**
     * helper BTree node class
     */
    private static final class Node {
        private long pointer;
        //number of children the node has
        private int nodeNumChildren;
        //array of children the node has
        private final Entry[] children = new Entry[MAX];
        //construct a node with k children
        private Node(int k){
            nodeNumChildren = k;
        }
    }
    
    /**
     * internal nodes: only use key and next
     * external nodes: only use key and value
     */
    private static class Entry{
        private Comparable key;
        private final Object value;
        private Node next;// the next node after this one
        public Entry(Comparable key, Object value, Node next){
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
    
    /**
     * BTree constructor
     */
    public LBTree(){ 
        root = new Node(0);
    }
    
    /**
     * return number of key-value pairs are in the BTree
     */
    public int size(){ return bTreeNumKeys;}
    
    /**
     * return height of the BTree
     */
    public int height(){ return bTreeHeight;}
    
    /**
     * public method to call insert()
     * inserts key-value pair into tree
     * TODO: add check for duplicate key
     * completes split operation if need be
     */
    public void put(Key key, Value value){
        /**
         * insert the key-value pair
         * the return value, a node or null, will tell us if we need to split the root
         * insertion will return null if tree does not need to be split: exit
         * insertion will return the node where the key-value was inserted if tree need s to be split
         */
        Node insertionResultNode = insert(root, key, value, bTreeHeight);
        bTreeNumKeys++;
        if(insertionResultNode == null){
            System.out.println("| u == null | | current tree: \n");
            System.out.println(this.toString());
            System.out.println("_________________________________________________________________");
            return ;
        }
        
        splitRootNode(insertionResultNode);
    }
    
    /**
     * inserts a key-value pair into the tree starting at the head node
     */
    private Node insert(Node insertionNode, Key key, Value value, int treeHeight){
        System.out.println("\n#Inserting: " + key);
        int insertionPosition = 0;
        Entry newEntry = new Entry(key, value, null);
        
        /**
         * if tree's height is 0, the insertion node (current node) has no children
         * the insertion node (current node) is an *external node*
         * 
         * else, if the tree's height is greater than 0,
         * the insertion node (current node) has children
         * the insertion node (current node) is an *internal node*
         */
        if(treeHeight == 0){
            System.out.println("**Node (tree) has no children: External Node at Height " + treeHeight + "**");
            /**
             * start the insertion position at 0
             * for the following for loop
             * nodeNumChildren will be MAX-1. 
             * MAX = 4 as of 3/16/15
             * so 0 <= nodeNumChildren <= 3.
             * insrease insertion position 
             * until the insertion new entry's key is less than the key of the entry at
             * the current insertion position of the node
             */
            for( insertionPosition = 0; insertionPosition < insertionNode.nodeNumChildren; insertionPosition++ ){
                /**
                 * check IF the new entry's key is less than the key at 
                 * the current insertion position in the insertion node
                 * 
                 * if so, break: stop incrementing insertion position
                 */
                if(LMeths.stringLessThan(key, insertionNode.children[insertionPosition].key)){
                    //stop incrementing insertion position
                    break;
                }
            }
        } else if(treeHeight > 0){//internal node
            System.out.println("**Node (tree) has children: Internal Node at Height "+treeHeight+"**");
            
            /**
             * start the insertion position at 0
             * 
             */
            for( insertionPosition = 0; insertionPosition < insertionNode.nodeNumChildren; insertionPosition++ ){
                /**
                 * check IF the insertion position is at the final child of the node (insertionPosition+1 == MAX-1)
                 * else if the new entry's key is less than the key of the entry at
                 * the current insertion  position in the insertion node
                 * 
                 * if so, go up a level in the tree (decrement tree's height by 1) and recursively call insert
                 */
                if( (insertionPosition+1 == insertionNode.nodeNumChildren)){
                    System.out.println("Reached end of node");
                    
                    System.out.println("\nBefore recursive insertion: insertionPosition = " + (insertionPosition+1) );
                    
                    /*
                        recursively insert the new entry's key and value at lower height 
                    with the insertionNode's next child's {@code next} node as the insertionNode
                    */
                    Node insertionResultNode = insert(insertionNode.children[insertionPosition++].next, key, value, treeHeight-1);
                    
                    if(insertionResultNode == null) {
                        //the insertion process is complete. return the result
                        System.out.println("***After recursive insertion: END INSERTION\n"
                                + "Insertion Node Num Children : " + insertionNode.nodeNumChildren + "***\n");
                        return null;
                    }
                    
                    System.out.println("***After recursive insertion: new entry's key = {" + insertionResultNode.children[0].key
                                + "}. Insertion Node Num Children : " + insertionNode.nodeNumChildren + "***\n");
                    newEntry.key = insertionResultNode.children[0].key;
                    newEntry.next = insertionResultNode;
                    break;
                    
                } else if( LMeths.stringLessThan(key, insertionNode.children[insertionPosition+1].key) ){
                    System.out.println("Entry's Key {" + key + "} < Existing Entry's Key {" 
                            + insertionNode.children[insertionPosition+1].key + "} at position " + (insertionPosition+1) );
                    
                    System.out.println("\nBefore recursive insertion: insertionPosition = " + (insertionPosition+1) );
                    
                    /*
                        recursively insert the new entry's key and value at lower height 
                    with the insertionNode's next child's {@code next} node as the insertionNode
                    */
                    Node insertionResultNode = insert(insertionNode.children[insertionPosition++].next, key, value, treeHeight-1);
                    
                    if(insertionResultNode == null) {
                        //the insertion process is complete. return the result
                        System.out.println("***After recursive insertion: END INSERTION\n"
                                + "Insertion Node Num Children : " + insertionNode.nodeNumChildren + "***\n");
                        return null;
                    }
                    
                    System.out.println("***After recursive insertion: new entry's key = {" + insertionResultNode.children[0].key
                                + "}. Insertion Node Num Children : " + insertionNode.nodeNumChildren + "***\n");
                    newEntry.key = insertionResultNode.children[0].key;
                    newEntry.next = insertionResultNode;
                    break;
                }
            }
        }
        System.out.println("\n-->Key {" + newEntry.key + "}'s Final Insertion Position: " + insertionPosition + " at height " + treeHeight + "\n");
        startInsertionAtFinalPosition(insertionNode, insertionPosition, newEntry);
        
        return checkSplitNeeded(insertionNode);
    }

    public void startInsertionAtFinalPosition(Node insertionNode, int insertionPosition, Entry newEntry) {
        shiftInsertionNodeEntriesRight(insertionNode, insertionPosition);
        insertTheEntry(insertionNode, insertionPosition, newEntry);
    }

    public void insertTheEntry(Node insertionNode, int insertionPosition, Entry newEntry) {
        /**
         * put the new entry in its final insertion position
         * i.e. if the insertion node has 3 children & insertion position is 2
         * after the shift [a][b][d][d] becomes [a][b][d][d]
         * so put the entry at index 2 in the insertion node
         * i.g. if the new entry's key is {c}
         * the insertion node now looks like this
         * [a][b][c][d]
         * with {c} being inserted at index 2
         */
        insertionNode.children[insertionPosition] = newEntry;
        // increase the insertion node's children count
        insertionNode.nodeNumChildren++;
    }

    public void shiftInsertionNodeEntriesRight(Node insertionNode, int insertionPosition) {
        /**
         * shift the entries of the insertion node right
         * i.e. if the insertion node has 3 children & insertion position is 2
         * child 4 becomes child 3
         * (element at index 3 becomes element at index 2) in the insertion node
         * [a][b][d][] => [a][b][d][d]
         */
        for(int i = insertionNode.nodeNumChildren; i > insertionPosition; i--){
            insertionNode.children[i] = insertionNode.children[i-1];
        }
    }

    public Node checkSplitNeeded(Node insertionNode) {
        if(insertionNode.nodeNumChildren < MAX){
            System.out.println("\n#Insertion node still has room for more children: DON'T SPLIT. END INSERTION");
            return null;
        } else {
            return split(insertionNode);
        }
    }
    
    
    /**
     * split passed node in half
     */
    private Node split(Node head){
        System.out.println("#Splitting Insertion Node: ");
        Node newNode = new Node(MAX/2);
        head.nodeNumChildren = MAX/2;
        for(int j = 0; j < MAX/2; j++){
            newNode.children[j] = head.children[MAX/2+j];
            System.out.println("SplitN: child " + j + " = " +  newNode.children[j].key);
        }
        return newNode;
    }
    
    /**
     *
     * @param newNode
     */
    public void splitRootNode(Node newNode) {
        /**
         * we need to split the root
         */
        System.out.println("#Splitting Root: ");
        //create a new node of size 2
        Node newRoot = new Node(2);
        newRoot.children[0] = new Entry(root.children[0].key, null, root);
        System.out.println("SplitR: root left child 0 node key" + " = " +  newRoot.children[0].key);
        newRoot.children[1] = new Entry(newNode.children[0].key, null, newNode);
        System.out.println("SplitR: root right child 0 node key" + " = " +  newRoot.children[1].key);
        root = newRoot;
        bTreeHeight++;
        
        System.out.println("\n| Splitted Tree | | current tree: \n");
        System.out.println(this.toString());
        System.out.println("_________________________________________________________________");
    }
    
    
    /**
     * search for given key
     * @param key
     * @return associated value: if key found
     */
    public Value get(Key key) { return search(root, key, bTreeHeight);}
    private Value search(Node x, Key key, int ht){
        //create array of the search node's children
        Entry[] children = x.children;
        
        //external node: because the tree only has 1 level, a height of 0
        if(ht == 0) {
            //go through each node of the single level tree
            for(int j = 0; j<x.nodeNumChildren; j++){
                //check if the search key equals the key of child of the search node at index {@code j}
                if(LMeths.stringsEqual(key, children[j].key)){
                    /*
                     * if so, SUCCESFULL SEARCH!!!! return the value of the found node corresponding to the search key
                     */
                    return (Value) children[j].value;
                }
            }
        } else {//internal node: because the tree has more levels, a height of 1 or more
            for(int j = 0; j < x.nodeNumChildren; j++){
                if(j+1 == x.nodeNumChildren || LMeths.stringLessThan(key, children[j+1].key)){
                    //recursively search through the children of the node
                    return search(children[j].next, key, ht-1);
                }
            }
        }
        return null;
    }
    
    
    // for debugging
    public String toString() {
        return toString(root, bTreeHeight, "") + "\n";
    }
    private String toString(Node h, int ht, String indent) {
        String s = "";
        Entry[] children = h.children;

        if (ht == 0) {
            s += "| OUT Height = 0 || children = " + h.nodeNumChildren + " |\n";
            for (int j = 0; j < h.nodeNumChildren; j++) {
                s += indent + children[j].key + " " + children[j].value + "\n";
            }
        }
        else {
            for (int j = 0; j < h.nodeNumChildren; j++) {
                if (j > 0){ 
                    s += indent + "{Child 1: " + children[j].key + "}\n";
                }
                s += toString(children[j].next, ht-1, indent + "     ");
            }
        }
        return s;
    }
}