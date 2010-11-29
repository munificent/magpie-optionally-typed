package com.stuffwithstuff.magpie.interpreter;

import java.util.*;

import com.stuffwithstuff.magpie.util.Expect;

/**
 * A runtime object representing a class.
 */
public class ClassObj extends Obj {
  public ClassObj(ClassObj metaclass, String name, ClassObj parent) {
    super(metaclass);
    mName = name;
    mParent = parent;
    mFields = new HashMap<String, Field>();
    mGetters = new HashMap<String, Callable>();
    mSetters = new HashMap<String, Callable>();
  }

  public ClassObj(String name, ClassObj parent) {
    mName = name;
    mParent = parent;
    mFields = new HashMap<String, Field>();
    mGetters = new HashMap<String, Callable>();
    mSetters = new HashMap<String, Callable>();
  }
  
  public Map<String, Field> getFieldDefinitions() {
    return mFields;
  }
  
  public Obj instantiate() {
    return new Obj(this);
  }

  public Obj instantiate(Object primitiveValue) {
    return new Obj(this, primitiveValue);
  }
    
  public String getName() { return mName; }
  
  public ClassObj getParent() { return mParent; }
  
  public void setParent(ClassObj parent) {
    mParent = parent;
  }
  
  public void addMethod(String name, Callable method) {
    mMethods.put(name, method);
  }
  
  public Map<String, Callable> getMethods() {
    return mMethods;
  }
  
  public Map<String, Callable> getGetters() {
    return mGetters;
  }
  
  public Callable findMethod(String name) {
    // Walk up the inheritance chain.
    ClassObj classObj = this;
    while (classObj != null) {
      Callable method = classObj.mMethods.get(name);
      if (method != null) return method;
      classObj = classObj.mParent;
    }
    
    // If we got here, it wasn't found.
    return null;
  }
  
  public Callable findGetter(String name) {
    // Walk up the inheritance chain.
    ClassObj classObj = this;
    while (classObj != null) {
      Callable getter = classObj.mGetters.get(name);
      if (getter != null) return getter;
      classObj = classObj.mParent;
    }
    
    // If we got here, it wasn't found.
    return null;
  }
  
  public Callable findSetter(String name) {
    // Walk up the inheritance chain.
    ClassObj classObj = this;
    while (classObj != null) {
      Callable setter = classObj.mSetters.get(name);
      if (setter != null) return setter;
      classObj = classObj.mParent;
    }
    
    // If we got here, it wasn't found.
    return null;
  }
  
  public void addConstructor(Callable constructor) {
    Expect.notNull(constructor);
    
    mConstructor = constructor;
  }

  public Callable getConstructor() {
    return mConstructor;
  }
  
  public void declareField(String name, FnObj type) {
    mFields.put(name, new Field(false, type));
  }
  
  public void defineField(String name, FnObj initializer) {
    mFields.put(name, new Field(true, initializer));
  }
  
  public void defineGetter(String name, Callable body) {
    mGetters.put(name, body);
  }
  
  public void defineSetter(String name, Callable body) {
    mSetters.put(name, body);
  }
  
  @Override
  public String toString() {
    return mName;
  }
  
  private final String mName;
  private ClassObj mParent;
  private Callable mConstructor;
  private final Map<String, Field> mFields;
  private final Map<String, Callable> mGetters;
  private final Map<String, Callable> mSetters;
  private final Map<String, Callable> mMethods = new HashMap<String, Callable>();
}
