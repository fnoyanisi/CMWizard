package com.fnisi.cmwizard;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.DigestException;

public class AboutWindowGui extends JFrame {
    private static final int height = 480;
    private static final int width = 640;

    public AboutWindowGui() {
        super("About CM Wizard");

        setMinimumSize(new Dimension(width, height));
        setResizable(false);
        setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        JLabel titleLabel = new JLabel("CM Wizard");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        titleLabel.setVerticalAlignment(JLabel.CENTER);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        JPanel titlePanel = new JPanel();
        //titlePanel.setMaximumSize(new Dimension(width, 30));
        titlePanel.setMaximumSize(new Dimension(width, titleLabel.getPreferredSize().height + 10));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel);

        JLabel urlLabel = new JLabel("https://github.com/fnoyanisi/CMWizard");
        urlLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        urlLabel.setVerticalAlignment(JLabel.CENTER);
        urlLabel.setHorizontalAlignment(JLabel.CENTER);
        JPanel urlPanel = new JPanel();
        urlPanel.setMaximumSize(new Dimension(width, urlLabel.getPreferredSize().height + 10));
        urlPanel.add(urlLabel);
        mainPanel.add(urlPanel);

        InputStream inputStream = getClass().getResourceAsStream("LICEN,,SE");
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            try {
                textArea.read(inputStreamReader, null);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            mainPanel.add(scrollPane);
        } else {
            JLabel label = new JLabel("CM Wizard", JLabel.CENTER);
            label.setFont(new Font("Verdana", Font.PLAIN, 12));
            mainPanel.add(label);
        }

        add(mainPanel);
        pack();
        setVisible(true);
    }
}
