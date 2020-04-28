package org.easysoc.plugins.firrtl.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;

public class HelloPsi extends AnAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    // TODO: insert action logic here
//    PsiFile file = e.getData(LangDataKeys.PSI_FILE);
//    file.accept(new PsiRecursiveElementWalkingVisitor(){
//      @Override
//      public void visitElement(PsiElement element) {
//        if(element instanceof PsiNamedElement){
//          String t = element.getText();
////          System.out.println(t);
//        }
//        super.visitElement(element);
//      }
//    });
  }
}
