import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import java.io.*;
import java.util.*;

public class Mines extends DrawableObject
{
   //Variables
   Random rand = new Random();
   private double t = rand.nextDouble(0,1);
   private boolean explode = false;
   private double dt = .1;
   
   //Mines
   public Mines(float x, float y)
   {
      super(x,y);
   }
   
   //Drawing the mines and color
   public void drawMe(float x, float y, GraphicsContext gc)
   {
      setT();
      gc.setFill(Color.BLACK);
      gc.fillOval(x-5,y-5,11,11);
      gc.setFill(Color.WHITE.interpolate(Color.RED,t));
      gc.fillOval(x-4,y-4,9,9);
   }
   
   //Setting explode
   public void setExplode (boolean explode)
   {
      this.explode = explode;
   }
   
   //Returning explode
   public boolean getExplode()
   {
      return explode;
   }
   
   //Setting the color variable
   public void setT()
   {
      t += dt*.15;
      if(t >= 1)
         dt = -.1;
      if (t <= 0)
         dt = .1;
   }
}