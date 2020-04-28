package org.easysoc.plugins.firrtl.psi.subtree;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.util.PsiTreeUtil;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.easysoc.plugins.firrtl.FirrtlParserDefinition;
import org.easysoc.plugins.firrtl.psi.leaf.IdentifierLeafNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;

public class CircuitTreeNode extends ANTLRPsiNode {
    public CircuitTreeNode(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public ItemPresentation getPresentation() {
        IdentifierLeafNode id = PsiTreeUtil.findChildOfType(this, IdentifierLeafNode.class);

        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {

                return "circuit:" + id.getText();
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
