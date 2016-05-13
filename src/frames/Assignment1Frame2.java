/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

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
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Lwdthe1
 */
public class Assignment1Frame2 extends JFrame implements ActionListener {

    String mSearchTextFieldHint = "enter a URL ...";

    LHashTable mSearchSiteHashTable;

    JButton mHelpButton;
    JButton mSearchButton;

    JTextField mSearchTextField;
    JTextField mHelpTitleTextField;
    JTextField mResultsTitleTextField;
    JTextField mSimilarSitesTitleTextField;

    JTextArea mHelpTextArea;
    JTextArea mResultsTextArea;
    JTextArea mSimilarSitesTextArea;

    public Assignment1Frame2(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponents(getContentPane());
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
        similarSitesPanel.add(new JLabel("Similar Sites"), BorderLayout.NORTH);
        similarSitesPanel.add(mSimilarSitesTextArea, BorderLayout.CENTER);

        return similarSitesPanel;
    }

    private void makeContentTextObjectsUneditable() {
        if (confirmContentTextObjectsNotNull()) {
            mHelpTitleTextField.setEditable(false);
            mHelpTextArea.setEditable(false);

            mResultsTitleTextField.setEditable(false);
            mResultsTextArea.setEditable(false);

            mSimilarSitesTitleTextField.setEditable(false);
            mSimilarSitesTextArea.setEditable(false);
        }
    }

    private boolean confirmContentTextObjectsNotNull() {
        return mHelpTitleTextField != null
                && mHelpTextArea != null
                && mResultsTitleTextField != null
                && mResultsTextArea != null
                && mSimilarSitesTitleTextField != null
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
            mSearchSiteHashTable = new LHashTable();
            String urlString = mSearchTextField.getText().trim();
            mResultsTextArea.setText("");
            mResultsTextArea.append("\n\n*Working my butt off over here, just a second...\n\n");
            try {
                mSearchSiteHashTable.scanInWords(getWebsiteText(urlString));
                
                int totalSiteWords = 0;
                for (Object wordV : mSearchSiteHashTable.values()) {
                    totalSiteWords += (Integer) wordV;
                }
                mResultsTextArea.append("\n\n***** Total Word Instertion Frequencies: " + totalSiteWords +" *****\n\n");

                for (Object wordK : mSearchSiteHashTable.keySet()) {
                    String wordV = mSearchSiteHashTable.get(wordK).toString();
                    mResultsTextArea.append( "'" + wordK + "'" + " : " + wordV + "\n ");
                }
            } catch (MalformedURLException e) {
                mResultsTextArea.setText("#WARNING:\n You've gone screwed up now! \n\n" + 
                        urlString + " is not a proper URL.\nURL must resemble http://www.website.com");
            } catch (IOException e) {
                mResultsTextArea.setText("#WARNING:\n Connection to " + urlString + " failed...");
            }

        } else if (command.equalsIgnoreCase("help")) {
            mHelpTextArea.setText("Google.com is a wonderful source of information...");
            mHelpTextArea.append("\n\n*");
            mHelpTextArea.append("Try these sites:\n\n");
            mHelpTextArea.append("http://cs.oswego.edu/~ldaniel/\n\n");
            mHelpTextArea.append("https://en.wikipedia.org/wiki/Lincoln\n\n");
            mHelpTextArea.append("https://www.oswego.edu\n\n");
            mHelpTextArea.append("https://en.wikipedia.org/wiki/Android\n\n");
            mHelpTextArea.append("https://en.wikipedia.org/wiki/Jordan\n\n");
            mHelpTextArea.append("http://www.lincolnwdaniel.com\n\n");
            mHelpTextArea.append("http://www.trufriend.lincolnwdaniel.com\n\n");
            mHelpTextArea.append("http://www.tuurtle.com\n\n");
            mHelpTextArea.append("https://en.wikipedia.org/wiki/America\n\n");
            mHelpTextArea.append("https://en.wikipedia.org/wiki/Africa\n\n");
            mHelpTextArea.append("https://en.wikipedia.org/wiki/Turtle\n\n");
            mHelpTextArea.append("https://en.wikipedia.org/wiki/Java\n\n");
            mHelpTextArea.append("https://en.wikipedia.org/wiki/Html\n\n");
            mHelpTextArea.append("https://en.wikipedia.org/wiki/Linux\n\n");

        }
    }

    public String getWebsiteText(String urlString) throws IOException, MalformedURLException {
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
        InputStream siteInputStream = urlConnection.getInputStream();
        InputStreamReader siteInputStreamReader = new InputStreamReader(siteInputStream);
        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuilder stringBuilder = new StringBuilder();
        while ((numCharsRead = siteInputStreamReader.read(charArray)) > 0) {
            stringBuilder.append(charArray, 0, numCharsRead);
        }
        String htmlResult = stringBuilder.toString();
        Document doc = Jsoup.parse(htmlResult);
        String allWebSiteText = doc.text();
        return allWebSiteText;
    }

    public static boolean isAWord(String word) {
        return word.length()>1 || (word.length()<1 && word.contains("abcdefghijklmnopqrstuvwxyz"));
    }

}
