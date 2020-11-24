package com.m1lk4fr3553r.intellij.vcscopy.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.m1lk4fr3553r.intellij.vcscopy.helper.Notifyer;
import com.m1lk4fr3553r.intellij.vcscopy.helper.URLUtil;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import org.jetbrains.annotations.NotNull;

public class CopyAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
    URLUtil.getURL(anActionEvent)
        .ifPresent(url -> {
          setClipboard(url);
          Notifyer.notifySuccess(anActionEvent.getProject(), "URL successfully copied.");
        });
  }

  private void setClipboard(String string) {
    StringSelection selection = new StringSelection(string);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(selection, selection);
  }
}
