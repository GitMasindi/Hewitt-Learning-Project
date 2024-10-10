/*
 * Author: Mohau Sebiloiane, msebiloane2023@my.fit.edu
 * Course: CSE 1002, Spring 2023
 * Project: Snake Game
*/

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Snake {



   public static class Snakes {
      int xHead;
      int yHead;
      boolean alive = true;
      boolean bodyBite = false;
      int score = 0;
      List<Points> body = new ArrayList<>(); // arraylist of body segments
      List<Points> food = new ArrayList<>();
      final Points up = new Points (0, 1);
      final Points down = new Points (0, -1);
      final Points left = new Points (-1, 0);
      final Points right = new Points (1, 0);
      Snakes(final List<Points> list) {
         this.xHead = 0; 
         this.yHead = 0; 
         this.body = list;
      }
      
      final int borderX = 49;
      final int borderY = 24;
      final int lengthX = 24;
      final double lengthY = 49.5;
      final double thickness = 0.5;

      public void frame () {
         StdDraw.filledRectangle(-borderX, 0, thickness, lengthX);
         StdDraw.filledRectangle(borderX, 0, thickness, lengthX);
         StdDraw.filledRectangle(0, -borderY, lengthY, thickness);
         StdDraw.filledRectangle(0, borderY, lengthY, thickness);
      } // draw borders of game

      public void move () {
         final double newY = body.get(body.size()-1).y;
         final double newX = body.get(body.size()-1).x;
         final double oldY = body.get(body.size()-2).y;
         final double oldX = body.get(body.size()-2).x;
         Points direction = new Points(0, 0);

         for (int i = 0; i < body.size()-1; i++) {
            final Points p = body.get(i);
            if (p.x == newX && p.y == newY) {
               bodyBite = true;
            }
         }

         if ((newX == borderX) || (newX == -borderX) 
               || (newY == borderY) || (newY == -borderY) || (bodyBite)) {
            alive = false;
         }

         if ((newY - oldY) > 0) {
            direction = up; 
         }
         if (newY - oldY < 0) {
            direction = down;
         }   
         if ((newX - oldX) > 0) {
            direction = right; 
         }
         if (newX - oldX < 0) {
            direction = left;
         }

         // determine direction of movement
         final int arrowUp = 38;
         final int arrowDown = 40;
         final int arrowLeft = 37;
         final int arrowRight = 39;
         if (StdDraw.isKeyPressed(arrowUp)) {
            direction = up;
         }
         if (StdDraw.isKeyPressed(arrowDown)) {
            direction = down;
         }
         if (StdDraw.isKeyPressed(arrowLeft)) {
            direction = left;
         }
         if (StdDraw.isKeyPressed(arrowRight)) {
            direction = right;
         }
         // Essentially all movement in any direction is treeated as a change in direction 

         change(direction);
      }

      public void newfood () {
         final Random rand = new Random();
         final int x  = rand.nextInt(-borderX+1, borderX);
         final int y  = rand.nextInt(-borderY+1, borderY);
         // find random coordinates


         for (final Points p : body) {
            if (p.x == x && p.y == y) newfood();
            
         } // make sure they arent part of the snake
         food.clear();
         food.add(new Points(x, y));
         // add to food arraylist
      }

      public void eat () {
         if (body.get(body.size()-1).equals(food.get(0))) {
            body.add(0, body.get(0));
            score++;
            newfood();
         }   
         // if food consumed, create new food and add to snake body
      }

      public void change (final Points movement) {
         final double newY = body.get(body.size()-1).y + movement.y;
         final double newX = body.get(body.size()-1).x + movement.x;
         final double oldY = body.get(body.size()-2).y;
         final double oldX = body.get(body.size()-2).x;
         final Points dp = new Points(newX, newY);
         
         // determine new position of head after movement 

         if (!(newY == oldY && newX == oldX)) {
            body.add(dp);
            body.remove(0);
         } // ensure snake is not reversed
         final int time = 50;
         StdDraw.pause(time); // pause
         StdDraw.clear(); // clear canvas
         frame(); // redraw border
         eat(); // check if food is consumed
         StdDraw.circle(food.get(0).x, food.get(0).y, 0.5); // draw food
         for (int i = body.size()-1; i >= 1; i--) {
            final Points p = body.get(i);
            StdDraw.filledSquare(p.x, p.y, 0.5);
         } // redraw every body segment of snake from head-to-tail
         StdDraw.text(0, 0, "SCORE: " + String.valueOf(score));
         StdDraw.show(); // show
         
      }


      public void gameOver () {
         StdDraw.setPenColor(StdDraw.WHITE);
         StdDraw.clear(StdDraw.BLACK);
         StdDraw.text(0, 0, "Game Over");
         StdDraw.text(0, -5, "Final Score: " + String.valueOf(score));
         StdDraw.show();
      } // GAME OVER screen


   }



   public static void main (final String[] args) {
      final int canvasX = 1000;
      final int canvasY = 500;
      final int scaleX = 50;
      final int scaleY = 25;

      StdDraw.setCanvasSize(canvasX, canvasY);
      StdDraw.setXscale(-scaleX, scaleX); // init canvas
      StdDraw.setYscale(-scaleY, scaleY); // init canvas
      StdDraw.setPenColor(StdDraw.BLACK); // init canvas
      StdDraw.enableDoubleBuffering();

      
      final List<Points> snakeBody = new ArrayList<>();
      final Points origin = new Points(0, 0);
      snakeBody.add(new Points(-1, 0)); // include initial points for right movement;
      snakeBody.add(origin); // include initial points for right movement;
      final Snakes game = new Snakes(snakeBody); // new instance of snake
      game.newfood(); // create new food
      while (game.alive) {
         game.move(); // play game
      }
      game.gameOver(); // show GAME OVER Screen

   }

   public record Points(double x, double y) {
   }
}
