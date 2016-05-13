/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import btrees.LBTreeA2;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Lwdthe1
 */
public class LRaf extends RandomAccessFile implements Serializable {
    
    //cache data files info
    public static final String mSystemWebsiteUrlStringsFile = "websites.txt";
    public static final String mWebsitesCacheIndexFilePath = "cacheA2/index.dat";
    public static final String mWebsitesCacheDirectory = "cacheA2/data/";
    public static final String padding = " ";
    public static final String FILE_EXT = ".dat";

    public static String ensureFixedNode(long bytesLeft) {
        String blockString = "";
        for (int i = 0; i < bytesLeft; i++) {
            blockString+=padding;
        }
        return blockString;
    }
    
    private int blockSize;
    public static final int RANDOM_ACCESS_FILE_URL_BLOCK_SIZE = 100;
    
    public static final int BTREE_KEY_BLOCK_SIZE = 150;
    public static final int BTREE_KEY_FREQUENCY_BLOCK_SIZE = 4;
    public static final int BTREE_NODE_POINTER_BLOCK_SIZE = 8;
    public static int BTREE_COMPOSITE_DATA_BLOCK_SIZE = BTREE_KEY_BLOCK_SIZE 
            + BTREE_KEY_FREQUENCY_BLOCK_SIZE;
    public static int NODE_BLOCK = BTREE_COMPOSITE_DATA_BLOCK_SIZE*(LBTreeA2.MAX-1);
    
    
    

    public LRaf(int blockSize, String file) throws IOException {
        super(file, "rw");
        this.blockSize = blockSize;
    }

    
    
    public static byte[] readFromRaf(String filePath, int position, int size)

            throws IOException {

 

        RandomAccessFile file = new RandomAccessFile(filePath, "r");

        file.seek(position);
        //file.readLong();
        byte[] bytes = new byte[size];

        file.read(bytes);

        file.close();

        return bytes;

 

    }
    
    public String read(int size){
        byte[] bytes = new byte[size];
        try {
            this.read(bytes);
        } catch (IOException ex) {
            Logger.getLogger(LRaf.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new String(bytes);
    }

    /*public void write(byte[] b, int blockNumber) throws IOException {
        seek(blockNumber*blockSize);
        write(b);
    }*/
    public static LRaf getRandomAccessFileByUrlString(Object urlString) {
        //get the website's cache file
        LRaf websitesCacheRandAF = LRaf.getRandomAccessFileFromFileName(
                LRaf.filterUrlToFileName(urlString.toString()) + ".dat");
        return websitesCacheRandAF;
    }
    public static LRaf getRandomAccessFileFromFileName(String fileName) {
        LRaf websiteCacheRandomAccessFile = null;
        try {
            websiteCacheRandomAccessFile = new LRaf(RANDOM_ACCESS_FILE_URL_BLOCK_SIZE, 
                    mWebsitesCacheDirectory+fileName);
        } catch (IOException ex) { }
        return websiteCacheRandomAccessFile;
    }
    
    public static LRaf getKeysRandomAccessFileFromFileName(String fileName) {
        LRaf websiteCacheRandomAccessFile = null;
        try {
            websiteCacheRandomAccessFile = new LRaf(BTREE_KEY_BLOCK_SIZE, mWebsitesCacheDirectory+fileName);
        } catch (IOException ex) { }
        return websiteCacheRandomAccessFile;
    }
    
    
    public static int getDataBlockHash(String dataBlock, int blockSize) {
        int cacheWebsiteUrlStringHash = Math.abs(dataBlock.hashCode() %
                (blockSize))+1;
        return cacheWebsiteUrlStringHash;
    }
    
    public static long getDataBlockSeekPosition(int dataHash, int blockSize) {
        return dataHash * blockSize;
    }
    
    public static String createDataBlock(String data, int blockSize) {
        String dataBlock = data;
        int originalDataLength = dataBlock.length();
        int dataBlockLengthNeededDifference = blockSize-originalDataLength;
        if( dataBlockLengthNeededDifference > 0 ){
            for (int i = 0; i < dataBlockLengthNeededDifference; i++) {
                dataBlock += padding;
            }
        }
        return dataBlock;
    }
    
    public static String filterUrlToFileName(String urlString) {
        String fileNameReadyUrlString = urlString;
        //make sure url is a minimally valid url
        if(urlString.substring(0,8).equalsIgnoreCase("https://")){
            fileNameReadyUrlString = fileNameReadyUrlString.replaceAll("https://", "");
        } else if(urlString.substring(0,7).equalsIgnoreCase("http://")){
            fileNameReadyUrlString = fileNameReadyUrlString.replaceAll("http://", "");
        }
        
        fileNameReadyUrlString = fileNameReadyUrlString.replaceAll("[^a-zA-Z0-9\\\\s]", "");
        fileNameReadyUrlString = fileNameReadyUrlString.replaceAll("\"", "");
        fileNameReadyUrlString = fileNameReadyUrlString.replaceAll("'", "");
       fileNameReadyUrlString = fileNameReadyUrlString.substring(0, (int) (fileNameReadyUrlString.length()*.9));
        
        
        return fileNameReadyUrlString.toLowerCase();
    }
    
    public static String removePadding(String string) {
        String filterString = string;
        //filter the string
        filterString = filterString.replaceAll(padding, "");
        filterString = filterString.replaceAll(" ", "");     
        //return the filtered string
        return filterString.toLowerCase();
    }
    
    public static String replacePaddingWithSpaces(String string) {
        String filterString = string;
        //replace the padding characters with spaces
        filterString = filterString.replaceAll(padding, " ");
        //return the filtered string
        return filterString.toLowerCase();
    }
    
 
    public static long getFileLastModifiedDate(String filePathUrl){
        File file = new File(filePathUrl);
        //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return file.lastModified();
    }
    
    public static long getFileLastModifiedDateFromFile(File file) {
        //Before Format : 1275265349422
        //System.out.println("Before Format : " + file.lastModified());
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        //After Format : 05/31/2010 08:22:29
        //System.out.println("After Format : " + sdf.format(file.lastModified()));

        return file.lastModified();
    }
    
    
}

