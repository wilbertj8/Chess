/* Wilbert Joseph
*  Date: May 15, 2018
*  Program Name: Player.java
*  Description:
*/

public class Player
{
   private String name;
   private String color;
   
   public Player(String name, String color)
   {
      this.name = name;
      this.color = color;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getColor()
   {
      return color;
   }

   public void setColor(String color)
   {
      this.color = color;
   }
}
