package com.stuffwithstuff.magpie.ast.pattern;

import com.stuffwithstuff.magpie.ast.Expr;
import com.stuffwithstuff.magpie.ast.pattern.Pattern;

public class ValuePattern implements Pattern {
  public ValuePattern(Expr value) {
    mValue = value;
  }
  
  public Expr getValue() { return mValue; }
  
  @Override
  public <R, C> R accept(PatternVisitor<R, C> visitor, C context) {
    return visitor.visit(this, context);
  }

  @Override
  public String toString() {
    return mValue.toString();
  }

  private final Expr mValue;
}