package com.m1lk4fr3553r.intellij.vcscopy;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import git4idea.GitUtil;
import git4idea.repo.GitRepository;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Formatter;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class CopyAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
    Optional.ofNullable(anActionEvent.getData(CommonDataKeys.EDITOR))
        .map(Editor::getDocument)
        .map(document -> FileDocumentManager.getInstance().getFile(document))
        .ifPresent( virtualFile -> Optional.ofNullable(anActionEvent.getProject())
            .map(Project::getBasePath)
            .ifPresent(basePath -> Optional.ofNullable(anActionEvent.getData(CommonDataKeys.EDITOR))
                .map(Editor::getCaretModel)
                .ifPresent(caretModel -> Optional.ofNullable(GitUtil.getRepositoryManager(anActionEvent.getProject())
                    .getRepositoryForFileQuick(virtualFile))
                    .map(GitRepository::getCurrentRevision)
                    .ifPresentOrElse(revision -> Optional.of(caretModel.getLogicalPosition())
                        .map(logicalPosition -> logicalPosition.line + 1)
                        .ifPresent(startLine -> Optional
                            .ofNullable(anActionEvent.getData(CommonDataKeys.EDITOR))
                            .map(Editor::getSelectionModel)
                            .map(SelectionData::new)
                            .ifPresent(selectionData -> {
                              setClipboard(buildURL(virtualFile, basePath, revision, startLine,
                                  selectionData, anActionEvent.getProject()));
                              Notifyer.notifySuccess(anActionEvent.getProject(),
                                  "URL to line %d successfully copied.", startLine);
                            })), () -> Messages.showMessageDialog("Could not get Revision number.", "Error",
                        Messages.getErrorIcon())))));
  }

  @NotNull
  private String buildURL(com.intellij.openapi.vfs.VirtualFile virtualFile, String basePath,
      String revision, Integer startLine, SelectionData selectionData, Project project) {
    String url;
    var finalPath = virtualFile.getPath().replace(basePath, "");
    var settings = project.getService(SettingsState.class);
    try (Formatter formatter = new Formatter()) {
      if (selectionData.offset == 0) {
        url = formatter
            .format(settings.vcsType.singleLinePattern, settings.urlPrefix, revision, finalPath,
                startLine).toString();
      } else if (!selectionData.isReverse) {
        url = formatter
            .format(settings.vcsType.multiLinePattern, settings.urlPrefix, revision, finalPath,
                startLine
                    - selectionData.offset, startLine).toString();
      } else {
        url = formatter
            .format(settings.vcsType.multiLinePattern, settings.urlPrefix, revision, finalPath,
                startLine, startLine + selectionData.offset).toString();
      }
    }
    return url;
  }

  private void setClipboard(String string) {
    StringSelection selection = new StringSelection(string);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(selection, selection);
  }

}
