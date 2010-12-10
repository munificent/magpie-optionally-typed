package com.stuffwithstuff.magpie.interpreter.builtin;

import com.stuffwithstuff.magpie.Identifiers;
import com.stuffwithstuff.magpie.interpreter.Callable;
import com.stuffwithstuff.magpie.interpreter.ClassObj;
import com.stuffwithstuff.magpie.interpreter.Field;
import com.stuffwithstuff.magpie.interpreter.FnObj;
import com.stuffwithstuff.magpie.interpreter.Interpreter;
import com.stuffwithstuff.magpie.interpreter.Member;
import com.stuffwithstuff.magpie.interpreter.MemberType;
import com.stuffwithstuff.magpie.interpreter.Obj;

public class ClassBuiltIns {
  @Signature("declareField(name String, delegate? Bool, type ->)")
  public static class DeclareField implements BuiltInCallable {
    public Obj invoke(Interpreter interpreter, Obj thisObj, Obj arg) {
      String name = arg.getTupleField(0).asString();
      boolean isDelegate = arg.getTupleField(1).asBool();
      FnObj type = (FnObj)arg.getTupleField(2);
      
      ClassObj classObj = (ClassObj)thisObj;
  
      // Declare the field.
      classObj.getFieldDefinitions().put(name,
          new Field(false, isDelegate, type.getFunction()));
      
      // Add a getter.
      classObj.getMembers().defineGetter(name, new FieldGetter(name, 
          type.getFunction().getFunction().getBody()));
      
      // Add a setter.
      classObj.getMembers().defineSetter(name,
          new FieldSetter(name, type.getFunction().getFunction().getBody()));
  
      return interpreter.nothing();
    }
  }
  
  @Signature("defineField(name String, delegate? Bool, type, initializer ->)")
  public static class DefineField implements BuiltInCallable {
    public Obj invoke(Interpreter interpreter, Obj thisObj, Obj arg) {
      String name = arg.getTupleField(0).asString();
      boolean isDelegate = arg.getTupleField(1).asBool();
      FnObj type = (FnObj)arg.getTupleField(2);
      FnObj initializer = (FnObj)arg.getTupleField(3);
      
      ClassObj classObj = (ClassObj)thisObj;
      classObj.getFieldDefinitions().put(name,
          new Field(true, isDelegate, initializer.getFunction()));
  
      // Add a getter.
      classObj.getMembers().defineGetter(name, new FieldGetter(name,
          type.getFunction().getFunction().getBody()));
      
      // Add a setter.
      classObj.getMembers().defineSetter(name,
          new FieldSetter(name, type.getFunction().getFunction().getBody()));
  
      return interpreter.nothing();
    }
  }
    
  @Signature("defineMethod(name String, body ->)")
  public static class DefineMethod implements BuiltInCallable {
    public Obj invoke(Interpreter interpreter, Obj thisObj, Obj arg) {
      String name = arg.getTupleField(0).asString();
      FnObj method = (FnObj)arg.getTupleField(1);
      
      ClassObj classObj = (ClassObj)thisObj;
      classObj.getMembers().defineMethod(name, method.getCallable());
      
      return interpreter.nothing();
    }
  }
  
  @Signature("defineGetter(name String, body ->)")
  public static class DefineGetter implements BuiltInCallable {
    public Obj invoke(Interpreter interpreter, Obj thisObj, Obj arg) {
      String name = arg.getTupleField(0).asString();
      FnObj body = (FnObj)arg.getTupleField(1);
      
      ClassObj classObj = (ClassObj)thisObj;
      classObj.getMembers().defineGetter(name, body.getCallable());
      
      return interpreter.nothing();
    }
  }
  
  @Signature("defineSetter(name String, body ->)")
  public static class DefineSetter implements BuiltInCallable {
    public Obj invoke(Interpreter interpreter, Obj thisObj, Obj arg) {
      String name = arg.getTupleField(0).asString();
      FnObj body = (FnObj)arg.getTupleField(1);
      
      ClassObj classObj = (ClassObj)thisObj;
      classObj.getMembers().defineSetter(name, body.getCallable());
      
      return interpreter.nothing();
    }
  }
  
  @Signature("getMemberType(name String -> Type)")
  public static class GetMemberType implements BuiltInCallable {
    public Obj invoke(Interpreter interpreter, Obj thisObj, Obj arg) {
      String name = arg.asString();
      
      ClassObj thisClass = (ClassObj)thisObj;
      
      // Look for a member.
      Member member = ClassObj.findMember(thisClass, null, name);
      if (member != null) {
        return interpreter.evaluateCallableType(
            member.getDefinition(), member.getType() != MemberType.METHOD);
      }
  
      // Member not found.
      return interpreter.getNothingClass();
    }
  }
  
  @Getter("mixins(-> Array newType(Class))")
  public static class Mixin implements BuiltInCallable {
    public Obj invoke(Interpreter interpreter, Obj thisObj, Obj arg) {
      ClassObj classObj = (ClassObj)thisObj;
      
      classObj.getMixins().add((ClassObj)arg);
      return interpreter.nothing();
    }
  }

  @Getter("name(-> String)")
  public static class Name implements BuiltInCallable {
    public Obj invoke(Interpreter interpreter, Obj thisObj, Obj arg) {
      ClassObj classObj = (ClassObj)thisObj;
      
      return interpreter.createString(classObj.getName());
    }
  }
  
  // TODO(bob): If mixins don't subtype, then this doesn't actually return the
  // right type (since the class of the object returned (a metaclass) isn't an
  // instance of Class, it just mixes it in). Make Class an interface?
  @Shared
  @Signature("new(name String -> Class)")
  public static class New implements BuiltInCallable {
    public Obj invoke(Interpreter interpreter, Obj thisObj, Obj arg) {
      // Get the name of the class.
      String name = arg.asString();
      
      // Create the metaclass. This will hold shared methods on the class.
      ClassObj metaclass = new ClassObj(interpreter.getMetaclass(),
          name + "Class");
      metaclass.getMixins().add(interpreter.getMetaclass());
      
      // Create the class object itself. This will hold the instance methods for
      // objects of the class.
      ClassObj classObj = new ClassObj(metaclass, name);
      classObj.getMixins().add(interpreter.getObjectClass());
      
      // Add the factory methods.
      Callable signify = new ClassSignify(classObj);
      metaclass.getMembers().defineMethod(Identifiers.SIGNIFY, signify);
      // By default, "new" just signifies too.
      metaclass.getMembers().defineMethod(Identifiers.NEW, signify);
      
      return classObj;
    }
  }
}
