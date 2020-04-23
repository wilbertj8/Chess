import java.awt.Image;
import javax.swing.ImageIcon;

/* Wilbert Joseph
*  Date: May 8, 2018
*  Program Name: Bishop.java
*  Description:
*/

public class Bishop extends Piece
{
   
   public Bishop(boolean isInPlay, String color, int[] position)
   {
      super(isInPlay, color, position, false);
   }
   
   @Override
   public boolean isValidMove(int[] newPosition)
   {
      if(this.getPosition()[0] == newPosition[0] || this.getPosition()[1] == newPosition[1])
         return false;
      return Math.abs(this.getPosition()[0] - newPosition[0]) == Math.abs(this.getPosition()[1] - newPosition[1]);
   }

   @Override
   public Image getImage() {
	   return (new ImageIcon("images/" + this.getColor() + this.getClass().getName() + ".png")).getImage();
   }

   @Override
   public boolean isFirstMove() {
      // TODO Auto-generated method stub
      return false;
   }
}
