package org.easysoc.plugins.treadle.simulator;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SimulatorWindow extends JFrame {

    private static SimulatorPanel simulatorPanel;

    public SimulatorWindow(String firrtlFile, Project project) {
        setTitle("Treadle - " + firrtlFile);
        setSize(800,600);
//        setSize(900,650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        simulatorPanel = new SimulatorPanel(firrtlFile, project);
        JRootPane rootPane = getRootPane();
        rootPane.setGlassPane(new IdeGlassPaneImpl(rootPane, false));
        rootPane.getContentPane().add(simulatorPanel);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (simulatorPanel != null) {
                    simulatorPanel.dispose();
                    simulatorPanel = null;
                }
            }
        });
    }

    public static DataContext getDataContext() {
        return DataManager.getInstance().getDataContext(simulatorPanel);
    }

    public static SimulatorPanel getSimulatorPanel() {
        return simulatorPanel;
    }
}
