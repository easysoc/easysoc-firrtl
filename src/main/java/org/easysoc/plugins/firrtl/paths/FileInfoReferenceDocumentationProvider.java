package org.easysoc.plugins.firrtl.paths;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.psi.PsiElement;
import org.easysoc.plugins.firrtl.resolve.reference.FileInfoRef;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileInfoReferenceDocumentationProvider extends AbstractDocumentationProvider {

    @Override
    public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        if (element instanceof FileInfoRef.MyFakePsiElement ) {
            return "Jump to the Chisel code";
        }
        return null;
    }

//    @Override
//    public String generateDoc(PsiElement element, PsiElement originalElement) {
//        if (element instanceof FileInfoRef.MyFakePsiElement ) {
//            return "Ctrl-click jump to the Chisel code";
//        }
//        return null;
//    }
}
