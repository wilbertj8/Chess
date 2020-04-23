import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/* Wilbert Joseph
*  Date: May 17, 2018
*  Program Name: ChessRunner.java
*  Description:
*/

public class ChessRunner extends JFrame implements MouseMotionListener
{
   private static final long serialVersionUID = 1L;
   private JPanel panel;
   private Chess game;
   private boolean squareSelected = false;
   private final int WEIRD_OFFSET = 30;
   
   public ChessRunner()
   {
      panel = new JPanel();
      game = new Chess();
      init();
   }
   
   private void init()
   {
      panel.add(game);
      super.addMouseMotionListener(this);
      
      super.addMouseListener(new MouseListener(){

         @Override
         public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub
            
         }

         @Override
         public void mousePressed(MouseEvent e) {
            if(!squareSelected) {
               game.selectPiece(e.getX(), e.getY()-WEIRD_OFFSET);
               squareSelected = true;
            }
         }

         @Override
         public void mouseReleased(MouseEvent e) {
            squareSelected = false;
            game.isValidMove(new int[]{e.getX()/100, (e.getY()-WEIRD_OFFSET)/100});
         }

         @Override
         public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub
            
         }

         @Override
         public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub
            
         }});
      
      add(panel); //adds panel to JFrame
      setResizable(false);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      pack();
      setLocationRelativeTo(null);
      setVisible(true);
   }
   
   //runs game
   public static void main(String[] args)
   {
      new ChessRunner();
   }

   @Override
   public void mouseDragged(MouseEvent e) {
      game.drag(e.getX(), e.getY()-WEIRD_OFFSET);
   }

   @Override
   public void mouseMoved(MouseEvent e) {
      
   }
}
