package com.m1lk4fr3553r.intellij.vcscopy.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.m1lk4fr3553r.intellij.vcscopy.model.VCSType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(name = "com.m1lk4fr3553r.intellij.vcscopy.settings.SettingsState",
    storages = {@Storage("VCSURLCopyPlugin.xml")})
public class SettingsState implements PersistentStateComponent<SettingsState> {
  public String urlPrefix = "https://github.com/M1lk4fr3553r/VCSCopy";
  public VCSType vcsType = VCSType.GITHUB;

  @Nullable
  @Override
  public SettingsState getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull SettingsState state) {
    XmlSerializerUtil.copyBean(state, this);
  }

}
