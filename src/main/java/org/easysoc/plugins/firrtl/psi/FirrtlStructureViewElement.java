package org.easysoc.plugins.firrtl.psi;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.easysoc.plugins.firrtl.psi.subtree.CircuitTreeNode;
import org.easysoc.plugins.firrtl.psi.subtree.ModuleTreeNode;
import org.easysoc.plugins.firrtl.psi.subtree.PortTreeNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FirrtlStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
  private final NavigatablePsiElement myElement;

  public FirrtlStructureViewElement(NavigatablePsiElement element) {
    this.myElement = element;
  }

  @Override
  public Object getValue() {
    return myElement;
  }

  @Override
  public void navigate(boolean requestFocus) {
    myElement.navigate(requestFocus);
  }

  @Override
  public boolean canNavigate() {
    return myElement.canNavigate();
  }

  @Override
  public boolean canNavigateToSource() {
    return myElement.canNavigateToSource();
  }

  @NotNull
  @Override
  public String getAlphaSortKey() {
    String name = myElement.getName();
    return name != null ? name : "";
  }

  @NotNull
  @Override
  public ItemPresentation getPresentation() {
    ItemPresentation presentation = myElement.getPresentation();
    return presentation != null ? presentation : new PresentationData();
  }

  @NotNull
  @Override
  public TreeElement[] getChildren() {
    if (myElement instanceof FirrtlFile) {
      CircuitTreeNode circuit = PsiTreeUtil.findChildOfType(myElement, CircuitTreeNode.class);
      TreeElement[] treeElements = {new FirrtlStructureViewElement(circuit)};
      return treeElements;
    } else if (myElement instanceof CircuitTreeNode) {
      List<ModuleTreeNode> modules = PsiTreeUtil.getChildrenOfTypeAsList(myElement, ModuleTreeNode.class);
      List<TreeElement> treeElements = new ArrayList<>(modules.size());

      for (ModuleTreeNode module : modules) {
        treeElements.add(new FirrtlStructureViewElement(module));
      }
      return treeElements.toArray(new TreeElement[0]);
    } else if (myElement instanceof ModuleTreeNode) {
      List<PortTreeNode> ports = PsiTreeUtil.getChildrenOfTypeAsList(myElement, PortTreeNode.class);
      List<TreeElement> treeElements = new ArrayList<>(ports.size());

      for (PortTreeNode port : ports) {
        treeElements.add(new FirrtlStructureViewElement(port));
      }
      return treeElements.toArray(new TreeElement[0]);
    }
    return EMPTY_ARRAY;
  }
}