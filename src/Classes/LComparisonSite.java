/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import btrees.LBTreeA2;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import util.LMeths;
import util.LRaf;

/**
 *
 * @author Lwdthe1
 */
public class LComparisonSite {

    public String urlString;
    public int comparisonPoints = 0;

    public LComparisonSite(String urlString) {
        this.urlString = urlString;
    }

    public String getUrlString() {
        return this.urlString;
    }

    public int getComparisonPoints() {
        return this.comparisonPoints;
    }

    

}
