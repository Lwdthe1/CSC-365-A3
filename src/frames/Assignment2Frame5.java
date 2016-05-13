/*
 * This is the 5th Frame for the CSC 365 assignment 2 GUI
 * This is the UI that allows a user to search for similar sites to another
 */
package frames;

import Objects.LWebsiteA2;
import btrees.LBTreeA2;
import hashtables.LHashTableA2;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import util.FileImporter;
import util.LHttp;
import util.LJSoup;
import util.LMeths;
import util.LRaf;

/**
 *
 * @author Lwdthe1
 */
public final class Assignment2Frame5 extends JFrame implements ActionListener {

    public static LHashTableA2 mCachedWebsiteUrlStringsHashTableA2 = new LHashTableA2();
    public static LHashTableA2 mNewCacheWebsiteUrlStringsHashTableA2 = new LHashTableA2();
    private LRaf mWebsitesCacheIndexRandomAccessFile = getWebsitesCacheIndexRandomAccessFile();
    //initiate loading from cache as false
    public boolean loadFromCache = false;

    String mSearchTextFieldHint = "enter a URL ...";
    String mSearchUrlString = "";

    /**
     * Data Stores
     */
    LBTreeA2 mSearchSiteBTree;
    ArrayList<String> mSearchSiteWords;
    ArrayList<LWebsiteA2> mComparisonSystemSites = new ArrayList<>();
    ArrayList<LWebsiteA2> mComparedSites;
    ArrayList<HashMap<String, Object>> mComparedSitesMap = new ArrayList<HashMap<String, Object>>();
    int mComparingCounter;

    JButton mHelpButton;
    JButton mSearchButton;

    JTextField mSearchTextField;
    JTextField mResultsTitleTextField;
    JTextField mSimilarSitesTitleTextField;

    JPanel mResultsPanel;
    JTextArea mResultsTextArea;
    JTextArea mComparisonSitesTextArea;

    SystemActivityFrame mSystemActivityFrame;
    SystemActivityFrame mSearchSystemActivityFrame;

    SystemActivityFrame mAfterSaveCloseSystemActivityFrame;
    JButton mAfterCacheSaveExitProgramButton;
    JTextArea mAfterCacheSaveExitProgramTextArea;

    public Assignment2Frame5(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponents(getContentPane());
        addActionListeners();
        mSearchTextField.setText("http://en.wikipedia.org/wiki/Procedural_programming");
        setVisible(true);
    }

    private void addComponents(Container contentPane) {
        JPanel controlPanel = createControlPanel();
        JPanel contentPanel = createMasterContentPanel();

        JPanel resultsPanel = createResultsPanel();
        JPanel similarSitesPanel = createSimilarSitesPanel();

        JScrollPane resultsScrollPane = new JScrollPane(resultsPanel);
        JScrollPane similarSitesScrollPane = new JScrollPane(similarSitesPanel);

        contentPanel.add(resultsScrollPane);
        contentPanel.add(similarSitesScrollPane);

        contentPane.setLayout(new BorderLayout());
        contentPane.add(controlPanel, BorderLayout.NORTH);
        contentPane.add(contentPanel, BorderLayout.CENTER);

        makeContentTextObjectsUneditable();
    }

    private JPanel createMasterContentPanel() {
        //create the master content panel to hold the three sub content panels
        JPanel contentPanel = new JPanel();
        GridLayout contentPanelLayout = new GridLayout();
        contentPanel.setLayout(contentPanelLayout);
        return contentPanel;
    }

    private JPanel createControlPanel() {
        //create control panel
        JPanel controlPanel = new JPanel();
        GridLayout controlPanelLayout = new GridLayout();
        controlPanel.setLayout(controlPanelLayout);
        //components for control panel
        mHelpButton = new JButton("Help");
        mSearchButton = new JButton("Search");
        mSearchTextField = new JTextField();
        mSearchTextField.setMinimumSize(new Dimension(20, 30));
        //add the control panel's components
        controlPanel.add(mHelpButton, controlPanelLayout);
        controlPanel.add(mSearchTextField);
        controlPanel.add(mSearchButton);

        return controlPanel;
    }

    private JPanel createResultsPanel() {
        //create the results panel
        mResultsPanel = new JPanel();
        BorderLayout resultsPanelLayout = new BorderLayout();
        mResultsPanel.setLayout(resultsPanelLayout);
        //components for the results panel
        mResultsPanel.add(new JLabel("Search Results"), BorderLayout.NORTH);
        mResultsTextArea = new JTextArea();
        mResultsTextArea.setLineWrap(true);
        mResultsTextArea.setWrapStyleWord(true);
        //add the results panel's components
        mResultsPanel.add(mResultsTextArea, BorderLayout.CENTER);

        return mResultsPanel;
    }

    private JPanel createSimilarSitesPanel() {
        //create the similarSites panel
        JPanel similarSitesPanel = new JPanel();
        BorderLayout similarSitesPanelLayout = new BorderLayout();
        similarSitesPanel.setLayout(similarSitesPanelLayout);
        //components for the similarSites panel
        mComparisonSitesTextArea = new JTextArea();
        //add the similarSites panel's components
        similarSitesPanel.add(new JLabel("Cached Sites (" + LRaf.mSystemWebsiteUrlStringsFile + ")"), BorderLayout.NORTH);
        similarSitesPanel.add(mComparisonSitesTextArea, BorderLayout.CENTER);

        return similarSitesPanel;
    }

    private void makeContentTextObjectsUneditable() {
        if (confirmContentTextObjectsNotNull()) {
            mResultsTextArea.setEditable(false);
            mComparisonSitesTextArea.setEditable(false);
        }
    }

    private boolean confirmContentTextObjectsNotNull() {
        return mResultsTextArea != null
                && mComparisonSitesTextArea != null;
    }

    public void addActionListeners() {
        mSearchButton.addActionListener(this);
        mHelpButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equalsIgnoreCase("search")) {
            //thread to do search asynchronisly
            Thread doSearchThread = new Thread() {
                @Override
                public void run() {
                    mSearchButton.setEnabled(false);
                    mSearchButton.setText("Searching...");
                    doSearch();
                    mSearchButton.setText("Search");
                    mSearchButton.setEnabled(true);
                }
            };
            doSearchThread.start();
        } else if (command.equalsIgnoreCase("help")) {
            /**
             * thread to populate help text area
             */
            Thread showHelpThread = new Thread() {
                @Override
                public void run() {
                    new HelpFrame("Help", 500, 500);
                }
            };
            showHelpThread.start();
        } else if (event.getSource().equals(mAfterCacheSaveExitProgramButton)) {
            System.exit(0);
        }
    }

    /**
     * *********************************************************************************************
     *
     * ********************************FUNCTIONS FOR USER
     * SEARCH************************************
     *
     * *********************************************************************************************
     */
    public void doSearch() {

        mResultsTextArea.setText("\n[#System Note:\nWorking, just a second...\n]\n");

        mSearchSiteBTree = new LBTreeA2();
        mSearchSiteWords = new ArrayList<>();
        mSearchUrlString = mSearchTextField.getText().trim();
        mResultsPanel.removeAll();
        mResultsPanel.add(new JLabel("Search Results: " + mSearchUrlString), BorderLayout.NORTH);
        mResultsPanel.add(mResultsTextArea);

        
        mComparingCounter = 0;
        if (searchUrlExistsInCache()) {
            //load search btree cache
            //construct the site's btree from its cache file
            constructCachedWebsiteBTreeFromCache(mSearchUrlString, true);
            compareSearchSiteToCachedSystemSites();
            
        } else {
            try {
                //fetch the search site's btree data from online
                mSearchSiteBTree.scanInWords(LMeths.fetchWebsiteText(mSearchUrlString), mSearchUrlString);
                mResultsTextArea.setText("\n\n\n\n\n\n\n\n\n\n\n\n"+ mResultsTextArea.getText());
                mResultsTextArea.append(LMeths.formatS("\n\n#%s Total Insertion Frequencies: %s\n\n", mSearchUrlString,
                        mSearchSiteBTree.totalKeyFrequencies()));
                mResultsTextArea.append(mSearchSiteBTree.toStringClean());

                //initialize the search site's BTree keys for comparing
                mSearchSiteWords = mSearchSiteBTree.keys();
                //compare the search site's btree keys to the system sites' btrees
                compareSearchSiteToCachedSystemSites();
            } catch (MalformedURLException e) {
                mResultsTextArea.setText("[#System Warning:\n"
                        + mSearchUrlString + " is not a proper URL.\nURL must resemble http://www.website.com\n]");
            } catch (IOException e) {
                mResultsTextArea.setText("[#System Warning:\n Connection tox " + mSearchUrlString + " failed...\n]");
            }
        }
        System.out.println("#System Note: Search complete!");
    }

    public boolean searchUrlExistsInCache() {
        try {
            //get the hash for the data block and return its seek position for reading
            long searchUrlRafSeekPosition = getUrlHashAbsoluteSeekPositionByLinearProbingRaf(mSearchUrlString);
            mWebsitesCacheIndexRandomAccessFile.seek(searchUrlRafSeekPosition);

            String urlStringAtHashPositionInCacheIndex = new String(
                    LRaf.readFromRaf(LRaf.mWebsitesCacheIndexFilePath, (int) searchUrlRafSeekPosition,
                            LRaf.RANDOM_ACCESS_FILE_URL_BLOCK_SIZE));

            if (mSearchUrlString.equalsIgnoreCase(LRaf.removePadding(urlStringAtHashPositionInCacheIndex))) {
                mResultsTextArea.setText(LMeths.formatS("[#System Note:\nFOUND %s in cache at position %s", urlStringAtHashPositionInCacheIndex, searchUrlRafSeekPosition));
                System.out.println(LMeths.formatS("########## FOUND IN CACHE AT POS %s############", searchUrlRafSeekPosition));
                System.out.println(urlStringAtHashPositionInCacheIndex);
                System.out.println("####################################################\n\n");
                return true;
            } else {
                return false;
            }
        } catch (IOException ex) {
            Logger.getLogger(Assignment2Frame5.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * **************************** FUNCTIONS FOR SIMILARITY METRICS
     * *******************************
     * *****************************************************************
     */
    /**
     * Similarity Metric compares the user's search site to the system's pre
     * loaded sites displays the system site that most closely matches the
     * specific words and their frequencies of the user's search site
     */
    public void compareSearchSiteToCachedSystemSites() {
        if (mComparisonSystemSites.size() > 0) {
            mSearchButton.setText("Comparing Sites ...");
        } else {
            mSearchButton.setText("Loading Comparison Sites ...");
        }

        mComparedSites = new ArrayList<>();

        /**
         * compare each cached site to the user's search site to get similarity
         * points of each
         */
        compareEachCachedSiteToUserSite();
        computeAndDisplayMostSimilarSites();
    }

    /**
     * compare each cached site to the other cached sites to see which one has
     * highest similarity points to the user's search site
     */
    public void computeAndDisplayMostSimilarSites() {
        System.out.println("#System Note: Computing Similarities");
        ArrayList<LWebsiteA2> mostSimilarSites = compareCachedSitesForMostSimilarToUserSite();

        for (LWebsiteA2 similarSite : mostSimilarSites) {
            displayMostSimilarSystemSite(similarSite);
        }
    }

    /**
     * Similarity Metric: first step
     *
     * for each cached site, get its word frequency similarity to the user's
     * site and add the returned LWebsite to the array list for comparison to
     * the other system sites in next step
     */
    public void compareEachCachedSiteToUserSite() {
        System.out.println("#Comparing Each Cached Site to User Site\n");
        loadSytemSitesBTreesDataFromCache();
    }

    /**
     * Similarity Metric: second step compare the system sites to each other to
     * see which one most closely match the user's site words frequency
     *
     * @return the most similar site's data as an instance of LWebsiteA2
     */
    public ArrayList<LWebsiteA2> compareCachedSitesForMostSimilarToUserSite() {
        ArrayList<LWebsiteA2> mostSimilarSites = new ArrayList<>();
        double mostSimilarSiteSimilarity = 0.0;
        double mostSimilarSiteSimilarity2 = 0.0;
        double mostSimilarSiteSimilarity3 = 0.0;
        LWebsiteA2 mostSimilarSite = null;
        LWebsiteA2 mostSimilarSite2 = null;
        LWebsiteA2 mostSimilarSite3 = null;

        for (int i = 1; i <= 3; i++) {
            if (i == 1) {
                for (LWebsiteA2 comparedSite : mComparedSites) {
                    if (comparedSite.getSimilarityPercentage() > mostSimilarSiteSimilarity) {
                        if (!isSearchUrlString(comparedSite)) {
                            mostSimilarSite = comparedSite;
                            mostSimilarSiteSimilarity = comparedSite.getSimilarityPercentage();
                        }
                    }
                }
            } else if (i == 2) {
                for (LWebsiteA2 comparedSite : mComparedSites) {
                    if (comparedSite.getSimilarityPercentage() > mostSimilarSiteSimilarity2
                            && comparedSite.getSimilarityPercentage() < mostSimilarSiteSimilarity) {

                        if (!isSearchUrlString(comparedSite)) {
                            mostSimilarSite2 = comparedSite;
                            mostSimilarSiteSimilarity2 = comparedSite.getSimilarityPercentage();
                        }
                    }
                }
            } else if (i == 3) {
                for (LWebsiteA2 comparedSite : mComparedSites) {
                    if (comparedSite.getSimilarityPercentage() > mostSimilarSiteSimilarity3
                            && comparedSite.getSimilarityPercentage() < mostSimilarSiteSimilarity2) {

                        if (!isSearchUrlString(comparedSite)) {
                            mostSimilarSite3 = comparedSite;
                            mostSimilarSiteSimilarity3 = comparedSite.getSimilarityPercentage();
                        }
                    }
                }
            }
        }

        //add the most similar sites in descending order, so the top will be shown on top
        mostSimilarSites.add(mostSimilarSite);
        mostSimilarSites.add(mostSimilarSite2);
        mostSimilarSites.add(mostSimilarSite3);

        return mostSimilarSites;
    }

    public boolean isSearchUrlString(LWebsiteA2 comparedSite) {
        return comparedSite.getUrlString().equalsIgnoreCase(mSearchUrlString);
    }

    /**
     * display the most similar system site to the user's search site
     *
     * @param similarSite
     */
    public void displayMostSimilarSystemSite(LWebsiteA2 similarSite) {
        if (similarSite != null) {
            mResultsTextArea.setText(mResultsTextArea.getText()
                    + LMeths.formatS("[\n\nSimilar Site\n%s\n%sPoints\n\n]",
                            similarSite.getUrlString(),
                            similarSite.getSimilarityPercentage())
            );
        }
    }

    /**
     * *************************************************************************
     *
     * *****************************FUNCTIONS FOR LOADING FROM CACHE************
     *
     * *************************************************************************
     */
    private void loadSytemSitesBTreesDataFromCache() {
        beforeLoad();
        duringLoadSystemSitesBTreesDataFromCache(null);
        postLoad();
    }

    /**
     * #Loading CACHE Step 1#:
     *
     * @param updateUIArea
     */
    public void duringLoadSystemSitesBTreesDataFromCache(JTextArea updateUIArea) {
        int progressCounter = 0;

        loadFromCache = checkWebsitesCacheAvailable(mWebsitesCacheIndexRandomAccessFile);

        if (!loadFromCache) {
            loadSystemSitesFromOnline(progressCounter, updateUIArea);
        } else {
            //load in the cached sites
            loadSystemSitesFromCache(progressCounter, updateUIArea);
        }

        Thread saveWebsiteCacheIndexThread2 = new Thread() {
            @Override
            public void run() {
                saveWebsitesDataToCache(mWebsitesCacheIndexRandomAccessFile);
            }
        };
        saveWebsiteCacheIndexThread2.start();
    }

    public boolean checkWebsitesCacheAvailable(LRaf mWebsitesCacheIndexRandAF) {
        //must be 20 cached url's in order to load from cache
        try {
            loadFromCache = mWebsitesCacheIndexRandAF.length() > LRaf.RANDOM_ACCESS_FILE_URL_BLOCK_SIZE * 2;
        } catch (IOException ex) {
        }
        return loadFromCache;
    }

    /**
     * #Loading from Online Step 1#:
     *
     * gets loads data for the list of base system sites from online and makes
     * their BTrees
     *
     * @param progressCounter
     * @param updateUIArea
     */
    public void loadSystemSitesFromOnline(int progressCounter, JTextArea updateUIArea) {
        System.out.println("#Loading Uncached System Sites From Online\n");

        Scanner fileScanner = FileImporter.importFileInScanner(LRaf.mSystemWebsiteUrlStringsFile);
        while (fileScanner.hasNext()) {
            progressCounter++;
            String urlString = fileScanner.next();
            if (!isAlreadyCached(urlString)) {
                constructCacheWebsiteBTreeFromOnline(urlString);
                ArrayList<String> externalLinks = LJSoup.getPageExternalLinks(urlString);
                for (String externalLinkUrlString : externalLinks) {
                    progressCounter++;
                    constructCacheWebsiteBTreeFromOnline(externalLinkUrlString);
                }
            }
        }
    }

    public static boolean isAlreadyCached(String urlString) {
        return mCachedWebsiteUrlStringsHashTableA2.containsKey(urlString);
    }

    /**
     * #Loading CACHE Step 2#: grabs all URLs from cache index
     *
     * @param progressCounter
     * @param updateUIArea
     */
    public void loadSystemSitesFromCache(int progressCounter, JTextArea updateUIArea) {
        try {
            System.out.println("#Loading System Sites From Cache\n");
            //get the cache index random access file (CI.RAF) to read from
            LRaf mWebsitesCacheIndexRandAF = getWebsitesCacheIndexRandomAccessFile();

            ArrayList<String> urlStrings = new ArrayList<>();
            //go through the cache CI.RAF increasing the position 
            //by the size of a url block each time until the end
            String cachedUrlString = "";
            for (int i = 0; i < mWebsitesCacheIndexRandAF.length(); i += LRaf.RANDOM_ACCESS_FILE_URL_BLOCK_SIZE) {
                cachedUrlString = new String(LRaf.readFromRaf(LRaf.mWebsitesCacheIndexFilePath,
                        i, LRaf.RANDOM_ACCESS_FILE_URL_BLOCK_SIZE));

                cachedUrlString = LRaf.removePadding(cachedUrlString.trim());
                
                //check if this block contains content, a url string
                if (!cachedUrlString.isEmpty()) {
                    //add it to the list of url strings to be loaded
                    urlStrings.add(cachedUrlString);
                    System.out.println(cachedUrlString);
                    mCachedWebsiteUrlStringsHashTableA2.put(cachedUrlString, i);
                }
            }

            for (String urlString : urlStrings) {
                if (urlString.length() > 12) {
                    //System.out.println("START FROM CACHE " + urlString);
                    //construct the site's btree from its cache file
                    constructCachedWebsiteBTreeFromCache(urlString, false);
                }
            }
        } catch (IOException x) {
            System.out.println("#System Error: caught exception: " + x);
        }
    }

    /**
     * #Loading from Online Step 2#
     *
     * @param urlString
     */
    public void constructCacheWebsiteBTreeFromOnline(String urlString) {
        //make sure site hasn't already been loaded from cache
        if (!isAlreadyCached(urlString)) {
            LBTreeA2 cacheSiteBTree = new LBTreeA2();
            try {
                cacheSiteBTree.scanInWords(LMeths.fetchWebsiteText(urlString), urlString);
                if (urlString.length() > 12 && urlString.length() <= LRaf.RANDOM_ACCESS_FILE_URL_BLOCK_SIZE) {
                    if (cacheSiteBTree.size() > 0
                            && (urlString.substring(0, 8).equalsIgnoreCase("https://")
                            || urlString.substring(0, 7).equalsIgnoreCase("http://"))) {
                        //System.out.println(LMeths.formatS("\n%s %s Data Exists", urlString, "Online\n"));
                        LWebsiteA2 cacheWebsite = new LWebsiteA2(cacheSiteBTree, urlString);
                        mComparisonSystemSites.add(cacheWebsite);
                        mComparisonSitesTextArea.append(cacheWebsite.toString());
                        //add the site's url to the new urls list to be saved to cache later
                        mNewCacheWebsiteUrlStringsHashTableA2.put(urlString, cacheSiteBTree);
                    }
                }
            } catch (IOException ex) {
                mComparisonSitesTextArea.append("\n[#System Warning:\n Connection to " + urlString + " failed...\n]");
            }
        }
    }

    /**
     * #Loading CACHE Step 3#:
     *
     * @param urlString
     * @param isSearchBTreeRequest
     */
    public void constructCachedWebsiteBTreeFromCache(String urlString, boolean isSearchBTreeRequest) {
        try {
            //load the site btree data from cache or recache from online
            if (!newVersionOfSiteAvailable(urlString)) {
                loadSiteBTreeFromCache(urlString, isSearchBTreeRequest);
            } else if (newVersionOfSiteAvailable(urlString)) {
                recacheSiteBTreeFromOnline(urlString);
            }
        } catch (IOException x) {
        }
    }

    public boolean newVersionOfSiteAvailable(String urlString) {
        //get the website's cache file path
        Path path = Paths.get(LRaf.mWebsitesCacheDirectory + LRaf.filterUrlToFileName(urlString) + ".dat");

        //check the site's cache file's last modified date
        long siteLastCacheDate = LRaf.getFileLastModifiedDateFromFile(path.toFile());
        /*
        compare the site's cache file's last modified date to the site's HTTP header last update date
         *note: if dateA is greater than dateB, dateA is more recent
         */
        return LHttp.lastModifiedFromUrlString(urlString) > siteLastCacheDate;
    }

    /**
     * #Loading CACHE Step 4#:
     *
     * loads a site's BTree data from cache
     *
     * @param urlString
     *
     * @param isSearchBTreeRequest
     * @throws IOException
     * @throws NumberFormatException
     */
    public void loadSiteBTreeFromCache(String urlString, boolean isSearchBTreeRequest)
            throws IOException, NumberFormatException {
        //create the site's website object
        LWebsiteA2 cachedWebsite = new LWebsiteA2(urlString, 0);

        if (isSearchBTreeRequest) {
            //initialize the search site's BTree keys as this.
            mSearchSiteWords = cachedWebsite.readInBtreeWordsFromRaf(urlString);
        } else {
            //make sure the cached url string is not the same as the search url string
            if (!urlString.equalsIgnoreCase(mSearchUrlString)) {
                LRaf websitesCacheRandAF = getRandomAccessFileByUrlString(urlString);
                //make sure there's enough words in the cache to even do a camparison
                if (websitesCacheRandAF.length() / LRaf.BTREE_COMPOSITE_DATA_BLOCK_SIZE > 0) {
                    //compare the site's btree data to the search site's btree data
                    cachedWebsite.compareToSearchSite(urlString, mSearchSiteWords);
                    //add to system's comparison sites
                    mComparisonSystemSites.add(cachedWebsite);
                    //add to the list of already compared sites
                    mComparedSites.add(cachedWebsite);
                    //display the cached site's info
                    mComparisonSitesTextArea.setText("Sites Compared: " + mComparingCounter++);
                    //mComparisonSitesTextArea.append(cachedWebsite.toString());
                    //add site's url to the list of already cached urls that won't be cached at close
                    mCachedWebsiteUrlStringsHashTableA2.put(urlString, 1);
                }
            }
        }
    }

    
    /**
    * new version of this site is available online since we last cached it.
    * Delete the existing cache file, then reconstruct the site's BTree
    * from online data and cache it.
     * @param urlString
    */
    public void recacheSiteBTreeFromOnline(String urlString) throws IOException {
        destroyFileByUrlString(urlString);
        //construct the site's btree from online
        constructCacheWebsiteBTreeFromOnline(urlString);
    }

    /**
     * destroys a file by fetching its file path 
     * by use of the provided corresponding URL string
     * @param urlString used to create file path
     * @throws IOException 
     */
    public void destroyFileByUrlString(String urlString) throws IOException {
        //get the website's cache file path
        Path path = Paths.get(LRaf.mWebsitesCacheDirectory + LRaf.filterUrlToFileName(urlString) + ".dat");
        //destroy its file
        Files.deleteIfExists(path);
    }

    public static LRaf getWebsitesCacheIndexRandomAccessFile() {
        LRaf websitesCacheIndexRandomAccessFile = null;
        try {
            websitesCacheIndexRandomAccessFile = new LRaf(LRaf.RANDOM_ACCESS_FILE_URL_BLOCK_SIZE, LRaf.mWebsitesCacheIndexFilePath);
        } catch (IOException ex) {
            Logger.getLogger(Assignment2Frame5.class.getName()).log(Level.SEVERE, null, ex);
        }
        return websitesCacheIndexRandomAccessFile;
    }

    /**
     * ****************************************************************************************************
     * ******************************* LOADING HELPERS
     * ****************************************************
     * ****************************************************************************************************
     */
    public void beforeLoad() {
        mResultsTextArea.setText("Loading, just a second ...\n\n");
        mSearchButton.setEnabled(false);
    }

    public void postLoad() {
        if (mSystemActivityFrame != null) {
            mSystemActivityFrame.dispatchEvent(new WindowEvent(mSystemActivityFrame, WindowEvent.WINDOW_CLOSING));
        }

        mComparisonSitesTextArea.setText("******\nLoading Complete!\n*******\n" + mComparisonSitesTextArea.getText());
        mSearchTextField.setText("");        
        mSearchButton.setText("Search");
        mSearchButton.setEnabled(true);
        mSearchTextField.setEnabled(true);
    }

    /**
     * *************************************************************************************************
     *
     * *********************************************SAVING
     * FUNCTIONS************************************
     *
     * *************************************************************************************************
     */
    public void duringSaveCacheSites(JTextArea updateUIArea) {
        LRaf mWebsitesCacheIndexRandAF = getWebsitesCacheIndexRandomAccessFile();
        saveWebsitesDataToCache(mWebsitesCacheIndexRandAF);
    }

    Thread saveWebsiteCacheIndexThread = new Thread() {
        @Override
        public void run() {
            saveWebsitesDataToCache(mWebsitesCacheIndexRandomAccessFile);
        }
    };

    public void saveWebsitesDataToCache(LRaf mWebsitesCacheIndexRandAF) {
        if (mWebsitesCacheIndexRandAF != null) {
            try {
                //1. move to end of the file to append new data
                mWebsitesCacheIndexRandAF.seek(mWebsitesCacheIndexRandAF.length());
            } catch (IOException ex) {
                Logger.getLogger(Assignment2Frame5.class.getName()).log(Level.SEVERE, null, ex);
            }

            //2. go through all the sites that have not been saved to the cache
            for (Object cacheWebsiteUrlString : mNewCacheWebsiteUrlStringsHashTableA2.keySet()) {
                //3. make sure the site is not already in the cache
                if (!isAlreadyCached(cacheWebsiteUrlString.toString())) {
                    //System.out.print("\nSaving [ " + cacheWebsiteUrlString + " ]'s data to cache");

                    //save the site's data to cache
                    writeSiteDataToCache(cacheWebsiteUrlString);
                    //add the site's url to the cache index
                    saveSiteUrlToCacheIndex(cacheWebsiteUrlString, mWebsitesCacheIndexRandAF);
                }
            }

            //4. clear the new cache webistes hashtable so we don't recache these sites
            mNewCacheWebsiteUrlStringsHashTableA2 = new LHashTableA2();
            //System.out.println("\n\n#System Note: Done saving cache.");
        }
        System.out.println("[#System Note:\nSaving Cache Complete.\n]");
    }

    /**
     * writes the BTree of the provided URL string to its own cache Random
     * Access File
     *
     * @param cacheWebsiteUrlString
     */
    public void writeSiteDataToCache(Object cacheWebsiteUrlString) {
        LRaf websitesCacheRandAF = getRandomAccessFileByUrlString(cacheWebsiteUrlString);
        //save each key from the btree to site's cache file
        for (LWebsiteA2 website : mComparisonSystemSites) {
            //check if current website's url corresponds to the site url attemping save
            if (website.getUrlString().equalsIgnoreCase(cacheWebsiteUrlString.toString())) {
                //get each key from site's btree and write it to the site's random access file cache
                website.getWordsFrequencyBTree().setRaf(websitesCacheRandAF);
                try {
                    website.getWordsFrequencyBTree().writeToCacheRAF(websitesCacheRandAF);
                } catch (IOException ex) {
                    Logger.getLogger(Assignment2Frame5.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Writes the provided URL string to the cache index for later access
     *
     * @param cacheWebsiteUrlString
     * @param mWebsitesCacheIndexRandAF
     */
    public void saveSiteUrlToCacheIndex(Object cacheWebsiteUrlString, LRaf mWebsitesCacheIndexRandAF) {
        //save the site's url to the cache index
        try {
            String cachedWebsiteUrlStringDataBlock = LRaf.createDataBlock(cacheWebsiteUrlString.toString(),
                    LRaf.RANDOM_ACCESS_FILE_URL_BLOCK_SIZE);
            //get the hash for the data block and return its seek position for writing
            long cacheSiteUrlRafSeekPosition = getUrlHashAbsoluteSeekPositionByLinearProbingRaf(cacheWebsiteUrlString.toString());
            //get the seek position for the data and seek to it to begin writing
            mWebsitesCacheIndexRandAF.seek(cacheSiteUrlRafSeekPosition);
            //write the data at that block position in the file
            mWebsitesCacheIndexRandAF.write(cachedWebsiteUrlStringDataBlock.getBytes());

            /*
            add this site's url to the hashtable of already cached sites 
            so its not unnecisarily recached
            */
            mCachedWebsiteUrlStringsHashTableA2.put(cacheWebsiteUrlString, 1);
        } catch (IOException ex) {
        }
    }

    /**
     * linearly probes the hash-based cache index to find the correct position
     * in the random access file to either start read or write from
     *
     * @param urlString
     * @return
     */
    public long getUrlHashAbsoluteSeekPositionByLinearProbingRaf(String urlString) {
        String urlStringDataBlock = LRaf.createDataBlock(urlString,
                LRaf.RANDOM_ACCESS_FILE_URL_BLOCK_SIZE);

        //get the hash for the data block and return its seek position
        long urlRafSeekPosition = getUrlHashAndReturnInitialSeekPosition(urlStringDataBlock, 1);

        //get the datablock at that position
        String urlStringAtHashPositionInCacheIndex = getDataBlockAtPositionFromCacheIndex(urlRafSeekPosition);

        int i = 1;
        //linearly probe until we find the correct spot for the datablock
        while (!LRaf.removePadding(
                urlStringAtHashPositionInCacheIndex).equalsIgnoreCase(urlString)
                && !urlStringAtHashPositionInCacheIndex.trim().isEmpty()) {
            //increase i
            i++;
            //get the next seek position with i as the shifter
            urlRafSeekPosition = getUrlHashAndReturnInitialSeekPosition(urlStringDataBlock, i);
            //get the datablock at that position to be compared to the provided datablock
            urlStringAtHashPositionInCacheIndex = getDataBlockAtPositionFromCacheIndex(urlRafSeekPosition);
        }
        return urlRafSeekPosition;
    }

    /**
     * used by the getUrlHashAbsoluteSeekPositionByLinearProbingRaf() method to
     * get the a seek position by a URL string and a Random Access File block
     * size multiplier.
     *
     * As the multiplier increases, the return value of this method will
     * increase by a factor of the multiplier
     *
     * @param urlString
     * @param multiplier
     * @return
     */
    public long getUrlHashAndReturnInitialSeekPosition(String urlString, int multiplier) {
        int dataBlockHash = LRaf.getDataBlockHash(
                urlString, LRaf.RANDOM_ACCESS_FILE_URL_BLOCK_SIZE);
        
        long cacheSiteUrlRafSeekPosition = LRaf.getDataBlockSeekPosition(
                dataBlockHash,
                LRaf.RANDOM_ACCESS_FILE_URL_BLOCK_SIZE * multiplier);
        
        return cacheSiteUrlRafSeekPosition;
    }

    public String getDataBlockAtPositionFromCacheIndex(long urlRafSeekPosition) {
        String urlStringAtHashPositionInCacheIndex = "";
        try {
            mWebsitesCacheIndexRandomAccessFile.seek(urlRafSeekPosition);
            urlStringAtHashPositionInCacheIndex = new String(
                    LRaf.readFromRaf(LRaf.mWebsitesCacheIndexFilePath, (int) urlRafSeekPosition,
                            LRaf.RANDOM_ACCESS_FILE_URL_BLOCK_SIZE));
        } catch (IOException ex) {
            Logger.getLogger(Assignment2Frame5.class.getName()).log(Level.SEVERE, null, ex);
        }
        return urlStringAtHashPositionInCacheIndex;
    }

    public LRaf getRandomAccessFileByUrlString(Object cacheWebsiteUrlString) {
        //get the website's btree cache file
        LRaf websitesCacheRandAF = LRaf.getRandomAccessFileFromFileName(
                LRaf.filterUrlToFileName(cacheWebsiteUrlString.toString()) + LRaf.FILE_EXT);
        
        return websitesCacheRandAF;
    }

}