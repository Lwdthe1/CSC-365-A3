/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programs;

import btrees.LBTreeInt;

/**
 *
 * @author Lwdthe1
 */
public class LBTreeDemo {

    public static void main(String[] args) {
        LBTreeInt bTree = new LBTreeInt();
        /*
         * Refer Textbook, the section 13.3 B-Trees,
         * inserting into a B-Tree
         * Figure 13.30: Building a B-tree of order 5
         */
        int[] arr = {11, 23, 21, 12, 31, 18, 25, 35, 29, 20, 45,
            27, 42, 55, 15, 33, 36, 47, 50, 39};
        for (int i = 0; i < arr.length; i++) {
            bTree.insertTree(arr[i]);
            System.out.println("Current BTree: ");
            bTree.displayTree();
        }
        System.out.println("B-Tree of order 5:");
        bTree.displayTree();
    }

}
