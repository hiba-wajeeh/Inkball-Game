package inkball;

import processing.core.PConstants;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;


/**
 * Handles the behaviour of all game objects.
 * 
 * @author hiba wajeeh
 */
public class GameObject {
    private int x;
    private int y;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Draws the level based on the flags and character passed in.
     * 
     * @param app to gain access to the getSprite method in App class.
     * @param ch which is the passed in character to check.
     * @param ballFlag which is the flag indicating whether a ball has been
     * encountered or not.
     * @param holeFlag which is the flag indicating whether a hole has been
     * encountered or not.
     */
    public void draw(App app, char ch, boolean ballFlag, boolean holeFlag) {   
        PImage tile = app.getSprite("tile");
        if (holeFlag==false) {
            if (ballFlag==false) {
                switch (ch) {
                    case 'X':
                        tile = app.getSprite("wall0");
                        break;
                    case '2':
                        tile = app.getSprite("wall2");
                        break;
                    case '1':
                        tile = app.getSprite("wall1");
                        break;
                    case '3':
                        tile = app.getSprite("wall3");
                        break;
                    case '4':
                        tile = app.getSprite("wall4");
                        break;
                    case ' ':
                        tile = app.getSprite("tile");
                        break;
                    case 'S':
                        tile = app.getSprite("entrypoint");
                        break;
                    case 'G':
                        tile = app.getSprite("hole0");
                        break;
                    case 'H':
                        tile = app.getSprite("hole1");
                        break;
                    case 'I':
                        tile = app.getSprite("hole2");
                        break;
                    case 'J':
                        tile = app.getSprite("hole3");
                        break;
                    case 'K':
                        tile = app.getSprite("hole4");
                        break;
                    case 'Y':
                        break;
                    case 'Z':
                        break;
                }
            } 

            if (ch!='Z' && ch!='Y') {
                app.image(tile, x*App.CELLSIZE, y*App.CELLSIZE+App.TOPBAR);
            }

        }  
    }

}