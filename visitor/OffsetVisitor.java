package visitor;

import java.util.HashMap;
import ast.AST;

public class OffsetVisitor extends ASTVisitor {

  private int[] nCount = new int[100];
  private int[] nextAvailable = new int[100];
  private int depth = 0;
  private int maxDepth;
  private HashMap<AST, Integer> offsets = new HashMap<>();

  public HashMap<AST, Integer> getOffsets() {
    HashMap<AST, Integer> offsetMap = offsets;
    return offsetMap;
  }

  public int getMaxDepth() {
    return maxDepth;
  }

  public int getMaxOffset() {
    int maxOS = 0;
    for (int i = 0; i < nextAvailable.length; i++) {
      if (nextAvailable[i] > maxOS) {
        maxOS = nextAvailable[i];
      }
    }
    return maxOS;
  }

  private int getParentOffset(AST left, AST right) {
    int a = offsets.get(left);
    int b = offsets.get(right);
    return (a + b) / 2;
  }

  private void setOffset(AST t) {
    if (t.kidCount() > 0) {

      depth++;
      visitKids(t);
      depth--;

      offsets.put(t, nextAvailable[depth]);
      nextAvailable[depth] += 2;

      int kidTotal = t.kidCount();
      AST left = t.getKid(1);
      AST right = t.getKid(kidTotal);
      int parentOS = getParentOffset(left, right);

      if (parentOS < offsets.get(t)) {
        offsets.put(t, parentOS);
        nextAvailable[depth] = offsets.get(t) - parentOS;
      } else {
        offsets.put(t, nextAvailable[depth]);
        nextAvailable[depth] = offsets.get(t) + 2;
      }
    } else {
      offsets.put(t, nextAvailable[depth]);
      nextAvailable[depth] += 2;
    }
  }

  public Object visitProgramTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitBlockTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitFunctionDeclTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitCallTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitDeclTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitIntTypeTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitNumberTypeTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitScientificTypeTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitFloatTypeTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitVoidTypeTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitBoolTypeTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitFormalsTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitActualArgsTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitIfTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitWhileTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitForTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitReturnTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitAssignTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitIntTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitNumberTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitScientificTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitFloatTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitVoidTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitIdTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitRelOpTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitAddOpTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitMultOpTree(AST t) {
    setOffset(t);
    return null;
  }

  // new methods here
  public Object visitStringTypeTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitCharTypeTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitStringTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitCharTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitUnlessTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitSwitchTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitSwitchBlockTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitCaseTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitDefaultTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitDateTypeTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitDateLitTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitNumberLitTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitDoTree(AST t) {
    setOffset(t);
    return null;
  }

  public Object visitListTree(AST t) {
    setOffset(t);
    return null;
  }
}
