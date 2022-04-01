package org.easysoc.plugins.treadle.utils;

import com.intellij.openapi.actionSystem.DataKey;
import org.easysoc.plugins.treadle.browser.WaveJSONBrowser;
import org.easysoc.plugins.treadle.simulator.PokePanel;
import org.easysoc.plugins.treadle.simulator.WatchPanel;
import treadle.TreadleTester;

public class DataKeys {

    public static final DataKey<TreadleTester> TREADLE_TESTER = DataKey.create("TREADLE_TESTER");
    public static final DataKey<PokePanel> POKE_PANEL = DataKey.create("POKE_PANEL");
    public static final DataKey<WatchPanel> WATCH_PANEL = DataKey.create("WATCH_PANEL");
    public static final DataKey<WaveJSONBrowser> WAVEJSON_BROWSER = DataKey.create("WAVEJSON_BROWSER");
    public static final DataKey<String> FIRRTL_FILE = DataKey.create("FIRRTL_FILE");

    public static final int RollBackBuffers = 32;
}
