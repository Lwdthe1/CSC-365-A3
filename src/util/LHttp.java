/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lwdthe1
 */
public class LHttp {

    public static long lastModifiedFromUrlString(String urlString) {
        URL obj = null;
        if(urlString.trim().substring(0,8).equalsIgnoreCase("https://") || 
                urlString.trim().substring(0,7).equalsIgnoreCase("http://")){
            
            try {
                obj = new URL(urlString);
            } catch (MalformedURLException ex) {
                Logger.getLogger(LHttp.class.getName()).log(Level.SEVERE, null, ex);
            }
            URLConnection conn = null;
            if (obj != null) {
                try {
                    conn = obj.openConnection();
                } catch (IOException ex) {
                    Logger.getLogger(LHttp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(conn!= null){
                return conn.getLastModified();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
    
    public static boolean isValidUrlString(String urlString){
        return urlString.trim().substring(0,8).equalsIgnoreCase("https://") || 
                urlString.trim().substring(0,7).equalsIgnoreCase("http://");
    }
}
