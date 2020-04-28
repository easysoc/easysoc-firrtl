package org.easysoc.plugins.firrtl.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.easysoc.plugins.firrtl.FirrtlFileType;
import org.easysoc.plugins.firrtl.FirrtlLanguage;
import org.easysoc.plugins.firrtl.icons.Icons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class FirrtlFile extends PsiFileBase {
    public FirrtlFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, FirrtlLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return FirrtlFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Firrtl Language file";
    }

    @Override
    public Icon getIcon(int flags) {
        return Icons.FirrtlFileType;
    }

//    @Override
//    public ItemPresentation getPresentation() {}
}
