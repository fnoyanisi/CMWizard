package com.fnisi.cmwizard;

import org.xml.sax.SAXException;
import sun.lwawt.macosx.CSystemTray;

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

public class CMWizard {
    private static XMLReader xmlReader;
    private static JFrame frame;
    private static final int width = 640;
    private static final int height = 480;

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {

        //Create and set up the window.
        frame = new JFrame("CM Wizard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(width, height));
        frame.setResizable(false);

        // The main layout is a vertical box
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        // Tabs panel
        JPanel tabsPanel = new JPanel();
        tabsPanel.setBackground(new Color(0,0,255));

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
                    tabsPanel.add(createTabs());
                    frame.revalidate();
                    frame.repaint();

                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(() -> {
                        for (Map.Entry<String, List<ManagedObject>> entry: xmlReader.getManagedObjects().entrySet()) {
                            System.out.println("entries for " + entry.getKey());
                            System.out.println("===============================");
                            for (ManagedObject mo: entry.getValue()){
                                System.out.println("\t" + mo.getName());
                                for (Map.Entry<String, String> p : mo.getProperties().entrySet()){
                                    System.out.println("\t\t- " + p.getKey() + "\t= " + p.getValue());
                                }
                            }
                        }
                    });

                    executor.shutdown();

                } catch (ParserConfigurationException parserConfigurationException) {
                    parserConfigurationException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (SAXException saxException) {
                    saxException.printStackTrace();
                }
            }
        });

        mainPanel.add(openFileButton);
        mainPanel.add(tabsPanel);

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

    private static JPanel createTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(255,0,0));
        JPanel tabPanel = new JPanel();
        tabPanel.setLayout(new BorderLayout());
        tabPanel.setBackground(new Color(0,255,0));

        if (xmlReader != null) {
            for (String tabName: xmlReader.getMoClasses()) {
                tabbedPane.addTab(tabName, makeTable());
            }
        }

        tabPanel.add(tabbedPane, BorderLayout.CENTER);
        return tabPanel;
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(CMWizard::createAndShowGUI);
    }
}