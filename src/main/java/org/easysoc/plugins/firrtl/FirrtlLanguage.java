package org.easysoc.plugins.firrtl;

import com.intellij.lang.Language;

public class FirrtlLanguage extends Language {
    public static final FirrtlLanguage INSTANCE = new FirrtlLanguage();

    private FirrtlLanguage() {
        super("Firrtl");
    }
}
