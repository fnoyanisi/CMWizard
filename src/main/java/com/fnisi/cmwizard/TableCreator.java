package com.fnisi.cmwizard;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Vector;
import java.util.List;

public class TableCreator {
    private final JTabbedPane tabbedPane;
    private final CmXmlReader cmXmlReader;
    private final Color headerColor, gridColor, bgColor, selectionColor;
    private final Font headerFont;
    private Task task;

    public TableCreator(CmXmlReader cmXmlReader) {
        this.cmXmlReader = cmXmlReader;
        this.tabbedPane = new JTabbedPane();
        this.headerColor = new Color(198, 198, 198);
        this.gridColor = new Color(0, 0, 0);
        this.bgColor = new Color(250, 250, 250);
        this.selectionColor = new Color(5, 52, 154);
        this.headerFont = new Font("Arial", Font.PLAIN, 12);
        this.task = null;
    }

    public void setTask(Task t) {
        this.task = t;
    }

    public JComponent createTabs() {
        tabbedPane.setBackground(new Color(12, 234, 170, 81));

        if (cmXmlReader != null) {
            final int totalRows = cmXmlReader.getTotalNumberOfManagedObjects();
            int rowsSoFar = 0;
            for (String moClassName : cmXmlReader.getManagedObjectClassNames()) {
                List<String> moClassProperties = cmXmlReader.getPropertiesOf(moClassName);
                Vector<String> header = new Vector<>(moClassProperties);
                header.add(0, "Name");
                Vector<Vector<String>> data = new Vector<>();

                // for each managedObject, iterate through its properties
                // and construct a vector that will be used for the JTable
                for (ManagedObject mo : cmXmlReader.getManagedObjectsOf(moClassName)) {
                    Map<String, String> properties = mo.getProperties();

                    // while populating the data, use the properties list returned from
                    // ManagedObjectClass.getPropertiesOf() method since this includes every
                    // possible properties.
                    Vector<String> row = new Vector<>();
                    row.add(0, mo.getName());
                    for (String property: moClassProperties) {
                        row.add(properties.getOrDefault(property, "#N/A"));
                    }
                    rowsSoFar++;
                    if (task != null) {
                        int p = (rowsSoFar * 100) / totalRows;
                        task.updateProgress(p);
                    }
                    data.add(row);
                }
                JTable table = new JTable(data, header) {
                    // make the first column non-editable
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return column != 0;
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
                // add a MouseListener to column header so that the user can select
                // a column when the header is clicked
                table.getTableHeader().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                        int clickedIndex = table.convertColumnIndexToModel(table.columnAtPoint(e.getPoint()));
                        table.setColumnSelectionInterval(clickedIndex, clickedIndex); //selects which column will have all its rows selected
                        table.setRowSelectionInterval(0, table.getRowCount() - 1); //once column has been selected, select all rows from 0 to the end of that column
                    }
                });
                table.setPreferredScrollableViewportSize(table.getPreferredSize());
                table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
                table.setGridColor(gridColor);
                table.getTableHeader().setFont(headerFont);
                table.getTableHeader().setBackground(headerColor);
                table.setShowGrid(true);
                table.setAutoCreateRowSorter(true);
                table.setColumnSelectionAllowed(true);
                table.setRowSelectionAllowed(true);
                table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                table.setCellSelectionEnabled(true);

                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.add(table.getTableHeader());

                tabbedPane.addTab(moClassName,scrollPane);
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
