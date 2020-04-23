import java.awt.Image;
import javax.swing.ImageIcon;

/* Wilbert Joseph
*  Date: May 8, 2018
*  Program Name: Knight.java
*  Description:
*/

public class Knight extends Piece
{

   public Knight(boolean isInPlay, String color, int[] position)
   {
      super(isInPlay, color, position, false);
      // TODO Auto-generated constructor stub
   }

   @Override
   public boolean isValidMove(int[] newPosition)
   {
      if(this.getPosition()[0] - newPosition[0] == 0 && this.getPosition()[1] - newPosition[1] == 0)
         return false;
      return Math.abs(this.getPosition()[0] - newPosition[0]) == 2 && Math.abs(this.getPosition()[1] - newPosition[1]) == 1 || Math.abs(this.getPosition()[0] - newPosition[0]) == 1 && Math.abs(this.getPosition()[1] - newPosition[1]) == 2;
   }

   @Override
   public Image getImage()
   {
	   return (new ImageIcon("images/" + this.getColor() + this.getClass().getName() + ".png")).getImage();
   }

   @Override
   public boolean isFirstMove() {
      // TODO Auto-generated method stub
      return false;
   }
   
}
