import java.awt.Image;
import javax.swing.ImageIcon;

/* Wilbert Joseph
*  Date: May 15, 2018
*  Program Name: Pawn.java
*  Description:
*/

public class King extends Piece
{
   public King(boolean isInPlay, String color, int[] position, boolean firstMove)
   {
      super(isInPlay, color, position, firstMove);
      // TODO Auto-generated constructor stub
   }

   @Override
   public boolean isValidMove(int[] newPosition)
   {
      if(this.getPosition()[0] - newPosition[0] == 0 && this.getPosition()[1] - newPosition[1] == 0)
         return false;
      return Math.abs(this.getPosition()[0] - newPosition[0]) <= 1 && Math.abs(this.getPosition()[1] - newPosition[1]) <= 1;      
   }

   @Override
   public Image getImage()
   {
      return (new ImageIcon(this.getClass().getResource(this.getColor() + this.getClass().getName() + ".png"))).getImage();
   }
}
