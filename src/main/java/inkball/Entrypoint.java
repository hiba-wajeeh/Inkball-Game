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
 * Handles all behaviour related to the entrypoint object.
 * 
 * @author hiba wajeeh
 */
public class Entrypoint {

    private int x;
    private int y;

    public Entrypoint (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX () {
        return this.x;
    }

    public int getY () {
        return this.y;
    }
}