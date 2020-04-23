import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/* Wilbert Joseph
*  Date: May 15, 2018
*  Program Name: Chess.java
*  Description:
*/

public class Chess extends JComponent {
   private static final long serialVersionUID = 1L;
   private Square[][] squares;
   private final Dimension d = new Dimension(800, 800);

   private ArrayList<Piece> white;
   private ArrayList<Piece> black;
   private boolean whiteMove, blackMove, whiteTurn;
   private int index, xDifference, yDifference;
   private int[] checkingPiecePosition;

   public Chess() {
      super();
      squares = new Square[8][8];
      white = new ArrayList<Piece>();
      black = new ArrayList<Piece>();
      xDifference = yDifference = 0;
      index = 8;
      whiteTurn = true;
      checkingPiecePosition = new int[2];
      setPreferredSize(d);
      init();
   }

   public void init() {
      // white pieces
      black.add(new King(true, "Black", new int[]{4, 0}, true));
      black.add(new Knight(true, "Black", new int[]{1, 0}));
      black.add(new Knight(true, "Black", new int[]{6, 0}));
      black.add(new Bishop(true, "Black", new int[]{2, 0}));
      black.add(new Bishop(true, "Black", new int[]{5, 0}));
      black.add(new Rook(true, "Black", new int[]{0, 0}, true));
      black.add(new Rook(true, "Black", new int[]{7, 0}, true));
      black.add(new Queen(true, "Black", new int[]{3, 0}));
      for (int i = 0; i < 8; i++)
         black.add(new Pawn(true, "Black", new int[]{i, 1}, true));

      // black pieces
      white.add(new King(true, "White", new int[]{4, 7}, true));
      white.add(new Knight(true, "White", new int[]{1, 7}));
      white.add(new Knight(true, "White", new int[]{6, 7}));
      white.add(new Bishop(true, "White", new int[]{2, 7}));
      white.add(new Bishop(true, "White", new int[]{5, 7}));
      white.add(new Rook(true, "White", new int[]{0, 7}, true));
      white.add(new Rook(true, "White", new int[]{7, 7}, true));
      white.add(new Queen(true, "White", new int[]{3, 7}));
      for (int i = 0; i < 8; i++)
         white.add(new Pawn(true, "White", new int[]{i, 6}, true));

      for (int i = 0; i < 8; i++)
         for (int j = 0; j < 8; j++) {
            squares[i][j] = new Square(100 * i, 100 * j, null);
            if (j == 0 || j == 1) {
               squares[i][j].setOccupied(true);
               squares[i][j].setBlackOccupied(true);
            } else if (j == 6 || j == 7) {
               squares[i][j].setOccupied(true);
               squares[i][j].setWhiteOccupied(true);
            }
         }

      setVisible(true);
   }

   public void paint(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      Color color = Color.white;
      for (int i = 0; i < 8; i++)
         for (int j = 0; j < 8; j++) {
            if ((j + i) % 2 == 0)
               color = Color.white;
            else
               color = Color.black;
            g2.setColor(color);
            g2.fill(squares[i][j]);
         }
      for (int i = 0; i < black.size(); i++) {
         if (black.get(i).isInPlay())
            g2.drawImage(black.get(i).getImage(), black.get(i).getX(), black.get(i).getY(), this);
         if (white.get(i).isInPlay())
            g2.drawImage(white.get(i).getImage(), white.get(i).getX(), white.get(i).getY(), this);
      }
   }

   public void start() {

   }

   public void selectPiece(int x, int y) {
      for (int i = 0; i < black.size(); i++)
         if (x / 100 == black.get(i).getPosition()[0] && y / 100 == black.get(i).getPosition()[1]) {
            index = i;
            blackMove = true;
            xDifference = x - black.get(i).getX();
            yDifference = y - black.get(i).getY();
            break;
         } else if (x / 100 == white.get(i).getPosition()[0] && y / 100 == white.get(i).getPosition()[1]) {
            index = i;
            whiteMove = true;
            xDifference = x - white.get(i).getX();
            yDifference = y - white.get(i).getY();
            break;
         }
      repaint();
   }

   public void drag(int x, int y) {
      if (!whiteTurn)
         if (blackMove) {
            black.get(index).setX(x - xDifference);
            black.get(index).setY(y - yDifference);
         }
      if (whiteTurn)
         if (whiteMove) {
            white.get(index).setX(x - xDifference);
            white.get(index).setY(y - yDifference);
         }
      repaint();
   }

   public void isValidMove(int[] position) {
      if (!whiteTurn)
         if (blackMove) {
            // if king is in check, lets king move away
            if (black.get(index) instanceof King && Math.abs(black.get(index).getPosition()[0] - position[0]) == 2)
               if (black.get(index).isFirstMove())
                  castle(position);
               else
                  invalidMove();
            else if (black.get(index) instanceof King && isInCheck(black.get(0).getPosition()))
               if (kingMovesFromCheck(position)) {
                  movePiece(position);
                  black.get(index).setFirstMove(false);
               } else
                  invalidMove();
            else if (black.get(index).isValidMove(position) && moveBlocksCheck(position)) {
               // rook valid move
               if (black.get(index) instanceof Rook)
                  if (validRookMove(position)) {
                     black.get(index).setFirstMove(false);
                     movePiece(position);
                  } else
                     invalidMove();
               // bishop valid move
               else if (black.get(index) instanceof Bishop)
                  if (validBishopMove(position))
                     movePiece(position);
                  else
                     invalidMove();
               // queen valid move
               else if (black.get(index) instanceof Queen)
                  if (validBishopMove(position) && validRookMove(position))
                     movePiece(position);
                  else
                     invalidMove();
               // pawn valid move
               else if (black.get(index) instanceof Pawn)
                  if (validPawnMove(position)) {
                     black.get(index).setFirstMove(false);
                     movePiece(position);
                     if (black.get(index).getPosition()[1] == 7) {
                        String[] pieces = {"Knight", "Bishop", "Rook", "Queen"};
                        String selectedPiece = null;
                        while (selectedPiece == null)
                           selectedPiece = (String) JOptionPane.showInputDialog(null, "Piece?", "Select Piece",
                                 JOptionPane.QUESTION_MESSAGE, null, pieces, pieces[0]);
                        switch (selectedPiece) {
                        case "Knight":
                           black.add(new Knight(true, "Black", new int[]{position[0], position[1]}));
                           break;
                        case "Bishop":
                           black.add(new Bishop(true, "Black", new int[]{position[0], position[1]}));
                           break;
                        case "Queen":
                           black.add(new Queen(true, "Black", new int[]{position[0], position[1]}));
                           break;
                        case "Rook":
                           black.add(new Rook(true, "Black", new int[]{position[0], position[1]}, false));
                           break;
                        }
                        black.remove(index);
                     }
                  } else
                     invalidMove();
               // valid king move
               else if (black.get(index) instanceof King)
                  if (isInCheck(position))
                     invalidMove();
                  else {
                     movePiece(position);
                     black.get(index).setFirstMove(false);
                  }
               // any other piece
               else
                  movePiece(position);
            } else
               invalidMove();
         }
      if (whiteTurn)
         if (whiteMove) {
            // if king is in check, lets king move away
            if (white.get(index) instanceof King && Math.abs(white.get(index).getPosition()[0] - position[0]) == 2) {
               if (white.get(index).isFirstMove())
                  castle(position);
               else
                  invalidMove();
            } else if (white.get(index) instanceof King && isInCheck(white.get(0).getPosition()))
               if (kingMovesFromCheck(position)) {
                  white.get(index).setFirstMove(false);
                  movePiece(position);
               } else
                  invalidMove();
            else if (white.get(index).isValidMove(position) && moveBlocksCheck(position)) {
               // rook valid move
               if (white.get(index) instanceof Rook)
                  if (validRookMove(position)) {
                     white.get(index).setFirstMove(false);
                     movePiece(position);
                  } else
                     invalidMove();
               // bishop valid move
               else if (white.get(index) instanceof Bishop)
                  if (validBishopMove(position))
                     movePiece(position);
                  else
                     invalidMove();
               // queen valid move
               else if (white.get(index) instanceof Queen)
                  if (validBishopMove(position) && validRookMove(position))
                     movePiece(position);
                  else
                     invalidMove();
               // pawn valid move
               else if (white.get(index) instanceof Pawn)
                  if (validPawnMove(position)) {
                     white.get(index).setFirstMove(false);
                     movePiece(position);
                     if (white.get(index).getPosition()[1] == 0) {
                        String[] pieces = {"Knight", "Bishop", "Rook", "Queen"};
                        String selectedPiece = null;
                        while (selectedPiece == null)
                           selectedPiece = (String) JOptionPane.showInputDialog(null, "Piece?", "Select Piece",
                                 JOptionPane.QUESTION_MESSAGE, null, pieces, pieces[0]);
                        switch (selectedPiece) {
                        case "Knight":
                           white.add(new Knight(true, "White", new int[]{position[0], position[1]}));
                           break;
                        case "Bishop":
                           white.add(new Bishop(true, "White", new int[]{position[0], position[1]}));
                           break;
                        case "Queen":
                           white.add(new Queen(true, "White", new int[]{position[0], position[1]}));
                           break;
                        case "Rook":
                           white.add(new Rook(true, "White", new int[]{position[0], position[1]}, false));
                           break;
                        }
                        white.remove(index);
                     }
                  } else
                     invalidMove();
               // valid king move
               else if (white.get(index) instanceof King) {
                  if (isInCheck(position))
                     invalidMove();
                  else {
                     movePiece(position);
                     white.get(index).setFirstMove(false);
                  }
               }
               // any other piece
               else
                  movePiece(position);
            } else
               invalidMove();
         }
      blackMove = !blackMove;
      whiteMove = !whiteMove;
      if (

      isInCheckmate())
         if (whiteMove) {
            JOptionPane.showMessageDialog(this, "Black Wins");
            System.exit(0);
         } else if (blackMove) {
            JOptionPane.showMessageDialog(this, "White Wins");
            System.exit(0);
         }
      blackMove = whiteMove = false;
      repaint();
   }

   private boolean kingMovesFromCheck(int[] position) {
      if (whiteMove) {
         squares[white.get(0).getPosition()[0]][white.get(0).getPosition()[1]].setOccupied(false);
         // if king takes square to get out of check
         boolean takes = false;
         if (squares[position[0]][position[1]].isOccupied()) {
            takes = true;
            for (int i = 0; i < black.size(); i++)
               if (Arrays.equals(black.get(i).getPosition(), position))
                  black.get(i).setInPlay(false);
               else if (Arrays.equals(white.get(i).getPosition(), position)) {
                  squares[black.get(0).getPosition()[0]][black.get(0).getPosition()[1]].setOccupied(true);
                  return false;
               }
         }
         squares[position[0]][position[1]].setOccupied(true);
         if (isInCheck(position)) {
            squares[white.get(0).getPosition()[0]][white.get(0).getPosition()[1]].setOccupied(true);
            if (!takes)
               squares[position[0]][position[1]].setOccupied(false);
            if (takes)
               if (squares[position[0]][position[1]].isOccupied()) {
                  for (int i = 0; i < black.size(); i++)
                     if (Arrays.equals(black.get(i).getPosition(), position))
                        black.get(i).setInPlay(true);
               }
            return false;
         } else {
            squares[white.get(0).getPosition()[0]][white.get(0).getPosition()[1]].setOccupied(true);
            squares[position[0]][position[1]].setOccupied(false);
            return true;
         }
      } else {
         squares[black.get(0).getPosition()[0]][black.get(0).getPosition()[1]].setOccupied(false);
         // if king takes square to get out of check
         boolean takes = false;
         if (squares[position[0]][position[1]].isOccupied()) {
            takes = true;
            for (int i = 0; i < white.size(); i++)
               if (Arrays.equals(white.get(i).getPosition(), position))
                  white.get(i).setInPlay(false);
               else if (Arrays.equals(black.get(i).getPosition(), position)) {
                  squares[black.get(0).getPosition()[0]][black.get(0).getPosition()[1]].setOccupied(true);
                  return false;
               }
         }
         squares[position[0]][position[1]].setOccupied(true);
         if (isInCheck(position)) {
            squares[black.get(0).getPosition()[0]][black.get(0).getPosition()[1]].setOccupied(true);
            if (!takes)
               squares[position[0]][position[1]].setOccupied(false);
            if (takes)
               if (squares[position[0]][position[1]].isOccupied()) {
                  for (int i = 0; i < white.size(); i++)
                     if (Arrays.equals(white.get(i).getPosition(), position))
                        white.get(i).setInPlay(true);
               }
            return false;
         } else {
            squares[white.get(0).getPosition()[0]][white.get(0).getPosition()[1]].setOccupied(true);
            squares[position[0]][position[1]].setOccupied(false);
            return true;
         }
      }
   }

   private boolean moveBlocksCheck(int[] position) {
      if (whiteMove) {
         if (white.get(index).isValidMove(position)
               && (white.get(index) instanceof Queen && validBishopMove(position) && validRookMove(position)
                     || white.get(index) instanceof Rook && validRookMove(position)
                     || white.get(index) instanceof Bishop && validBishopMove(position)
                     || white.get(index) instanceof Pawn && validPawnMove(position)
                     || white.get(index) instanceof Knight || white.get(index) instanceof King)) {
            // if piece attacks to stop check, return true
            if (squares[position[0]][position[1]].isOccupied())
               for (int i = 0; i < black.size(); i++)
                  if (Arrays.equals(position, black.get(i).getPosition())) {
                     squares[white.get(index).getPosition()[0]][white.get(index).getPosition()[1]].setOccupied(false);
                     black.get(i).setInPlay(false);
                     boolean failed = isInCheck(white.get(0).getPosition());
                     squares[white.get(index).getPosition()[0]][white.get(index).getPosition()[1]].setOccupied(true);
                     black.get(i).setInPlay(true);
                     return !failed;
                  } else if (Arrays.equals(position, white.get(i).getPosition()))
                     return false;
            squares[position[0]][position[1]].setOccupied(true);
            squares[white.get(index).getPosition()[0]][white.get(index).getPosition()[1]].setOccupied(false);
            boolean failed = isInCheck(white.get(0).getPosition());
            squares[position[0]][position[1]].setOccupied(false);
            squares[white.get(index).getPosition()[0]][white.get(index).getPosition()[1]].setOccupied(true);
            return !failed;
         }
      }
      if (blackMove) {
         if (black.get(index).isValidMove(position)
               && (black.get(index) instanceof Queen && validBishopMove(position) && validRookMove(position)
                     || black.get(index) instanceof Rook && validRookMove(position)
                     || black.get(index) instanceof Bishop && validBishopMove(position)
                     || black.get(index) instanceof Pawn && validPawnMove(position)
                     || black.get(index) instanceof Knight || black.get(index) instanceof King)) {
            // if piece attacks to stop check, return true
            if (squares[position[0]][position[1]].isOccupied())
               for (int i = 0; i < black.size(); i++)
                  if (Arrays.equals(position, white.get(i).getPosition())) {
                     white.get(i).setInPlay(false);
                     squares[black.get(index).getPosition()[0]][black.get(index).getPosition()[1]].setOccupied(false);
                     boolean failed = isInCheck(black.get(0).getPosition());
                     squares[black.get(index).getPosition()[0]][black.get(index).getPosition()[1]].setOccupied(true);
                     white.get(i).setInPlay(true);
                     return !failed;
                  } else if (Arrays.equals(position, black.get(i).getPosition()))
                     return false;
            squares[position[0]][position[1]].setOccupied(true);
            squares[black.get(index).getPosition()[0]][black.get(index).getPosition()[1]].setOccupied(false);
            boolean failed = isInCheck(black.get(0).getPosition());
            squares[position[0]][position[1]].setOccupied(false);
            squares[black.get(index).getPosition()[0]][black.get(index).getPosition()[1]].setOccupied(true);
            return !failed;
         }
      }
      return false;
   }

   private boolean validRookMove(int[] position) {
      if (blackMove)
         if (black.get(index).getPosition()[0] - position[0] == 0) {
            int largest = Math.max(black.get(index).getPosition()[1], position[1]);
            int smallest = Math.min(black.get(index).getPosition()[1], position[1]);
            for (int i = smallest + 1; i < largest; i++)
               if (squares[position[0]][i].isOccupied())
                  return false;
         } else if (black.get(index).getPosition()[1] - position[1] == 0) {
            int largest = Math.max(black.get(index).getPosition()[0], position[0]);
            int smallest = Math.min(black.get(index).getPosition()[0], position[0]);
            for (int i = smallest + 1; i < largest; i++)
               if (squares[i][position[1]].isOccupied())
                  return false;
         }
      if (whiteMove)
         if (white.get(index).getPosition()[0] - position[0] == 0) {
            int largest = Math.max(white.get(index).getPosition()[1], position[1]);
            int smallest = Math.min(white.get(index).getPosition()[1], position[1]);
            for (int i = smallest + 1; i < largest; i++)
               if (squares[position[0]][i].isOccupied())
                  return false;
         } else if (white.get(index).getPosition()[1] - position[1] == 0) {
            int largest = Math.max(white.get(index).getPosition()[0], position[0]);
            int smallest = Math.min(white.get(index).getPosition()[0], position[0]);
            for (int i = smallest + 1; i < largest; i++)
               if (squares[i][position[1]].isOccupied())
                  return false;
         }
      return true;
   }

   private boolean validBishopMove(int[] position) {
      if (blackMove) {
         int largestX = Math.max(position[0], black.get(index).getPosition()[0]);
         int smallestX = Math.min(position[0], black.get(index).getPosition()[0]);
         int largestY = Math.max(position[1], black.get(index).getPosition()[1]);
         int smallestY = Math.min(position[1], black.get(index).getPosition()[1]);
         int yDifference = position[0] - black.get(index).getPosition()[0];
         int xDifference = position[1] - black.get(index).getPosition()[1];

         for (int i = 1; i < largestX - smallestX; i++)
            if (xDifference > 0 && yDifference > 0) {
               if (squares[smallestX + i][smallestY + i].isOccupied())
                  return false;
            } else if (xDifference > 0 && yDifference < 0) {
               if (squares[smallestX + i][largestY - i].isOccupied())
                  return false;
            } else if (xDifference < 0 && yDifference > 0) {
               if (squares[largestX - i][smallestY + i].isOccupied())
                  return false;
            } else if (xDifference < 0 && yDifference < 0) {
               if (squares[largestX - i][largestY - i].isOccupied())
                  return false;
            }
      }
      if (whiteMove) {
         int largestX = Math.max(position[0], white.get(index).getPosition()[0]);
         int smallestX = Math.min(position[0], white.get(index).getPosition()[0]);
         int largestY = Math.max(position[1], white.get(index).getPosition()[1]);
         int smallestY = Math.min(position[1], white.get(index).getPosition()[1]);
         int yDifference = position[0] - white.get(index).getPosition()[0];
         int xDifference = position[1] - white.get(index).getPosition()[1];

         for (int i = 1; i < largestX - smallestX; i++)
            if (xDifference > 0 && yDifference > 0) {
               if (squares[smallestX + i][smallestY + i].isOccupied())
                  return false;
            } else if (xDifference > 0 && yDifference < 0) {
               if (squares[smallestX + i][largestY - i].isOccupied())
                  return false;
            } else if (xDifference < 0 && yDifference > 0) {
               if (squares[largestX - i][smallestY + i].isOccupied())
                  return false;
            } else if (xDifference < 0 && yDifference < 0) {
               if (squares[largestX - i][largestY - i].isOccupied())
                  return false;
            }
      }
      return true;
   }

   private boolean validPawnMove(int[] position) {
      if (blackMove) {
         // first pawn move
         if (black.get(index).isFirstMove() && Math.abs(position[1] - black.get(index).getPosition()[1]) == 2
               && squares[position[0]][2].isOccupied())
            return false;
         // diagonal capture
         if (Math.abs(position[0] - black.get(index).getPosition()[0]) == 1
               && !squares[position[0]][position[1]].isOccupied())
            return false;
         // forward 1 square
         if (position[0] - black.get(index).getPosition()[0] == 0 && squares[position[0]][position[1]].isOccupied())
            return false;
      } else if (whiteMove) {
         // first pawn move
         if (white.get(index).isFirstMove() && Math.abs(position[1] - white.get(index).getPosition()[1]) == 2
               && squares[position[0]][5].isOccupied())
            return false;
         // diagonal capture
         if (Math.abs(position[0] - white.get(index).getPosition()[0]) == 1
               && !squares[position[0]][position[1]].isOccupied())
            return false;
         // forward 1 square
         if (position[0] - white.get(index).getPosition()[0] == 0 && squares[position[0]][position[1]].isOccupied()) {
            return false;
         }
      }
      return true;
   }

   private void invalidMove() {
      if (blackMove) {
         black.get(index).setX(black.get(index).getPosition()[0] * 100);
         black.get(index).setY(black.get(index).getPosition()[1] * 100);
      } else if (whiteMove) {
         white.get(index).setX(white.get(index).getPosition()[0] * 100);
         white.get(index).setY(white.get(index).getPosition()[1] * 100);
      }
   }

   private void movePiece(int[] position) {
      boolean invalid = false;
      if (blackMove) {
         if (squares[position[0]][position[1]].isOccupied())
            if (squares[position[0]][position[1]].isWhiteOccupied())
               removePiece(position);
            else if (squares[position[0]][position[1]].isBlackOccupied()) {
               invalidMove();
               invalid = true;
            }
         if (!invalid) {
            squares[black.get(index).getPosition()[0]][black.get(index).getPosition()[1]].setOccupied(false);
            squares[black.get(index).getPosition()[0]][black.get(index).getPosition()[1]].setBlackOccupied(false);
            black.get(index).setPosition(position);
            black.get(index).setX(position[0] * 100);
            black.get(index).setY(position[1] * 100);
            squares[position[0]][position[1]].setOccupied(true);
            squares[position[0]][position[1]].setBlackOccupied(true);
            whiteTurn = true;
         }
      } else if (whiteMove) {
         if (squares[position[0]][position[1]].isOccupied())
            if (squares[position[0]][position[1]].isBlackOccupied())
               removePiece(position);
            else if (squares[position[0]][position[1]].isWhiteOccupied()) {
               invalidMove();
               invalid = true;
            }
         if (!invalid) {
            squares[white.get(index).getPosition()[0]][white.get(index).getPosition()[1]].setOccupied(false);
            squares[white.get(index).getPosition()[0]][white.get(index).getPosition()[1]].setWhiteOccupied(false);
            white.get(index).setPosition(position);
            white.get(index).setX(position[0] * 100);
            white.get(index).setY(position[1] * 100);
            squares[position[0]][position[1]].setOccupied(true);
            squares[position[0]][position[1]].setWhiteOccupied(true);
            whiteTurn = false;
         }
      }
      repaint();
   }

   private void removePiece(int[] position) {
      if (blackMove)
         for (int i = 0; i < black.size(); i++)
            if (Arrays.equals(position, white.get(i).getPosition())) {
               squares[position[0]][position[1]].setWhiteOccupied(false);
               white.get(i).setPosition(new int[]{8, 8});
               white.get(i).setInPlay(false);
               break;
            }
      if (whiteMove)
         for (int i = 0; i < black.size(); i++)
            if (Arrays.equals(position, black.get(i).getPosition())) {
               squares[position[0]][position[1]].setBlackOccupied(false);
               black.get(i).setPosition(new int[]{8, 8});
               black.get(i).setInPlay(false);
               break;
            }
   }

   private boolean isInCheck(int position[]) {
      int num = index;
      if (whiteMove) {
         whiteMove = false;
         blackMove = true;
         for (int i = 0; i < black.size(); i++) {
            index = i;
            if (black.get(i).isInPlay())
               if (black.get(i).isValidMove(position)
                     && (black.get(i) instanceof Queen && validBishopMove(position) && validRookMove(position)
                           || black.get(i) instanceof Rook && validRookMove(position)
                           || black.get(i) instanceof Bishop && validBishopMove(position)
                           || black.get(i) instanceof Pawn && Math.abs(black.get(i).getPosition()[0] - position[0]) == 1
                           || black.get(i) instanceof Knight || black.get(i) instanceof King)) {
                  if (num == 0)
                     checkingPiecePosition = black.get(i).getPosition();
                  index = num;
                  blackMove = false;
                  whiteMove = true;
                  return true;
               }
         }
      }
      // if black move, then check if the white pieces can attack square
      else if (blackMove) {
         whiteMove = true;
         blackMove = false;
         for (int i = 0; i < white.size(); i++) {
            index = i;
            if (white.get(i).isInPlay())
               if (white.get(i).isValidMove(position)
                     && (white.get(i) instanceof Queen && validBishopMove(position) && validRookMove(position)
                           || white.get(i) instanceof Rook && validRookMove(position)
                           || white.get(i) instanceof Bishop && validBishopMove(position)
                           || white.get(i) instanceof Pawn && Math.abs(white.get(i).getPosition()[0] - position[0]) == 1
                           || white.get(i) instanceof Knight || white.get(i) instanceof King)) {
                  if (num == 0)
                     checkingPiecePosition = black.get(i).getPosition();
                  blackMove = true;
                  whiteMove = false;
                  index = num;
                  return true;
               }
         }
      }
      whiteMove = !whiteMove;
      blackMove = !blackMove;
      index = num;
      return false;
   }

   private boolean isInCheckmate() {
      if (blackMove)
         if (isInCheck(black.get(0).getPosition())) {
            for (int i = 1; i < black.size(); i++) {
               index = i;
               if (moveBlocksCheck(checkingPiecePosition)) {
                  System.out.println(false + "" + i);
                  return false;
               }
            }
            // if king is anywhere in middle
            if (black.get(0).getPosition()[0] > 0 && black.get(0).getPosition()[0] < 7
                  && black.get(0).getPosition()[1] > 0 && black.get(0).getPosition()[1] < 7) {
               if (!isInCheck(new int[]{black.get(0).getPosition()[0] - 1, black.get(0).getPosition()[1] - 1})
                     && !squares[black.get(0).getPosition()[0] - 1][black.get(0).getPosition()[1] - 1]
                           .isBlackOccupied())
                  return false;
               else if (!isInCheck(new int[]{black.get(0).getPosition()[0] - 1, black.get(0).getPosition()[1]})
                     && !squares[black.get(0).getPosition()[0] - 1][black.get(0).getPosition()[1]].isBlackOccupied())
                  return false;
               else if (!isInCheck(new int[]{black.get(0).getPosition()[0] - 1, black.get(0).getPosition()[1] + 1})
                     && !squares[black.get(0).getPosition()[0] - 1][black.get(0).getPosition()[1] + 1]
                           .isBlackOccupied())
                  return false;
               else if (!isInCheck(new int[]{black.get(0).getPosition()[0], black.get(0).getPosition()[1] - 1})
                     && !squares[black.get(0).getPosition()[0]][black.get(0).getPosition()[1] - 1].isBlackOccupied())
                  return false;
               else if (!isInCheck(new int[]{black.get(0).getPosition()[0], black.get(0).getPosition()[1] + 1})
                     && !squares[black.get(0).getPosition()[0]][black.get(0).getPosition()[1] + 1].isBlackOccupied())
                  return false;
               else if (!isInCheck(new int[]{black.get(0).getPosition()[0] + 1, black.get(0).getPosition()[1] - 1})
                     && !squares[black.get(0).getPosition()[0] + 1][black.get(0).getPosition()[1] - 1]
                           .isBlackOccupied())
                  return false;
               else if (!isInCheck(new int[]{black.get(0).getPosition()[0] + 1, black.get(0).getPosition()[1]})
                     && !squares[black.get(0).getPosition()[0] + 1][black.get(0).getPosition()[1]].isBlackOccupied())
                  return false;
               else if (!isInCheck(new int[]{black.get(0).getPosition()[0] + 1, black.get(0).getPosition()[1] + 1})
                     && !squares[black.get(0).getPosition()[0] + 1][black.get(0).getPosition()[1] + 1]
                           .isBlackOccupied())
                  return false;
            }
            // if king is in corner
            else if ((black.get(0).getPosition()[0] == 0 || black.get(0).getPosition()[0] == 7)
                  && (black.get(0).getPosition()[1] == 0 || black.get(0).getPosition()[1] == 7)) {
               int xNum, yNum;
               xNum = yNum = 1;
               if (black.get(0).getPosition()[0] == 7)
                  xNum = -1;
               if (black.get(0).getPosition()[1] == 7)
                  yNum = -1;
               if (!isInCheck(new int[]{black.get(0).getPosition()[0], black.get(0).getPosition()[1] + yNum})
                     && !squares[black.get(0).getPosition()[0]][black.get(0).getPosition()[1] + yNum].isBlackOccupied())
                  return false;
               else if (!isInCheck(new int[]{black.get(0).getPosition()[0] + xNum, black.get(0).getPosition()[1]})
                     && !squares[black.get(0).getPosition()[0] + xNum][black.get(0).getPosition()[1]].isBlackOccupied())
                  return false;
               else if (!isInCheck(
                     new int[]{black.get(0).getPosition()[0] + xNum, black.get(0).getPosition()[1] + yNum})
                     && !squares[black.get(0).getPosition()[0] + xNum][black.get(0).getPosition()[1] + yNum]
                           .isBlackOccupied())
                  return false;
            }
            // if king is on left or right edges
            else if (black.get(0).getPosition()[0] == 0 || black.get(0).getPosition()[0] == 7) {
               int num = -1;
               if (black.get(0).getPosition()[0] == 0)
                  num = 1;
               if (!isInCheck(new int[]{black.get(0).getPosition()[0], black.get(0).getPosition()[1] - 1})
                     && !squares[black.get(0).getPosition()[0]][black.get(0).getPosition()[1] - 1].isBlackOccupied())
                  return false;
               else if (!isInCheck(new int[]{black.get(0).getPosition()[0], black.get(0).getPosition()[1] + 1})
                     && !squares[black.get(0).getPosition()[0]][black.get(0).getPosition()[1] + 1].isBlackOccupied())
                  return false;
               else if (!isInCheck(new int[]{black.get(0).getPosition()[0] + num, black.get(0).getPosition()[1] - 1})
                     && !squares[black.get(0).getPosition()[0] + num][black.get(0).getPosition()[1] - 1]
                           .isBlackOccupied())
                  return false;
               else if (!isInCheck(new int[]{black.get(0).getPosition()[0] + num, black.get(0).getPosition()[1]})
                     && !squares[black.get(0).getPosition()[0] + num][black.get(0).getPosition()[1]].isBlackOccupied())
                  return false;
               else if (!isInCheck(new int[]{black.get(0).getPosition()[0] + num, black.get(0).getPosition()[1] + 1})
                     && !squares[black.get(0).getPosition()[0] + num][black.get(0).getPosition()[1] + 1]
                           .isBlackOccupied())
                  return false;
            }
            // if king is on top or bottom edges
            else if (black.get(0).getPosition()[1] == 0 || black.get(0).getPosition()[1] == 7) {
               int num = -1;
               if (black.get(0).getPosition()[1] == 0)
                  num = 1;
               if (!isInCheck(new int[]{black.get(0).getPosition()[0] + 1, black.get(0).getPosition()[1]})
                     && !squares[black.get(0).getPosition()[0] + 1][black.get(0).getPosition()[1]].isBlackOccupied())
                  return false;
               else if (!isInCheck(new int[]{black.get(0).getPosition()[0] - 1, black.get(0).getPosition()[1]})
                     && !squares[black.get(0).getPosition()[0] - 1][black.get(0).getPosition()[1]].isBlackOccupied())
                  return false;
               else if (!isInCheck(new int[]{black.get(0).getPosition()[0] + 1, black.get(0).getPosition()[1] + num})
                     && !squares[black.get(0).getPosition()[0] + 1][black.get(0).getPosition()[1] + num]
                           .isBlackOccupied())
                  return false;
               else if (!isInCheck(new int[]{black.get(0).getPosition()[0], black.get(0).getPosition()[1] + num})
                     && !squares[black.get(0).getPosition()[0]][black.get(0).getPosition()[1] + num].isBlackOccupied())
                  return false;
               else if (!isInCheck(new int[]{black.get(0).getPosition()[0] - 1, black.get(0).getPosition()[1] + num})
                     && !squares[black.get(0).getPosition()[0] - 1][black.get(0).getPosition()[1] + num]
                           .isBlackOccupied())
                  return false;
            }
         } else
            return false;

      if (whiteMove)
         if (isInCheck(white.get(0).getPosition())) {
            for (int i = 1; i < black.size(); i++) {
               index = i;
               if (moveBlocksCheck(checkingPiecePosition))
                  return false;
            }
            // if king is anywhere in middle
            if (white.get(0).getPosition()[0] > 0 && white.get(0).getPosition()[0] < 7
                  && white.get(0).getPosition()[1] > 0 && white.get(0).getPosition()[1] < 7) {
               if (!isInCheck(new int[]{white.get(0).getPosition()[0] - 1, white.get(0).getPosition()[1] - 1})
                     && !squares[white.get(0).getPosition()[0] - 1][white.get(0).getPosition()[1] - 1]
                           .isWhiteOccupied())
                  return false;
               else if (!isInCheck(new int[]{white.get(0).getPosition()[0] - 1, white.get(0).getPosition()[1]})
                     && !squares[white.get(0).getPosition()[0] - 1][white.get(0).getPosition()[1]].isWhiteOccupied())
                  return false;
               else if (!isInCheck(new int[]{white.get(0).getPosition()[0] - 1, white.get(0).getPosition()[1] + 1})
                     && !squares[white.get(0).getPosition()[0] - 1][white.get(0).getPosition()[1] + 1]
                           .isWhiteOccupied())
                  return false;
               else if (!isInCheck(new int[]{white.get(0).getPosition()[0], white.get(0).getPosition()[1] - 1})
                     && !squares[white.get(0).getPosition()[0]][white.get(0).getPosition()[1] - 1].isWhiteOccupied())
                  return false;
               else if (!isInCheck(new int[]{white.get(0).getPosition()[0], white.get(0).getPosition()[1] + 1})
                     && !squares[white.get(0).getPosition()[0]][white.get(0).getPosition()[1] + 1].isWhiteOccupied())
                  return false;
               else if (!isInCheck(new int[]{white.get(0).getPosition()[0] + 1, white.get(0).getPosition()[1] - 1})
                     && !squares[white.get(0).getPosition()[0] + 1][white.get(0).getPosition()[1] - 1]
                           .isWhiteOccupied())
                  return false;
               else if (!isInCheck(new int[]{white.get(0).getPosition()[0] + 1, white.get(0).getPosition()[1]})
                     && !squares[white.get(0).getPosition()[0] + 1][white.get(0).getPosition()[1]].isWhiteOccupied())
                  return false;
               else if (!isInCheck(new int[]{white.get(0).getPosition()[0] + 1, white.get(0).getPosition()[1] + 1})
                     && !squares[white.get(0).getPosition()[0] + 1][white.get(0).getPosition()[1] + 1]
                           .isWhiteOccupied())
                  return false;
            }
            // if king is in corner
            else if ((white.get(0).getPosition()[0] == 0 || white.get(0).getPosition()[0] == 7)
                  && (white.get(0).getPosition()[1] == 0 || white.get(0).getPosition()[1] == 7)) {
               int xNum, yNum;
               xNum = yNum = 1;
               if (white.get(0).getPosition()[0] == 7)
                  xNum = -1;
               if (white.get(0).getPosition()[1] == 7)
                  yNum = -1;
               if (!isInCheck(new int[]{white.get(0).getPosition()[0], white.get(0).getPosition()[1] + yNum})
                     && !squares[white.get(0).getPosition()[0]][white.get(0).getPosition()[1] + yNum].isWhiteOccupied())
                  return false;
               else if (!isInCheck(new int[]{white.get(0).getPosition()[0] + xNum, white.get(0).getPosition()[1]})
                     && !squares[white.get(0).getPosition()[0] + xNum][white.get(0).getPosition()[1]].isWhiteOccupied())
                  return false;
               else if (!isInCheck(
                     new int[]{white.get(0).getPosition()[0] + xNum, white.get(0).getPosition()[1] + yNum})
                     && !squares[white.get(0).getPosition()[0] + xNum][white.get(0).getPosition()[1] + yNum]
                           .isWhiteOccupied())
                  return false;
            }
            // if king is on left or right edges
            else if (white.get(0).getPosition()[0] == 0 || white.get(0).getPosition()[0] == 7) {
               int num = -1;
               if (white.get(0).getPosition()[0] == 0)
                  num = 1;
               if (!isInCheck(new int[]{white.get(0).getPosition()[0], white.get(0).getPosition()[1] - 1})
                     && !squares[white.get(0).getPosition()[0]][white.get(0).getPosition()[1] - 1].isWhiteOccupied())
                  return false;
               else if (!isInCheck(new int[]{white.get(0).getPosition()[0], white.get(0).getPosition()[1] + 1})
                     && !squares[white.get(0).getPosition()[0]][white.get(0).getPosition()[1] + 1].isWhiteOccupied())
                  return false;
               else if (!isInCheck(new int[]{white.get(0).getPosition()[0] + num, white.get(0).getPosition()[1] - 1})
                     && !squares[white.get(0).getPosition()[0] + num][white.get(0).getPosition()[1] - 1]
                           .isWhiteOccupied())
                  return false;
               else if (!isInCheck(new int[]{white.get(0).getPosition()[0] + num, white.get(0).getPosition()[1]})
                     && !squares[white.get(0).getPosition()[0] + num][white.get(0).getPosition()[1]].isWhiteOccupied())
                  return false;
               else if (!isInCheck(new int[]{white.get(0).getPosition()[0] + num, white.get(0).getPosition()[1] + 1})
                     && !squares[white.get(0).getPosition()[0] + num][white.get(0).getPosition()[1] + 1]
                           .isWhiteOccupied())
                  return false;
            }
            // if king is on top or bottom edges
            else if (white.get(0).getPosition()[1] == 0 || white.get(0).getPosition()[1] == 7) {
               int num = -1;
               if (white.get(0).getPosition()[1] == 0)
                  num = 1;
               if (!isInCheck(new int[]{white.get(0).getPosition()[0] + 1, white.get(0).getPosition()[1]})
                     && !squares[white.get(0).getPosition()[0] + 1][white.get(0).getPosition()[1]].isWhiteOccupied())
                  return false;
               else if (!isInCheck(new int[]{white.get(0).getPosition()[0] - 1, white.get(0).getPosition()[1]})
                     && !squares[white.get(0).getPosition()[0] - 1][white.get(0).getPosition()[1]].isWhiteOccupied())
                  return false;
               else if (!isInCheck(new int[]{white.get(0).getPosition()[0] + 1, white.get(0).getPosition()[1] + num})
                     && !squares[white.get(0).getPosition()[0] + 1][white.get(0).getPosition()[1] + num]
                           .isWhiteOccupied())
                  return false;
               else if (!isInCheck(new int[]{white.get(0).getPosition()[0], white.get(0).getPosition()[1] + num})
                     && !squares[white.get(0).getPosition()[0]][white.get(0).getPosition()[1] + num].isWhiteOccupied())
                  return false;
               else if (!isInCheck(new int[]{white.get(0).getPosition()[0] - 1, white.get(0).getPosition()[1] + num})
                     && !squares[white.get(0).getPosition()[0] - 1][white.get(0).getPosition()[1] + num]
                           .isWhiteOccupied())
                  return false;
            }
         } else
            return false;
      return true;
   }

   private void castle(int[] position) {
      // if black move
      if (blackMove) {
         // if queen side castle
         if (position[0] == 2) {
            // check if squares are occupied or are in check
            if (!squares[1][0].isOccupied() && !squares[2][0].isOccupied() && !squares[3][0].isOccupied()
                  && black.get(5).isFirstMove()) {
               if (!isInCheck(new int[]{0, 0}) && !isInCheck(new int[]{1, 0}) && !isInCheck(new int[]{2, 0})
                     && !isInCheck(new int[]{3, 0}) && !isInCheck(new int[]{4, 0})) {
                  movePiece(new int[]{2, 0});
                  index = 5;
                  movePiece(new int[]{3, 0});
               } else
                  invalidMove();
            } else
               invalidMove();
         } else if (position[0] == 6) { // if king side castle
            // check if squares are occupied or are in check
            if (!squares[5][0].isOccupied() && !squares[5][0].isOccupied() && black.get(6).isFirstMove()) {
               if (!isInCheck(new int[]{4, 0}) && !isInCheck(new int[]{5, 0}) && !isInCheck(new int[]{6, 0})
                     && !isInCheck(new int[]{7, 0})) {
                  movePiece(new int[]{6, 0});
                  index = 6;
                  movePiece(new int[]{5, 0});
                  black.get(index).setFirstMove(false);
               } else
                  invalidMove();
            } else
               invalidMove();
         }
      } else if (whiteMove) { // if white move
         // if queen side castle
         if (position[0] == 2) {
            if (!squares[1][7].isOccupied() && !squares[2][7].isOccupied() && !squares[3][7].isOccupied()
                  && white.get(5).isFirstMove()) {
               // check if squares are occupied or are in check
               if (!isInCheck(new int[]{0, 7}) && !isInCheck(new int[]{1, 7}) && !isInCheck(new int[]{2, 7})
                     && !isInCheck(new int[]{3, 7}) && !isInCheck(new int[]{4, 7})) {
                  movePiece(new int[]{2, 7});
                  index = 5;
                  movePiece(new int[]{3, 7});
               } else
                  invalidMove();
            } else
               invalidMove();
         } else if (position[0] == 6) { // if king side castle
            // check if squares are occupied or are in check
            if (!squares[5][7].isOccupied() && !squares[5][7].isOccupied() && white.get(6).isFirstMove()) {
               if (!isInCheck(new int[]{4, 7}) && !isInCheck(new int[]{5, 7}) && !isInCheck(new int[]{6, 7})
                     && !isInCheck(new int[]{7, 7})) {
                  movePiece(new int[]{6, 7});
                  index = 6;
                  movePiece(new int[]{5, 7});
                  white.get(index).setFirstMove(false);
               } else
                  invalidMove();
            } else
               invalidMove();
         }
      }
   }
}
