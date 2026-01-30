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
 * Handles all behaviour related to the TimedTiles.
 * 
 * @author hiba wajeeh
 */
public class TimedTile {

    private int x;
    private int y;
    private String spriteName;
  

    public TimedTile (int x, int y, String spriteName) {
        this.x = x;
        this.y = y;
        this.spriteName = spriteName;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public String getSpriteName() {
        return this.spriteName;
    }

    /**
     * Draws the timed tile on the game board depending on its spritename.
     * 
     * @param app to gain access to the getSprite method.
     */
    public void draw(App app) {
        PImage tile = app.getSprite(this.spriteName);
        app.image(tile, this.x, this.y);
    }

    /**
     * Changes the sprite name when the framecount condition has been
     * reached.
     */
    public void changeSprite() {
        if (this.spriteName.equals("timed_tile0")) {
            this.spriteName = "timed_tile1";
        } else if (this.spriteName.equals("timed_tile1")) {
            this.spriteName = "timed_tile2";
        } else if (this.spriteName.equals("timed_tile2")) { 
            this.spriteName = "timed_tile3";
        } else if (this.spriteName.equals("timed_tile3")) { 
            this.spriteName = "timed_tile4";
        } else {
            this.spriteName = "tile";
        }
    }

}