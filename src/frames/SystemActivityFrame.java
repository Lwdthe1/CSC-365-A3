/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JScrollPane;


/**
 *
 * @author Lwdthe1
 */
public class SystemActivityFrame extends JFrame implements ActionListener{

    public String mFrameTitle;
    public JTextArea mSystemActivityTextArea;
    //used by other parts of system
    public JButton mSystemActivityActionButton;
    public SystemActivityFrame(String title, int width, int height) {
        super(title);
        mFrameTitle = title;
        
        setSize(width, height);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addComponents(getContentPane());
        setVisible(true);
    }

    private void addComponents(Container contentPane) {
        JPanel contentPanel = createMasterContentPanel();
        JPanel helpPanel = createHelpPanel();
        
        JScrollPane systemActivityScrollPane = new JScrollPane(helpPanel);
        contentPanel.add(systemActivityScrollPane);
        
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
        mSystemActivityTextArea = new JTextArea();
        mSystemActivityActionButton = new JButton("Exit Program");
        mSystemActivityActionButton.setEnabled(false);
        mSystemActivityActionButton.setVisible(false);
        mSystemActivityTextArea.setLineWrap(true);
        //add the help panel's components
        helpPanel.add(new JLabel(mFrameTitle), BorderLayout.SOUTH);
        helpPanel.add(mSystemActivityTextArea, BorderLayout.CENTER);
        helpPanel.add(mSystemActivityActionButton, BorderLayout.NORTH);

        return helpPanel;
    }


    private void makeContentTextObjectsUneditable() {
        if (confirmContentTextObjectsNotNull()) {
            mSystemActivityTextArea.setEditable(false);
        }
    }

    private boolean confirmContentTextObjectsNotNull() {
        return mSystemActivityTextArea != null;
    } 
    
    public void appendActivity(String activity) {
        mSystemActivityTextArea.append(activity);
    }
    
    public void prependActivity(String activity) {
        mSystemActivityTextArea.setText(activity + mSystemActivityTextArea.getText());
    }

    
    @Override
    public void actionPerformed(ActionEvent event) {
       System.exit(0);
    }
}