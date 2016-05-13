/*
 * 4th gui for assignment 2. uses 4th frame for assignment 2
 */
package gui;
import frames.A3Frame5;
import javax.swing.SwingUtilities;

/**
 *
 * @author Lwdthe1
 */
public class A3GUI5 {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ThreadForGUI());
    }

    private static class ThreadForGUI implements Runnable {
        @Override
        public void run() {
            A3GUI5 gui = new A3GUI5();
        }
    }
    
    public A3GUI5(){
        A3Frame5 frame = new A3Frame5("A3F5: Web Connectivity", 1200, 900);
    }
}
