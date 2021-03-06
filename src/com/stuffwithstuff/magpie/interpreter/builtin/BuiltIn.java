package com.stuffwithstuff.magpie.interpreter.builtin;

import java.util.List;

import com.stuffwithstuff.magpie.ast.Expr;
import com.stuffwithstuff.magpie.ast.FunctionType;
import com.stuffwithstuff.magpie.interpreter.Callable;
import com.stuffwithstuff.magpie.interpreter.ClassObj;
import com.stuffwithstuff.magpie.interpreter.Interpreter;
import com.stuffwithstuff.magpie.interpreter.Obj;
import com.stuffwithstuff.magpie.util.Expect;

public class BuiltIn implements Callable {
  public BuiltIn(FunctionType type, BuiltInCallable callable) {
    Expect.notNull(type);
    Expect.notNull(callable);
    
    mFnType = type;
    mType = null;
    mCallable = callable;
  }

  public BuiltIn(Expr type, BuiltInCallable callable) {
    Expect.notNull(type);
    Expect.notNull(callable);
    
    mFnType = null;
    mType = type;
    mCallable = callable;
  }

  @Override
  public Callable bindTo(ClassObj classObj) {
    // Since built-ins don't look up any members within their bodies, they
    // don't need to rebind.
    return this;
  }

  @Override
  public Obj getType(Interpreter interpreter) {
    if (mFnType != null) {
      return interpreter.evaluateFunctionType(mFnType, null);
    } else {
      return interpreter.evaluate(mType);
    }
  }

  @Override
  public Obj invoke(Interpreter interpreter, Obj thisObj,
      List<Obj> typeArgs, Obj arg) {
    // No built-ins that use type args, so just ignore them.
    return mCallable.invoke(interpreter, thisObj, arg);
  }

  private final FunctionType    mFnType;
  private final Expr            mType;
  private final BuiltInCallable mCallable;
}
