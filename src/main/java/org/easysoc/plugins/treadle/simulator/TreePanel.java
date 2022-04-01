package org.easysoc.plugins.treadle.simulator;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.SimpleTree;
import org.easysoc.plugins.treadle.listener.TreeMouseListener;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

public class TreePanel extends SimpleToolWindowPanel {

    private final SimpleTree tree;

    public TreePanel() {
        super(true,true);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        tree = new SimpleTree(new DefaultTreeModel(root)) {

            private final JLabel myPane = new JLabel("Initializing ...", JLabel.CENTER);

            {
                myPane.setOpaque(false);
            }

            @Override
            protected void paintComponent(Graphics g) {
                try {
                    super.paintComponent(g);
                }catch (Exception e){
                    return;
                }


                DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
                // 如果没有子元素显示定制内容
                if (!root.isLeaf()) {
                    return;
                }

                myPane.setFont(getFont());
                myPane.setBackground(getBackground());
                myPane.setForeground(getForeground());
                Rectangle bounds = getBounds();
                myPane.setBounds(0, 0, bounds.width - 10, bounds.height);

                Graphics g2 = g.create(bounds.x, bounds.y, bounds.width, bounds.height);
                try {
                    myPane.paint(g2);
                } finally {
                    g2.dispose();
                }
            }
        };
        tree.getEmptyText().clear();
        //tree.setRowHeight(21);
        tree.setOpaque(true);
//        tree.setCellRenderer(new CustomTreeCellRenderer());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRootVisible(false);
        tree.addMouseListener(new TreeMouseListener(tree));
//        tree.addTreeWillExpandListener(new TreeWillListener(tree, toolWindow, project));


        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        JBScrollPane contentScrollPanel = new JBScrollPane(tree, JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JBScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        groupPanel.add(contentScrollPanel);

        setContent(groupPanel);
    }

    public SimpleTree getTree() {
        return tree;
    }
}
