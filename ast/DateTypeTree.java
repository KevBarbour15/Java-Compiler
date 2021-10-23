package ast;

import visitor.*;

public class DateTypeTree extends AST{

  public DateTypeTree(){

  }
  
  public Object accept(ASTVisitor v) {
    return v.visitDateTypeTree(this);
  }
}
