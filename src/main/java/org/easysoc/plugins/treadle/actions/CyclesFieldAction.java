package org.easysoc.plugins.treadle.actions;

import com.intellij.openapi.vcs.ui.TextFieldAction;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CyclesFieldAction extends TextFieldAction {
    private int cycles = 1;

    public CyclesFieldAction(String text, String description, Icon icon, int initSize) {
        super(text, description, icon, initSize);
        myField.setText("1");
        myField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                e.consume();
                save();
            }
        });
    }

    private void save() {
        if (!myField.getText().isEmpty()) {
            try {
                cycles = Integer.parseInt(myField.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public int getCycles() {
        return cycles;
    }
}
