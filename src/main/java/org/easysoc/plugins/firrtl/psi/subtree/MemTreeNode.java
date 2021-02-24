package org.easysoc.plugins.firrtl.psi.subtree;

import com.intellij.lang.ASTNode;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

public class MemTreeNode extends ANTLRPsiNode {
    public MemTreeNode(@NotNull ASTNode node) {
        super(node);
    }
}
