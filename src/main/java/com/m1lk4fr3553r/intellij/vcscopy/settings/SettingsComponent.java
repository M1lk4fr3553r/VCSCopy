package com.m1lk4fr3553r.intellij.vcscopy.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.m1lk4fr3553r.intellij.vcscopy.model.VCSType;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

public class SettingsComponent {
  private final JPanel settingsPanel;
  private final JBTextField urlPrefix = new JBTextField();
  private final ComboBox<VCSType> vcsType = new ComboBox<>();

  SettingsComponent() {
    Arrays.stream(VCSType.values()).forEach(vcsType::addItem);
    settingsPanel = FormBuilder.createFormBuilder()
        .addLabeledComponent(new JBLabel("URL to Repository: "), urlPrefix, 1, false)
        .addLabeledComponent(new JBLabel("Git provider: "), vcsType, 1, false)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();
  }

  public JPanel getPanel() {
    return settingsPanel;
  }

  public JComponent getPreferredFocusedComponent() {
    return urlPrefix;
  }

  @NotNull
  public String getUrlPrefix() {
    return urlPrefix.getText();
  }

  public void setUrlPrefix(String urlPrefix) {
    this.urlPrefix.setText(urlPrefix);
  }

  public VCSType getVCSType() {
    return (VCSType) vcsType.getSelectedItem();
  }


  public void setVCSType(VCSType vcsType) {
    this.vcsType.setItem(vcsType);
  }
}
