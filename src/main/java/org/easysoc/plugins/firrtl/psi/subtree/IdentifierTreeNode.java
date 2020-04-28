package org.easysoc.plugins.firrtl.psi.subtree;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class IdentifierTreeNode extends ANTLRPsiNode {
    public IdentifierTreeNode(@NotNull ASTNode node) {
        super(node);
    }
}
