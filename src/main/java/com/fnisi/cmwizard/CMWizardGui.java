package com.fnisi.cmwizard;

import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CMWizardGui {
    private static XMLReader xmlReader;
    private static JFrame frame;

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {

        //Create and set up the window.
        frame = new JFrame("CM Wizard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // The main layout is a vertical box
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        // Tabs panel
        JPanel tabsPanel = new JPanel();

        // Open file button
        JButton openFileButton = new JButton("Open XML file");
        openFileButton.addActionListener((ActionEvent e) -> {
            final JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("xml files (*.xml)", "xml"));
            fc.setDialogTitle("Open XML File");

            int returnVal = fc.showOpenDialog(openFileButton.getParent());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    xmlReader = new XMLReader(file);
                    TableCreator tc = new TableCreator(xmlReader);
                    tabsPanel.removeAll();
                    //ExecutorService executorService = Executors.newSingleThreadExecutor();
                    //executorService.submit(() -> {
                        tc.createTabs();
                    //});

                } catch (ParserConfigurationException parserConfigurationException) {
                    parserConfigurationException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (SAXException saxException) {
                    saxException.printStackTrace();
                }
            }
        });

        // About CM Wizard button
        JButton aboutButton = new JButton("About CM Wizard");
        aboutButton.addActionListener((ActionEvent e) -> {
            new AboutWindowGui();
        });

        mainPanel.add(openFileButton);
        mainPanel.add(aboutButton);

        //Display the window.
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private static JComponent makeTable() {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel("Panel #4 (has a preferred size of 410 x 50).");
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(CMWizardGui::createAndShowGUI);
    }
}