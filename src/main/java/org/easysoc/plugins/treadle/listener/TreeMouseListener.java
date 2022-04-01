package org.easysoc.plugins.treadle.listener;

import com.intellij.ui.treeStructure.SimpleTree;
import org.easysoc.plugins.treadle.simulator.PokePanel;
import org.easysoc.plugins.treadle.simulator.SimulatorWindow;
import org.easysoc.plugins.treadle.simulator.WatchPanel;
import org.easysoc.plugins.treadle.utils.DataKeys;
import treadle.TreadleTester;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TreeMouseListener extends MouseAdapter {

    private SimpleTree tree;
    private PokePanel pokePanel;
    private WatchPanel watchPanel;

    private TreadleTester treadleTester;

    public TreeMouseListener(SimpleTree tree) {
        this.tree = tree;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (pokePanel == null) {
            pokePanel = SimulatorWindow.getDataContext().getData(DataKeys.POKE_PANEL);
            watchPanel = SimulatorWindow.getDataContext().getData(DataKeys.WATCH_PANEL);

            treadleTester = SimulatorWindow.getDataContext().getData(DataKeys.TREADLE_TESTER);
        }

        TreePath selPath = tree.getClosestPathForLocation(e.getX(), e.getY());
        if (selPath != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
            if (node.isLeaf() && !node.getParent().toString().equals("root")) {
                String symbol = node.getUserObject().toString();
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (e.getClickCount() == 2) {
                        watchPanel.updateSymbol(symbol);
//                    ProgressManager.getInstance().run(new Task.Backgroundable(project,"leetcode.editor.openCode",false) {
//                        @Override
//                        public void run(@NotNull ProgressIndicator progressIndicator) {
//                        }
//                    });
                    } else {
                        pokePanel.peek(symbol, !node.getParent().toString().equals("Output"));
                    }
                } else if (e.getButton() == 3) { //鼠标右键
                    pokePanel.peek(symbol, !node.getParent().toString().equals("Output"));
//                    final ActionManager actionManager = ActionManager.getInstance();
//                    final ActionGroup actionGroup = (ActionGroup) actionManager.getAction("leetcode.NavigatorActionsMenu");
//                    if (actionGroup != null) {
//                        actionManager.createActionPopupMenu("", actionGroup).getComponent().show(e.getComponent(), e.getX(), e.getY());
//                    }
                }
            }
        }

        e.consume();
    }
}
