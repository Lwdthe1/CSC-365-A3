/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btrees;

import util.LMeths;

/**
 *
 * @author Lwdthe1
 */
public class LBTreeInt {

    final int MAX = 4;
    final int MIN = 2;

    /**
     * inner LBTreeInt node class
    */
    private class BTNode{
        int count;
        /**create array of keys to be held in this node
        * keys are ints
        */
        int key[] = new int[MAX + 1];
        BTNode child[] = new BTNode[MAX + 1];
    }
    //initiate the tree with a root node
    BTNode root = new BTNode();

    /**
     * this inner class creates an object reference
     */
    class Ref{
        /**
         * used to retain/save index values of current node between method calls
         */
        int m;
    } 

    /**
     * New key is inserted into an appropriate node.
     * New key cannot be equal to that of an existing node (duplicate keys are not allowed
     */
    public void insertTree(int val) {
        Ref i = new Ref();
        BTNode c = new BTNode();
        BTNode node = new BTNode();
        boolean pushup;
        pushup = pushDown(val, root, i, c);
        if (pushup) {
            node.count = 1;
            node.key[1] = i.m;
            node.child[0] = root;
            node.child[1] = c;
            root = node;
        }
    }
    
    /**
     * New key is inserted into subtree to which current node points
     * if pushup becomes true, then the height of the tree grows
     */
    boolean pushDown(int val, BTNode node, Ref p, BTNode c) {
        Ref k = new Ref();
        if (node == null) {
            p.m = val;
            c = null;
            return true;
        } else {
            //make sure the key to be insterted isn't already in the tree
            if (searchNode(val, node, k)) {
                LMeths.systemPrint("Key already exists.");
            }
            if (pushDown(val, node.child[k.m], p, c)) {

                if (node.count < MAX) {
                    pushIn(p.m, c, node, k.m);
                    return false;
                } else {
                    split(p.m, c, node, k.m, p, c);
                    return true;
                }
            }
            return false;
        }
    }
    /*
     * internal search method
     * Search through a B-Tree for a target key in the node: val
     * Outputs target node and its position (pos) in the node
     */
    BTNode searchTree(int val, BTNode root, Ref pos) {
        if (root == null) {
            //the root is null so the tree does not yet exist
            return null;
        } else {
            if (searchNode(val, root, pos)) {
                return root;
            } else {
                //recurse until we find the node
                return searchTree(val, root.child[pos.m], pos);
            }
        }
    }
    
    /**
     * Determines if the target key is present in
     * the current node or not. Searches keys in the current node
     * returns the position of the target or child on which to continue
     */
    boolean searchNode(int val, BTNode node, Ref pos) {
        if (val < node.key[1]) {
            //go left
            pos.m = 0;
            return false;
        } else {
            //go right because search key is larger than node's key at index 1
            //set the current position to the number of keys in the node
            pos.m = node.count;
            while ((val < node.key[pos.m]) && pos.m > 1) {
                //decrease the position by 1
                (pos.m)--;
            }
            if (val == node.key[pos.m]) {
                //the target node is found!!!!!!! SUCCESSFUL SEARCH
                return true;
            } else {
                //the target node was not found! UNSUCCESSFUL SEARCH
                return false;
            }
        }
    }
    
    /**
     * Inserts the key into a node, if there is room for it
     * used for insertion
     */
    void pushIn(int val, BTNode c, BTNode node, int k) {
        int i;
        for (i = node.count; i > k; i--) {
            node.key[i + 1] = node.key[i];
            node.child[i + 1] = node.child[i];
        }
        //set the node's next key  equal to the key of the one before it (the current one)
        node.key[k + 1] = val;
        //set the node's next child equal to the child of the one before it (the current one)
        node.child[k + 1] = c;
        //increase the node's count of keys;
        node.count++;
    }
    
     /**
     * splits a full node into current node and new right child
     * splits at median
     */
    void split(int val, BTNode c, BTNode node,
            int k, Ref y, BTNode newnode) {
        int i, mid;//mid is median of the node being split
        if (k <= MIN) {
            mid = MIN;
        } else {
            mid = MIN + 1;
        }
        newnode = new BTNode();
        for (i = mid + 1; i <= MAX; i++) {
            newnode.key[i - mid] = node.key[i];
            newnode.child[i - mid] = node.child[i];
        }
        newnode.count = MAX - mid;
        node.count = mid;
        if (k <= MIN) {
            pushIn(val, c, node, k);
        } else {
            pushIn(val, c, newnode, k - mid);
        }
        y.m = node.key[node.count];
        //make the new node's first child the last child of the node being split
        newnode.child[0] = node.child[node.count];
        node.count--;
    }

    /**
     * public method for calling the actual display method
     */    
    public void displayTree() {
        display(root);
    }
    
    /**
     * displays the B-Tree
     */
    void display(BTNode root) {

        int i;
        if (root != null) {
            for (i = 0; i < root.count; i++) {
                display(root.child[i]);
                System.out.print(root.key[i + 1] + " ");
            }
            display(root.child[i]);
        }
    }
}
