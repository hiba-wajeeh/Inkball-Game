package inkball;

import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;

/**
 * This class is for handling all actions relating to a tile
 * object in the game.
 * 
 * @author hiba wajeeh
 */
public class Tile { 

    //Intialising all attributes
    private int width=32; 
    private int height=32; 
    private char label;
    private int x;
    private int y;
    private int collisionBuffer = 0;
    
    /**
     * The constructor for a tile object.
     */
    public Tile(int x, int y, char label) {
        this.x = x;
        this.y = y;
        this.label = label;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    /**
     * Handles a collision between a tile and a ball and performs the necessary actions.
     * 
     * @param ball is the ball that is potentially colliding with the tile.
     */
    public void handleCollision(Ball ball) {
        if (this.label == 'X' || this.label == '1' || this.label == '2' || this.label == '3' || this.label == '4') {

            PVector overallVelocity = ball.getOverallVelocity();
            PVector newVelocity = overallVelocity;

            if (collisionBuffer % newVelocity.y == 0) {
                int tileLeft = x;
                int tileRight = x + width;
                int tileTop = y;
                int tileBottom = y + height;
                int ballCenterX = ball.getX() + ball.getRadius();
                int ballCenterY = ball.getY() + ball.getRadius();

                if (ballCenterY + ball.getRadius() > tileTop && ballCenterY - ball.getRadius() < tileTop &&
                    ballCenterX > tileLeft && ballCenterX < tileRight) {
                    int newY = tileTop - 2 * ball.getRadius();
                    ball.setY(newY);
                    newVelocity.y *= -1;
                    ball.setNewVelocity(newVelocity);
                    collisionBuffer = 1;
                } else if (ballCenterY - ball.getRadius() < tileBottom && ballCenterY + ball.getRadius() > tileBottom &&
                       ballCenterX > tileLeft && ballCenterX < tileRight) {
                    int newY = tileBottom;
                    ball.setY(newY);
                    newVelocity.y *= -1;
                    ball.setNewVelocity(newVelocity);
                    collisionBuffer = 1;
                } else if (ballCenterX + ball.getRadius() > tileLeft && ballCenterX - ball.getRadius() < tileLeft &&
                       ballCenterY > tileTop && ballCenterY < tileBottom) {
                    int newX = tileLeft - 2 * ball.getRadius();
                    ball.setX(newX);
                    newVelocity.x *= -1;
                    ball.setNewVelocity(newVelocity);
                    collisionBuffer = 1;
                } else if (ballCenterX - ball.getRadius() < tileRight && ballCenterX + ball.getRadius() > tileRight &&
                       ballCenterY > tileTop && ballCenterY < tileBottom) {
                    int newX = tileRight;
                    ball.setX(newX);
                    newVelocity.x *= -1;
                    ball.setNewVelocity(newVelocity);
                    collisionBuffer = 1;
                }
            } else {
                collisionBuffer++;
            }
        }
    }

}