package org.easysoc.plugins.firrtl.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import firrtl.AnnotationSeq;
import firrtl.annotations.Annotation;
import firrtl.elk.transforms.MakeCircuit;
import firrtl.stage.FirrtlStage;
import firrtl.stage.RunFirrtlTransformAnnotation;
import org.jetbrains.annotations.NotNull;
import scala.jdk.javaapi.CollectionConverters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ElkAction extends AnAction {

  @Override
  public void actionPerformed(AnActionEvent e) {

    /*
    PsiFile file = e.getData(LangDataKeys.PSI_FILE);
    file.accept(new PsiRecursiveElementWalkingVisitor(){
      @Override
      public void visitElement(PsiElement element) {
        if(element instanceof PsiNamedElement){
          String t = element.getText();
          System.out.println(t);
        }
        super.visitElement(element);
      }
    });
     */

    VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);
    Project project = e.getProject();
    String filePath = file.getPath();
    ProgressManager.getInstance().run(new Task.Backgroundable(project, "Generating ELK Graph", false) {
      @Override
      public void run(@NotNull ProgressIndicator progressIndicator) {
          String[] commands = new String[6];
          commands[0] = "-i";
          commands[1] = filePath;
          commands[2] = "-td";
          commands[3] = file.getParent().getPath();
          commands[4] = "-X";
          commands[5] = "low"; // none

          List<Annotation> annotationSeq = new ArrayList<>();
          annotationSeq.add(new RunFirrtlTransformAnnotation(new MakeCircuit()));

          FirrtlStage stage = new FirrtlStage();
          stage.execute(commands, AnnotationSeq.apply(CollectionConverters.asScala(annotationSeq).toSeq()));

          String graphPath;
          if (filePath.endsWith(".lo.fir")) {
              graphPath = filePath.replace(".lo.fir", ".graph");
          } else {
              graphPath = filePath.replace(".fir", ".graph");
          }
          VirtualFile graphFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(new File(graphPath));
          if (graphFile != null) {
              ApplicationManager.getApplication().invokeLater(() ->
                      new OpenFileDescriptor(project, graphFile).navigate(true)
              );
          }
      }
    });
  }

}
