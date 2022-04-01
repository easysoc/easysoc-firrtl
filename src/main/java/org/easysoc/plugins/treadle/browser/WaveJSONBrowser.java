package org.easysoc.plugins.treadle.browser;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.jcef.JBCefBrowser;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.cef.browser.CefBrowser;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class WaveJSONBrowser {

    private String currentWaveJSON = "";

    private JComponent browserComponent;
    private JPanel rootPanel;
    private ActionToolbar toolBar;
    private DefaultActionGroup actionGroup;

    private JBCefBrowser jbBrowser;
    private CefBrowser browser;

    public WaveJSONBrowser() {
        setupUI();
    }

    public void dispose() {
        Disposer.dispose(jbBrowser);
        actionGroup.removeAll();
    }

    public void renderWaveForm(String waveJSON) {
        currentWaveJSON = waveJSON;
        browser.executeJavaScript("renderWaveForm(" + waveJSON + ")", null , 0);
    }

    private void setupUI() {
        createUIComponents();

        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1, false, false));
        rootPanel.add(browserComponent, new GridConstraints(1, 0, 1, 5, 0, 3, 3, 3, (Dimension)null, (Dimension)null, (Dimension)null));

        JPanel var3 = new JPanel();
        var3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, false, false));
        rootPanel.add(var3, new GridConstraints(0, 0, 1, 4, 1, 1, 3, 0, (Dimension)null, (Dimension)null, (Dimension)null));

        var3.add(toolBar.getComponent(), new GridConstraints(0, 0, 1, 1, 8, 0, 6, 0, (Dimension)null, new Dimension(-1, 20), (Dimension)null));
        Spacer var6 = new Spacer();
        rootPanel.add(var6, new GridConstraints(0, 4, 1, 1, 0, 1, 6, 1, (Dimension)null, (Dimension)null, (Dimension)null));
    }

    private void createUIComponents() {
        initBrowser();

        ActionManager actionManager = ActionManager.getInstance();
        actionGroup = new DefaultActionGroup(); // must new

        // 必须在窗口关闭时清空，否则 action 内的 jbBrowser 始终指向旧的
        if (actionGroup.getChildrenCount() == 0) {
            actionGroup.add(new AnAction("Copy WaveJSON to Clipboard","Copy WaveJSON data to clipboard", AllIcons.Actions.Copy) {
                @Override
                public void actionPerformed(@NotNull AnActionEvent e) {
                    setSysClipboardText();
                }
            });
//            actionGroup.add(new AnAction("Ask Questions", "Ask questions", AllIcons.Actions.Help) {
//                @Override
//                public void actionPerformed(@NotNull AnActionEvent e) {
//                    BrowserUtil.browse("https://github.com/easysoc/easysoc-firrtl");
//                }
//            });
            actionGroup.addSeparator();
            actionGroup.add(new AnAction("Open Devtools", "Open devtools", AllIcons.Actions.StartDebugger) {
                @Override
                public void actionPerformed(@NotNull AnActionEvent e) {
                    jbBrowser.openDevtools();
                }
            });
        }

        toolBar = actionManager.createActionToolbar(ActionPlaces.TOOLBAR, actionGroup, true);
        toolBar.setTargetComponent(rootPanel);
        browserComponent = jbBrowser.getComponent();
    }

    private void initBrowser() {
        jbBrowser = new JBCefBrowser(getStartUrl());
        browser = jbBrowser.getCefBrowser();
    }

    private void setSysClipboardText() {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(currentWaveJSON);
        clip.setContents(tText, null);
    }

    private String getStartUrl() {
//        return "https://www.baidu.com";
//        return "file://" + "/home/itviewer/IdeaProjects/plugins-sandbox/plugins/easysoc-firrtl/webapp/index.html";
        return "file://" + PathManager.getPluginsPath() + "/easysoc-firrtl/webapp/index.html";
    }

    public void reload() {
        browser.reload();
    }

    public JPanel getComponent() {
        return rootPanel;
    }

}
