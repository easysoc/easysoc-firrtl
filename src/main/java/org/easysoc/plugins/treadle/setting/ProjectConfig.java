package org.easysoc.plugins.treadle.setting;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.SmartList;

import java.util.HashMap;
import java.util.Map;

@State(name = "Treadle", storages = {@Storage(value = "easysoc/treadle.xml")})
public class ProjectConfig implements PersistentStateComponent<ProjectConfig.State> {

    public static ProjectConfig getInstance(Project project) {
        return project.getService(ProjectConfig.class);
    }

    private State innerState = new State();

    @Override
    public State getState() {
        return innerState;
    }

    @Override
    public void loadState(State innerState) {
        this.innerState = innerState;
    }

    public void setWatchSymbols(String path, SmartList<String> watchSymbols) {
        innerState.projectConfig.put(path, watchSymbols);
    }

    public SmartList<String> getWatchSymbols(String path) {
        return innerState.projectConfig.get(path);
    }

    static class State {

        public Map<String, SmartList<String>> projectConfig;

        State() {
            projectConfig = new HashMap<>();
        }
    }
}
