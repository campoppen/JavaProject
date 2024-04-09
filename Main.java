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


public class Main extends Application
{
   //Variables
   FlowPane fp;
   PlayerObject thePlayer = new PlayerObject(300,300);
   DrawableObject home = new PlayerObject(300,300);
   Canvas theCanvas = new Canvas(600,600);
   float highScore;
   ArrayList<Mines> minesList = new ArrayList<Mines>();
   Random rand = new Random();
   GraphicsContext gc;

   public void start(Stage stage)
   {
      //Adding pieces
      fp = new FlowPane();
      fp.getChildren().add(theCanvas);
      gc = theCanvas.getGraphicsContext2D();
      drawBackground(300,300,gc);
      
      //Setting keys
      theCanvas.setOnKeyPressed(new KeyListenerDown());
      theCanvas.setOnKeyReleased(new KeyListenerUp());
      
      //Start animantion
      AnimationHandler ta = new AnimationHandler();
      ta.start();
      
      //Scene
      Scene scene = new Scene(fp, 600, 600);
      stage.setScene(scene);
      stage.setTitle("Project");
      stage.show();
      
      //Focus
      theCanvas.requestFocus();
   }
   
   Image background = new Image("stars.png");
   Image overlay = new Image("starsoverlay.png");
   Random backgroundRand = new Random();
   //this piece of code doesn't need to be modified
   public void drawBackground(float playerx, float playery, GraphicsContext gc)
   {
	  //re-scale player position to make the background move slower. 
      playerx*=.1;
      playery*=.1;
   
	//figuring out the tile's position.
      float x = (playerx) / 400;
      float y = (playery) / 400;
      
      int xi = (int) x;
      int yi = (int) y;
      
	  //draw a certain amount of the tiled images
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(background,-playerx+i*400,-playery+j*400);
         }
      }
      
	  //below repeats with an overlay image
      playerx*=2f;
      playery*=2f;
   
      x = (playerx) / 400;
      y = (playery) / 400;
      
      xi = (int) x;
      yi = (int) y;
      
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(overlay,-playerx+i*400,-playery+j*400);
         }
      }
   }
   
   //Booleans for key pressed
   boolean left, right, up, down;
   
   //If the key was pressed
   public class KeyListenerDown implements EventHandler<KeyEvent>  
   {
      public void handle(KeyEvent event) 
      {
         //If A was pressed
         if (event.getCode() == KeyCode.A) 
            left = true;
            
         //If S is pressed
         if (event.getCode() == KeyCode.S)  
            down = true;
            
         //If D is pressed
         if (event.getCode() == KeyCode.D)  
            right = true;
            
         //If W is pressed
         if (event.getCode() == KeyCode.W)
            up = true;
      }
   }

   //If the key was released
   public class KeyListenerUp implements EventHandler<KeyEvent>  
   {
      public void handle(KeyEvent event) 
      {
         //If A was released
         if (event.getCode() == KeyCode.A) 
            left = false;
            
         //If S is released
         if (event.getCode() == KeyCode.S)  
            down = false;
            
         //If D is released
         if (event.getCode() == KeyCode.D)  
            right = false;
            
         //If W is released
         if (event.getCode() == KeyCode.W)
            up = false;
      }
   }
   
   //For each grid
   public void newGrid()
   {
      //If the player entered a new grid
      if(thePlayer.gridChange())
      {
         //The max and min grid spots
         int gridMinY = ((int)thePlayer.getY()/100)-5;
         int gridMaxY = ((int)thePlayer.getY()/100)+4;
         int gridMinX = ((int)thePlayer.getX()/100)-5;
         int gridMaxX = ((int)thePlayer.getX()/100)+4;
         
         //Going through the grid sports
         for(int i = gridMinX; i <= gridMaxX; i++)
         {
            for(int j = gridMinY; j <= gridMaxY; j++)
            {
               //If any number is equal to the max or min, spawn a mine
               if(i == gridMinX || i == gridMaxX || j == gridMinY || j == gridMaxY)
                  spawnMines(i,j);
            }
         }
         
         //Removes the mines from the list if they're too far away
         for(int i = 0; i < minesList.size(); i++)
         {
            if(thePlayer.distance(minesList.get(i)) > 800)
               minesList.remove(i);
         }
      }
   }
   
   //Spawning mines based on the grids given
   public void spawnMines(int gridX, int gridY)
   {
      //Getting the distance
      int distanceFromOrigin = (int)Math.sqrt(Math.pow((double)gridX*100-300,2)+Math.pow((double)gridY*100-300,2));
      int n = distanceFromOrigin/1000;
      
      //Based on the distance, making the amount added
      for(int i = 0; i < n; i++)
      {
         //Random number between 1-10
         int randNum = rand.nextInt(1,11);
         
         //If the number is less than or equal to 3
         if(randNum <= 3)
         {
            //Making a random mine somewhere in the grids more than 3 away
            int positionX = gridX*100 + (int)(rand.nextFloat()*100);
            int positionY = gridY*100 + (int)(rand.nextFloat()*100);
            minesList.add(new Mines(positionX, positionY));
         }
      }
      
   }
   
   //Animation
   public class AnimationHandler extends AnimationTimer
   {
      public void handle(long currentTimeInNanoSeconds) 
      {
         //Clearing
         gc.clearRect(0,0,600,600);
         
         //Making grids, drawing the background, and drawing the player
         newGrid();
         drawBackground(thePlayer.getX(),thePlayer.getY(),gc);
         thePlayer.draw(300,300,gc,true);
         
         //Drawing all the mines
         for(int i = 0; i < minesList.size(); i++)
         {
            if(!minesList.get(i).getExplode())
               minesList.get(i).draw(thePlayer.getX(), thePlayer.getY(), gc, false);
         }
         
         //Stopping the action if the player dies
         if(!thePlayer.isDead()) 
               thePlayer.act(left,right,up,down);
         
         //Seeing if the player has died
         thePlayer.dies(minesList);
         
         //Setting high score
         setHighScore();
      }
   }
   
   //Setting high score
   public void setHighScore()
   {
      //Trying the file
      try
      {
         File file = new File("ProjectHighScore.txt");
         Scanner scan = new Scanner(file);
         highScore = scan.nextFloat();
         //Seeing if the player's score is higher than the higher than the high score
         if(thePlayer.distance(home) >= highScore)
         {
            //Printing the high score onto the file
            PrintWriter pw = new PrintWriter(new FileOutputStream("ProjectHighScore.txt", false));
            highScore = (float)thePlayer.distance(home);
            pw.print(highScore);
            pw.close();
         }
         //Putting the high score's onto a text
         gc.setFill(Color.WHITE);
         gc.fillText("Score: " + (int)thePlayer.distance(home), 20, 20);
         gc.fillText("High Score: " + (int)highScore, 20, 40);
      }
      //Exception catch
      catch (Exception e)
      {
         System.out.println("File not found");
      }
   }
   
   //Launching
   public static void main(String[] args)
   {
      launch(args);
   }
}