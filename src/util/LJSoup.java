/*
 * a utils class to handle jsoup functions
 */
package util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Lwdthe1
 */
public class LJSoup {
    /**
     * @param urlString URL string to the page to be parse
     * @return a list of the external links from page at the provided URL
     */
    public static ArrayList<String> getPageExternalLinks(String urlString){
        final int MAX_EXTERNAL_LINKS_TO_RETURN = 10;
        ArrayList<String> externalLinks = new ArrayList<>();
        Document doc = null;
        try {
            if(Jsoup.connect(urlString)!=null){
                doc = Jsoup.connect(urlString).get();
            }
        } catch (IOException ex) {
            //Logger.getLogger(LJSoup.class.getName()).log(Level.SEVERE, null, ex);
        }
        if( doc != null ){
            Elements links = doc.select("a[href]");

            
            //print("{Found %d exetrnal links for %s}", links.size(), urlString);
            /*if(links.size() < MAX_EXTERNAL_LINKS_TO_RETURN){
                print("{\nOnly found %d exetrnal links for %s\n}", links.size(), urlString);
            } else {
                print("{\nFound %d exetrnal links for %s!\nOnly parsing " + MAX_EXTERNAL_LINKS_TO_RETURN
                        + " cause any more would be extreme. Nobody likes an extremist ...\n}", links.size(), urlString);
            }*/
            int externalLinksCount = 0;
            //print("{%s -> %d links}",  urlString, links.size());
            for (Element link : links ) {
                String linkUrl = link.attr("abs:href");
                if( linkUrl.length() > urlString.length() && externalLinksCount <= MAX_EXTERNAL_LINKS_TO_RETURN ){
                    externalLinksCount++;
                    if( ( isExternalLink(linkUrl, urlString) ) 
                            && ( linkUrl.contains("wikipedia.org") || linkUrl.contains("mediawiki.org") ) ){
                        externalLinks.add( linkUrl );
                    }
                }
            }
        }
        
        return externalLinks;
    }

    public static boolean isExternalLink(String linkUrlString, String urlString) {
        if(linkUrlString.length() > urlString.length()){
            return !linkUrlString.substring(0,urlString.length()).equalsIgnoreCase(urlString) && !linkUrlString.contains(urlString);
        } else if(linkUrlString.length() < urlString.length()){
            return !urlString.substring(0,linkUrlString.length()).equalsIgnoreCase(linkUrlString) && !urlString.contains(linkUrlString);
        } else return false;
    }
    
    public static boolean isReachable(String urlString){
        Document doc = null;
        if(Jsoup.connect(urlString)!=null){
            try {
                doc = Jsoup.connect(urlString).get();
            } catch (IOException ex) {
                //Logger.getLogger(LJSoup.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        return doc!=null;
    }

    private static void print(String msg, Object... args) {
        System.out.print(String.format(msg, args));
    }
    
    private static void println(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}