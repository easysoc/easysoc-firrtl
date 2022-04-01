package org.easysoc.plugins.treadle.terminal;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import org.easysoc.plugins.treadle.utils.DataKeys;
import org.jetbrains.plugins.terminal.LocalTerminalCustomizer;

import java.util.Map;

public class DefaultTerminalLocationCustomizer extends LocalTerminalCustomizer {

    @Override
    public String[] customizeCommandAndEnvironment(Project project, String[] command, Map<String, String> envs) {

        String rtl = System.getProperty("FIRRTL_FILE");
        if (rtl != null) {
            String[] commands = new String[8];
            commands[0] = "java";
            commands[1] = "-cp";
            commands[2] = PathManager.getPluginsPath() + "/easysoc-firrtl/lib/*";
            commands[3] = "treadle.TreadleRepl";
            commands[4] = "--tr-rollback-buffers";
            commands[5] = String.valueOf(DataKeys.RollBackBuffers);
            commands[6] = "-i";
            commands[7] = rtl;

            System.clearProperty("FIRRTL_FILE");
            return super.customizeCommandAndEnvironment(project, commands, envs);
        } else {
            return super.customizeCommandAndEnvironment(project, command, envs);
        }
    }
}