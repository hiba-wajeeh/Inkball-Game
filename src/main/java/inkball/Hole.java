package inkball;

import processing.core.PConstants;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;


/**
 * Handles all behaviour related to the Hole object.
 * 
 * @author hiba wajeeh
 */
public class Hole {

    private int x;
    private int y;
    private String color = "grey";

    public Hole (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public String getColor() {
        return this.color;
    }

    /**
     * Based on the character passed in from the file,
     * the colour of the hole is set.
     * 
     * @param passedInCh is the character passed in.
     */
    public void setColour(char passedInCh) {
        if (passedInCh == '0') {
            this.color = "grey";
        } else if (passedInCh == '1') {
            this.color = "orange";
        } else if (passedInCh == '2') {
            this.color = "blue";
        } else if (passedInCh == '3') {
            this.color = "green";
        } else if (passedInCh == '4') {
            this.color = "yellow";
        }
    }   

    /**
     * Checks to see if a ball is on top of the hole's center
     * and if it is, handles it accordingly.
     * 
     * @param balls is the list of balls to check to see if any 
     * are on the thole.
     * @param app is the app's class to pass into handleBallRemoval method.
     */
    public void isOnHole(ArrayList<Ball> balls, App app) {
        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);
            double ballCenterX = ball.getX() + ball.getRadius();
            double ballCenterY = ball.getY() + ball.getRadius();
            double distance = Math.sqrt(Math.pow(ballCenterX - this.x, 2) + Math.pow(ballCenterY - this.y, 2));
            if (distance < ball.getRadius()) {
                balls.remove(i);
                this.handleBallRemoval(ball, app);
                i--;
            }
        }
    }

    /**
     * Handles the situation where a ball is falling
     * into the hole.
     * 
     * @param ball is the ball thats falling into the hole.
     * @param app is the app object to gain access to the configballs arraylist.
     */
    public void handleBallRemoval(Ball ball, App app) {
        if ((ball.getColor().equals(this.color)) || this.color.equals("grey") || ball.getColor().equals("grey")) {
            app.setIncreaseScore(ball.getColor());       
        } else {
            app.configBalls.add(ball.getLabel());
            app.setDecreaseScore(ball.getColor());
        }
    }
    
}