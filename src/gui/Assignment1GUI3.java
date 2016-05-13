/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import frames.Assignment1Frame3;
import javax.swing.SwingUtilities;

/**
 *
 * @author Lwdthe1
 */
public class Assignment1GUI3 {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ThreadForGUI());
    }
    
    public Assignment1GUI3(){
        Assignment1Frame3 frame = new Assignment1Frame3("Web String Frequency", 800, 600);
    }

    private static class ThreadForGUI implements Runnable {
        @Override
        public void run() {
            Assignment1GUI3 gui = new Assignment1GUI3();
        }
    }
}
