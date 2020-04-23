import java.awt.Image;
import javax.swing.ImageIcon;

/* Wilbert Joseph
*  Date: May 15, 2018
*  Program Name: Pawn.java
*  Description:
*/

public class Rook extends Piece
{
   public Rook(boolean isInPlay, String color, int[] position, boolean firstMove)
   {
      super(isInPlay, color, position, firstMove);
      // TODO Auto-generated constructor stub
   }

   @Override
   public boolean isValidMove(int[] newPosition)
   {
      if(this.getPosition()[0] - newPosition[0] == 0 && this.getPosition()[1] - newPosition[1] == 0)
         return false;
      return (this.getPosition()[0] - newPosition[0] == 0 || this.getPosition()[1] - newPosition[1] == 0);
   }

   @Override
   public Image getImage()
   {
      return (new ImageIcon(this.getClass().getResource(this.getColor() + this.getClass().getName() + ".png"))).getImage();
   }
}
