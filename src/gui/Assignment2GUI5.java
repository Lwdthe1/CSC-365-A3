/*
 * 4th gui for assignment 2. uses 4th frame for assignment 2
 */
package gui;
import frames.Assignment2Frame5;
import javax.swing.SwingUtilities;

/**
 *
 * @author Lwdthe1
 */
public class Assignment2GUI5 {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ThreadForGUI());
    }

    private static class ThreadForGUI implements Runnable {
        @Override
        public void run() {
            Assignment2GUI5 gui = new Assignment2GUI5();
        }
    }
    
    public Assignment2GUI5(){
        Assignment2Frame5 frame = new Assignment2Frame5("A2F5: Web Word Frequencies", 400, 500);
    }
}
