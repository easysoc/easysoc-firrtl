package org.easysoc.plugins.firrtl.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.easysoc.plugins.firrtl.psi.leaf.DedentLeafNode;
import org.easysoc.plugins.firrtl.psi.subtree.MemTreeNode;
import org.easysoc.plugins.firrtl.psi.subtree.ModuleBlockTreeNode;
import org.easysoc.plugins.firrtl.psi.subtree.ModuleTreeNode;
import org.easysoc.plugins.firrtl.psi.subtree.SuiteTreeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FirrtlFoldingBuilder extends FoldingBuilderEx implements DumbAware {
    @Override
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean b) {
        // 不作为成员变量减少内存?
        List<FoldingDescriptor> descriptors = new ArrayList<>();
        addModuleFoldingDescriptor(descriptors, root);
        return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
    }

    private void addModuleFoldingDescriptor(List<FoldingDescriptor> descriptors, PsiElement root) {
        for (ModuleTreeNode moduleNode : PsiTreeUtil.findChildrenOfType(root, ModuleTreeNode.class)) {
            if (moduleNode != null && moduleNode.getLastChild() != null) {
                descriptors.add(new FoldingDescriptor(moduleNode, noDedentTextRange(moduleNode)));
                addModuleBlockFoldingDescriptor(descriptors, moduleNode);
            }
        }
    }

    private void addModuleBlockFoldingDescriptor(List<FoldingDescriptor> descriptors, PsiElement root) {
        ModuleBlockTreeNode blockNode = PsiTreeUtil.findChildOfType(root, ModuleBlockTreeNode.class);
        if (blockNode != null && blockNode.getLastChild() != null) {
            descriptors.add(new FoldingDescriptor(blockNode, noDedentTextRange(blockNode)));
            addSuiteFoldingDescriptor(descriptors, blockNode);
            addMemFoldingDescriptor(descriptors, blockNode);
        }
    }

    private void addSuiteFoldingDescriptor(List<FoldingDescriptor> descriptors, PsiElement root) {
        for (SuiteTreeNode suiteNode : PsiTreeUtil.findChildrenOfType(root, SuiteTreeNode.class)) {
            if (suiteNode != null && suiteNode.getLastChild() != null) {
                descriptors.add(new FoldingDescriptor(suiteNode, noDedentTextRange(suiteNode)));
            }
        }
    }

    private void addMemFoldingDescriptor(List<FoldingDescriptor> descriptors, PsiElement root) {
        for (MemTreeNode memNode : PsiTreeUtil.findChildrenOfType(root, MemTreeNode.class)) {
            if (memNode != null && memNode.getLastChild() != null) {
                descriptors.add(new FoldingDescriptor(memNode, noDedentTextRange(memNode)));
            }
        }
    }

    private TextRange noDedentTextRange(PsiElement element) {
        TextRange range = element.getTextRange();
        PsiElement lastChild = element.getLastChild();
        if (lastChild instanceof DedentLeafNode) {
            return new TextRange(range.getStartOffset(), range.getEndOffset() - lastChild.getTextLength());
        }
        return range;
    }

    @Override
    public @Nullable String getPlaceholderText(@NotNull ASTNode astNode) {
        String retTxt = "...";
        PsiElement node = astNode.getPsi();
        if (node instanceof ModuleTreeNode) {
            ModuleTreeNode moduleNode = (ModuleTreeNode)node;
            ItemPresentation presentation = moduleNode.getPresentation();
            if (presentation != null) {
                retTxt = presentation.getPresentableText();
            }
        } else if (node instanceof ModuleBlockTreeNode) {
            retTxt = "moduleBlock";
        } else if (node instanceof MemTreeNode) {
            retTxt = "mem";
        }
        return retTxt;
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode astNode) {
        return false;
    }
}
