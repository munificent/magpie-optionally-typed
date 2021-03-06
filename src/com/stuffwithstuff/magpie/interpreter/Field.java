package com.stuffwithstuff.magpie.interpreter;

import com.stuffwithstuff.magpie.ast.Expr;

/**
 * Represents a declared or defined field in a class. This describes the field
 * itself from the class's perspective. It is not a field *value* in a
 * particular instance of a class. (Those just use a regular Scope.)
 */
public class Field {
  public Field(boolean isDelegate, Callable initializer, Expr type) {
    mIsDelegate = isDelegate;
    mInitializer = initializer;
    mType = type;
  }
  
  /**
   * Gets whether this field is declared (only its type is specified) or defined
   * (it has an initializer expression).
   * 
   * @return true if the field has an initializer.
   */
  public boolean hasInitializer() { return mInitializer != null; }
  
  /**
   * Gets whether this field is a delegate for unhandled messages.
   * 
   * @return true if the field is a delegate.
   */
  public boolean isDelegate() { return mIsDelegate; }
  
  /**
   * Gets the initializer for this field. Returns null if the field is just
   * declared.
   */
  public Callable getInitializer() { return mInitializer; }
  
  /**
   * Gets the type annotation for this field. Will be null if the field has an
   * initializer.
   */
  public Expr getType() { return mType; }
  
  private final boolean  mIsDelegate;
  private final Callable mInitializer;
  private final Expr     mType;
}
