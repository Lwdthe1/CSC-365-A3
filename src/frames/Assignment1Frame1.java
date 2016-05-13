/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Lwdthe1
 */
public class Assignment1Frame1 extends JFrame implements ActionListener{
    
    JButton mHelpButton;
    JButton mSearchButton;
    
    JTextField mSearchTextField;
    JTextField mHelpTitleTextField;
    JTextField mResultsTitleTextField;
    JTextField mSimilarSitesTitleTextField;
    
    JTextArea mHelpTextArea;
    JTextArea mResultsTextArea;
    JTextArea mSimilarSitesTextArea;
    

    public Assignment1Frame1(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponents(getContentPane());
        addListeners();
        setVisible(true);
    }

    private void addComponents(Container contentPane) {
        JPanel controlPanel = createControlPanel();
        JPanel contentPanel = createMasterContentPanel();
        
        JPanel helpPanel = createHelpPanel();
        JPanel resultsPanel = createResultsPanel();
        JPanel similarSitesPanel = createSimilarSitesPanel();
      
        contentPanel.add(helpPanel, BorderLayout.WEST);
        contentPanel.add(resultsPanel, BorderLayout.CENTER);
        contentPanel.add(similarSitesPanel, BorderLayout.EAST);
        
        contentPane.setLayout(new BorderLayout());
        contentPane.add(controlPanel, BorderLayout.NORTH);
        contentPane.add(contentPanel, BorderLayout.CENTER);
        
        makeContentTextObjectsUneditable();
    }

    private JPanel createMasterContentPanel() {
        //create the master content panel to hold the three sub content panels
        JPanel contentPanel = new JPanel();
        BorderLayout contentPanelLayout = new BorderLayout();
        contentPanel.setLayout(contentPanelLayout);
        
        return contentPanel;
    }

    private JPanel createControlPanel() {
        //create control panel
        JPanel controlPanel = new JPanel();
        FlowLayout controlPanelLayout = new FlowLayout();
        controlPanel.setLayout(controlPanelLayout);
        //components for control panel
        mHelpButton = new JButton("Help");
        mSearchButton = new JButton("Search");
        mSearchTextField = new JTextField("enter a URL ...");
        //add the control panel's components
        controlPanel.add(mHelpButton);
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
        mHelpTitleTextField = new JTextField("Help");
        mHelpTextArea = new JTextArea();
        //add the help panel's components
        helpPanel.add(mHelpTitleTextField, BorderLayout.NORTH);
        helpPanel.add(mHelpTextArea, BorderLayout.CENTER);
        
        return helpPanel;
    }
    
    private JPanel createResultsPanel() {
        //create the results panel
        JPanel resultsPanel = new JPanel();
        BorderLayout resultsPanelLayout = new BorderLayout();
        resultsPanel.setLayout(resultsPanelLayout);
        //components for the results panel
        mResultsTitleTextField = new JTextField("Site's String Frequencies");
        mResultsTextArea = new JTextArea();
        //add the results panel's components
        resultsPanel.add(mResultsTitleTextField, BorderLayout.NORTH);
        resultsPanel.add(mResultsTextArea, BorderLayout.CENTER);
        
        return resultsPanel;
    }
    
    private JPanel createSimilarSitesPanel() {
        //create the similarSites panel
        JPanel similarSitesPanel = new JPanel();
        BorderLayout similarSitesPanelLayout = new BorderLayout();
        similarSitesPanel.setLayout(similarSitesPanelLayout);
        similarSitesPanel.setBackground(Color.BLACK);
        //components for the similarSites panel
        mSimilarSitesTitleTextField = new JTextField("Similar Sites");
        mSimilarSitesTextArea = new JTextArea();
        
        //add the similarSites panel's components
        similarSitesPanel.add(mSimilarSitesTitleTextField, BorderLayout.NORTH);
        similarSitesPanel.add(mSimilarSitesTextArea, BorderLayout.CENTER);
        
        return similarSitesPanel;
    }

    private void addListeners() {
        //not being used in this iteration of the frame
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        //not being used in this iteration of the frame
    }

    private void makeContentTextObjectsUneditable() {
        if(confirmContentTextObjectsNotNull()){
            mHelpTitleTextField.setEditable(false);
            mHelpTextArea.setEditable(false);

            mResultsTitleTextField.setEditable(false);
            mResultsTextArea.setEditable(false);

            mSimilarSitesTitleTextField.setEditable(false);
            mSimilarSitesTextArea.setEditable(false);
        }
    }

    private boolean confirmContentTextObjectsNotNull() {
        return 
                mHelpTitleTextField!= null 
                && mHelpTextArea!=null
                && mResultsTitleTextField!=null
                && mResultsTextArea!=null
                && mSimilarSitesTitleTextField!=null
                && mSimilarSitesTextArea!=null;
    }
}
