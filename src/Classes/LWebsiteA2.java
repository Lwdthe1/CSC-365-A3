/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import btrees.LBTreeA2;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import util.LMeths;
import util.LRaf;

/**
 *
 * @author Lwdthe1
 */
public class LWebsiteA2 {

    LBTreeA2 wordsBTree;
    ArrayList<LWord> wordsList;
    ArrayList<String> words;
    private int totalWordsFrequencies;
    private String urlString;
    double similarityPercentage = 0.0;
    
    int mostFrequentSimilarWordFrequency;
    int mostFrequentSimilarWordFrequency2;
    int mostFrequentSimilarWordFrequency3;

    public LWebsiteA2(LBTreeA2 wordsBTree, String urlString) {
        this.wordsBTree = wordsBTree;
        this.urlString = urlString;
        this.totalWordsFrequencies = getBTreeTotalWordFrequencies();
        addBTreeWordsToSiteWordDataStores();
    }

    public LWebsiteA2(LBTreeA2 wordsBTree, String urlString, double similarityPercentage) {
        this.wordsBTree = wordsBTree;
        this.urlString = urlString;
        this.totalWordsFrequencies = getBTreeTotalWordFrequencies();
        addBTreeWordsToSiteWordDataStores();
        this.similarityPercentage = similarityPercentage;
    }
    
    public LWebsiteA2(String urlString, int wordsSimilarity){
        this.urlString = urlString;
        this.similarityPercentage = wordsSimilarity;
    }

    public LBTreeA2 getWordsFrequencyBTree() {
        return wordsBTree;
    }
    public ArrayList<LWord> getWordsList() { return wordsList;
    }

    public int getTotalWordsFrequencies() {
        return totalWordsFrequencies;
    }
    
    public int getWordsCount() {
        return this.wordsBTree.keys().size();
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

    private int getBTreeTotalWordFrequencies() {
        return this.wordsBTree.totalKeyFrequencies();
    }


    private void addBTreeWordsToSiteWordDataStores() {
        this.wordsList = new ArrayList<>();
        this.words = new ArrayList<>();
        ArrayList<String> wordsBTreeKeySet = this.wordsBTree.keys();
        for (Object wordKey : wordsBTreeKeySet) {
            String wordKString = String.valueOf(wordKey);
            int wordVInt = (int) this.wordsBTree.frequencyOf(wordKString);
            LWord wordObject = new LWord(wordKString, wordVInt);
            this.wordsList.add(wordObject);
            this.words.add(wordKString);
        }
    }

    
    @Override
    public String toString() {    
        String toString = "{\n\n" + this.getUrlString();
        
        if(totalWordsFrequencies>0){
            toString += "\nTotal Word Frequencies: " + totalWordsFrequencies; 
        }
        if(this.similarityPercentage>0 ){
            toString += "\nSimilarity: " + this.similarityPercentage;
        }
        
        toString += "\n\n}";
        return toString;
    }
    public String toString2() {
        String toString = "\n{\n\n" +
            urlString + " \n\n" +
            " Similarity points: " + this.similarityPercentage;
        return toString;
    }
    
    public void compareToSearchSite(String searchSiteUrl, ArrayList<String> searchSiteWords) throws IOException {
        //System.out.println(String.format("\n####Comparing %s from cache%s", urlString, "####\n"));
        try (LRaf raf = LRaf.getRandomAccessFileByUrlString(urlString)) {
            long numKeysInBTreeCache = raf.length()/LRaf.BTREE_COMPOSITE_DATA_BLOCK_SIZE;
            
            long pointer = 8;
            while (raf.getFilePointer() < raf.length()) {
                //System.out.println("POINTER: "+ pointer);
                raf.seek(pointer);
                //raf.readLong();
                FileChannel inChannel = raf.getChannel();
                int readNodeBlockAllocation = (LBTreeA2.MAX - 1) * LRaf.BTREE_COMPOSITE_DATA_BLOCK_SIZE;
                ByteBuffer buffer = ByteBuffer.allocate(readNodeBlockAllocation);
                //System.out.println("|Allocated " + readNodeBlockAllocation +": Reading| Node at position: " + raf.getFilePointer());
                inChannel.read(buffer);
                buffer.flip();
                String nodeBlock = "";
                for (int i = 0; i < buffer.limit(); i++) {
                    nodeBlock += (char) buffer.get();
                }

                //System.out.println(nodeBlock);
                extractAndCompareDataBlockToSearchSiteWords(nodeBlock, searchSiteWords);

                ByteBuffer buffer2 = ByteBuffer.allocate(8);
                inChannel.read(buffer2);
                buffer2.flip();
                for (int i = 0; i < buffer2.limit(); i++) {
                    //pointer += readNodeBlockAllocation;
                }
                pointer += readNodeBlockAllocation + 8;
            }
            
            /**
             * divide the total points accumulated by this site
             * by the number of keys it has 
             * multiplied by 100 
             * to get its similarity percentage to the search site.
             * This way, sites with more words don't outweigh sites with less simply on that basis
             */
            this.similarityPercentage = (this.similarityPercentage / numKeysInBTreeCache) * 100;
        }        
    }

    public void extractAndCompareDataBlockToSearchSiteWords(String singleNodeBlock,
            ArrayList<String> searchSiteWords) throws NumberFormatException {

        String singleBlockKey = "";
        int singleNodeBlockKeysFrequency = 0;

        for (int i = 1; i < LBTreeA2.MAX; i++) {
            String singleDataBlock = singleNodeBlock.substring((i-1) * LRaf.BTREE_COMPOSITE_DATA_BLOCK_SIZE,
                    i * LRaf.BTREE_COMPOSITE_DATA_BLOCK_SIZE);

            
            if (!singleDataBlock.trim().isEmpty()) {
                //get the key's data block from end of composite data block
                singleBlockKey = singleDataBlock.substring(0, LRaf.BTREE_KEY_BLOCK_SIZE).trim();
                singleBlockKey = LMeths.filterWord(singleBlockKey);
                //System.out.println(singleBlockKey);
            
                if(searchSiteWords.contains(singleBlockKey)){
                    //get the key's frequency data block from end of key's block
                    String singleNodeBlockKeyFrequency = singleDataBlock.substring(LRaf.BTREE_KEY_BLOCK_SIZE);
                    singleNodeBlockKeysFrequency += Integer.parseInt(LRaf.removePadding(singleNodeBlockKeyFrequency));
                    //System.out.print("| MATCH: " + singleBlockKey + "-> " + singleNodeBlockKeyFrequency + " |");
                }
            }
        }

        
            //add the key and its frequency to the site's btree
            this.similarityPercentage += singleNodeBlockKeysFrequency;
        
    }

    public ArrayList<String> readInBtreeWordsFromRaf(String searchSiteUrl) throws IOException {
        words = new ArrayList<>();
        try (LRaf raf = LRaf.getRandomAccessFileByUrlString(urlString)) {
            long pointer = 8;
            while (raf.getFilePointer() < raf.length()) {
                //System.out.println("POINTER: "+ pointer);
                raf.seek(pointer);
                //raf.readLong();
                FileChannel inChannel = raf.getChannel();
                int readNodeBlockAllocation = (LBTreeA2.MAX - 1) * LRaf.BTREE_COMPOSITE_DATA_BLOCK_SIZE;
                ByteBuffer buffer = ByteBuffer.allocate(readNodeBlockAllocation);
                //System.out.println("|Allocated " + readNodeBlockAllocation +": Reading| Node at position: " + raf.getFilePointer());
                inChannel.read(buffer);
                buffer.flip();
                String nodeBlock = "";
                for (int i = 0; i < buffer.limit(); i++) {
                    nodeBlock += (char) buffer.get();
                }

                //System.out.println(nodeBlock);
                extractAndAddToKeysList(nodeBlock);

                ByteBuffer buffer2 = ByteBuffer.allocate(8);
                inChannel.read(buffer2);
                buffer2.flip();
                for (int i = 0; i < buffer2.limit(); i++) {
                    //pointer += readNodeBlockAllocation;
                }
                pointer += readNodeBlockAllocation + 8;
            }
        }
        System.out.println(LMeths.formatS("Loaded %s words from [%s]'s cache file", words.size(), urlString));
        return words;
    }  
    
    public void extractAndAddToKeysList(String singleNodeBlock) throws NumberFormatException {
        //add each key to the list to be returned
        for (int i = 1; i < LBTreeA2.MAX; i++) {
            String singleDataBlock = singleNodeBlock.substring((i-1) * LRaf.BTREE_COMPOSITE_DATA_BLOCK_SIZE,
                    i * LRaf.BTREE_COMPOSITE_DATA_BLOCK_SIZE);

            if (!singleDataBlock.trim().isEmpty()) {
                //get the key's data block from end of composite data block
                String singleBlockKey = singleDataBlock.substring(0, LRaf.BTREE_KEY_BLOCK_SIZE).trim();
                singleBlockKey = LMeths.filterWord(singleBlockKey);
                //System.out.println(singleBlockKey);
                words.add(singleBlockKey);
            }
        }
    }
}
