package com.m1lk4fr3553r.intellij.vcscopy.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import java.util.Optional;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingsConfigurable implements Configurable {
  Logger log = LoggerFactory.getLogger(this.getClass());
  private SettingsComponent settingsComponent;
  private final Project project;

  public SettingsConfigurable(Project project) {
    this.project = project;
  }

  @Nls(capitalization = Nls.Capitalization.Title)
  @Override
  public String getDisplayName() {
    return "VCS Copy URL";
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return settingsComponent.getPreferredFocusedComponent();
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    settingsComponent = new SettingsComponent();
    return settingsComponent.getPanel();
  }

  @Override
  public boolean isModified() {
    return Optional.ofNullable(project.getService(SettingsState.class))
        .map(settings -> !settingsComponent.getUrlPrefix().equals(settings.urlPrefix) ||
            !settingsComponent.getVCSType().equals(settings.vcsType))
        .orElseGet( () -> {
          log.error("Could not check modification state");
          return false;
        });
  }

  @Override
  public void apply() {
    Optional.ofNullable(project.getService(SettingsState.class))
        .ifPresentOrElse(settings -> {
          settings.urlPrefix = settingsComponent.getUrlPrefix();
          settings.vcsType = settingsComponent.getVCSType();
            },
            () -> log.error("Could not apply changes"));
  }

  @Override
  public void reset() {
    Optional.ofNullable(project.getService(SettingsState.class))
        .ifPresentOrElse(settings ->  {
          settingsComponent.setUrlPrefix(settings.urlPrefix);
          settingsComponent.setVCSType(settings.vcsType);
            },
            () -> log.error("Could not reset settings"));
  }

  @Override
  public void disposeUIResources() {
    settingsComponent = null;
  }
}
