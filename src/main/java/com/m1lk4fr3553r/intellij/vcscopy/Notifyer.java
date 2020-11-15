package com.m1lk4fr3553r.intellij.vcscopy;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

public class Notifyer {
  private static final NotificationGroup NOTIFICATION_GROUP =
      new NotificationGroup("VCS URL Copied", NotificationDisplayType.BALLOON, false);

  public static void notifySuccess(@Nullable Project project, String content) {
    NOTIFICATION_GROUP.createNotification(content, NotificationType.INFORMATION)
        .notify(project);
  }

  public static void notifySuccess(@Nullable Project project, String content, Object ... params) {
    notifySuccess(project, String.format(content, params));
  }
}
