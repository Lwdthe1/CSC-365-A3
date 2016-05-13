/*
 * To change this license header, choose License Headers scanner Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template scanner the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lwdthe1
 */
public class FileImporter {
    public static Scanner importFileInScanner(String fileName) {
        File file = new File(fileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileImporter.class.getName()).log(Level.SEVERE, null, ex);
        }        

        //return the scanner over the file
        return scanner;
    }
}
