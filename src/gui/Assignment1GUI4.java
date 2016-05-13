/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;
import frames.Assignment1Frame4;
import javax.swing.SwingUtilities;

/**
 *
 * @author Lwdthe1
 */
public class Assignment1GUI4 {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ThreadForGUI());
    }

    private static class ThreadForGUI implements Runnable {
        @Override
        public void run() {
            Assignment1GUI4 gui = new Assignment1GUI4();
        }
    }
    
    public Assignment1GUI4(){
        Assignment1Frame4 frame = new Assignment1Frame4("A1: Web Word Frequencies", 900, 600);
    }
}
