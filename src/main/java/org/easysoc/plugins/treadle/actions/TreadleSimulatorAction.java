package org.easysoc.plugins.treadle.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.easysoc.plugins.treadle.simulator.SimulatorWindow;

public class TreadleSimulatorAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        ApplicationManager.getApplication().invokeLater(() -> {
            new SimulatorWindow(file.getPath(), e.getProject()).setVisible(true);
        });
    }
}
