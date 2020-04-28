// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package org.easysoc.plugins.firrtl.psi;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class FirrtlStructureViewModel extends StructureViewModelBase implements
      StructureViewModel.ElementInfoProvider {
  public FirrtlStructureViewModel(PsiFile psiFile) {
    super(psiFile, new FirrtlStructureViewElement(psiFile));
  }
  
  @NotNull
  public Sorter[] getSorters() {
    return new Sorter[]{Sorter.ALPHA_SORTER};
  }
  
  
  @Override
  public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
    return false;
  }
  
  @Override
  public boolean isAlwaysLeaf(StructureViewTreeElement element) {
    return element instanceof FirrtlFile;
  }
}