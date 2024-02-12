package compiler;

import ast.*;
import parser.Parser;
import visitor.*;

import java.io.*;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;

/**
 * The Compiler class contains the main program for compiling a source program
 * to bytecodes
 */
public class Compiler {

  /**
   * The Compiler class reads and compiles a source program
   */

  String sourceFile;

  public Compiler(String sourceFile) {
    this.sourceFile = sourceFile;
  }

  void compileProgram() {
    try {

      Parser parser = new Parser(sourceFile);
      AST ast = parser.execute();

      System.out.println("\n---------------AST-------------\n");

      PrintVisitor printer = new PrintVisitor();
      ast.accept(printer);
      System.out.println("\n ----------- \n");

      CountVisitor counter = new CountVisitor();
      ast.accept(counter);

      OffsetVisitor ov = new OffsetVisitor();
      ast.accept(ov);

      DrawVisitor visitor = new DrawVisitor(counter.getCount());
      ast.accept(visitor);

      try {
        File imagefile = new File(sourceFile + ".png");
        ImageIO.write(visitor.getImage(), "png", imagefile);
      } catch (Exception e) {
        System.out.println("Error in saving image: " + e.getMessage());
      }

      final JFrame f = new JFrame();
      f.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          f.dispose();
        }
      });
      JLabel imagelabel = new JLabel(new ImageIcon(visitor.getImage()));
      f.add("Center", imagelabel);
      f.pack();
      f.setSize(new Dimension(visitor.getImage().getWidth() + 30, visitor.getImage().getHeight() + 40));
      f.setVisible(true);
      f.setResizable(false);
      f.repaint();

    } catch (Exception e) {
      System.out.println("******** exception *******" + e.toString());
      System.out.println("\n ----------- \n");
    }
  }

  public static void main(String args[]) {

    boolean exit = false;
    Scanner scanner = new Scanner(System.in);

    while (!exit) {
      int choice = 100;

      System.out.println("\nSelect a file to compile: ");
      System.out.println("-1: codegen.x");
      System.out.println("-2: date.x");
      System.out.println("-3: doloop.x");
      System.out.println("-4: error.x");
      System.out.println("-5: factorial.x");
      System.out.println("-6: factorialErr.x");
      System.out.println("-7: fib.x");
      System.out.println("-8: forloop.x");
      System.out.println("-9: if.x");
      System.out.println("-10: scopes.x");
      System.out.println("-11: simple.x");
      System.out.println("\n-0: Exit Program");

      try {
        choice = scanner.nextInt();
      } catch (Exception e) {
        System.out.println("Invalid input. Please choose a number from the list.");
        scanner.nextLine();
        continue;
      }

      if (choice == 0) {
        exit = true;
      } else {
        String[] fileNames = { "codegen.x", "date.x", "doloop.x", "error.x", "factorial.x", "factorialErr.x", "fib.x",
            "forloop.x", "if.x", "scopes.x", "simple.x" };
        String path = "sample_files/" + fileNames[choice - 1];
        (new Compiler(path)).compileProgram();
      }
    }
    scanner.close();
    System.exit(0);
  }
}