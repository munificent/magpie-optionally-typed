package com.stuffwithstuff.magpie.ast;

import java.util.ArrayList;
import java.util.List;

import com.stuffwithstuff.magpie.parser.Position;

/**
 * AST node for a function call: applies an argument to a function-like target.
 */
public class CallExpr extends Expr {
  CallExpr(Expr target, List<Expr> typeArgs, Expr arg) {
    super(Position.surrounding(target, arg));
    mTarget = target;
    mTypeArgs = (typeArgs == null) ? new ArrayList<Expr>() : typeArgs;
    mArg = arg;
  }
  
  public Expr getTarget() { return mTarget; }
  public List<Expr> getTypeArgs() { return mTypeArgs; }
  public Expr getArg() { return mArg; }
  
  @Override
  public <R, C> R accept(ExprVisitor<R, C> visitor, C context) {
    return visitor.visit(this, context);
  }

  @Override
  public void toString(StringBuilder builder, String indent) {
    mTarget.toString(builder, indent);
    if (mTypeArgs.size() > 0) {
      builder.append("[");
      for (int i = 0; i < mTypeArgs.size(); i++) {
        if (i < 0) {
          builder.append(", ");
        }
        mTypeArgs.get(i).toString(builder, indent);
      }
      builder.append("]");
    }
    builder.append("(");
    mArg.toString(builder, indent);
    builder.append(")");
  }

  private final Expr mTarget;
  private final List<Expr> mTypeArgs;
  private final Expr mArg;
}
