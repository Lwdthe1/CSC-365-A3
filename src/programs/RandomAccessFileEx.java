/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programs;

import java.io.IOException;
import java.io.RandomAccessFile;
import util.LRaf;

/**
 *
 * @author Lwdthe1
 */
public class RandomAccessFileEx {

    static final String FILEPATH = "cacheA2/data/enwiktionaryorgwikiandr.dat";

    public static void main(String[] args) {

        try {

            System.out.println(new String(readFromFile(FILEPATH, LRaf.BTREE_COMPOSITE_DATA_BLOCK_SIZE*6+16, LRaf.BTREE_COMPOSITE_DATA_BLOCK_SIZE*3)));

            //writeToFile(FILEPATH, "JavaCodeGeeks Rocks!", 22);

        } catch (IOException e) {}

    }

    public static byte[] readFromFile(String filePath, int position, int size)

            throws IOException {

 

        RandomAccessFile file = new RandomAccessFile(filePath, "r");

        file.seek(position);
        //file.readLong();
        byte[] bytes = new byte[size];

        file.read(bytes);

        file.close();

        return bytes;

 

    }

 

    private static void writeToFile(String filePath, String data, int position)

            throws IOException {

 

        RandomAccessFile file = new RandomAccessFile(filePath, "rw");

        file.seek(position);

        file.write(data.getBytes());

        file.close();

    }

}

