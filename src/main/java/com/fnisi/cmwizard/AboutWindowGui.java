package com.fnisi.cmwizard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AboutWindowGui extends JFrame {
    private final int height = 400;
    private final int width = 580;
    private final String licenseFileName = "LICENSE";

    public AboutWindowGui() {
        super("About CM Wizard");

        setMinimumSize(new Dimension(width, height));
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        Image myImage = null;
        try {
            myImage = ImageIO.read(getClass().getResource("cmwizard-64.png"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        myImage = myImage.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        ImageIcon myImageIcon = new ImageIcon(myImage);
        mainPanel.add(centeredComponent(new JLabel(myImageIcon), false, true));

        mainPanel.add(centeredComponent(new JLabel("CM Wizard"), true, true));

        mainPanel.add(centeredComponent(new JLabel("https://github.com/fnoyanisi/CMWizard"), false, true));

        InputStream inputStream = getClass().getResourceAsStream(licenseFileName);
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            JTextArea textArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(textArea);
            try {
                textArea.read(inputStreamReader, null);
                textArea.setEditable(false);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            mainPanel.add(scrollPane);
        } else {
            JPanel p = centeredComponent(new JLabel("Cannot find the " + licenseFileName + " file!"),false, false);
            mainPanel.add(p);
        }

        add(mainPanel);
        pack();
        setVisible(true);
    }

    private JPanel centeredComponent(JLabel label, boolean isBold, boolean isPacked) {
        label.setHorizontalAlignment(JLabel.CENTER);
        Font font;
        if (isBold)
            font = new Font("Verdana", Font.BOLD, 16);
        else
            font = new Font("Verdana", Font.PLAIN, 12);
        label.setFont(font);
        JPanel panel = new JPanel();
        // use GridBagLayout with default constraints for vertical alignment
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        if (isPacked) {
            panel.setMaximumSize(new Dimension(width, label.getPreferredSize().height + 10));
        }
        panel.add(label, gbc);
        return panel;
    }
}
