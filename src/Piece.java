import java.awt.Image;

public abstract class Piece {
   private String color;
   private boolean isInPlay;
   private int[] position;
   private int x;
   private int y;
   private boolean firstMove;

   public Piece(boolean isInPlay, String color, int[] position, boolean firstMove) {
      this.isInPlay = isInPlay;
      this.color = color;
      this.position = position;
      this.firstMove = firstMove;
      x = position[0] * 100;
      y = position[1] * 100;
   }

   public String getColor() {
      return color;
   }

   public boolean isInPlay() {
      return isInPlay;
   }

   public int[] getPosition() {
      return position;
   }

   public int getX() {
      return x;
   }

   public int getY() {
      return y;
   }

   public boolean isFirstMove() {
      return firstMove;
   }

   public void setColor(String color) {
      this.color = color;
   }

   public void setInPlay(boolean isInPlay) {
      this.isInPlay = isInPlay;
   }

   public void setPosition(int[] position) {
      this.position = position;
   }

   public void setX(int x) {
      this.x = x;
   }

   public void setY(int y) {
      this.y = y;
   }

   public void setFirstMove(boolean firstMove) {
      this.firstMove = firstMove;
   }

   public abstract boolean isValidMove(int[] newPosition);

   public abstract Image getImage();
}
