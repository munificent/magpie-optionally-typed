package com.stuffwithstuff.magpie.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.stuffwithstuff.magpie.ast.pattern.MatchCase;
import com.stuffwithstuff.magpie.ast.pattern.Pattern;
import com.stuffwithstuff.magpie.ast.pattern.ValuePattern;
import com.stuffwithstuff.magpie.ast.pattern.VariablePattern;
import com.stuffwithstuff.magpie.parser.Position;
import com.stuffwithstuff.magpie.util.Expect;
import com.stuffwithstuff.magpie.util.Pair;

/**
 * Base class for AST expression node classes. Any chunk of Magpie code can be
 * represented by an instance of one of the subclasses of this class.
 * 
 * <p>Also includes factory methods to create the appropriate nodes. Aside from
 * being a bit more terse, these methods perform some basic desugaring and
 * simplification such as expanding special forms and discarding useless nodes.
 * 
 * @author bob
 */
public abstract class Expr {
  public static Expr assign(Position position, Expr receiver, String name,
      Expr value) {
    return new AssignExpr(position, receiver, name, value);
  }
  
  public static Expr bool(boolean value) {
    return bool(Position.none(), value);
  }
  
  public static Expr bool(Position position, boolean value) {
    return new BoolExpr(position, value);
  }
  
  public static Expr block(List<Expr> exprs) {
    return block(exprs, null);
  }
  
  public static Expr block(List<Expr> exprs, Expr catchExpr) {
    // Discard unneeded blocks.
    if (catchExpr == null) {
      switch (exprs.size()) {
      case 0:
        return nothing();
      case 1:
        return exprs.get(0);
      default:
        return new BlockExpr(exprs, null);
      }
    } else {
      return new BlockExpr(exprs, catchExpr);
    }
  }

  public static Expr block(Expr... exprs) {
    return new BlockExpr(Arrays.asList(exprs));
  }

  public static Expr break_(Position position) {
    return new BreakExpr(position);
  }
  
  public static Expr call(Expr target, List<Expr> typeArgs, Expr arg) {
    Expect.notNull(target);
    Expect.notNull(typeArgs);
    Expect.notNull(arg);
    
    return new CallExpr(target, typeArgs, arg);
  }
  
  public static Expr call(Expr target, Expr arg) {
    Expect.notNull(target);
    Expect.notNull(arg);
    
    return new CallExpr(target, null, arg);
  }
  
  public static FnExpr fn(Expr body) {
    return fn(Position.none(), FunctionType.nothingToDynamic(), body);
  }
  
  public static FnExpr fn(Position position, FunctionType type, Expr body) {
    return new FnExpr(position, type, body);
  }
  
  // TODO(bob): Hackish. Eliminate.
  public static Expr if_(Expr condition, Expr thenExpr, Expr elseExpr) {
    List<MatchCase> cases = new ArrayList<MatchCase>();
    cases.add(new MatchCase(new ValuePattern(Expr.bool(true)), thenExpr));
    cases.add(new MatchCase(new VariablePattern("_", null), elseExpr));
    
    return match(Position.surrounding(condition, elseExpr),
        condition,
        cases);
  }
  
  public static Expr int_(int value) {
    return int_(Position.none(), value);
  }
  
  public static Expr int_(Position position, int value) {
    return new IntExpr(position, value);
  }
  
  public static Expr loop(Position position, Expr body) {
    return new LoopExpr(position, body);
  }
  
  public static Expr match(Position position,
      Expr value, List<MatchCase> cases) {
    return new MatchExpr(position, value, cases);
  }
  
  public static Expr message(Position position, Expr receiver, String name, Expr arg) {
    return call(message(position, receiver, name), arg);
  }
  
  public static Expr message(Position position, Expr receiver, String name) {
    return new MessageExpr(position, receiver, name);
  }
  
  public static Expr name(Position position, String name) {
    return new MessageExpr(position, null, name);
  }
  
  public static Expr name(String name) {
    return new MessageExpr(Position.none(), null, name);
  }
  
  public static Expr nothing() {
    return nothing(Position.none());  
  }
  
  public static Expr nothing(Position position) {
    return new NothingExpr(Position.none());  
  }

  public static Expr quote(Position position, Expr body) {
    return new QuotationExpr(position, body);
  }

  public static Expr record(Position position,
      List<Pair<String, Expr>> fields) {
    return new RecordExpr(position, fields);
  }

  public static Expr scope(Expr body) {
    // Unwrap redundant scopes.
    if (body instanceof ScopeExpr) return body;
    return new ScopeExpr(body);
  }

  public static Expr staticMessage(Expr receiver, String name, Expr arg) {
    return call(new MessageExpr(Position.none(), receiver, name), 
        Collections.singletonList(arg), nothing());
  }

  public static Expr string(String text) {
    return string(Position.none(), text);
  }

  public static Expr string(Position position, String text) {
    return new StringExpr(position, text);
  }
  
  public static Expr this_(Position position) {
    return new ThisExpr(position);  
  }

  public static Expr tuple(List<Expr> fields) {
    return new TupleExpr(fields);
  }

  public static Expr tuple(Expr... fields) {
    return new TupleExpr(Arrays.asList(fields));
  }

  public static Expr using(Position position, String name) {
    return new UsingExpr(position, name);
  }

  public static Expr var(String name, Expr value) {
    return var(value.getPosition(), name, value);
  }

  public static Expr var(Position position, String name, Expr value) {
    return var(position, new VariablePattern(name, null), value);
  }
  
  public static Expr var(Position position, Pattern pattern, Expr value) {
    return new VariableExpr(position, pattern, value);
  }
  
  public Expr(Position position) {
    mPosition = position;
  }
  
  public Position getPosition() { return mPosition; }
  
  public abstract <TReturn, TContext> TReturn accept(
      ExprVisitor<TReturn, TContext> visitor, TContext context);
  
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    toString(builder, "");
    return builder.toString();
  }
  
  public abstract void toString(StringBuilder builder, String indent);
  
  private final Position mPosition;
}

