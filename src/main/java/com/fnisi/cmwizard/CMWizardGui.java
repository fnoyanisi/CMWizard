package com.fnisi.cmwizard;

import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

class CMWizardGui extends JPanel implements PropertyChangeListener {
    private JProgressBar progressBar;
    private XMLReader xmlReader;
    private final String iconFileName = "cmwizard-64.png";

    /**
     * Invoked when task's progress property changes.
     * required by the PropertyChangeListener interface
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public CMWizardGui() {
        super();

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
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
            add(imgPanel);
        } else {
            JLabel imgLabel = new JLabel("Missing image");
            imgLabel.setHorizontalAlignment(JLabel.CENTER);
            add(imgLabel);
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
                    Task task = new Task();
                    task.addPropertyChangeListener(this);

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

        add(buttonPanel);
    }
}