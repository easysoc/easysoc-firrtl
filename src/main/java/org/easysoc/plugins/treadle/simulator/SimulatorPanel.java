package org.easysoc.plugins.treadle.simulator;

import com.intellij.icons.AllIcons;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.ui.Splitter;
import com.intellij.ui.OnePixelSplitter;
import firrtl.AnnotationSeq;
import firrtl.FileUtils;
import firrtl.annotations.Annotation;
import firrtl.stage.FirrtlSourceAnnotation;
import org.easysoc.plugins.treadle.actions.CyclesFieldAction;
import org.easysoc.plugins.treadle.actions.OutFormatAction;
import org.easysoc.plugins.treadle.browser.WaveJSONBrowser;
import org.easysoc.plugins.treadle.manager.ViewManager;
import org.easysoc.plugins.treadle.setting.ProjectConfig;
import org.easysoc.plugins.treadle.utils.DataKeys;
import org.jetbrains.annotations.NotNull;
import scala.jdk.javaapi.CollectionConverters;
import treadle.RollBackBuffersAnnotation;
import treadle.TreadleTester;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SimulatorPanel extends SimpleToolWindowPanel {

    private TreadleTester treadleTester;
    private TreePanel treePanel;
    private PokePanel pokePanel;
    private WatchPanel watchPanel;
    private WaveJSONBrowser waveJSONBrowser;

    private Project project;
    private String firrtlFile;
    private int outFormat = 10;

    private TotalCyclesAction totalCyclesAction;

    public SimulatorPanel(String firrtlFile, Project project) {
        super(true, true);

        this.project = project;
        this.firrtlFile = firrtlFile;

        ActionManager actionManager = ActionManager.getInstance();
        DefaultActionGroup actionGroup = new DefaultActionGroup();

        actionGroup.add(new AnAction("Save", "Save the current configuration", AllIcons.Actions.MenuSaveall) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                if (!watchPanel.getWatchSymbols().isEmpty()) {
                    ProjectConfig.getInstance(project).setWatchSymbols(
                            firrtlFile.replace(project.getBasePath(), "").substring(1),
                            watchPanel.getWatchSymbols());
                    project.save();
                }
            }
        });

        actionGroup.addSeparator();
        CyclesFieldAction cyclesField = new CyclesFieldAction("Cycles", "Cycles to perform", null, 2);

        actionGroup.add(cyclesField);
        actionGroup.add(new AnAction("Step Cycles", "To advance the clock of the DUT", AllIcons.Actions.RunAll) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                pokePanel.step(cyclesField.getCycles());
            }
        });
        actionGroup.add(new AnAction("Step", "To advance the clock of the DUT", AllIcons.Actions.Execute) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                pokePanel.step(1);
            }
        });
        actionGroup.addSeparator();
        actionGroup.add(new OutFormatAction());
        actionGroup.add(new AnAction("Help", "Open help link", AllIcons.Actions.Help) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                BrowserUtil.browse("https://github.com/easysoc/easysoc-firrtl");
            }
        });

        totalCyclesAction = new TotalCyclesAction("", "", null, 20);
        actionGroup.add(totalCyclesAction);

        ActionToolbar actionToolbar = actionManager.createActionToolbar(ActionPlaces.TOOLBAR, actionGroup, true);
        actionToolbar.setTargetComponent(this);
        setToolbar(actionToolbar.getComponent());

        setContent(createCenterComponent());

        ProgressManager.getInstance().run(new Task.Backgroundable(project, "TreadleTester", false) {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                String firrtlString = FileUtils.getText(firrtlFile);
                List<Annotation> annotationSeq = new ArrayList<>();
                annotationSeq.add(new FirrtlSourceAnnotation(firrtlString));
                annotationSeq.add(new RollBackBuffersAnnotation(DataKeys.RollBackBuffers));
                treadleTester = TreadleTester.apply(
                        // scala2.13
                        AnnotationSeq.apply(CollectionConverters.asScala(annotationSeq).toSeq())
                );

                new ViewManager(treadleTester, treePanel.getTree()).loadData();
                ApplicationManager.getApplication().invokeLater(() -> {
                    watchPanel.init();
                });
            }
        });
    }

    public void dispose() {
        treadleTester = null;
        waveJSONBrowser.dispose();
    }

    protected JComponent createCenterComponent() {
        SimpleToolWindowPanel splitPanel = new SimpleToolWindowPanel(false);

        splitPanel.setLayout(new BorderLayout());
        Splitter splitter = new OnePixelSplitter(false, .25f);

        splitter.setFirstComponent(createLeftComponent());
        splitter.setSecondComponent(createRightComponent());

        splitPanel.add(splitter, BorderLayout.CENTER);

        return splitPanel;
    }

    protected JComponent createLeftComponent() {
        JPanel splitPanel = new JPanel();

        splitPanel.setLayout(new BorderLayout());
        Splitter splitter = new OnePixelSplitter(true, .8f);

        treePanel = new TreePanel();
        pokePanel = new PokePanel(totalCyclesAction, watchPanel);

        splitter.setFirstComponent(treePanel);
        splitter.setSecondComponent(pokePanel.getPanel());

        splitPanel.add(splitter, BorderLayout.CENTER);

        return splitPanel;
    }

    protected JComponent createRightComponent() {
        JPanel splitPanel = new JPanel();

        splitPanel.setLayout(new BorderLayout());
        Splitter splitter = new OnePixelSplitter(true, .5f);

        watchPanel = new WatchPanel();
        waveJSONBrowser = new WaveJSONBrowser();

        splitter.setFirstComponent(waveJSONBrowser.getComponent());
        splitter.setSecondComponent(watchPanel.getPanel());

        splitPanel.add(splitter, BorderLayout.CENTER);

        return splitPanel;
    }

    @Override
    public Object getData(String dataId) {
        if (DataKeys.TREADLE_TESTER.is(dataId)) {
            return treadleTester;
        } else if (DataKeys.POKE_PANEL.is(dataId)) {
            return pokePanel;
        } else if (DataKeys.WATCH_PANEL.is(dataId)) {
            return watchPanel;
        } else if (DataKeys.WAVEJSON_BROWSER.is(dataId)) {
            return waveJSONBrowser;
        } else if (CommonDataKeys.PROJECT.is(dataId)) {
            return project;
        } else if (DataKeys.FIRRTL_FILE.is(dataId)) {
            return firrtlFile.replace(project.getBasePath(), "").substring(1);
        }

        return super.getData(dataId);
    }

    public int getOutFormat() {
        return outFormat;
    }

    public void setOutFormat(int outFormat) {
        this.outFormat = outFormat;
    }
}