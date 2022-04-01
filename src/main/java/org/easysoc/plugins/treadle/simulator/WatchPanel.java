package org.easysoc.plugins.treadle.simulator;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.SmartList;
import org.easysoc.plugins.treadle.browser.WaveJSONBrowser;
import org.easysoc.plugins.treadle.model.Reorderable;
import org.easysoc.plugins.treadle.utils.datatransfer.TableRowTransferHandler;
import org.easysoc.plugins.treadle.setting.ProjectConfig;
import org.easysoc.plugins.treadle.utils.DataKeys;
import treadle.TreadleTester;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class WatchPanel {

    private JPanel watchPanel;
    private JBTable table;
    private TableModel tableModel;

    private TreadleTester treadleTester;
    private WaveJSONBrowser waveJSONBrowser;
    private SimulatorPanel simulatorPanel;

    private SmartList<String> watchSymbols = new SmartList<>();

    public WatchPanel() {
        tableModel = new TableModel();
        table = new JBTable(tableModel);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowSelectionAllowed(true);
//        table.setRowSelectionInterval(0, 0);
//        table.getColumnModel().getColumn(0).setPreferredWidth(350);
//        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//                setBorder(noFocusBorder);
                setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
                return this;
            }
        });
        table.setDragEnabled(true);
        table.setDropMode(DropMode.INSERT_ROWS);
        table.getTransferHandler();
        table.setTransferHandler(new TableRowTransferHandler(table));

        watchPanel.add(new JBScrollPane(table, JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JBScrollPane.HORIZONTAL_SCROLLBAR_NEVER),  BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return watchPanel;
    }

    public SmartList<String> getWatchSymbols() {
        return watchSymbols;
    }

    public void updateSymbol(String symbol) {
        if (watchSymbols.contains(symbol)) {
            watchSymbols.remove(symbol);
        } else {
            watchSymbols.add(symbol);
        }

//        Collections.sort(watchSymbols);   // we should support drag and drop
//        System.out.println(watchSymbols);
        tableModel.update(true);
    }

    public void stepUpdate() {
        if (!watchSymbols.isEmpty()) {
            tableModel.update(false);
        }
    }

    public void init() {
        if (treadleTester == null) {
            treadleTester = SimulatorWindow.getDataContext().getData(DataKeys.TREADLE_TESTER);
            waveJSONBrowser = SimulatorWindow.getDataContext().getData(DataKeys.WAVEJSON_BROWSER);
            simulatorPanel = SimulatorWindow.getSimulatorPanel();

            Project project = SimulatorWindow.getDataContext().getData(CommonDataKeys.PROJECT);
            String firrtlFile = SimulatorWindow.getDataContext().getData(DataKeys.FIRRTL_FILE);
            SmartList<String> symbols = ProjectConfig.getInstance(project).getWatchSymbols(firrtlFile);
            if (symbols != null) {
                watchSymbols = symbols;
                tableModel.update(true);
            }
        }
    }

    public void poke(String symbol) {
        if (!watchSymbols.isEmpty()) {
            tableModel.poke(symbol);
        }
    }

    public class TableModel extends AbstractTableModel implements Reorderable {

        String[] columnNames = {"Symbol", "Value"};

        String[][] data;

        public TableModel() {
            data = new String[0][0];
        }

        public void update(boolean rebuild) {
            int format = simulatorPanel.getOutFormat();
            if (rebuild) {  // add or remove Symbol
                data = new String[watchSymbols.size()][columnNames.length];
                for (int i = 0, j = watchSymbols.size(); i < j; i++) {
                    String symbol = watchSymbols.get(i);
                    data[i][0] = symbol;
                    data[i][1] = treadleTester.peek(symbol).toString(format);
                }
            } else {    // step
                for (int i = 0, j = watchSymbols.size(); i < j; i++) {
                    String symbol = watchSymbols.get(i);
                    data[i][1] = treadleTester.peek(symbol).toString(format);
                }
            }
            fireTableDataChanged();
            if (treadleTester.cycleCount() > 0) {
                String waveJSON = treadleTester.waveformValues(watchSymbols.toArray(new String[0]),0,-1).toJsonString();
                waveJSONBrowser.renderWaveForm(waveJSON);
            }
        }

        public void poke(String symbol) {
            int row = watchSymbols.indexOf(symbol);
            if (row != -1) {
                data[row][1] = treadleTester.peek(symbol).toString(simulatorPanel.getOutFormat());
            }
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public void reorder(int fromIndex, int toIndex) {
//            watchSymbols.add(toIndex, watchSymbols.remove(fromIndex));
            // https://stackoverflow.com/a/36011672
            String fromValue = watchSymbols.get(fromIndex);
            int delta = fromIndex < toIndex ? 1 : -1;
            for (int i = fromIndex; i != toIndex; i += delta) {
                watchSymbols.set(i, watchSymbols.get(i + delta));
            }
            watchSymbols.set(toIndex, fromValue);

            update(true);
        }
    }
}
