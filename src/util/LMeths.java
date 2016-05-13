/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Lwdthe1
 */
public class LMeths {
    public static void systemPrint(Object o){
        System.out.print(String.valueOf(o));
    }
    
    public static String fetchWebsiteText(String urlString) throws IOException, MalformedURLException {
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
        InputStream siteInputStream = urlConnection.getInputStream();
        InputStreamReader siteInputStreamReader = new InputStreamReader(siteInputStream);
        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuilder stringBuilder = new StringBuilder();
        while ((numCharsRead = siteInputStreamReader.read(charArray)) > 0) {
            stringBuilder.append(charArray, 0, numCharsRead);
        }
        String htmlResult = stringBuilder.toString();
        Document doc = Jsoup.parse(htmlResult);
        String allWebSiteText = doc.text();
        return allWebSiteText;
    }
    
    /**
     * Compare two comparable objects
     * @return true if @param k1 is less than @param k2
     */
    public static boolean stringLessThan(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }
    
    /**
     * Compare two comparable objects
     * @return true if @param k1 is greater than @param k2
     */
    public static boolean stringGreaterThan(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }

    /**
     * Compare two comparable objects
     * @return true if @param k1 equal to @param k2
     */
    public static boolean stringsEqual(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }
    
    
    public static boolean isAWord(String word) {

        return (word.length()>1 && !word.equalsIgnoreCase("the")) || ( word.length()<2 && word.contains("ai") );
    }
    
    public static String filterWord(String word) {
        word = word.replaceAll("[^a-zA-Z\\\\s]", "");
        word = word.replaceAll("\"", "");
        word = word.replaceAll("'", "");
        word = word.trim();
        
        return word;
    }
    public static String filterWordKeepNumbers(String word) {
        word = word.replaceAll("[^a-zA-Z0-9\\\\s]", "");
        word = word.replaceAll("\"", "");
        word = word.replaceAll("'", "");
        word = word.trim();
        
        return word;
    }
    
    
    public static void print(String msg, Object... args) {
        System.out.print(String.format(msg, args));
    }
    
    public static void println(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }
    
    public static String formatS(String msg, Object... args) {
        return String.format(msg, args);
    }
    
}
