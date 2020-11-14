package com.m1lk4fr3553r.intellij.vcscopy;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import git4idea.GitUtil;
import git4idea.repo.GitRepository;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class CopyAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
    Optional.ofNullable(anActionEvent.getData(LangDataKeys.EDITOR))
        .map(Editor::getDocument)
        .map(document -> FileDocumentManager.getInstance().getFile(document))
        .ifPresent( virtualFile -> Optional.ofNullable(anActionEvent.getProject())
            .map(Project::getBasePath)
            .ifPresent(basepath -> Optional.ofNullable(anActionEvent.getData(CommonDataKeys.EDITOR))
                .map(Editor::getCaretModel)
                .map(CaretModel::getLogicalPosition)
                .map(model -> model.line + 1)
                .ifPresent( lineNumber -> {
                  var finalPath = virtualFile.getPath().replace(basepath, "");
                  Optional.ofNullable(GitUtil.getRepositoryManager(anActionEvent.getProject()).getRepositoryForFile(virtualFile))
                      .map(GitRepository::getCurrentRevision)
                      .ifPresentOrElse( revision -> {
                        var url = String.format("%s/blob/%s/%s#L%d", "PLACEHOLDER---", revision, finalPath, lineNumber);
                        Messages.showMessageDialog( url, "Message", Messages.getInformationIcon());
                      }, () -> Messages.showMessageDialog( "Could not get Revision number.", "Error", Messages.getErrorIcon()));
                })));
  }
}
