/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Queues;

/**
 *
 * @author Lwdthe1
 */
public class PriorityQueueA3<V> {

    public boolean isEmpty() {
        return lastPosition<1;
    }
    
    class Task<V> implements Comparable {
        V key;
        double priority;
        int parentPos = -1;
        int pos;

        private Task(V key, double priority) {
            this.key = key;
            this.priority = priority;
        }

        public void setPriority(double priority){
            this.priority = priority;
        }
        
        public void setPos(int pos){
            this.parentPos = pos;
        }
        
        public void setParentPos(int parentPos){
            this.parentPos = parentPos;
        }
        
        @Override
        public int compareTo(Object task) {
            if(this.priority == ((Task)task).priority) return 0;
            else if(this.priority > ((Task)task).priority) return 1;
            else return -1;
        }
    }

    int MAX_SIZE = 100; // default max size
    int lastPosition = 0; //last position which is empty
    //left child:2n+1, Right child: 2n+2 
    Task[] heap;
  

    public PriorityQueueA3(int max) {
        MAX_SIZE = max;
        heap = new Task[MAX_SIZE];
    }
    
    /**
     * 
     * @param key the key to the task
     * @return the task with the associated key
     */
    public boolean contains(String key){
        for (int i = 0; i < heap.length; i++) {
            if(heap[i]!=null){
                if(heap[i].key.equals(key)) return true;
            }
        }
        return false;
    }
    
    /**
     * 
     * @param key the key to the task
     * @return the task with the associated key
     */
    public Task get(String key){
        for (int i = 0; i < heap.length; i++) {
            if(heap[i].key.equals(key)) return heap[i];
        }
        return null;
    }
    
    public int size(){ return lastPosition;}

    public void insert(String key, double priority) {
        Task task = new Task(key, priority);
        int i = lastPosition;
        lastPosition++;
        task.setPos(i);// set the node's current pos
        heap[i] = task;
        int parentPos = getParentPosition(i);
        task.setParentPos(i);// set the node's parent pos

        if (parentPos == -1) { 
            // No more checks needed for first insertion, so return
            return;
        }
        siftUp(parentPos, i);
    }

    public void siftUp(int parentPos, int i) {
        Task temp;
        if (parentPos == -1) return;
        //###################sift up
        while (heap[parentPos].compareTo(heap[i]) > 0) {
            //while the parent of the node at i is greater than the node
            temp = heap[i];// hold the node to be sifted up in a temp var
            
            // bring the parent node down to where the node was
            temp.setParentPos(i);
            heap[i] = heap[parentPos];
            
            // push the node up to where its parent node was
            temp.setPos(parentPos);
            heap[parentPos] = temp;
            
            // set i equal to the parentPos (where the node is now) to continue loop
            i = parentPos; 
            //get the new parent of the node and continue sifting up
            parentPos = getParentPosition(i); 
            
            //if the parentPos is -1, you are at the top node of the graph
            if (parentPos == -1) {
                //so it has no parent, stop sifting up
                return;//To avoid array out of bound exception
            }
            
            //continue sifting up
        }
    }
    
    public boolean decreasePriority(String w, Double priority) {
        Task task = get(w);
        if(task!=null) {
            //set the node's new priority
            task.setPriority(priority);
            //start the sifting up process to realign the graph
            siftUp(task.parentPos, task.pos);
            return true;
        } else return false;
    }

    public Object deleteMin() {
        Object node = getMin();//get the node which will be the root of the graph

        if (lastPosition == 0) {
            return null; // graph is empty
        } else if (lastPosition == 1) {
            //graph only had 1 element
            // set the first element to null as there's nothing in the graph
            heap[0] = null;
            lastPosition--;//decrement the last position as we just took out a node
            return node;
        } else {
            ///graph had more than 1 element in it, begin sift down
            
            // bring the last node at the bottom of the graph
            // to the root of the graph to replace the node that was just deleted
            int i = lastPosition - 1; 
            heap[i].setPos(0);// set the node's new pos to 0
            // set the node's new parent pos to -1 as it is now the root of the graph
            heap[i].setParentPos(-1); 
            heap[0] = heap[i];// make the node the root of the graph
            //set the position of node that was just brought to the top to null
            //because that position, the last node of the graph is now empty
            //and is no longer part of the graph
            heap[i] = null;
            i = 0;//set i equal to 0, the root of the graph, to begin sift down process

            // If there exists a smaller child for i, then get it.
            // meaning the current node is illegally larger than it's children
            int next = getSmallestChild(i);// get the node's smallest child of the node
            siftDown(next, i);

            // finally, decrease the lastPosition of the lastPosition 
            // of the graph because we just deleted a node
            lastPosition--; 
            return node; // return the node that was just deleted
        }

    }

    public void siftDown(int next, int i) {
        Task temp;
        //###################sift down
        while (next != -1) {
            //while the node has a child
            // Go down the heap until you have no smaller child
            temp = heap[i]; // hold the node to be sifted down in temp var
            int childPos = heap[next].pos;
            int childParentPos = heap[next].parentPos;
            
            
            //set the node's child's new parent pointer to the node's parent pointer
            heap[next].setParentPos(temp.parentPos);
            // set the node's child's new position to the node's position
            heap[next].setPos(i);
            heap[i] = heap[next]; // bring the node's child up to where it is
            
            temp.setPos(childPos);
            temp.setParentPos(childParentPos);
            heap[next] = temp; // push the node down to where its child was
            
            // set i equal to the child's old pos (where the node is now) to continue sifting down
            i = next;
            //set next equal to the node's next child from where it now is
            next = getSmallestChild(i);
            
            //continue sifting down
        }
    }
    
    public String removeMin() {
        String s = ((Task)this.deleteMin()).key.toString();
        return s;

    }

    public Object getMin() {
        //the min node of a min-heap is the root node
        return heap[0];
    }

    public int getLeftChildPosition(int i) {
        //left child of any given node in graph is (2*its position)+1
        return (2 * i) + 1;
    }

    public int getRightChildPosition(int i) {
        //right child of any given node in graph is (2*its position)+2
        return (2 * i) + 2;
    }

      public int getParentPosition(int i) {
        if(i==0) return -1;
        // the parent position of the node = the (position of the node / 2) if i is even
        else if(i%2!=0) return (i/2);
        // the parent position of the node = the (position of the node / 2)-1 if i is odd
        else return (i/2)-1;
    }

      /**
       * Returns the index of the smaller child, if any of the children are smaller  
       * @param i position of the node in the graph
       * @return the index of the smaller child, if any of the children are smaller
       */
    public int getSmallestChild(int i) {
        
        int left = getLeftChildPosition(i);
        int right = getRightChildPosition(i);

        //if the node has no children, return -1 for error check
        if (heap[left] == null && heap[right] == null) return -1;
        //if the node only has a left child
        else if (heap[left] != null && heap[right] == null) {
            //if the left child is less than the node, 
            //return the left as the smaller child
            if (heap[left].compareTo(heap[i]) < 0) return left;
            //else return -1 to say the node has no smallest children
            else return -1;
        } else if (heap[left] != null && heap[right] != null) {
            //if the left child is smaller than both the node and its right child
            if (heap[left].compareTo(heap[right]) 
                    < 0 && heap[left].compareTo(heap[i]) < 0) {
                //return the left as the smallest child
                return left;
            } 
            //else if the right child is less than both the node and its left child
            else if (heap[right].compareTo(heap[left]) 
                    < 0 && heap[right].compareTo(heap[i]) < 0) {
                //return the right as the smallest child
                return right;
            } 
            //else return -1 to say the node has no smallest children
            else return -1;
        }
        //return -1 if this is ever reached (shouldn't happen)
        return -1;
    }

}
