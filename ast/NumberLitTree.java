package ast;

import lexer.Symbol;
import lexer.Token;
import visitor.*;

public class NumberLitTree extends AST {
  private Symbol symbol;

  public NumberLitTree(Token tok) {
    this.symbol = tok.getSymbol();
  }

  public Object accept(ASTVisitor v) {
    return v.visitNumberLitTree(this);
  }

  public Symbol getSymbol() {
    return symbol;
  }
}