/*
 * 4th gui for assignment 2. uses 4th frame for assignment 2
 */
package gui;
import frames.A3Frame2;
import javax.swing.SwingUtilities;

/**
 *
 * @author Lwdthe1
 */
public class Assignment3GUI2 {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ThreadForGUI());
    }

    private static class ThreadForGUI implements Runnable {
        @Override
        public void run() {
            Assignment3GUI2 gui = new Assignment3GUI2();
        }
    }
    
    public Assignment3GUI2(){
        A3Frame2 frame = new A3Frame2("A3F2: Web Connectivity", 400, 500);
    }
}
