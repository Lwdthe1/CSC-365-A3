/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import hashtables.LHashTable;
import java.util.ArrayList;
import java.util.HashMap;
import util.LMeths;

/**
 *
 * @author Lwdthe1
 */
public class LWebsite {

    LHashTable wordsHistogram;
    ArrayList<LWord> wordsList;
    ArrayList<String> words;
    int totalWordsFrequencies;
    String urlString;
    double similarityPercentage = 0.0;
    String mostFrequentSimilarWord;
    int mostFrequentSimilarWordFrequency;

    public LWebsite(LHashTable wordsHistogram, String urlString) {
        this.wordsHistogram = wordsHistogram;
        this.urlString = urlString;
        this.totalWordsFrequencies = getHistogramTotalValues();
        addHistogramWords();
    }

    public LWebsite(LHashTable wordsHistogram, String urlString,
            double similarityPercentage,
            String mostFrequentSimilarWord,
            int mostFrequentSimilarWordFrequency) {
        this.wordsHistogram = wordsHistogram;
        this.urlString = urlString;
        this.totalWordsFrequencies = getHistogramTotalValues();
        addHistogramWords();
        this.similarityPercentage = similarityPercentage;
        this.mostFrequentSimilarWord = mostFrequentSimilarWord;
        this.mostFrequentSimilarWordFrequency = mostFrequentSimilarWordFrequency;
    }
    
    public LWebsite(String urlString, double similarityPercentage){
        this.urlString = urlString;
        this.similarityPercentage = similarityPercentage;
    }

    public LHashTable getWordsHistogram() {
        return wordsHistogram;
    }
    public ArrayList<LWord> getWordsList() { return wordsList;
    }

    public int getTotalWordsFrequencies() {
        return totalWordsFrequencies;
    }
    
    public int getWordsCount() {
        return wordsList.size();
    }
    
    public void setSimilarityPercentage(double value) {
        this.similarityPercentage = value;
    }
    public double getSimilarityPercentage() {
        return similarityPercentage;
    }
    
    public ArrayList<String> getWords() {
        return words;
    }
    public String getUrlString() {
        return urlString;
    }

    public boolean matchesSiteUrlString(String urlString) {
        return this.urlString.equalsIgnoreCase(urlString);
    }

    public boolean matchesWord(String webSiteWord, String comparison) {
        return webSiteWord.equalsIgnoreCase(comparison);
    }

    private int getHistogramTotalValues() {
        int returnTotalWordsFrequencies = 0;
        for (Object wordV : this.wordsHistogram.values()) {
            returnTotalWordsFrequencies += (Integer) wordV;
        }
        return returnTotalWordsFrequencies;
    }
    
    private LWord getMostFrequentWord() {
        int highestFrequency = 0;
        LWord mostFrequentWord = null;
        for (LWord word : this.wordsList) {
            if( word.wordFrequency > highestFrequency ){
                highestFrequency = word.wordFrequency;
                mostFrequentWord = word;
            }
        }
        return mostFrequentWord;
    }

    private void addHistogramWords() {
        this.wordsList = new ArrayList<>();
        this.words = new ArrayList<>();
        for (Object wordK : this.wordsHistogram.keySet()) {
            String wordKString = String.valueOf(wordK);
            String wordV = this.wordsHistogram.get(wordK).toString();
            int wordVInt = Integer.parseInt(wordV);
            LWord wordx = new LWord(wordKString, wordVInt);
            this.wordsList.add(wordx);
            this.words.add(wordKString);
        }
    }

    
    @Override
    public String toString() {
        LWord getMostFrequentWord = getMostFrequentWord();      
        
        String toString = "{\n\n" + urlString + 
                "\n\nTotal Words: " + getWordsCount() + 
                "\nTotal Word Frequencies: " + totalWordsFrequencies + 
                "\nMost Frequent Word: [" + 
                getMostFrequentWord().word + " : " + getMostFrequentWord().wordFrequency +"]";
        if(this.similarityPercentage>0 ){
            toString += "\nSimilarity: " + this.similarityPercentage;
        }
        
        toString += "\n\n}";
        return toString;
    }
    public String toString2() {
        String toString = "\n{\n\n" +
            urlString + " \n\n" +
            " Similarity points: " + this.similarityPercentage +
            "\nMost Frequent Word: [" + 
            getMostFrequentWord().word + " : " + getMostFrequentWord().wordFrequency +"]"+
            "\n}\n";
        return toString;
    }

    
}
