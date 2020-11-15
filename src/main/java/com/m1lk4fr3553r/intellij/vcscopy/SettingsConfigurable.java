package com.m1lk4fr3553r.intellij.vcscopy;

import com.intellij.openapi.options.Configurable;
import java.util.Optional;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingsConfigurable implements Configurable {
  Logger log = LoggerFactory.getLogger(this.getClass());
  private SettingsComponent settingsComponent;

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
    return Optional.ofNullable(SettingsState.getInstance())
        .map(settings -> !settingsComponent.getUrlPrefix().equals(settings.urlPrefix))
        .orElseGet( () -> {
          log.error("Could not check modification state");
          return false;
        });
  }

  @Override
  public void apply() {
    Optional.ofNullable(SettingsState.getInstance())
        .ifPresentOrElse(settings -> settings.urlPrefix = settingsComponent.getUrlPrefix(),
            () -> log.error("Could not apply changes"));
  }

  @Override
  public void reset() {
    Optional.ofNullable(SettingsState.getInstance())
        .ifPresentOrElse(settings -> settingsComponent.setUrlPrefix(settings.urlPrefix),
            () -> log.error("Could not reset URL prefix"));
  }

  @Override
  public void disposeUIResources() {
    settingsComponent = null;
  }
}
