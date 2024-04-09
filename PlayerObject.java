import java.util.*;
import java.text.*;
import java.io.*;
import java.lang.*;
import javafx.application.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import java.net.*;
import javafx.geometry.*;
import javafx.scene.shape.*;

public class PlayerObject extends DrawableObject
{
   //Private members
   private float speedX, speedY, x, y;
   private boolean death = false;
   private int gridX, gridY, lastGridX, lastGridY;
   
   //Making the player
   public PlayerObject(float x, float y)
   {
      super(x,y);
   }
   
   //Making the player act
   public void act(boolean left, boolean right, boolean up, boolean down)
   {
      //Moving the player
      move();
      //Making it go in the directions with accel of .1
      if(left)
         speedUp(-.1f,0);
      if(right)
         speedUp(.1f,0);
      if(up)
         speedUp(0,-.1f);
      if(down)
         speedUp(0,.1f);
      //If either up and down or left and right aren't being pushed, slow it down by .025
      if(!up && !down)
         slowDown(false, true, .025f);
      if(!left && !right)
         slowDown(true, false, .025f);
   }
   
   //Speeding the player up
   public void speedUp(float xAccel, float yAccel)
   {
      //Adding accel to the X speed if the speed is less than 5
      if(Math.abs(speedX) < 5)
         speedX += xAccel;
      //If the speed is greater than or equal to 5 in any direction
      else
         //Setting it either to -5
         if(speedX < 0)
            speedX = -5;
         //Or 5
         else
            speedX = 5;
      //Adding accel to the Y speed if the speed is less than 5
      if(Math.abs(speedY) < 5)
         speedY += yAccel;
      else
         //Setting it either to -5
         if(speedY < 0)
            speedY = -5;
         //Or 5
         else
            speedY = 5;
   }
   
   //Slowing the player down
   public void slowDown(boolean slowX, boolean slowY, float decel)
   {
      //If X is slowing down
      if(slowX)
      {
         //If the speed is negative, add the decel
         if(speedX < -.25)
            speedX += decel;
         //If the speed is positive, substract the decel
         else if(speedX > .25)
            speedX -= decel;
         //If it's between .25 & -.25, make it 0
         else
            speedX = 0;
      }
      //If X is slowing down
      if(slowY)
      {
         //If the speed is negative, add the decel
         if(speedY < -.25)
            speedY += decel;
         //If the speed is positive, substract the decel
         else if(speedY > .25)
            speedY -= decel;
         //If it's between .25 & -.25, make it 0
         else
            speedY = 0;
      }
   }
   
   //Moving the player
   public void move()
   {
      //Setting the X and Y positions based on the speed and the current position
      setX(getX() + speedX);
      setY(getY() + speedY);
      
      //Getting the grid position based on where X & Y is
      gridX = (int)(getX()/100);
      gridY = (int)(getY()/100);
   }
   
   //Seeing if the player has died
   public void dies(ArrayList<Mines> mines)
   {
      //Going through all of the mines
      for(int i = 0; i < mines.size(); i++)
      {
         //Getting the distance from the player to the mine
         double dist = distance(mines.get(i));
         //If the distance is greater than 20
         //(the distance from the center of the player to the center of the mines)
         if(dist <= 20)
         {
            //Making death true and making the mine explode be true
            death = true;
            mines.get(i).setExplode(true);
         }
      }
   }
   
   //Returning whether it's dead or alive
   public boolean isDead()
   {
      return death;
   }
   
   //Seeing if the players grid has changed
   public boolean gridChange()
   {
      //If the player isn't in the grid they were previously in
      if(lastGridX != gridX || lastGridY != gridY)
      {
         //Making the lastGrid equal to the grid they are in
         lastGridX = gridX;
         lastGridY = gridY;
         return true;
      }
      else
      {
         //Otherwise, return false
         lastGridX = gridX;
         lastGridY = gridY;
         return false;  
      }
   }
   
   //Drawing the circle only if it is alive
   public void drawMe(float x, float y, GraphicsContext gc)
   {
      //Drawing the player if it is alive
      if(!death)
      {
         gc.setFill(Color.BLACK);
         gc.fillOval(x-14,y-14,27,27);
         gc.setFill(Color.GRAY);
         gc.fillOval(x-13,y-13,25,25);
         gc.setFill(Color.BLACK);
         gc.fillOval(x-4,y-4,7,7);
      }
   }
}