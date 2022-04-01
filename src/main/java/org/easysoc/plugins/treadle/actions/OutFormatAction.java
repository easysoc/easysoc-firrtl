package org.easysoc.plugins.treadle.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.ex.ComboBoxAction;
import org.easysoc.plugins.treadle.simulator.SimulatorPanel;
import org.easysoc.plugins.treadle.simulator.SimulatorWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class OutFormatAction extends ComboBoxAction {

  private String format = "";

  public OutFormatAction() {
    getTemplatePresentation().setDescription("Set Output format");
  }

  @Override
  public void update(@NotNull AnActionEvent e) {
    e.getPresentation().setText(format.isEmpty()? "Output" : format);
  }

  @Override
  protected @NotNull DefaultActionGroup createPopupActionGroup(JComponent button) {
    DefaultActionGroup actionGroup = new DefaultActionGroup();
    actionGroup.add(new AnAction("DEC", "Output in decimal format", null) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        format = "DEC";
        setFormat();
      }
    });
    actionGroup.add(new AnAction("HEX", "Output in hexadecimal format", null) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        format = "HEX";
        setFormat();
      }
    });
    actionGroup.add(new AnAction("BIN", "Output in binary format", null) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        format = "BIN";
        setFormat();
      }
    });
    return actionGroup;
  }

  private void setFormat() {
    SimulatorPanel simulatorPanel = SimulatorWindow.getSimulatorPanel();
    if (format.equals("HEX")) {
      simulatorPanel.setOutFormat(16);
    } else if (format.equals("BIN")) {
      simulatorPanel.setOutFormat(2);
    } else {
      simulatorPanel.setOutFormat(10);
    }
  }
}
