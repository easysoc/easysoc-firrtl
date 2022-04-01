package org.easysoc.plugins.treadle.manager;

import com.intellij.ui.treeStructure.SimpleTree;
import scala.jdk.javaapi.CollectionConverters;
import treadle.TreadleTester;
import treadle.executable.ExecutionEngine;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.List;
import java.util.stream.Collectors;

public class ViewManager {

    private SimpleTree tree;

    private ExecutionEngine engine;

    public ViewManager(TreadleTester treadleTester,SimpleTree tree) {
        this.tree = tree;

        engine = treadleTester.engine();
    }

    public void loadData() {
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
//        root.removeAllChildren();

        DefaultMutableTreeNode inputNode = new DefaultMutableTreeNode("Input");
        root.add(inputNode);

        List<String> inputs = CollectionConverters.asJava(engine.symbolTable().inputPortsNames().toSeq())
                .stream().sorted().collect(Collectors.toList());
        for (String in : inputs) {
            if (!in.equals("clock")) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(in);
                inputNode.add(node);
            }
        }

        DefaultMutableTreeNode outputNode = new DefaultMutableTreeNode("Output");
        root.add(outputNode);

        List<String> outputs = CollectionConverters.asJava(engine.symbolTable().outputPortsNames().toSeq())
                .stream().sorted().collect(Collectors.toList());
        for (String out : outputs) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(out);
            outputNode.add(node);
        }

        DefaultMutableTreeNode registerNode = new DefaultMutableTreeNode("Register");
        root.add(registerNode);

        List<String> registers = CollectionConverters.asJava(engine.symbolTable().registerNames().toSeq())
                .stream().sorted().collect(Collectors.toList());
        for (String register : registers) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(register);
            registerNode.add(node);
        }

//        System.out.println(engine.symbolTable().moduleMemoryToMemorySymbol());

//        tree.updateUI();
        treeModel.reload();
        tree.expandPath(new TreePath(outputNode.getPath()));
    }
}
