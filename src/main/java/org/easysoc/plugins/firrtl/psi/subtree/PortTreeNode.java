package org.easysoc.plugins.firrtl.psi.subtree;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PortTreeNode extends ANTLRPsiNode {
    public PortTreeNode(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public ItemPresentation getPresentation() {

        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return getText();
            }

            @Nullable
            @Override
            public String getLocationString() {
                return null;
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return null;
            }
        };
    }
}
