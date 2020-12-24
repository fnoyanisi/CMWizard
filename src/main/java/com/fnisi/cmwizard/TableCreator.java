package com.fnisi.cmwizard;

import javax.swing.*;
import java.awt.*;

public class TableCreator {
    private JTabbedPane tabbedPane;
    private XMLReader xmlReader;

    public TableCreator(XMLReader xmlReader) {
        this.xmlReader = xmlReader;
        this.tabbedPane = new JTabbedPane();
    }

    public JComponent createTabs() {
        tabbedPane.setBackground(new Color(255,0,0));
        JPanel tabPanel = new JPanel();
        tabPanel.setLayout(new BorderLayout());
        tabPanel.setBackground(new Color(0,255,0));

        if (xmlReader != null) {
            for (String tabName: xmlReader.getMoClasses()) {
                //tabbedPane.addTab(tabName, makeTable());
            }
        }

        tabPanel.add(tabbedPane, BorderLayout.CENTER);
        return tabPanel;
    }
}
