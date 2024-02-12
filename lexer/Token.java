
package lexer;

/**
 * <pre>
 *  The Token class records the information for a token:
 *  1. The Symbol that describes the characters in the token
 *  2. The starting column in the source file of the token and
 *  3. The ending column in the source file of the token
 * </pre>
 */
public class Token {
  private int leftPosition, rightPosition, lineFound;
  private Symbol symbol;

  /**
   * Create a new Token based on the given Symbol
   * 
   * @param leftPosition  is the source file column where the Token begins
   * @param rightPosition is the source file column where the Token ends
   */
  public Token(int leftPosition, int rightPosition, int lineFound, Symbol symbol) {
    this.leftPosition = leftPosition;
    this.rightPosition = rightPosition;
    this.symbol = symbol;
    this.lineFound = lineFound;
  }

  public Symbol getSymbol() {
    return symbol;
  }

  public void print() {
    System.out.println(
        "       " + symbol.toString() +
            "             left: " + leftPosition +
            " right: " + rightPosition +
            " line: " + lineFound);
    return;
  }

  public String toString() {
    return symbol.toString();
  }

  public int getLeftPosition() {
    return leftPosition;
  }

  public int getRightPosition() {
    return rightPosition;
  }

  public int getLineFound() {
    return lineFound;
  }

  /**
   * @return the integer that represents the kind of symbol we have which
   *         is actually the type of token associated with the symbol
   */
  public Tokens getKind() {
    return symbol.getKind();
  }
}
