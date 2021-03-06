package com.stuffwithstuff.magpie.interpreter;

import java.util.List;

import com.stuffwithstuff.magpie.util.Expect;

public class Obj {  
  public Obj(ClassObj classObj, Object value) {
    if (classObj == null) {
      // If we are a class, we're our own class.
      mClass = (this instanceof ClassObj) ? (ClassObj)this : null;
    } else {
      mClass = classObj;
    }
    
    mValue = value;
  }
  
  public Obj(ClassObj classObj) {
    this(classObj, null);
  }
  
  public ClassObj getClassObj() { return mClass; }
  
  public void bindClass(ClassObj classObj) {
    Expect.notNull(classObj);
    
    mClass = classObj;
  }
    
  public Scope getFields() { return mFields; }
  
  /**
   * Gets the value of a given field.
   * @param name   The name of the field.
   * @return The value or null if there is no field with that name.
   */
  public Obj getField(String name) {
    return mFields.get(name);
  }
  
  public Obj getTupleField(int index) {
    return getField(Name.getTupleField(index));
  }

  /**
   * Sets the given field to the given value.
   * @param name   The name of the field.
   * @param member The fields's value.
   */
  public void setField(String name, Obj field) {
    mFields.define(name, field);
  }
  
  public Object getValue() {
    return mValue;
  }
  
  public void setValue(Object value) {
    mValue = value;
  }
  
  @SuppressWarnings("unchecked")
  public List<Obj> asArray() {
    if (mValue instanceof List<?>) {
      return (List<Obj>)mValue;
    }
    
    throw new InterpreterException(String.format(
        "The object \"%s\" is not an array.", this));
  }

  public boolean asBool() {
    if (mValue instanceof Boolean) {
      return ((Boolean)mValue).booleanValue();
    }
    
    throw new InterpreterException(String.format(
        "The object \"%s\" is not a boolean.", this));
  }

  public ClassObj asClass() {
    if (this instanceof ClassObj) {
      return (ClassObj)this;
    }
    
    throw new InterpreterException(String.format(
        "The object \"%s\" is not a class.", this));
  }
  
  public FnObj asFn() {
    if (this instanceof FnObj) {
      return (FnObj)this;
    }
    
    throw new InterpreterException(String.format(
        "The object \"%s\" is not a function.", this));
  }
  
  public int asInt() {
    if (mValue instanceof Integer) {
      return ((Integer)mValue).intValue();
    }
    
    throw new InterpreterException(String.format(
        "The object \"%s\" is not an int.", this));
  }
  
  public String asString() {
    if (mValue instanceof String) {
      return (String)mValue;
    }
    
    throw new InterpreterException(String.format(
        "The object \"%s\" is not a string.", this));
  }
  
  @Override
  public String toString() {
    if (mValue instanceof String) {
      return "\"" + mValue + "\"";
    } else if (mValue != null) {
      return mValue.toString();
    } else if (mClass.getName().equals("Nothing")) {
      return "nothing";
    }

    return "Instance of " + mClass.getName();
  }
  
  private ClassObj mClass;
  private Object mValue;
  private final Scope mFields = new Scope();
}
