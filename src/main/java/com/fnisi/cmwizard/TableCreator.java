package com.fnisi.cmwizard;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class TableCreator {

    private final CmXmlReader cmXmlReader;
    private final Color headerColor, gridColor, fgColor, bgColor, selectionBgColor, selectionFgColor;
    private final Font headerFont;
    private Task task;
    private JTable table;
    private Set<Integer> selectedColumns;
    private List<Integer> selectedRows;

    public TableCreator(CmXmlReader cmXmlReader, Task task) {
        this(cmXmlReader);
        setTask(task);
    }

    public TableCreator(CmXmlReader cmXmlReader) {
        this.cmXmlReader = cmXmlReader;
        this.headerColor = new Color(198, 198, 198);
        this.gridColor = new Color(0, 0, 0);
        this.bgColor = new Color(250, 250, 250);
        this.fgColor = new Color(0, 0, 0);
        this.selectionBgColor = new Color(32, 85, 199);
        this.selectionFgColor = new Color(255, 255, 255);
        this.headerFont = new Font("Arial", Font.PLAIN, 12);
        this.task = null;
        this.selectedColumns = new HashSet<>();
        this.selectedRows = new ArrayList<>();
        this.table = null;
    }

    public void setTask(Task t) {
        this.task = t;
    }

    public void showGui() {
        JFrame frame = new JFrame("TabbedPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(createRemoveCheckedButton(), BorderLayout.NORTH);
        frame.add(createTabs(), BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private JComponent createRemoveCheckedButton(){
        JButton button = new JButton("Remove checked rows");
        button.addActionListener((ActionEvent event) -> {

        });
        return button;
    }

    private JComponent createTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(12, 234, 170, 81));

        if (cmXmlReader != null) {
            final int totalRows = cmXmlReader.getTotalNumberOfManagedObjects();
            int rowsSoFar = 0;
            for (String moClassName : cmXmlReader.getManagedObjectClassNames()) {
                List<String> moClassProperties = cmXmlReader.getPropertiesOf(moClassName);
                Vector<String> header = new Vector<>(moClassProperties);
                header.add(0, "Checked");
                header.add(1, "Name");
                Vector<Vector<Object>> data = new Vector<>();

                // for each managedObject, iterate through its properties
                // and construct a vector that will be used for the JTable
                for (ManagedObject mo : cmXmlReader.getManagedObjectsOf(moClassName)) {
                    Map<String, String> properties = mo.getProperties();

                    // while populating the data, use the properties list returned from
                    // ManagedObjectClass.getPropertiesOf() method since this includes every
                    // possible properties.
                    Vector<Object> row = new Vector<>();
                    row.add(Boolean.FALSE);
                    row.add(1, mo.getName());
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

                table = new JTable(data, header) {
                    // make the first column non-editable
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return column != 1;
                    }

                    // display check boxes for the first column
                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                        if (columnIndex == 0)
                            return Boolean.class;
                        else
                            return super.getColumnClass(columnIndex);
                    }

                    // match the format of the first column with the header's
                    @Override
                    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                        Component c = super.prepareRenderer(renderer, row, column);
                        if (column == 0 || column == 1) {
                            c.setBackground(headerColor);
                            c.setFont(headerFont);
                        } else {
                            c.setBackground(bgColor);
                        }

                        if (isCellSelected(row, column)) {
                            c.setBackground(selectionBgColor);
                            c.setForeground(selectionFgColor);
                        } else if (column != 0 && column != 1) {
                            c.setBackground(bgColor);
                            c.setForeground(fgColor);
                        }
                        return c;
                    }
                };

                // enable selection
                table.setColumnSelectionAllowed(true);
                table.setCellSelectionEnabled(true);
                table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

                table.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                        // clear the column selection
                        selectedColumns.clear();
                    }
                });

                // add a MouseListener to column header so that the user can select
                // a column when the header is clicked
                table.getTableHeader().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                        table.clearSelection();

                        // Ctrl + click to select columns
                        if (e.isControlDown()) {
                            int clickedColumn = table.convertColumnIndexToModel(table.columnAtPoint(e.getPoint()));
                            selectedColumns.add(clickedColumn);

                            for (Integer column : selectedColumns) {
                                table.addColumnSelectionInterval(column, column);
                                //once column has been selected, select all rows from 0 to the end of that column
                                table.setRowSelectionInterval(0, table.getRowCount() - 1);
                            }
                        } else {
                            selectedColumns.clear();
                        }
                    }
                });
                table.getModel().addTableModelListener(new TableModelListener() {
                    @Override
                    public void tableChanged(TableModelEvent e) {
                        if (e.getColumn() == 0) {
                            int row = e.getFirstRow();
                            TableModel model = (TableModel) e.getSource();
                            Boolean checked = (Boolean) model.getValueAt(row, 0);
                            System.out.println(row + ",0 = " + checked);
                        }
                    }
                });
                table.setPreferredScrollableViewportSize(table.getPreferredSize());
                table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
                table.setGridColor(gridColor);
                table.getTableHeader().setFont(headerFont);
                table.getTableHeader().setBackground(headerColor);
                table.setShowGrid(true);
                table.setAutoCreateRowSorter(true);

                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.add(table.getTableHeader());

                tabbedPane.addTab(moClassName,scrollPane);
            }
        }
        return tabbedPane;
    }
}