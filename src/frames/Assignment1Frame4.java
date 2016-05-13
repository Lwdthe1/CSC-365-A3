/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import Objects.LWebsite;
import Objects.LWord;
import hashtables.LHashTable;
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
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import util.FileImporter;
import util.LMeths;

/**
 *
 * @author Lwdthe1
 */
public class Assignment1Frame4 extends JFrame implements ActionListener {

    final String mSystemWebsiteUrlStringsFile = "websites.txt";
    String mSearchTextFieldHint = "enter a URL ...";
    String mSearchUrlString = "";

    LHashTable mSearchSiteHashTable;
    ArrayList<LWebsite> mSystemSites = new ArrayList<>();
    ArrayList<LWebsite> mComparedSites;

    JButton mHelpButton;
    JButton mSearchButton;

    JTextField mSearchTextField;
    JTextField mResultsTitleTextField;
    JTextField mSimilarSitesTitleTextField;

    JTextArea mResultsTextArea;
    JTextArea mSimilarSitesTextArea;

    public Assignment1Frame4(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addComponents(getContentPane());
        populateComparisonSitesThread.start();
        addActionListeners();
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
        JPanel resultsPanel = new JPanel();
        BorderLayout resultsPanelLayout = new BorderLayout();
        resultsPanel.setLayout(resultsPanelLayout);
        //components for the results panel
        resultsPanel.add(new JLabel("Site's Word Frequencies"), BorderLayout.NORTH);
        mResultsTextArea = new JTextArea();
        mResultsTextArea.setLineWrap(true);
        mResultsTextArea.setWrapStyleWord(true);
        //add the results panel's components
        resultsPanel.add(mResultsTextArea, BorderLayout.CENTER);

        return resultsPanel;
    }

    private JPanel createSimilarSitesPanel() {
        //create the similarSites panel
        JPanel similarSitesPanel = new JPanel();
        BorderLayout similarSitesPanelLayout = new BorderLayout();
        similarSitesPanel.setLayout(similarSitesPanelLayout);
        //components for the similarSites panel
        mSimilarSitesTextArea = new JTextArea();
        //add the similarSites panel's components
        similarSitesPanel.add(new JLabel("Comparison Sites (" + mSystemWebsiteUrlStringsFile + ")"), BorderLayout.NORTH);
        similarSitesPanel.add(mSimilarSitesTextArea, BorderLayout.CENTER);

        return similarSitesPanel;
    }

    private void makeContentTextObjectsUneditable() {
        if (confirmContentTextObjectsNotNull()) {
            mResultsTextArea.setEditable(false);
            mSimilarSitesTextArea.setEditable(false);
        }
    }

    private boolean confirmContentTextObjectsNotNull() {
        return mResultsTextArea != null
                && mSimilarSitesTextArea != null;
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
                    HelpFrame helpFrame = new HelpFrame("Help", 500, 500);
                }
            };
            showHelpThread.start();
        }
    }
    
    public void doSearch() {
        mSearchSiteHashTable = new LHashTable();
        
        String urlString = mSearchTextField.getText().trim();
        mSearchUrlString = urlString;
        mResultsTextArea.setText("\n\n*Working my butt off over here, just a second...\n\n");
        try {
            mSearchSiteHashTable.scanInWords(LMeths.fetchWebsiteText(urlString));
            LWebsite userSite = new LWebsite(mSearchSiteHashTable, urlString);
            mResultsTextArea.setText("");
            mResultsTextArea.append("\n\n** Total Word Instertion Frequencies: " + userSite.getTotalWordsFrequencies() + " **\n\n");

            for (LWord word : userSite.getWordsList()) {
                mResultsTextArea.append("'" + word.getWord() + "'" + " : " + word.getFrequency() + "\n ");
            }
            
            
            mSearchButton.setText("Comparing Words...");
            compareSearchSiteToSystemSites();
        } catch (MalformedURLException e) {
            mResultsTextArea.setText("#WARNING:\n You've gone screwed up now! \n\n"
                    + urlString + " is not a proper URL.\nURL must resemble http://www.website.com");
        } catch (IOException e) {
            mResultsTextArea.setText("#WARNING:\n Connection to " + urlString + " failed...");
        }
    }

    /**
     *  Similarity Metric: first step
     * compares the user's search site to the system's preloaded sites
     * displays the system site that most closely matches
     * the specific words and their frequencies of the user's search site
     */
    public void compareSearchSiteToSystemSites() {
        mComparedSites = new ArrayList<>();
        //open frame to show the background system activity
        SystemActivityFrame searchSystemActivityFrame = new SystemActivityFrame ("Search System Activity " + mSearchUrlString, 300, 900);
        
        compareEachSystemSiteToUSerSite(searchSystemActivityFrame);
        
        LWebsite mostSimilarSite = compareSystemSitesForMostSimilarToUserSite();
        displayMostSimilarSystemSite(mostSimilarSite);
        //close the background system activity frame
        searchSystemActivityFrame.dispatchEvent(new WindowEvent(searchSystemActivityFrame, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Similarity Metric: second step
    *
    * for each system site, 
    * get its word frequency similarity to the user's site
    * and add the returned LWebsite to the array list for 
    * comparison to the other system sites in next step
    */
    public void compareEachSystemSiteToUSerSite(SystemActivityFrame searchSystemActivityFrame) {
        
        for (LWebsite systemSite : mSystemSites) {
            System.out.println("");
            if( !systemSite.getUrlString().equals(mSearchUrlString)  ){
                mComparedSites.add(LHashTable.compare(mSearchSiteHashTable, systemSite, searchSystemActivityFrame));
            }
        }
    }

    /**
    * Similarity Metric: third step
    * compare the system sites to each other
    * to see which one most closely match
    * the user's site words frequency
    * @return the most similar site's data as an instance of LWebsite
    */
    public LWebsite compareSystemSitesForMostSimilarToUserSite() {
        
        double mostSimilarSiteSimilarity = 0.0;
        LWebsite mostSimilarSite = null;
        for (LWebsite comparedSite : mComparedSites) {
            if (comparedSite.getSimilarityPercentage() > mostSimilarSiteSimilarity) {
                mostSimilarSite = comparedSite;
                mostSimilarSiteSimilarity = comparedSite.getSimilarityPercentage();
            }
        }
        
        return mostSimilarSite;
    }
    
    /**
     * display the most similar system site to the user's search site
     * @param mostSimilarSite 
     */
    public void displayMostSimilarSystemSite(LWebsite mostSimilarSite) {
        if (mostSimilarSite != null) {
            mResultsTextArea.setText("\n\n*Most Similar Site*\n"
                    + mostSimilarSite.toString2() + mResultsTextArea.getText());
        } else {
            mResultsTextArea.setText("**#NO Similar Site**\n\n"
                    + mResultsTextArea.getText());
        }
    }

    //thread to populate comparison sites
    Thread populateComparisonSitesThread = new Thread() {
        @Override
        public void run() {
            populateComparisonSites();
        }
    };

    private void populateComparisonSites() {
        mSearchTextField.setText("After load, enter a URL....");
        mResultsTextArea.setText("Loading, just a second ...");
        mResultsTextArea.append("\n\nAfter load is complete, "
                + "enter a URL to fetch its word frequencies.");
        mSearchTextField.setEnabled(false);
        mSearchButton.setEnabled(false);

        Scanner fileScanner = FileImporter.importFileInScanner(mSystemWebsiteUrlStringsFile);

        while (fileScanner.hasNext()) {
            String urlString = fileScanner.next();
            LHashTable comparisonSiteHashTable = new LHashTable();

            try {
                comparisonSiteHashTable.scanInWords(LMeths.fetchWebsiteText(urlString));
                LWebsite comparisonWebsite = new LWebsite(comparisonSiteHashTable, urlString);
                mSystemSites.add(comparisonWebsite);
                mSimilarSitesTextArea.append(comparisonWebsite.toString());
            } catch (IOException ex) {
                mSimilarSitesTextArea.append("\n\n#SYSTEM WARNING:\n Connection to " + urlString + " failed...");
            }

        }
        
        mSimilarSitesTextArea.setText("******Loading Complete!*******\n\n" + mSimilarSitesTextArea.getText());
        mSearchTextField.setText("");
        mResultsTextArea.setText("");
        mSearchButton.setText("Search");
        mSearchButton.setEnabled(true);
        mSearchTextField.setEnabled(true);
    }

    public static boolean isAWord(String word) {
        return word.length() > 1 || (word.length() < 1 && word.contains("abcdefghijklmnopqrstuvwxyz"));
    }

}
