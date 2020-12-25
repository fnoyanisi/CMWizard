package com.fnisi.cmwizard;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Map;
import java.util.Vector;
import java.util.List;

public class TableCreator {
    private JTabbedPane tabbedPane;
    private XMLReader xmlReader;
    private final Color headerColor, gridColor, bgColor, selectionColor;
    private final Font headerFont;

    public TableCreator(XMLReader xmlReader) {
        this.xmlReader = xmlReader;
        this.tabbedPane = new JTabbedPane();
        this.headerColor = new Color(177, 177, 177);
        this.gridColor = new Color(71, 71, 71);
        this.bgColor = new Color(250, 250, 250);
        this.selectionColor = new Color(5, 52, 154);
        this.headerFont = new Font("Arial", Font.BOLD, 12);
    }

    public JComponent createTabs() {
        tabbedPane.setBackground(new Color(12, 234, 170, 81));

        if (xmlReader != null) {
            for (Map.Entry<String, List<ManagedObject>> entry: xmlReader.getManagedObjects().entrySet()) {
                // key -> managedObject class
                // value -> list of managed objects in this class
                Vector<String> header = new Vector<>(xmlReader.getPropertiesOf(entry.getKey()));
                header.add(0, "Name");
                Vector<Vector<String>> data = new Vector<>();

                // for each managedObject, iterate through its properties
                // and construct a vector that will be used for the JTable
                for (ManagedObject mo : entry.getValue()) {
                    Map<String, String> properties = mo.getProperties();

                    // while populating the data, use the properties list returned from
                    // getPropertiesOf() method since this includes every possible
                    // properties.
                    Vector<String> row = new Vector<>();
                    row.add(0, mo.getName());
                    for (String property: xmlReader.getPropertiesOf(entry.getKey())) {
                        if(properties.containsKey(property)) {
                            row.add(properties.get(property));
                        } else {
                            row.add("#N/A");
                        }
                    }
                    data.add(row);
                }
                JTable table = new JTable(data, header) {
                    // make the first column non-editable
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return column == 0? false : true;
                    }

                    // match the format of the first column with the header's
                    @Override
                    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                        Component c = super.prepareRenderer(renderer, row, column);
                        if (column == 0) {
                            c.setBackground(headerColor);
                            c.setFont(headerFont);
                        } else {
                            c.setBackground(bgColor);
                        }

                        if (isCellSelected(row, column)) {
                            c.setBackground(selectionColor);
                        }
                        return c;
                    }
                };
                table.setPreferredScrollableViewportSize(table.getPreferredSize());
                table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
                table.setShowGrid(true);
                table.setAutoCreateRowSorter(true);
                table.setGridColor(gridColor);
                table.getTableHeader().setFont(headerFont);
                table.getTableHeader().setBackground(headerColor);

                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.add(table.getTableHeader());

                tabbedPane.addTab(entry.getKey(),scrollPane);
            }
        }

        JFrame frame = new JFrame("TabbedPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(tabbedPane, BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        return null;
    }
}
