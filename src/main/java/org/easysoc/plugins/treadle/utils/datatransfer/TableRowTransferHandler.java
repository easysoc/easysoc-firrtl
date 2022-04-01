package org.easysoc.plugins.treadle.utils.datatransfer;

import com.intellij.ui.table.JBTable;
import org.easysoc.plugins.treadle.model.Reorderable;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;

public class TableRowTransferHandler extends TransferHandler {
    private final DataFlavor localObjectFlavor = new ActivationDataFlavor(Integer.class, "application/x-java-Integer;class=java.lang.Integer", "Integer Row Index");
    private JBTable           table;
    private boolean copy = false;

    public TableRowTransferHandler(JBTable table) {
        this.table = table;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        if (copy) {
            return createCopyTransferable();
        }
        return new DataHandler(table.getSelectedRow(), localObjectFlavor.getMimeType());
    }

    private Transferable createCopyTransferable() {
        if (!table.getRowSelectionAllowed() && !table.getColumnSelectionAllowed()) {
            return null;
        } else {
            int[] rows;
            int colCount;
            int counter;
            if (!table.getRowSelectionAllowed()) {
                colCount = table.getRowCount();
                rows = new int[colCount];

                for(counter = 0; counter < colCount; rows[counter] = counter++) {
                }
            } else {
                rows = table.getSelectedRows();
            }

            int[] cols;
            if (!table.getColumnSelectionAllowed()) {
                colCount = table.getColumnCount();
                cols = new int[colCount];

                for(counter = 0; counter < colCount; cols[counter] = counter++) {
                }
            } else {
                cols = table.getSelectedColumns();
            }

            if (rows != null && cols != null && rows.length != 0 && cols.length != 0) {
                StringBuilder plainStr = new StringBuilder();
                StringBuilder htmlStr = new StringBuilder();
                htmlStr.append("<html>\n<body>\n<table>\n");

                for(int row = 0; row < rows.length; ++row) {
                    htmlStr.append("<tr>\n");

                    for(int col = 0; col < cols.length; ++col) {
                        Object obj = table.getValueAt(rows[row], cols[col]);
                        String val = obj == null ? "" : obj.toString();
                        plainStr.append(val).append('\t');
                        htmlStr.append("  <td>").append(val).append("</td>\n");
                    }

                    plainStr.deleteCharAt(plainStr.length() - 1).append('\n');
                    htmlStr.append("</tr>\n");
                }

                plainStr.deleteCharAt(plainStr.length() - 1);
                htmlStr.append("</table>\n</body>\n</html>");
                return new CopyTransferable(plainStr.toString(), htmlStr.toString());
            } else {
                return null;
            }
        }
    }

    @Override
    public void exportToClipboard(JComponent comp, Clipboard clip, int action) throws IllegalStateException {
        copy = true;
        super.exportToClipboard(comp, clip, action);
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport info) {
        boolean b = info.getComponent() == table && info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
        table.setCursor(b ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
        return b;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
        JTable target = (JTable) info.getComponent();
        JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
        int index = dl.getRow();
//        int max = table.getModel().getRowCount();
//        if (index < 0 || index > max)
//            index = max;
        target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        try {
            int rowFrom = (Integer) info.getTransferable().getTransferData(localObjectFlavor);
            if (rowFrom != index) { // to up
                if (rowFrom > index) {
                    ((Reorderable)table.getModel()).reorder(rowFrom, index);
                    target.getSelectionModel().addSelectionInterval(index, index);
                    return true;
                } else if ((index - rowFrom) > 1) {
                    ((Reorderable)table.getModel()).reorder(rowFrom, index - 1);
                    index--;
                    target.getSelectionModel().addSelectionInterval(index, index);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void exportDone(JComponent c, Transferable t, int act) {
        if (copy) copy = false;
        if ((act == TransferHandler.MOVE) || (act == TransferHandler.NONE)) {
            table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }


}
