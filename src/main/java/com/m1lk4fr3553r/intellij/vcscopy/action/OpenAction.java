package com.m1lk4fr3553r.intellij.vcscopy.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.m1lk4fr3553r.intellij.vcscopy.helper.URLUtil;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.jetbrains.annotations.NotNull;

public class OpenAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
    URLUtil.getURL(anActionEvent)
        .ifPresent(url -> {try {
          Desktop.getDesktop().browse(new URI(url));
        } catch (URISyntaxException | IOException e) {
          System.out.println(e);
        }});
  }
}
