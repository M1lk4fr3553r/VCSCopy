package com.m1lk4fr3553r.intellij.vcscopy;

public enum VCSType {
  GITHUB("%1$s/blob/%2$s/%3$s#L%4$d", "%1$s/blob/%2$s/%3$s#L%4$d-L%5$d"),
  GITLAB("%1$s/-/blob/%2$s/%3$s#L%4$d", "%1$s/-/blob/%2$s/%3$s#L%4$d-%5$d");

  public final String singleLinePattern;
  public final String multiLinePattern;
  VCSType(String singleLinePattern, String multiLinePattern) {
    this.singleLinePattern = singleLinePattern;
    this.multiLinePattern = multiLinePattern;
  }
}
