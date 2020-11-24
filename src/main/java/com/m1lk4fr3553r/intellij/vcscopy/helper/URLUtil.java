package com.m1lk4fr3553r.intellij.vcscopy.helper;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.m1lk4fr3553r.intellij.vcscopy.model.SelectionData;
import com.m1lk4fr3553r.intellij.vcscopy.settings.SettingsState;
import git4idea.GitUtil;
import git4idea.repo.GitRepository;
import java.util.Formatter;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class URLUtil {

  public static Optional<String> getURL(AnActionEvent anActionEvent) {
    var virtualFile =  Optional.ofNullable(anActionEvent.getData(CommonDataKeys.EDITOR))
        .map(Editor::getDocument)
        .map(document -> FileDocumentManager.getInstance().getFile(document));

    var basePath = Optional.ofNullable(anActionEvent.getProject())
        .map(Project::getBasePath);

    var revision = virtualFile
        .map( vf -> GitUtil.getRepositoryManager(anActionEvent.getProject()).getRepositoryForFileQuick(vf))
        .map(GitRepository::getCurrentRevision);

    var startLine = Optional.ofNullable(anActionEvent.getData(CommonDataKeys.EDITOR))
        .map(Editor::getCaretModel)
        .map(CaretModel::getLogicalPosition)
        .map(logicalPosition -> logicalPosition.line + 1);

    var selectionData = Optional.ofNullable(anActionEvent.getData(CommonDataKeys.EDITOR))
        .map(Editor::getSelectionModel)
        .map(SelectionData::new);

    return virtualFile.flatMap(vf ->
        basePath.flatMap(bp ->
            revision.flatMap(rev ->
                startLine.flatMap(sl ->
                    selectionData.flatMap(sd ->
                        Optional.of(buildURL(vf, bp, rev, sl, sd, anActionEvent.getProject()))
                    )
                )
            )
        )
    );
  }

  @NotNull
  private static String buildURL(com.intellij.openapi.vfs.VirtualFile virtualFile, String basePath,
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
}
