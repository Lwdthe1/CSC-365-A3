/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

/**
 *
 * @author Lwdthe1
 */
public class LWord {
    String word;
    String wordSiteUrl;
    int wordFrequency;
    
    public LWord(String word, int frequency){
        this.word = word;
        this.wordFrequency = frequency;
    }
    
    public String getWord(){ return word;}
    public int getFrequency(){ return wordFrequency;}
}
