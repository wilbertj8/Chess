import java.awt.Rectangle;

public class Square extends Rectangle {
   
   private static final long serialVersionUID = 1L;
   private boolean isOccupied, isBlackOccupied, isWhiteOccupied;
   private static final int SQUARE_SIDE = 100;  
   
   public Square(int x, int y, String color) {
      super(x, y, SQUARE_SIDE, SQUARE_SIDE);
   }
   
   public boolean isBlackOccupied() {
      return isBlackOccupied;
   }
   
   public boolean isWhiteOccupied() {
      return isWhiteOccupied;
   }
   
   public void setBlackOccupied(boolean isBlackOccupied) {
      this.isBlackOccupied = isBlackOccupied;
   }

   public void setWhiteOccupied(boolean isWhiteOccupied) {
      this.isWhiteOccupied = isWhiteOccupied;
   }
   
   /**
    * Gets isOccupied
    * @return the isOccupied
    */
   public boolean isOccupied() {
      return isOccupied;
   }
   
   /**
    * Sets isOccupied
    * @param isOccupied the isOccupied to set
    */
   public void setOccupied(boolean isOccupied) {
      this.isOccupied = isOccupied;
   }
}
