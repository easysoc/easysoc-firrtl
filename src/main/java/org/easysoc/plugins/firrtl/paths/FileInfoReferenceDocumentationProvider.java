package org.easysoc.plugins.firrtl.paths;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.psi.PsiElement;
import org.easysoc.plugins.firrtl.resolve.reference.FileInfoRef;

public class FileInfoReferenceDocumentationProvider extends AbstractDocumentationProvider {
    @Override
    public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        if (element instanceof FileInfoRef.MyFakePsiElement ) {
            return "Jump to Chisel code";
        }
        return null;
    }
}
