package com.m1lk4fr3553r.intellij.vcscopy;

import com.intellij.openapi.editor.SelectionModel;

public class SelectionData {
  public final boolean isReverse;
  public final int offset;
  public SelectionData(SelectionModel selectionModel) {
    var offsetChar = selectionModel.getLeadSelectionOffset();
    var startChar = selectionModel.getSelectionStart();
    var startPos = selectionModel.getSelectionStartPosition().line;
    var endPos = selectionModel.getSelectionEndPosition().line;
    offset = endPos - startPos;
    isReverse = offsetChar != startChar;
  }
}
