package com.stuffwithstuff.magpie.ast;

import com.stuffwithstuff.magpie.parser.Position;

public class ThisExpr extends Expr {
  ThisExpr(Position position) {
    super(position);
  }
  
  @Override
  public <R, C> R accept(ExprVisitor<R, C> visitor, C context) {
    return visitor.visit(this, context);
  }

  @Override
  public void toString(StringBuilder builder, String indent) {
    builder.append("this");
  }
}
