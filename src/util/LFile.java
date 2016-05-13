/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import Classes.EdgeA3;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lwdthe1
 */
public class LFile extends File {
    
    public LFile(String string) {
        super(string);
    }
    
    public static void createFileIfNotExist(File file) throws IOException {
        // if file doesnt exists, then create it
        if (!file.exists()) { file.createNewFile();}
    }
    
    public static void closeFileIOStream(Object ioStream, String type) {
        if(type.equalsIgnoreCase("in")){
            if(ioStream != null){
                try {
                    ((FileInputStream)ioStream).close();
                } catch (IOException ex) {
                    
                }
            }
        } else if(type.equalsIgnoreCase("out")){
            if(ioStream != null){
                try {
                    ((FileOutputStream)ioStream).close();
                } catch (IOException ex) {
                    
                }
            }
        }
    }
    
    
    public static void saveSerializableObjecttoFile(Object object, String fileName) {
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(fileName);
            LFile.createFileIfNotExist(file);
            fileOutputStream = new FileOutputStream(file);
            
                        
            ObjectOutputStream os = new ObjectOutputStream(fileOutputStream);
            os.writeObject(object);
            fileOutputStream.close();
            os.close();
        } catch (Exception ex) {}
        finally {
            LFile.closeFileIOStream(fileOutputStream, "out");
        }
    }
    
    public static Object loadSerializedObjectFromFile(String fileName) {
        Object loadedObject = null;
        FileInputStream fileInputStream = null;
        try {
            File file = new File(fileName);
            LFile.createFileIfNotExist(file);   
            fileInputStream = new FileInputStream ( file );
            ObjectInputStream objectInputStream = new ObjectInputStream ( fileInputStream );
            loadedObject = objectInputStream.readObject ();
            objectInputStream.close ();
        } catch (Exception ex) { System.out.println(ex);} 
        finally { 
            LFile.closeFileIOStream(fileInputStream, "in");
        }
        
        return loadedObject;
    }
    
    public static boolean exists(String fileName){ return new File(fileName).exists();}

    
}
