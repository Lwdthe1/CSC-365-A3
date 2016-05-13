/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import frames.Assignment1Frame2;
import javax.swing.SwingUtilities;

/**
 *
 * @author Lwdthe1
 */
public class Assignment1GUI2 {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ThreadForGUI());
    }
    
    public Assignment1GUI2(){
        Assignment1Frame2 frame = new Assignment1Frame2("Web String Frequency", 600, 400);
    }

    private static class ThreadForGUI implements Runnable {
        @Override
        public void run() {
            Assignment1GUI2 gui = new Assignment1GUI2();
        }
    }
}
