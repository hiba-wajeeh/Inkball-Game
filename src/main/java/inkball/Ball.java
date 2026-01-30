package inkball;

import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.util.Random;

/**
 * This class handles all operations regarding the Ball object
 * in the game.
 * 
 * @author hiba wajeeh
 */
public class Ball {  

    //Declaring the attributes of this class:
    static Random random = new Random();
    private PVector overallVelocity; 
    private char label;
    private int radius = 12;
    private int x;
    private int y;
    private String color;
    private String spriteName;

    /**
     * This is the constructor of the ball class.
     */
    public Ball (int x, int y, char label) {
        this.x = x;
        this.y = y;
        this.overallVelocity = setRandomVelocity();
        this.label = label;
        this.color = setColor();
    }

    /**
     * This checks the label that was passed in the constructor
     * and sets the balls color and sprite name accordingly.
     */
    public String setColor() {
        if (this.label=='A') {
            this.spriteName = "ball0";
            this.color = "grey";
            return this.color;
        } else if (this.label =='C') {
            this.spriteName = "ball1";
            this.color = "orange";
            return this.color;
        } else if (this.label =='D') {
            this.spriteName = "ball2";
            this.color = "blue";
            return this.color;
        } else if (this.label == 'E') {
            this.spriteName = "ball3";
            this.color = "green";
            return this.color;
        } else if (this.label =='F') {
            this.spriteName = "ball4";
            this.color = "yellow";
            return this.color;
        }
        return "";
    }

    /**
     * A getter that returns the name of the ball's sprite.
     * 
     * @return the name of the balls sprite.
     */
    public String getSpriteName() {
        return this.spriteName;
    }

    /**
     * A getter that returns the private attribute 'color'
     * from the ball class.
     * 
     * @return color of the ball.
     */
    public String getColor() {
        return this.color;
    }

    /**
     * A getter method that returns the private attribute 'label'
     * from the ball class.
     * 
     * @return label of the ball.
     */
    public char getLabel() {
        return this.label;
    }

    /**
     * A getter method that returns the private x coordinate attribute
     * of the ball class.
     * 
     * @return the x coordinate of the ball.
     */
    public int getX() {
        return this.x;
    }

    /**
     * A getter method that returns the private y coordinate attribute
     * of the ball class.
     * 
     * @return the y coordinate of the ball.
     */

    public int getY() {
        return this.y;
    }

    /**
     * A getter method that returns the private radius attribute
     * of the ball class.
     * 
     * @return the radius of the ball.
     */
    public int getRadius() {
        return this.radius;
    }

    /**
     * A getter method that returns the private overallVelocity attribute
     * of the ball class.
     * 
     * @return the PVector overall velocity of the ball.
     */
    public PVector getOverallVelocity() {
        return this.overallVelocity;
    }

    /**
     * A setter method that sets the overall velocity attribute 
     * of the ball class by randomly generating a x vector thats either 2 or -2
     * and y vector thats either 2 or -2.
     * 
     * @return PVector value of the overallvelocity.
     */
    private PVector setRandomVelocity() {
        int randomNumber = random.nextInt(4);
        if (randomNumber == 0) {
            return new PVector(2,2);
        } else if (randomNumber == 1) {
            return new PVector(-2,2);
        } else if (randomNumber == 2) {
            return new PVector(-2,-2);
        } else {
            return new PVector(2,-2);
        }
    }

    /**
     * A setter method that sets the new y coordinate for the ball.
     * 
     * @param newY is the new integer y coordinate.
     */
    public void setY(int newY) {
        this.y = newY;
    }

    /**
     * A setter method that sets the new x coordinate for the ball.
     * 
     * @param newX is the new integer x coordinate.
     */
    public void setX(int newX) {
        this.x = newX;
    }

    /**
     * A setter method that sets the new overallVelocity of the ball.
     * 
     * @param newVelocity is the new overallVelocity
     */
    public void setNewVelocity(PVector newOverallVelocity) {
        overallVelocity.x = newOverallVelocity.x;
        overallVelocity.y = newOverallVelocity.y;
    }

    /**
     * A method that draws the ball's sprite image based on its
     * x and y coordinates every frame.
     * 
     * @param app is the app class thats being used in the function
     * to draw the sprite.
     */
    public void draw (App app) {
        PImage tile = app.getSprite("tile");
        boolean balled = false;
        PImage ball = app.getSprite("ball0");
        switch (this.label) {
            case 'A':
                ball= app.getSprite("ball0");
                break;
            case 'C':
                ball = app.getSprite("ball1");
                break;
            case 'D':
                ball = app.getSprite("ball2");
                break;
            case 'E':
                ball = app.getSprite("ball3");
                break;
            case 'F':
                ball = app.getSprite("ball4");
                break;
        }
        app.image(ball, x, y);
    }

    /**
     * Handles the ball's position changing every frame by updating
     * its coordinates, checking if a collision has been made, and checking if
     * its on top of any holes.
     * 
     * @param tiles is the arraylist of all the tile objects in the game.
     * @param holes is the arraylist of all the hole objects in the game.
     */
    public void updatePosition(ArrayList<Tile> tiles, ArrayList<Hole> holes) {
        this.x += overallVelocity.x;
        this.y += overallVelocity.y;
        PVector originalVelocity = overallVelocity;
        checkCollision(tiles);
        //isAttracted(holes);
    }

    /**
     * Handles a situation where the ball is on a hole and it's getting
     * attracted to the center of the hole. It handles the size of the sprite
     * and its attraction accordingly.
     * 
     * @param holes is the arraylist of holes to check if the ball is on top
     * of any of them.
     */
    public void isAttracted(ArrayList<Hole> holes) { 
        for (Hole hole : holes) {
            int holeX = hole.getX();
            int holeY = hole.getY();
            double attraction = 0.5;

            double xDistance = holeX - this.x;
            double yDistance = holeY - this.y;
            double elementOne = xDistance * xDistance;
            double elementTwo = yDistance * yDistance;
            double distance = Math.sqrt(elementOne + elementTwo);
            if (distance < 32 && distance != 0) {
                attraction = attraction / distance;
                overallVelocity.x += xDistance * attraction;
                overallVelocity.y += yDistance * attraction;
            }
        }
    }

    /**
     * Checks if the ball is colliding with any tiles or window sides,
     * it goes through the list of tile objects to see and handle any collisions that
     * might occur.
     * 
     * @param tiles is the arraylist of tiles to parse through.
     */
    public void checkCollision(ArrayList<Tile> tiles) {
        if (x < 0 || x + 2 * radius > App.WIDTH) {
            overallVelocity.x *= -1;
        }
        if (y < 0 || y + 2 * radius > App.HEIGHT) {
            overallVelocity.y *= -1;
        }
        for (Tile tile : tiles) {
            tile.handleCollision(this);
        }
        
    }

}