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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.FileImporter;
import util.LMeths;

/**
 *
 * @author Lwdthe1
 */
public class Assignment1Frame3 extends JFrame implements ActionListener {

    final String mSystemWebsiteUrlStringsFile = "websites.txt";
    String mSearchTextFieldHint = "enter a URL ...";

    LHashTable mSearchSiteHashTable;
    ArrayList<LWebsite> mComparisonSites = new ArrayList<>();
    ArrayList<LWebsite> mComparedSites;

    JButton mHelpButton;
    JButton mSearchButton;

    JTextField mSearchTextField;
    JTextField mHelpTitleTextField;
    JTextField mResultsTitleTextField;
    JTextField mSimilarSitesTitleTextField;

    JTextArea mHelpTextArea;
    JTextArea mResultsTextArea;
    JTextArea mSimilarSitesTextArea;

    public Assignment1Frame3(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        addComponents(getContentPane());
        populateComparisonSitesThread.start();
        addActionListeners();
        setVisible(true);
    }

    private void addComponents(Container contentPane) {
        JPanel controlPanel = createControlPanel();
        JPanel contentPanel = createMasterContentPanel();

        JPanel helpPanel = createHelpPanel();
        JPanel resultsPanel = createResultsPanel();
        JPanel similarSitesPanel = createSimilarSitesPanel();

        JScrollPane helpScrollPane = new JScrollPane(helpPanel);
        JScrollPane resultsScrollPane = new JScrollPane(resultsPanel);
        JScrollPane similarSitesScrollPane = new JScrollPane(similarSitesPanel);

        contentPanel.add(helpScrollPane);
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
        mSearchTextField.setMinimumSize(new Dimension(20,30));
        //add the control panel's components
        controlPanel.add(mHelpButton, controlPanelLayout);
        controlPanel.add(mSearchTextField);
        controlPanel.add(mSearchButton);
        

        return controlPanel;
    }

    private JPanel createHelpPanel() {
        //create the help panel
        JPanel helpPanel = new JPanel();
        BorderLayout helpPanelLayout = new BorderLayout();
        helpPanel.setLayout(helpPanelLayout);
        //components for the help panel
        mHelpTextArea = new JTextArea();
        mHelpTextArea.setLineWrap(true);
        //add the help panel's components
        helpPanel.add(new JLabel("Help"), BorderLayout.NORTH);
        helpPanel.add(mHelpTextArea, BorderLayout.CENTER);

        return helpPanel;
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
            mHelpTextArea.setEditable(false);
            mResultsTextArea.setEditable(false);
            mSimilarSitesTextArea.setEditable(false);
        }
    }

    private boolean confirmContentTextObjectsNotNull() {
        return mHelpTextArea != null
            && mResultsTextArea != null
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
            Thread doSearchThread = new Thread(){
                 @Override
                 public void run(){
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
            Thread populateHelpTextAreaThread = new Thread(){
               @Override
               public void run(){
                   mHelpButton.setEnabled(false);
                   mHelpButton.setText("Helping...");
                   populateHelpTextArea();
                   mHelpButton.setText("Help");
                   mHelpButton.setEnabled(true);
               }
           };
            populateHelpTextAreaThread.start();
        }
    }

    public void doSearch() {
        mSearchSiteHashTable = new LHashTable();
        
        String urlString = mSearchTextField.getText().trim();
        mResultsTextArea.setText("\n\n*Working my butt off over here, just a second...\n\n");
        try {
            mSearchSiteHashTable.scanInWords(LMeths.fetchWebsiteText(urlString));
            LWebsite userSite = new LWebsite(mSearchSiteHashTable, urlString);
            mResultsTextArea.setText("");
            mResultsTextArea.append("\n\n** Total Word Instertion Frequencies: " + userSite.getTotalWordsFrequencies() +" **\n\n");
            
            for(LWord word : userSite.getWordsList()){
                mResultsTextArea.append( "'" + word.getWord() + "'" + " : " + word.getFrequency() + "\n ");
            }
            
            mSearchButton.setText("Comparing Words...");
            compareSearchSiteToSystemSites();
        } catch (MalformedURLException e) {
            mResultsTextArea.setText("#WARNING:\n You've gone screwed up now! \n\n" +
                    urlString + " is not a proper URL.\nURL must resemble http://www.website.com");
        } catch (IOException e) {
            mResultsTextArea.setText("#WARNING:\n Connection to " + urlString + " failed...");
        }
    }

    public void compareSearchSiteToSystemSites() {
        mComparedSites = new ArrayList<>();
        for(LWebsite systemSite: mComparisonSites){
            System.out.println("");
            //mComparedSites.add(LHashTable.compare(mSearchSiteHashTable, systemSite));
        }
        
        double mostSimilarSiteSimilarity = 0.0;
        LWebsite mostSimilarSite = null;
        for(LWebsite comparedSite: mComparedSites){
            if( comparedSite.getSimilarityPercentage() > mostSimilarSiteSimilarity ){
                mostSimilarSite = comparedSite;
                mostSimilarSiteSimilarity = comparedSite.getSimilarityPercentage();
            }
        }
        if(mostSimilarSite != null){
            mResultsTextArea.setText( "\n\n**Most Similar Site**\n" +
                    mostSimilarSite.toString() + mResultsTextArea.getText() );
        } else {
            mResultsTextArea.setText( "**Most Similar Site**\n\n#NONE" +
                    mResultsTextArea.getText());
        }
    }

    
    //thread to populate comparison sites
    Thread populateComparisonSitesThread = new Thread(){
         @Override
         public void run(){
            populateComparisonSites();
         }
     };   
    
    private void populateHelpTextArea() {
        mHelpTextArea.setText("Google.com is a wonderful source of information...");
        mHelpTextArea.append("\n\n*");
        mHelpTextArea.append("Try these sites:\n\n");
        mHelpTextArea.append("http://cs.oswego.edu/~ldaniel/\n\n");
        mHelpTextArea.append("https://en.wikipedia.org/wiki/Stack_overflow\n\n");
    }
    
    private void populateComparisonSites() {
        mSearchButton.setText("Search");
        mSearchTextField.setText("After load, enter a URL....");
        mResultsTextArea.setText("Loading, just a second ...");
        mHelpTextArea.setText("Loading...\nAfter load is complete, "
                + "enter a URL to fetch its word frequencies.");
        mSearchTextField.setEnabled(false);
        mSearchButton.setEnabled(false);
        mHelpButton.setEnabled(false);
        
        Scanner fileScanner = FileImporter.importFileInScanner(mSystemWebsiteUrlStringsFile);
        
        while ( fileScanner.hasNext() ){
            String urlString = fileScanner.next();
            LHashTable comparisonSiteHashTable = new LHashTable();
            
            try {
                comparisonSiteHashTable.scanInWords(LMeths.fetchWebsiteText(urlString));
                LWebsite comparisonWebsite = new LWebsite(comparisonSiteHashTable , urlString);
                mComparisonSites.add(comparisonWebsite);
                mSimilarSitesTextArea.append(comparisonWebsite.toString());
            } catch (IOException ex) {
                mSimilarSitesTextArea.append("\n\n#SYSTEM WARNING:\n Connection to " + urlString + " failed...");
            }
            
        }
        mSearchButton.setText("Search");
        mSearchTextField.setText("");
        mHelpTextArea.setText("You may now search.\nClick help for tips at any time.");
        mResultsTextArea.setText("");
        mSearchButton.setEnabled(true);
        mHelpButton.setEnabled(true);
        mSearchTextField.setEnabled(true);
    }

    public static boolean isAWord(String word) {
        return word.length()>1 || (word.length()<1 && word.contains("abcdefghijklmnopqrstuvwxyz"));
    }
}
