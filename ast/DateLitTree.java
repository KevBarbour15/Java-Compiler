package ast;

import lexer.Symbol;
import lexer.Token;
import visitor.*;

public class DateLitTree extends AST {
  private Symbol symbol;

  public DateLitTree(Token tok) {
    this.symbol = tok.getSymbol();
  }

  public Object accept(ASTVisitor v) {
    return v.visitDateLitTree(this);
  }

  public Symbol getSymbol() {
    return symbol;
  }
}
