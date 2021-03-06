package com.stuffwithstuff.magpie;

// http://invisible-island.net/xterm/ctlseqs/ctlseqs.html

public class Term {
  public enum ForeColor {
    BLACK       ("0;30"),
    GRAY        ("1;30"),
    LIGHT_GRAY  ("0;37"),
    WHITE       ("1;37"),
    RED         ("0;31"),
    PINK        ("1;31"),
    GREEN       ("0;32"),
    LIGHT_GREEN ("1;32"),
    ORANGE      ("0;33"),
    YELLOW      ("1;33"),
    BLUE        ("0;34"),
    LIGHT_BLUE  ("1;34"),
    PURPLE      ("0;35"),
    LAVENDER    ("1;35"),
    CYAN        ("0;36"),
    LIGHT_CYAN  ("1;36");
    
    ForeColor(String escape) {
      mEscape = escape;
    }
    
    public String escape() { return mEscape; }
    
    private final String mEscape;
  }
  
  public static void restoreColor() {
    System.out.print("\033[0m");
  }
  
  public static void set(ForeColor color) {
    System.out.print("\033[" + color.escape() + "m");
  }
  
  public static void moveUp() {
    System.out.print("\033[F");
  }
}
