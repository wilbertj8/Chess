import java.awt.Image;
import javax.swing.ImageIcon;

/* Wilbert Joseph
*  Date: May 15, 2018
*  Program Name: Pawn.java
*  Description:
*/

public class Pawn extends Piece {
   public Pawn(boolean isInPlay, String color, int[] position, boolean firstMove) {
      super(isInPlay, color, position, firstMove);
      // TODO Auto-generated constructor stub
   }

   @Override
   public boolean isValidMove(int[] newPosition) {
      if (this.getPosition()[0] - newPosition[0] == 0 && this.getPosition()[1] - newPosition[1] == 0)
         return false;
      if (this.getColor().equals("Black")) {
         if (this.isFirstMove())
            return newPosition[1] - this.getPosition()[1] > 0 && newPosition[1] - this.getPosition()[1] <= 2
                  && this.getPosition()[0] - newPosition[0] == 0
                  || Math.abs(newPosition[0] - this.getPosition()[0]) <= 1
                        && newPosition[1] - this.getPosition()[1] == 1;
         return newPosition[1] - this.getPosition()[1] == 1 && Math.abs(this.getPosition()[0] - newPosition[0]) <= 1;
      } else if (this.getColor().equals("White")) {
         if (this.isFirstMove())
            return this.getPosition()[1] - newPosition[1] > 0 && this.getPosition()[1] - newPosition[1] <= 2
                  && this.getPosition()[0] - newPosition[0] == 0
                  || Math.abs(newPosition[0] - this.getPosition()[0]) <= 1
                        && this.getPosition()[1] - newPosition[1] == 1;
         return this.getPosition()[1] - newPosition[1] == 1 && Math.abs(this.getPosition()[0] - newPosition[0]) <= 1;
      }

      return false;
   }

   @Override
   public Image getImage() {
	   return (new ImageIcon("images/" + this.getColor() + this.getClass().getName() + ".png")).getImage();
   }
}
