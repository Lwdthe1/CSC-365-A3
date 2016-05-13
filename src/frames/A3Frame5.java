/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import Algorithms.DijkstraAlgorithm;
import Algorithms.PrimMST;
import Classes.EdgeA3C;
import Classes.GraphA3C;
import Objects.LWebsite;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
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
public class A3Frame5 extends JFrame implements ActionListener {

    public static final String EDGES_FILE_NAME = "edgesA3D.txt";
    final String mSystemWebsiteUrlStringsFile = "websitesA3.txt";
    String mSearchTextFieldHint = "enter a URL ...";
    String mSearchUrlString = "";
    
    //thread to populate comparison sites
    Thread mPopulateBaseSitesThread;

    private boolean mContinueLoading = true;
    private final int MAX_BASE_SITES = 3;
    LHashTable mSearchSiteHashTable;
    ArrayList<LWebsite> mBaseSites = new ArrayList<>();
    ArrayList<LWebsite> mComparedSites;

    ArrayList<EdgeA3C> mComparedEdges = new ArrayList<>();
    GraphA3C mSitesGraph = new GraphA3C();

    JButton mPathButton;
    JButton mSaveButton;

    JTextField mPathTextField;
    JTextField mResultsTitleTextField;
    JTextField mSimilarSitesTitleTextField;

    JTextArea mResultsTextArea;
    JTextArea mSimilarSitesTextArea;
    
    public static final String SAVE_BUTTON_TEXT = "Save";
    private String LOAD_BUTTON_TEXT = "Load";
    private String PATH_BUTTON_TEXT = "Show Path";
    

    public A3Frame5(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addComponents(getContentPane());
        if(LFile.exists(EDGES_FILE_NAME)) loadEdges(); 
        else {
            initPopulateBaseSites();
            mPopulateBaseSitesThread.start();
        }
        
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
        mPathButton = new JButton(PATH_BUTTON_TEXT);
        mSaveButton = new JButton(SAVE_BUTTON_TEXT);
        mPathTextField = new JTextField();
        mPathTextField.setMinimumSize(new Dimension(20, 30));
        //add the control panel's components
        controlPanel.add(mSaveButton);
        controlPanel.add(mPathTextField);
        controlPanel.add(mPathButton, controlPanelLayout);
        

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
        mPathButton.addActionListener(this);
    }

    /*******************ACTION PERFORME***************/
    /** @param event*******/
    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equalsIgnoreCase(SAVE_BUTTON_TEXT)) {
            //thread to do search asynchronisly
            Thread doSaveThread = new Thread() {
                @Override
                public void run() {
                    createAndSaveEdges();
                }
            };
            doSaveThread.start();
        } else if (command.equalsIgnoreCase(LOAD_BUTTON_TEXT)) {
            //thread to do search asynchronisly
            Thread loadEdges = new Thread() {
                @Override
                public void run() {
                    loadEdges();
                }
            };
            loadEdges.start();
        } else if(command.equalsIgnoreCase("Fetch Sites")){
            initPopulateBaseSites();
            mPopulateBaseSitesThread.start();
        } else if (command.equalsIgnoreCase("End Loading")) {
            mPopulateBaseSitesThread.interrupt();
            mContinueLoading = false;
            mResultsTextArea.append(LMeths.formatS("[# Ended Loading.\n "
                    + "Found %d sites #]\n\n %s", mBaseSites.size(),
                    mResultsTextArea.getText()));
            
            mSimilarSitesTextArea.setText("******Loading Interrupted!*******\n\n" 
                    + mSimilarSitesTextArea.getText());
            mPathTextField.setText("");
            mResultsTextArea.setText("");
            mSaveButton.setText(SAVE_BUTTON_TEXT);
            mSaveButton.setEnabled(true);
            setPathInputs(true, false);
            mPathButton.setText(PATH_BUTTON_TEXT);
        } else if (command.equalsIgnoreCase(PATH_BUTTON_TEXT)) {
            setPathInputs(false, false);
            
            String input = mPathTextField.getText();
            
            /**
             * thread to populate help text area
             */
            Thread showShortestPathThread = new Thread() {
                @Override
                public void run() {
                    int source = -1;
                    int destination= -1;
                    if(!input.isEmpty() && input.contains(",")){
                        source = Integer.parseInt(input.substring(0, input.indexOf(",")).trim());
                        destination = Integer.parseInt(input.substring(input.indexOf(",")+1).trim());
                    }
                    if(source>-1 && destination>-1 && mComparedEdges.size() > source && mComparedEdges.size() > destination ){
                        dijkShortestPath(source, destination);
                    } else {
                        mResultsTextArea.setText("Enter a source and a destination to calculate shortest path (i.g. 0, 1)");
                    }
                    
                    setPathInputs(true, true);
                }
            };
            showShortestPathThread.start();
        }
    }

    public void initPopulateBaseSites() {
        mPopulateBaseSitesThread = new Thread() {
            @Override
            public void run() {
                mContinueLoading = true;
                populateComparisonSitesEdges();
                mSimilarSitesTextArea.setText("******Loading Complete!*******\n\n"
                        + mSimilarSitesTextArea.getText());
                mPathTextField.setText("");
                mResultsTextArea.setText("");
                mSaveButton.setText(SAVE_BUTTON_TEXT);
                mSaveButton.setEnabled(true);
                setPathInputs(true, true);
                mPathButton.setText(PATH_BUTTON_TEXT);
                
                Thread doSaveThread = new Thread() {
                    @Override
                    public void run() {
                        createAndSaveEdges();
                        //System.exit(0);
                    }
                };
                doSaveThread.start();
            }
        };
    }

    private void populateComparisonSitesEdges() {
        mPathButton.setText("End Loading");
        mPathTextField.setText("After load, enter a URL....");
        mResultsTextArea.setText("Loading, just a second ...");
        mResultsTextArea.append("\n\nAfter load is complete, "
                + "enter a URL to fetch its word frequencies.");
        setPathInputs(false, true);
        mSaveButton.setEnabled(false);

        Scanner fileScanner = FileImporter.importFileInScanner(mSystemWebsiteUrlStringsFile);
        
        while (fileScanner.hasNext() && mBaseSites.size() <= MAX_BASE_SITES && mContinueLoading) {
            String urlString = fileScanner.next();
            createComparisonSiteFromOnline(urlString);
            addExtrenalSitesToComparisonSites(urlString, MAX_BASE_SITES);
            mResultsTextArea.setText("Progress: " + mBaseSites.size()+"");
        }

        
    }

    public void 
        createComparisonSiteFromOnline(String urlString) {
        LHashTable comparisonSiteHashTable = new LHashTable();

        try {
            comparisonSiteHashTable.scanInWords(LMeths.fetchWebsiteText(urlString));
            LWebsite baseSite = new LWebsite(comparisonSiteHashTable, urlString);
            if (comparisonSiteHashTable.size() > 1 && mContinueLoading) {
                if(LJSoup.isReachable(urlString)){ 
                    if(!mBaseSites.contains(baseSite))mBaseSites.add(baseSite);
                    //System.out.println("added " + urlString);
                    mResultsTextArea.setText("Progress: " + mBaseSites.size()+"");
                } else System.out.println(urlString + " is unreachable"); 
            }
        } catch (IOException ex) {
            mSimilarSitesTextArea.append("\n\n#SYSTEM WARNING:\n Connection to " + urlString + " failed...");
        }
    }

    public void addExtrenalSitesToComparisonSites(String urlString, int max) {
        ArrayList<String> externalLinks = LJSoup.getPageExternalLinks(urlString);
        
        for (String externalLinkUrlString : externalLinks) {
            if(mContinueLoading){
                //if (mBaseSites.size() <= MAX_BASE_SITES*2) {
                    createComparisonSiteFromOnline(externalLinkUrlString);
                    if(max == 0) continue;
                    addExtrenalSitesToComparisonSites(externalLinkUrlString, max-1);
                    /*System.out.println("RECURSE on : " + externalLinkUrlString 
                            + " || max = " + max+ " base sites = " 
                            + mBaseSites.size());*/
                //}
            }
        }
    }
    
    private void createAndSaveEdges() {
        int progressCounter = 0;
        mSaveButton.setEnabled(false);
        mResultsTextArea.setText(LMeths.formatS("[# Creating Edges From %d Sites #]", 
                mBaseSites.size()));
        for(LWebsite site : mBaseSites){
            progressCounter++;
            String urlStringA = site.getUrlString();
            compareBaseSiteToItsExternalSites(urlStringA, progressCounter);
        }
        
        saveEdgestoFile();
    }

    /**
     * compares the data from the site of the provided url to all the data of external sites that it links to
     * and creates the edges between them
     * @param baseUrlString 
     * @param baseSiteNum 
     */
    public void compareBaseSiteToItsExternalSites(String baseUrlString, int baseSiteNum) {
        String urlStringA = baseUrlString;
        LHashTable baseSiteHashTable = new LHashTable();
        try {
            baseSiteHashTable.scanInWords(LMeths.fetchWebsiteText(urlStringA));
        } catch (IOException ex) {
        }

        ArrayList<String> externalLinks = LJSoup.getPageExternalLinks(urlStringA);
        int numBaseSites = mBaseSites.size();
        int numExLinks = externalLinks.size();
        int progressCounter = 0;
        for (String externalLinkUrlString : externalLinks) {
            progressCounter++;
            String urlStringB = externalLinkUrlString;
            LHashTable externalSiteHashTable = new LHashTable();
            try {
                externalSiteHashTable.scanInWords(LMeths.fetchWebsiteText(urlStringB));
            } catch (IOException ex) {}
            LWebsite comparisonWebsite = new LWebsite(externalSiteHashTable, urlStringB);

            int weight = (int) LHashTable.compare(baseSiteHashTable, comparisonWebsite).getSimilarityPercentage();
            addEdge(urlStringA, urlStringB, weight);
            mResultsTextArea.setText(LMeths.formatS("%d/%d Edges from %s %d/%d", 
                    progressCounter, numExLinks, baseUrlString, baseSiteNum, numBaseSites) );
        }
    }

    public void addEdge(String urlStringA, String urlStringB, int weight) {
        EdgeA3C comparedEdge = new EdgeA3C(urlStringA, urlStringB, weight);
        if (weight>0) mComparedEdges.add(comparedEdge);
    }

    private void saveEdgestoFile() {
        System.out.println(LMeths.formatS("Saving %d edges to %s: ", mComparedEdges.size(), EDGES_FILE_NAME));
        if(mComparedEdges.size()>0){
            LFile.saveSerializableObjecttoFile(mComparedEdges, EDGES_FILE_NAME);
            mResultsTextArea.setText(LMeths.formatS("[# Save Completed #]\n\nSaved %d Edges to %s", 
                mComparedEdges.size(), EDGES_FILE_NAME ));
            mSaveButton.setText(LOAD_BUTTON_TEXT);
        } else {
            mResultsTextArea.setText("[# Save Failed #]");
            mSaveButton.setText("Fetch Sites");
        }
        mSaveButton.setEnabled(true);
        
    }
    
    private void loadEdges(){
        System.out.println(LMeths.formatS("#Loading edges from %s", EDGES_FILE_NAME));
        mComparedEdges = ( ArrayList<EdgeA3C> ) LFile.loadSerializedObjectFromFile(EDGES_FILE_NAME);
        
        if( mComparedEdges!=null ){
            System.out.println( LMeths.formatS("#Loaded %d edges", mComparedEdges.size()) );
            //LIterator.print(mComparedEdges);
            mSitesGraph.addEdges(mComparedEdges);
            mSaveButton.setText(LOAD_BUTTON_TEXT);
            
            mSimilarSitesTextArea.append("\n\n[# Source Sites #]");
            LinkedList pathNodes = new LinkedList<>();
            int progressCounter = -1;
            for(EdgeA3C edge: mComparedEdges){
                progressCounter++;
                if(!pathNodes.contains(edge.getNodeA())){
                    mSimilarSitesTextArea.append("\n"+progressCounter + ": " + edge.getNodeA());
                    pathNodes.add(edge.getNodeA());
                }
                else pathNodes.add(edge.getNodeA());
            }
            
            mSimilarSitesTextArea.append("\n\n[# Destination Sites #]");
            progressCounter = 0;
            for(EdgeA3C edge: mComparedEdges){
                if(!pathNodes.contains(edge.getNodeB())){
                    progressCounter++;
                    mSimilarSitesTextArea.append("\n"+progressCounter + ": " + edge.getNodeB());
                    pathNodes.add(edge.getNodeB());
                }
            }
            
            calculateSpanningTrees();
        } else System.out.println("Loading Failed");
    }

    public void calculateSpanningTrees() {
        PrimMST mst = new PrimMST(mSitesGraph);
        /*for (EdgeA3C e : mst.edges()) {
        System.out.println(e + " | ");
        }*/
        System.out.printf("\n[# Spanning Tree Cost: %.5f #]\n", mst.weight());
        if(mst.check(mSitesGraph))
            System.out.println("Graph is Fully Connected");
        else System.out.println("Graph NOT Fully Connected");
    }

    public void dijkShortestPath(int source, int destination) {
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(mSitesGraph);
        LinkedList<String> path = getShortestPath(dijkstra,
                mComparedEdges.get(source).getNodeA(),
                mComparedEdges.get(destination).getNodeB());
        
        
        if(path!=null && path.size()>0){
            mResultsTextArea.setText("# Shortest Path:\n " 
                    + mComparedEdges.get(source).getNodeA() +" to " 
                    + mComparedEdges.get(destination).getNodeB() 
                    + "\n________________________________________________\n" );
            
            for (String vertex : path) {
                mResultsTextArea.append(" \n--> "+ "[ "+ vertex + " ]");
            }
        } else  mResultsTextArea.setText("No path exists");
    }

    public LinkedList<String> getShortestPath(DijkstraAlgorithm dijkstra, String source, String destination) {
        //create distances from source to all nodes
        setPathInputs(true, false);
        dijkstra.perform(source);
        //get the shortest path from the source to the destination
        LinkedList<String> shortestPath = dijkstra.getShortestPath(destination);

        setPathInputs(false, false);
        return shortestPath;

    }

    public void setPathInputs(boolean switch1, boolean switch2) {
        mPathTextField.setEnabled(switch1);
        mPathButton.setEnabled(switch2);
    }
}