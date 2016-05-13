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
public class HelpFrame extends JFrame implements ActionListener {

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

    public HelpFrame(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        addComponents(getContentPane());
        populateHelpTextArea();
        setVisible(true);
    }

    private void addComponents(Container contentPane) {
        JPanel contentPanel = createMasterContentPanel();
        JPanel helpPanel = createHelpPanel();
        
        JScrollPane helpScrollPane = new JScrollPane(helpPanel);
        contentPanel.add(helpScrollPane);
        
        contentPane.setLayout(new BorderLayout());
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


    private void makeContentTextObjectsUneditable() {
        if (confirmContentTextObjectsNotNull()) {
            mHelpTextArea.setEditable(false);
        }
    }

    private boolean confirmContentTextObjectsNotNull() {
        return mHelpTextArea != null;
    } 
    
    private void populateHelpTextArea() {
        mHelpTextArea.setText("Google.com is a wonderful source of information...");
        mHelpTextArea.append("\n\n*");
        mHelpTextArea.append("Try these sites:\n\n");
        mHelpTextArea.append("http://cs.oswego.edu/~ldaniel/\n\n");
        mHelpTextArea.append("https://en.wikipedia.org/wiki/Stack_overflow\n\n");
        mHelpTextArea.append("https://en.wikipedia.org/wiki/Delaware\n\n");
        mHelpTextArea.append("https://en.wikipedia.org/wiki/Apple\n\n");
        mHelpTextArea.append("https://en.wikipedia.org/wiki/Swarthmore\n\n");
        mHelpTextArea.append("https://en.wikipedia.org/wiki/Russia\n\n");
        mHelpTextArea.append("https://en.wikipedia.org/wiki/Mexico\n\n");
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        //
    }
}
