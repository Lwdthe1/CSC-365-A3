/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import Classes.EdgeA3;
import Classes.NodeA3;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import util.FileImporter;
import util.LFile;
import util.LJSoup;
import util.LMeths;

/**
 *
 * @author Lwdthe1
 */
public class A3Frame2 extends JFrame implements ActionListener {

    public static final String EDGES_FILE_NAME = "edgesA3.txt";
    final String mSystemWebsiteUrlStringsFile = "websitesA3.txt";
    String mSearchTextFieldHint = "enter a URL ...";
    String mSearchUrlString = "";

    private int MAX_BASE_SITES = 10;
    LHashTable mSearchSiteHashTable;
    ArrayList<LWebsite> mSystemSites = new ArrayList<>();
    ArrayList<LWebsite> mComparedSites;

    ArrayList<EdgeA3> mComparedEdges = new ArrayList<>();

    JButton mHelpButton;
    JButton mSaveButton;

    JTextField mSearchTextField;
    JTextField mResultsTitleTextField;
    JTextField mSimilarSitesTitleTextField;

    JTextArea mResultsTextArea;
    JTextArea mSimilarSitesTextArea;
    
    public static final String mSaveButtonText = "Save";
    

    public A3Frame2(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addComponents(getContentPane());
        if(LFile.exists(EDGES_FILE_NAME)) loadInEdges(); 
        else populateComparisonSitesThread.start();
        
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
        mSaveButton = new JButton(mSaveButtonText);
        mSearchTextField = new JTextField();
        mSearchTextField.setMinimumSize(new Dimension(20, 30));
        //add the control panel's components
        controlPanel.add(mHelpButton, controlPanelLayout);
        controlPanel.add(mSearchTextField);
        controlPanel.add(mSaveButton);

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
        mSaveButton.addActionListener(this);
        mHelpButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equalsIgnoreCase(mSaveButtonText)) {
            //thread to do search asynchronisly
            Thread doSearchThread = new Thread() {
                @Override
                public void run() {
                    createEdges();
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
        mSaveButton.setEnabled(false);

        Scanner fileScanner = FileImporter.importFileInScanner(mSystemWebsiteUrlStringsFile);

        while (fileScanner.hasNext() && mSystemSites.size() <= MAX_BASE_SITES) {
            String urlString = fileScanner.next();
            createComparisonSiteFromOnline(urlString);
            addExtrenalSitesToComparisonSites(urlString);
            mResultsTextArea.setText("Progress: " + mSystemSites.size()+"");
        }

        mSimilarSitesTextArea.setText("******Loading Complete!*******\n\n" + mSimilarSitesTextArea.getText());
        mSearchTextField.setText("");
        mResultsTextArea.setText("");
        mSaveButton.setText(mSaveButtonText);
        mSaveButton.setEnabled(true);
        mSearchTextField.setEnabled(true);
    }

    public void createComparisonSiteFromOnline(String urlString) {
        LHashTable comparisonSiteHashTable = new LHashTable();

        try {
            comparisonSiteHashTable.scanInWords(LMeths.fetchWebsiteText(urlString));
            LWebsite comparisonWebsite = new LWebsite(comparisonSiteHashTable, urlString);
            if (comparisonSiteHashTable.size() > 1) {
                mSystemSites.add(comparisonWebsite);
            }
        } catch (IOException ex) {
            mSimilarSitesTextArea.append("\n\n#SYSTEM WARNING:\n Connection to " + urlString + " failed...");
        }
    }

    public void addExtrenalSitesToComparisonSites(String urlString) {
        ArrayList<String> externalLinks = LJSoup.getPageExternalLinks(urlString);
        for (String externalLinkUrlString : externalLinks) {
            if (mSystemSites.size() <= MAX_BASE_SITES) {
                createComparisonSiteFromOnline(externalLinkUrlString);
            }
        }
    }
    
    private void createEdges() {
        int progressCounter = 0;
        mSaveButton.setEnabled(false);
        mResultsTextArea.setText(LMeths.formatS("[# Creating Edges From %d Sites #]", 
                mSystemSites.size()));
        for(LWebsite site : mSystemSites){
            progressCounter++;
            String urlStringA = site.getUrlString();
            //System.out.println(LMeths.formatS( "\n####" +progressCounter + " Creating edges from %s\n", urlStringA));
            compareBaseSiteToItsExternalSites(urlStringA);
        }
        saveEdgestoFile();
    }

    /**
     * compares the data from the site of the provided url to all the data of external sites that it links to
     * and creates the edges between them
     * @param baseUrlString 
     */
    public void compareBaseSiteToItsExternalSites(String baseUrlString) {
        String urlStringA = baseUrlString;
        LHashTable baseSiteHashTable = new LHashTable();
        try {
            baseSiteHashTable.scanInWords(LMeths.fetchWebsiteText(urlStringA));
        } catch (IOException ex) {
        }

        ArrayList<String> externalLinks = LJSoup.getPageExternalLinks(urlStringA);
        int progressCounter = 0;
        for (String externalLinkUrlString : externalLinks) {
            String urlStringB = externalLinkUrlString;
            progressCounter++;
            LHashTable externalSiteHashTable = new LHashTable();
            try {
                externalSiteHashTable.scanInWords(LMeths.fetchWebsiteText(urlStringB));
            } catch (IOException ex) {
            }
            LWebsite comparisonWebsite = new LWebsite(externalSiteHashTable, urlStringB);

            int weight = (int) LHashTable.compare(baseSiteHashTable, comparisonWebsite).getSimilarityPercentage();
            createEdge(urlStringA, urlStringB, weight);
            mResultsTextArea.setText(LMeths.formatS("%d Edges Created", mComparedEdges.size()) );
        }
    }

    public void createEdge(String urlStringA, String urlStringB, int weight) {
        EdgeA3 comparedEdge = new EdgeA3(new NodeA3(urlStringA), new NodeA3(urlStringB), weight);
        if(weight>0) mComparedEdges.add(comparedEdge);
        //System.out.println(comparedEdge.toString());
    }

    private void saveEdgestoFile() {
        System.out.println("Saving edges to file: " + EDGES_FILE_NAME);
        LFile.saveSerializableObjecttoFile(mComparedEdges, EDGES_FILE_NAME);
        mResultsTextArea.setText(LMeths.formatS("[# Save Completed #]\n\nSaved %d Edges to %d", 
                mComparedEdges.size(), EDGES_FILE_NAME ));
        mSaveButton.setEnabled(true);
    }
    
    private void loadInEdges(){
        System.out.println(LMeths.formatS("#Loading edges from %s", EDGES_FILE_NAME));
        mComparedEdges = ( ArrayList<EdgeA3> ) LFile.loadSerializedObjectFromFile(EDGES_FILE_NAME);
        System.out.println( LMeths.formatS("#Loaded %d edges", mComparedEdges.size()) );
    }
}