package org.easysoc.plugins.treadle.simulator;

import com.intellij.openapi.util.NlsActions;
import com.intellij.openapi.vcs.ui.TextFieldAction;

import javax.swing.*;
import java.awt.*;

public class TotalCyclesAction extends TextFieldAction {
    public TotalCyclesAction(@NlsActions.ActionText String text, @NlsActions.ActionDescription String description, Icon icon, int initSize) {
        super(text, description, icon, initSize);

        myField.setEditable(false);
        myField.setBackground(Color.getColor("Panel.background"));
        setText("0");
    }

    public void setText(String text) {
        myField.setText("Total Cycles: " + text);
    }
}
