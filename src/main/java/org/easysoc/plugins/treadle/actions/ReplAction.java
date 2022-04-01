package org.easysoc.plugins.treadle.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.plugins.terminal.TerminalTabState;
import org.jetbrains.plugins.terminal.TerminalView;

public class ReplAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        System.setProperty("FIRRTL_FILE", file.getPath());

        TerminalView terminalView = TerminalView.getInstance(project);
        TerminalTabState state = new TerminalTabState();
        state.myTabName = file.getName();
        // history file will in this directory
        state.myWorkingDirectory = file.getParent().getPath();

        terminalView.createNewSession(terminalView.getTerminalRunner(), state);
    }
}
