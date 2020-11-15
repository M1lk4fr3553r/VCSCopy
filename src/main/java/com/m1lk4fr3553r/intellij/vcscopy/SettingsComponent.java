package com.m1lk4fr3553r.intellij.vcscopy;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

public class SettingsComponent {
  private JPanel settingsPanel;
  private JBTextField urlPrefix = new JBTextField();

  SettingsComponent() {
    settingsPanel = FormBuilder.createFormBuilder()
        .addLabeledComponent(new JBLabel("URL to Repository: "), urlPrefix, 1, false)
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
}
