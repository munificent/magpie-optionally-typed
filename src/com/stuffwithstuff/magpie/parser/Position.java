package com.stuffwithstuff.magpie.parser;

/**
 * Describes the location of a piece of text in a source file.
 */
public class Position {
  public static Position union(Position... positions) {
    int startLine = positions[0].getStartLine();
    int startCol = positions[0].getStartCol();
    int endLine = positions[0].getEndLine();
    int endCol = positions[0].getEndCol();
    
    for (Position position : positions) {
      startLine = Math.min(startLine, position.getStartLine());
      startCol = Math.min(startCol, position.getStartCol());
      endLine = Math.max(endLine, position.getEndLine());
      endCol = Math.max(endCol, position.getEndCol());
    }
    
    return new Position(positions[0].getSourceFile(), startLine, startCol,
        endLine, endCol);
  }
  
  public static Position none() {
    return new Position("", -1, -1, -1, -1);
  }
  
  public Position(String sourceFile, int startLine, int startCol,
      int endLine, int endCol) {
    mSourceFile = sourceFile;
    mStartLine = startLine;
    mStartCol = startCol;
    mEndLine = endLine;
    mEndCol = endCol;
  }
  
  public String getSourceFile() { return mSourceFile; }
  public int getStartLine() { return mStartLine; }
  public int getStartCol() { return mStartCol; }
  public int getEndLine() { return mEndLine; }
  public int getEndCol() { return mEndCol; }
  
  public String toString() {
    if (mStartLine == mEndLine) {
      return String.format("%s (line %d, col %d-%d)", mSourceFile, mStartLine,
          mStartCol, mEndCol);
    }
    
    return String.format("%s (line %d col %d - line %d col %d)", mSourceFile,
        mStartLine, mEndLine, mStartCol, mEndCol);
  }
  
  private final String mSourceFile;
  private final int mStartLine;
  private final int mStartCol;
  private final int mEndLine;
  private final int mEndCol;
}