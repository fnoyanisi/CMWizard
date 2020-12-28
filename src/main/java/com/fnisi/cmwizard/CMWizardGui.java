package com.fnisi.cmwizard;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

class CMWizardGui extends JPanel implements PropertyChangeListener {
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private Task task;
    private boolean done;
    private final String iconFileName = "cmwizard-64.png";

    /**
     * Invoked when task's progress property changes.
     * required by the PropertyChangeListener interface
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (!done) {
            int progress = task.getProgress();
            if (progress == 0) {
                progressBar.setIndeterminate(true);
                statusLabel.setText("Reading the XML file");
            } else {
                progressBar.setIndeterminate(false);
                progressBar.setString(null);
                progressBar.setValue(progress);
                statusLabel.setText("Parsing the XML file");
            }
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public CMWizardGui() {
        super();

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        statusLabel = new JLabel("");

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new EmptyBorder(10,10,10,10));

        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.LINE_AXIS));
        upperPanel.setBorder(new EmptyBorder(5,5,5,5));

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
            upperPanel.add(imgPanel);
        } else {
            JLabel imgLabel = new JLabel("Missing image");
            imgLabel.setHorizontalAlignment(JLabel.CENTER);
            upperPanel.add(imgLabel);
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2,1,20,5));

        // Open file button
        JButton openFileButton = new JButton("Open XML file");
        openFileButton.addActionListener((ActionEvent e) -> {
            final JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("xml files (*.xml)", "xml"));
            fc.setDialogTitle("Open XML File");

            int returnVal = fc.showOpenDialog(openFileButton.getParent());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //done = false;
                task = new Task(file);
                task.addPropertyChangeListener(this);
                task.execute();
            }
        });

        // About CM Wizard button
        JButton aboutButton = new JButton("About CM Wizard");
        aboutButton.addActionListener((ActionEvent e) -> {
            new AboutWindowGui();
        });

        // status panel
        JPanel statusPanel = new JPanel();
        statusPanel.add(statusLabel);

        buttonPanel.add(openFileButton);
        buttonPanel.add(aboutButton);

        upperPanel.add(buttonPanel);

        add(upperPanel);
        add(statusPanel);
        add(progressBar);
    }
}