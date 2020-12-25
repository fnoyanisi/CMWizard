package com.fnisi.cmwizard;

import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CMWizardGui {
    private static XMLReader xmlReader;
    private static JFrame frame;
    private static final String iconFileName = "cmwizard-64.png";

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {

        //Create and set up the window.
        frame = new JFrame("CM Wizard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
        Image myImage = null;
        URL imageUrl = CMWizardGui.class.getResource(iconFileName);
        if (imageUrl != null) {
            try {
                myImage = ImageIO.read(imageUrl);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            myImage = myImage.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            ImageIcon myImageIcon = new ImageIcon(myImage);
            JLabel imgLabel = new JLabel(myImageIcon, JLabel.CENTER);
            JPanel imgPanel = new JPanel();
            imgPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            imgPanel.add(imgLabel, gbc);
            mainPanel.add(imgPanel);
        } else {
            JLabel imgLabel = new JLabel("Missing image");
            imgLabel.setHorizontalAlignment(JLabel.CENTER);
            mainPanel.add(imgLabel);
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2,1,20,5));

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
                    tabsPanel.removeAll();
                    TableCreator tc = new TableCreator(xmlReader);
                    tc.createTabs();
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

        buttonPanel.add(openFileButton);
        buttonPanel.add(aboutButton);

        mainPanel.add(buttonPanel);

        //Display the window.
        frame.add(mainPanel);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(CMWizardGui::createAndShowGUI);
    }
}