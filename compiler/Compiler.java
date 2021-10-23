package compiler;

import ast.*;
import parser.Parser;
import visitor.*;

import java.io.*;
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

      System.out.println("\n---------------AST-------------");

      PrintVisitor pv = new PrintVisitor();
      ast.accept(pv);

      CountVisitor cv = new CountVisitor();
      ast.accept(cv);

      OffsetVisitor ov = new OffsetVisitor();
      ast.accept(ov);
      
      DrawOffsetVisitor dov = new DrawOffsetVisitor(cv.getCount(),ov.getOffsets(),ov.getMaxOffset());
      //DrawVisitor dov = new DrawVisitor(cv.getCount());
      ast.accept(dov);
      
      try {
        File imagefile = new File(sourceFile + ".png");
        ImageIO.write(dov.getImage(), "png", imagefile);
      } catch (Exception e) {
        System.out.println("Error in saving image: " + e.getMessage());
      }

      final JFrame f = new JFrame();
      f.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          f.dispose();
          System.exit(0);
        }
      });
      JLabel imagelabel = new JLabel(new ImageIcon(dov.getImage()));
      f.add("Center", imagelabel);
      f.pack();
      f.setSize(new Dimension(dov.getImage().getWidth() + 30, dov.getImage().getHeight() + 40));
      f.setVisible(true);
      f.setResizable(false);
      f.repaint();
      
    } catch (Exception e) {
      System.out.println("********exception*******" + e.toString());
    }
  }

  public static void main(String args[]) {
    if (args.length == 0) {
      System.out.println("***Incorrect usage, try: java compiler.Compiler <file>");
      System.exit(1);
    }
    (new Compiler(args[0])).compileProgram();
  }
}