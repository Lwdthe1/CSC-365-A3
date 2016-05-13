/*
 * 4th gui for assignment 2. uses 4th frame for assignment 2
 */
package gui;
import frames.A3Frame4;
import javax.swing.SwingUtilities;

/**
 *
 * @author Lwdthe1
 */
public class A3GUI4 {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ThreadForGUI());
    }

    private static class ThreadForGUI implements Runnable {
        @Override
        public void run() {
            A3GUI4 gui = new A3GUI4();
        }
    }
    
    public A3GUI4(){
        A3Frame4 frame = new A3Frame4("A3F4: Web Connectivity", 1200, 900);
    }
}
