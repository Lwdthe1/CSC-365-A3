/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Lwdthe1
 */
public class RunProgramFrame extends JFrame implements ActionListener {

    final String mSystemWebsiteUrlStringsFile = "websites.txt";
    String mSearchTextFieldHint = "enter a program ...";

    JButton mHelpButton;
    JButton mSearchButton;

    JTextField mSearchTextField;

    public RunProgramFrame(String title, int width, int height) {
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

        contentPane.setLayout(new BorderLayout());
        contentPane.add(controlPanel, BorderLayout.NORTH);
        contentPane.add(contentPanel, BorderLayout.CENTER);

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
        mSearchButton = new JButton("Run");
        mSearchTextField = new JTextField();
        mSearchTextField.setMinimumSize(new Dimension(20, 30));
        //add the control panel's components
        controlPanel.add(mHelpButton, controlPanelLayout);
        controlPanel.add(mSearchTextField);
        controlPanel.add(mSearchButton);

        return controlPanel;
    }

    /**
     *
     */
    public void addActionListeners() {
        mSearchButton.addActionListener(this);
        mHelpButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equalsIgnoreCase("run")) {
            //thread to do search asynchronisly
            Thread runProgramThread = new Thread() {
                @Override
                public void run() {
                    runProgram();
                }

            };
            runProgramThread.start();
        } else if (command.equalsIgnoreCase("run")) {
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

    public void runProgram() {
        mSearchButton.setEnabled(false);
        mSearchButton.setText("Running...");

        String program = mSearchTextField.getText();
        switch (program.trim()) {
            case "assignment1":
            case "assignment 1":
            case "a1":
            case "a 1":
                new Assignment1Frame4("A1F4: Web Word Frequencies", 900, 600);
                break;
            case "assignment2":
            case "assignment 2":
            case "a2":
            case "a 2":
                new Assignment2Frame5("A2F5: Web Word Frequencies", 400, 500);
                break;
        }
        mSearchButton.setText("Run");
        mSearchButton.setEnabled(true);
    }
}
