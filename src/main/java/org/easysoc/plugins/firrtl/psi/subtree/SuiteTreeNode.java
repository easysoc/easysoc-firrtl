package org.easysoc.plugins.firrtl.psi.subtree;

import com.intellij.lang.ASTNode;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

public class SuiteTreeNode extends ANTLRPsiNode {
    public SuiteTreeNode(@NotNull ASTNode node) {
        super(node);
    }
}
