package com.m1lk4fr3553r.intellij.vcscopy;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(name = "com.m1lk4fr3553r.intellij.vcscopy.Settings")
public class Settings implements PersistentStateComponent<Settings> {

  public String basePath = "github.com/M1lk4fr3553r/VCSCopy/";

  public static Settings getInstance() {
    return ServiceManager.getService(Settings.class);
  }

  @Nullable
  @Override
  public Settings getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull Settings state) {
    XmlSerializerUtil.copyBean(state, this);
  }

}
