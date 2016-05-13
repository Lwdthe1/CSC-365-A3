/*
 * 4th gui for assignment 2. uses 4th frame for assignment 2
 */
package gui;
import frames.A3Frame3;
import javax.swing.SwingUtilities;

/**
 *
 * @author Lwdthe1
 */
public class A3GUI3 {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ThreadForGUI());
    }

    private static class ThreadForGUI implements Runnable {
        @Override
        public void run() {
            A3GUI3 gui = new A3GUI3();
        }
    }
    
    public A3GUI3(){
        A3Frame3 frame = new A3Frame3("A3F3: Web Connectivity", 600, 600);
    }
}
