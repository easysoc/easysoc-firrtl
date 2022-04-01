package org.easysoc.plugins.treadle.simulator;

import com.intellij.openapi.ui.ComboBox;
import org.easysoc.plugins.treadle.utils.DataKeys;
import scala.math.BigInt;
import treadle.TreadleTester;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class PokePanel {
    private JLabel labelSymbol;
    private JLabel labelValue;
    private JTextField pokeField;
    private JButton pokeButton;
    private JPanel pokePanel;
    private JComboBox comboBoxPokeFormat;
    private JButton pokeStepButton;
    private JLabel labelWidth;

    private TotalCyclesAction totalCyclesAction;
    private WatchPanel watchPanel;
    private SimulatorPanel simulatorPanel;

    private String selectedSignal = "";
    private HashMap<String, Integer> format = new HashMap<>();

    private TreadleTester treadleTester;

    public PokePanel(TotalCyclesAction totalCyclesAction, WatchPanel watchPanel) {
        this.totalCyclesAction = totalCyclesAction;
        this.watchPanel = watchPanel;

        pokeButton.addActionListener(e -> poke());
        pokeStepButton.addActionListener(e -> {
            poke();
            step(1);
        });
        pokeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    poke();
                    e.consume();
                }
            }
        });
        format.put("DEC",10);
        format.put("HEX",16);
        format.put("BIN",2);
    }

    public JPanel getPanel() {
        return pokePanel;
    }

    public void peek(String signal, boolean canPoke) {
        selectedSignal = signal;
        if (treadleTester == null) {
            treadleTester = SimulatorWindow.getDataContext().getData(DataKeys.TREADLE_TESTER);
            watchPanel = SimulatorWindow.getDataContext().getData(DataKeys.WATCH_PANEL);
            simulatorPanel = SimulatorWindow.getSimulatorPanel();
        }
        updateSymbol();

        pokeField.setEnabled(canPoke);
        pokeButton.setEnabled(canPoke);
        pokeStepButton.setEnabled(canPoke);
        pokeField.setText("");
    }

    public void poke() {
        int radix = format.get(comboBoxPokeFormat.getSelectedItem().toString());

        BigInt pokeValue = BigInt.apply(pokeField.getText(), radix);
        treadleTester.poke(labelSymbol.getText(), pokeValue);

        updateSymbol();
        watchPanel.poke(labelSymbol.getText());
    }

    public void step(int i) {
        if (treadleTester == null) {
            treadleTester = SimulatorWindow.getDataContext().getData(DataKeys.TREADLE_TESTER);
            watchPanel = SimulatorWindow.getDataContext().getData(DataKeys.WATCH_PANEL);
            simulatorPanel = SimulatorWindow.getSimulatorPanel();
        }
        treadleTester.step(i);
        updateSymbol();
        totalCyclesAction.setText(String.valueOf(treadleTester.cycleCount()));
        watchPanel.stepUpdate();
    }

    public void updateSymbol() {
        if (!selectedSignal.isEmpty()) {
            labelSymbol.setText(selectedSignal);
            labelValue.setText(treadleTester.peek(selectedSignal).toString(simulatorPanel.getOutFormat()));
            labelWidth.setText(String.valueOf(treadleTester.engine().symbolTable().get(selectedSignal).get().bitWidth()));
        }
    }

    private void createUIComponents() {
        comboBoxPokeFormat = new ComboBox();
        comboBoxPokeFormat.addItem("DEC");
        comboBoxPokeFormat.addItem("HEX");
        comboBoxPokeFormat.addItem("BIN");

        comboBoxPokeFormat.setSelectedIndex(0);
    }
}
