package lexer;

/**
 * The Lexer class is responsible for scanning the source file which is a stream
 * of characters and returning a stream of tokens; each token object will
 * contain the string (or access to the string) that describes the token along
 * with an indication of its location in the source program to be used for error
 * reporting; we are tracking line numbers; white spaces are space, tab,
 * newlines
 */

public class Lexer {
  private boolean atEOF = false;
  // next character to process
  private char ch;
  private SourceReader source;

  // positions in line of current token
  private int startPosition, endPosition, line;

  /**
   * Lexer constructor
   * 
   * @param sourceFile is the name of the File to read the program source from
   */
  public Lexer(String sourceFile) throws Exception {
    // init token table
    new TokenType();
    source = new SourceReader(sourceFile);
    ch = source.read();
  }

  /**
   * newIdTokens are either ids or reserved words; new id's will be inserted in
   * the symbol table with an indication that they are id's
   * 
   * @param id            is the String just scanned - it's either an id or
   *                      reserved word
   * @param startPosition is the column in the source file where the token begins
   * @param endPosition   is the column in the source file where the token ends
   * @return the Token; either an id or one for the reserved words
   */

  public Token newIdToken(String id, int startPosition, int endPosition) {
    // System.out.println("New ID Token: " + id);
    return new Token(startPosition, endPosition, line, Symbol.symbol(id, Tokens.Identifier));
  }

  /**
   * number tokens are inserted in the symbol table; we don't convert the numeric
   * strings to numbers until we load the bytecodes for interpreting; this ensures
   * that any machine numeric dependencies are deferred until we actually run the
   * program; i.e. the numeric constraints of the hardware used to compile the
   * source program are not used
   * 
   * @param number        is the int String just scanned
   * @param startPosition is the column in the source file where the int begins
   * @param endPosition   is the column in the source file where the int ends
   * @return the int Token
   */
  public Token newNumberToken(String number, int startPosition, int endPosition, int line) {
    // System.out.println("New Number Token: " + number);
    return new Token(startPosition, endPosition, line, Symbol.symbol(number, Tokens.INTeger));
  }

  public Token newToken(String number, int startPosition, int endPosition, int lineNumber, Tokens kind) {
    return new Token(startPosition, endPosition, lineNumber, Symbol.symbol(number, kind));
  }

  /**
   * build the token for operators (+ -) or separators (parens, braces) filter out
   * comments which begin with two slashes
   * 
   * @param tokenString   is the String representing the token
   * @param startPosition is the column in the source file where the token begins
   * @param endPosition   is the column in the source file where the token ends
   * @return the Token just found
   */
  public Token makeToken(String tokenString, int startPosition, int endPosition, int line) {
    // filter comments

    if (tokenString.equals("//")) {
      try {
        int oldLine = source.getLineno();
        do {
          ch = source.read();
        } while (oldLine == source.getLineno());
      } catch (Exception e) {
        atEOF = true;
      }
      return nextToken();
    }

    // ensure it's a valid token
    Symbol symbol = Symbol.symbol(tokenString, Tokens.BogusToken);

    if (symbol == null) {
      System.out.println("******** illegal character: " + tokenString);
      atEOF = true;
      System.out.print("\n" + SourceReader.endLine);
      return nextToken();
    }

    return new Token(startPosition, endPosition, line, symbol);
  }

  /**
   * @return the next Token found in the source file
   */
  public Token nextToken() {
    // ch is always the next char to process

    if (atEOF) {
      if (source != null) {
        source.close();
        source = null;
      }

      return null;
    }

    try {
      // scan past whitespace
      while (Character.isWhitespace(ch)) {
        ch = source.read();
      }
    } catch (Exception e) {
      atEOF = true;
      return nextToken();
    }

    startPosition = source.getPosition();
    endPosition = startPosition - 1;
    line = source.getLineno();

    if (Character.isJavaIdentifierStart(ch)) {
      // return tokens for ids and reserved words
      String id = "";

      try {
        do {
          endPosition++;
          id += ch;
          ch = source.read();
        } while (Character.isJavaIdentifierPart(ch));
      } catch (Exception e) {
        atEOF = true;
      }
      return newIdToken(id, startPosition, endPosition);
    }

    if (Character.isDigit(ch)) {
      // return number tokens
      String number = "";
      Tokens token = Tokens.INTeger;

      try {
        do {
          endPosition++;
          number += ch;
          ch = source.read();

        } while (Character.isDigit(ch));
      } catch (Exception e) {
        atEOF = true;
      }

      try {
        if ('~' == ch) {
          endPosition++;
          number += ch;
          ch = source.read();
          do {
            endPosition++;
            number += ch;
            ch = source.read();
          } while (Character.isDigit(ch) || ch == '~');

          if (isDateLit(number)) {
            token = Tokens.DateLit;
          }
        }
      } catch (Exception e) {
        atEOF = true;
      }

      try {
        if ('.' == ch) {
          endPosition++;
          number += ch;
          ch = source.read();
          do {
            endPosition++;
            number += ch;
            ch = source.read();
          } while (Character.isDigit(ch));

          if (isNumberLit(number)) {
            token = Tokens.NumberLit;
          }
        }
      } catch (Exception e) {
        atEOF = true;
      }

      return newToken(number, startPosition, endPosition, line, token);
    }

    // At this point the only tokens to check for are one or two
    // characters; we must also check for comments that begin with
    // 2 slashes

    String charOld = "" + ch;
    String op = charOld;
    Symbol sym;
    try {
      endPosition++;
      ch = source.read();
      op += ch;

      sym = Symbol.symbol(op, Tokens.BogusToken);
      if (sym == null) {

        return makeToken(charOld, startPosition, endPosition, line);
      }

      endPosition++;
      ch = source.read();

      return makeToken(op, startPosition, endPosition, line);
    } catch (Exception e) {
      /* no-op */ }

    atEOF = true;
    if (startPosition == endPosition) {
      op = charOld;
    }
    return makeToken(op, startPosition, endPosition, line);
  }

  public static String evenString(Symbol token, int left, int right, int line, String sym) {
    return String.format("%-11s left: %-8s right: %-8s line: %-8s " + sym, token, left, right, line);
  }

  private boolean isNumberLit(String number) {
    return number.matches("^\\d*\\.\\d+|\\d+\\.\\d*$");
  }

  private boolean isDateLit(String number) {
    return number.matches("^\\d{2}~\\d{2}~\\d{4}$") ||
        number.matches("^\\d{1}~\\d{2}~\\d{4}$") ||
        number.matches("^\\d{1}~\\d{1}~\\d{4}$") ||
        number.matches("^\\d{2}~\\d{1}~\\d{4}$") ||
        number.matches("^\\d{1}~\\d{2}~\\d{2}$") ||
        number.matches("^\\d{1}~\\d{1}~\\d{2}$") ||
        number.matches("^\\d{2}~\\d{1}~\\d{2}$") ||
        number.matches("^\\d{2}~\\d{2}~\\d{2}$");
  }

  public static void main(String args[]) {
    Token token;

    if (args.length > 0) {
      try {
        Lexer newLexer = new Lexer(args[0].toString());

        while (true) {
          token = newLexer.nextToken();
          System.out.println(evenString(token.getSymbol(), token.getLeftPosition(), token.getRightPosition(),
              token.getLineFound(), token.getKind().toString()));
        }

      } catch (Exception e) {

      }
    } else {
      System.out.println("java lexer.Lexer\n" + "usage: java lexer.Lexer filename.x");
    }
  }
}
