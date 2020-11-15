package com.m1lk4fr3553r.intellij.vcscopy;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import git4idea.GitUtil;
import git4idea.repo.GitRepository;
import java.util.Formatter;
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
                .map(Editor::getSelectionModel)
                .ifPresent( selectionModel -> {
                  var finalPath = virtualFile.getPath().replace(basepath, "");
                  Optional.ofNullable(GitUtil.getRepositoryManager(anActionEvent.getProject()).getRepositoryForFile(virtualFile))
                      .map(GitRepository::getCurrentRevision)
                      .ifPresentOrElse( revision -> {
                        var settings = SettingsState.getInstance();
                        Formatter formatter = new Formatter();
                        Optional.ofNullable(selectionModel.getSelectionStartPosition())
                            .map(VisualPosition::getLine)
                            .ifPresent(startLine -> {
                              Optional.ofNullable(selectionModel.getSelectionEndPosition())
                                  .map(VisualPosition::getLine)
                                  .ifPresent(endLine -> {
                                    String url;
                                    if(startLine.equals(endLine)) {
                                      url = formatter.format(settings.vcsType.singleLinePattern, settings.urlPrefix, revision, finalPath, startLine).toString();
                                    } else {
                                      url = formatter.format(settings.vcsType.multiLinePattern, settings.urlPrefix, revision, finalPath, startLine, endLine).toString();
                                    }
                                    Messages.showMessageDialog(url, "Message", Messages.getInformationIcon());
                                  });
                            });
                      }, () -> Messages.showMessageDialog( "Could not get Revision number.", "Error", Messages.getErrorIcon()));
                })));
  }
}
