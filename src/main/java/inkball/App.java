package inkball;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.core.PConstants;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import java.io.*;
import java.util.*;

/**
 * This is the main App class for the program which extends PApplet.
 * It represents the game's main interface.
 * 
 * @author hiba wajeeh
 */

public class App extends PApplet {

    // Initialising constants for the size of each cell in the game interface
    public static final int CELLSIZE = 32; 
    public static final int CELLHEIGHT = 32;
    public static final int CELLAVG = 32; 
    public static final int TOPBAR = 64; 
    public static int WIDTH = 576; 
    public static int HEIGHT = 640; 
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE; 
    public static final int BOARD_HEIGHT = 20; 
    public static final int INITIAL_PARACHUTES = 1;
    public static final int FPS = 30; 


    // Initialising attributes to use in the main program
    public String configPath; 
    public static Scanner file = null; 
    public static Random random = new Random(); 
    private GameObject[][] board; 
    private HashMap<String, PImage> sprites = new HashMap<>(); 
    private int level = 1;
    private ArrayList<String> lines; 
    private double score = 0; 
    private int time; 
    private boolean gameEnd; 
    private boolean timeEnd; 
    private boolean drawing; 
    private boolean drawn; 
    List<List<PVector>> allLines; 
    List<PVector> currentLine; 
    private ArrayList<Ball> balls; 
    public List<Character> configBalls; 
    private ArrayList<Entrypoint> entryPoints; 
    private ArrayList<Hole> holes; 
    private ArrayList<Tile> tiles; 
    private ArrayList<TimedTile> timedTiles; 
    private ArrayList<Character> holeColors; 
    private float startX, startY; 
    private float endX, endY; 
    private boolean pauseFlag; 
    private double spawnInterval; 
    private double originalSpawnInterval; 
    private int count; 
    private boolean spawnedFlag; 
    private double increaseScore;
    private double decreaseScore;
    private ArrayList<Integer> ballIncreasePoints;
    private ArrayList<Integer> ballDecreasePoints;
    private int offset;

    public App() {
        this.configPath = "config.json"; //Set the name of the JSON file to the configPath variable
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
	@Override
    public void setup() {
        frameRate(FPS);
        JSONObject config; 
		config = loadJSONObject(configPath); 

        // Giving the attributes initial setup values: 
        this.time = 0;
        this.gameEnd = false;
        this.timeEnd = false;
        this.pauseFlag = false;
        this.drawing = false;
        this.drawn = false;
        this.count = 0;
        this.spawnedFlag = false;
        this.offset = 40;
        ballIncreasePoints = new ArrayList<>();
        ballDecreasePoints = new ArrayList<>();
        holeColors = new ArrayList<>();
        timedTiles = new ArrayList<>();
        tiles = new ArrayList<>();
        holes = new ArrayList<>();
        entryPoints = new ArrayList<>();
        configBalls = new ArrayList<>();
        balls = new ArrayList<>();
        lines = new ArrayList<>();
        currentLine = new ArrayList<>();
        allLines = new ArrayList<>();
        

        String[] sprites = new String[] {
                "ball0",
                "ball1",
                "ball2",
                "ball3",
                "ball4",
                "entrypoint",
                "hole0",
                "hole1",
                "hole2",
                "hole3",
                "hole4",
                "tile",
                "wall0",
                "wall1",
                "wall2",
                "wall3",
                "wall4",
                "timed_tile0",
                "timed_tile1",
                "timed_tile2",
                "timed_tile3",
                "timed_tile4"
        };

        for (int i = 0; i < sprites.length; i++) {
            this.getSprite(sprites[i]);
        }

        this.board = new GameObject[(HEIGHT-TOPBAR)/CELLSIZE][WIDTH/CELLSIZE];

        for (int i = 0; i < this.board.length; i++) {
            for (int i2 = 0; i2 < this.board[i].length; i2++) {
                this.board[i][i2] = new GameObject(i2, i); // Initialising each cell has a GameObject
            }
        }

        readingJSONFile(config); // Parse the config file
        objectsInLevel(); // Create all the objects in the level as per the config file
    }

    /**
     * Returns the increase score modifier that was
     * extracted from the configuration JSON file.
     * 
     * @return increaseScore which is the increase score modifier.
     */
    public double getScoreIncrease() {
        return this.increaseScore;
    }

    /**
     * Returns the decrease score modifier that was
     * extracted from the configuration JSON file.
     * 
     * @return decreaseScore which is the decrease
     * score modifier.
     */
    public double getScoreDecrease() {
        return this.decreaseScore;
    }

    /**
     * Increases the score according to the color of the ball.
     * 
     * @param colorName is the color of the ball.
     */
    public void setIncreaseScore(String colorName) {
        if (colorName.equals("grey")) {
            double toAdd = ballIncreasePoints.get(0) * this.increaseScore;
            this.score = this.score + toAdd;
        } else if (colorName.equals("orange")) {
            double toAdd = ballIncreasePoints.get(1) * this.increaseScore;
            this.score = this.score + toAdd;
        } else if (colorName.equals("blue")) {
            double toAdd = ballIncreasePoints.get(2) * this.increaseScore;
            this.score = this.score + toAdd;
        } else if (colorName.equals("green")) {
            double toAdd = ballIncreasePoints.get(3) * this.increaseScore;
            this.score = this.score + toAdd;
        } else {
            double toAdd = ballIncreasePoints.get(4) * this.increaseScore;
            this.score = this.score + toAdd;
        }
    }

    /**
     * Decreases the score according to the color of the ball.
     * 
     * @param colorName is the color of the ball.
     */
    public void setDecreaseScore(String colorName) {
        if (colorName.equals("grey")) {
            double toSubtract = ballDecreasePoints.get(0) * this.decreaseScore;
            this.score = this.score - toSubtract;
        } else if (colorName.equals("orange")) {
            double toSubtract = ballDecreasePoints.get(1) * this.decreaseScore;
            this.score = this.score - toSubtract;
        } else if (colorName.equals("blue")) {
            double toSubtract = ballDecreasePoints.get(2) * this.decreaseScore;
            this.score = this.score - toSubtract;
        } else if (colorName.equals("green")) {
            double toSubtract = ballDecreasePoints.get(3) * this.decreaseScore;
            this.score = this.score - toSubtract;
        } else {
            double toSubtract = ballDecreasePoints.get(4) * this.decreaseScore;
            this.score = this.score - toSubtract;
        }
    }


    /**
     * Change the level variable.
     */
    public void levelChange(){
        if (this.level==1){
            this.level=2;
        } else if (this.level==2){
            this.level=3;
        }
    }

    /**
     * Get an image/sprite from the resources.
     */
    public PImage getSprite(String s){
        PImage result = sprites.get(s);
        if (result == null) {
            result = loadImage(this.getClass().getResource(s+".png").getPath().replace("%20", " "));
            sprites.put(s, result);
        }
        return result;
    }

    /**
    * Reads the JSON configuration file, extracts values for the current level, 
    * and assigns them to the corresponding variables such as layout, time, and balls.
    *
    * @param config is the JSON object containing the data for all levels.
    */
    public void readingJSONFile(JSONObject config) {
        String fileName = ""; 
        JSONArray levels = config.getJSONArray("levels");
        if (this.level==1) { 
            fileName= levels.getJSONObject(0).getString("layout"); 
            this.time = levels.getJSONObject(0).getInt("time"); 
            JSONArray addedBalls = levels.getJSONObject(0).getJSONArray("balls");
            this.spawnInterval = levels.getJSONObject(0).getInt("spawn_interval");
            this.increaseScore = levels.getJSONObject(0).getDouble("score_increase_from_hole_capture_modifier");
            this.decreaseScore = levels.getJSONObject(0).getDouble("score_decrease_from_wrong_hole_modifier");
            JSONObject increasingScores = config.getJSONObject("score_increase_from_hole_capture");
            JSONObject decreasingScores = config.getJSONObject("score_decrease_from_wrong_hole");
            this.originalSpawnInterval = this.spawnInterval;
            makeScoreList(increasingScores, decreasingScores);
            editBalls(addedBalls);
        } else if (this.level==2) {
            fileName = levels.getJSONObject(1).getString("layout");
            this.time = levels.getJSONObject(1).getInt("time");
            JSONArray addedBalls = levels.getJSONObject(1).getJSONArray("balls");
            this.spawnInterval = levels.getJSONObject(1).getInt("spawn_interval");
            this.increaseScore = levels.getJSONObject(1).getDouble("score_increase_from_hole_capture_modifier");
            this.decreaseScore = levels.getJSONObject(1).getDouble("score_decrease_from_wrong_hole_modifier");
            JSONObject increasingScores = config.getJSONObject("score_increase_from_hole_capture");
            JSONObject decreasingScores = config.getJSONObject("score_decrease_from_wrong_hole");
            this.originalSpawnInterval = this.spawnInterval;
            editBalls(addedBalls);
            makeScoreList(increasingScores, decreasingScores);
        } else if (this.level==3) {
            fileName = levels.getJSONObject(2).getString("layout");
            this.time = levels.getJSONObject(2).getInt("time");
            JSONArray addedBalls = levels.getJSONObject(2).getJSONArray("balls");
            this.spawnInterval = levels.getJSONObject(2).getInt("spawn_interval");
            this.increaseScore = levels.getJSONObject(2).getDouble("score_increase_from_hole_capture_modifier");
            this.decreaseScore = levels.getJSONObject(1).getDouble("score_decrease_from_wrong_hole_modifier");
            JSONObject increasingScores = config.getJSONObject("score_increase_from_hole_capture");
            JSONObject decreasingScores = config.getJSONObject("score_decrease_from_wrong_hole");
            this.originalSpawnInterval = this.spawnInterval;
            editBalls(addedBalls);
            makeScoreList(increasingScores, decreasingScores);
        }
        readFile(fileName); 
    }

    /**
     * Goes through the scores for each ball and adds them into a list to keep track
     * of how many points to add or deduct when each ball is captured.
     * 
     * @param increasingScores is the JSON Object containing the scores to add depending
     * on each ball.
     * @param decreasingScores is the JSON Object containing the scores to add depending
     * on each ball.
     */
    public void makeScoreList(JSONObject increasingScores, JSONObject decreasingScores) { 
        Set<String> increasingKeysSet = increasingScores.keys(); 
        for (String key : increasingKeysSet) { 
            int score = increasingScores.getInt(key); 
            ballIncreasePoints.add(score);
        }
        Set<String> decreasingKeysSet = decreasingScores.keys();
        for (String key : decreasingKeysSet) {
            int score = decreasingScores.getInt(key); 
            ballDecreasePoints.add(score); 
        }
    }

    /**
     * Goes through the JSONArray of all the balls to add to the queue of configBalls,
     * and assigns them a special character value.
     * 
     * @param addedBalls is the JSONArray extracted from the JSON object, which contains all
     * of the balls that need to be spawned at random entrypoints everytime a spawn interval
     * reaches 0.
     */
    public void editBalls(JSONArray addedBalls) {
        for (int i=0; i<addedBalls.size(); i++) {
            String element = addedBalls.getString(i);
            if (element.equals("blue")) { 
                configBalls.add('D'); 
            } else if (element.equals("orange")) {
                configBalls.add('C'); 
            } else if (element.equals("grey")) {
                configBalls.add('A'); 
            } else if (element.equals("green")) {
                configBalls.add('E'); 
            } else {
                configBalls.add('F'); 
            }
        }
    }

    /**
     * Opens a scanner file and goes through the file, adding each
     * line inside of the file to an Arraylist of lines.
     * 
     * @param file_name is the name of the file that we are
     * creating a Scanner file for.
     */
    public void readFile(String file_name) {
        try {
            this.file = new Scanner(new File(file_name));
            if (this.file != null) {
                while (this.file.hasNextLine()) {
                    String line = this.file.nextLine();
                    lines.add(line);
                }
            }
            editLines(lines);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Goes through the Arraylist of lines to check to see if 
     * a H character is present (if a hole is present) and changes the corresponding
     * tiles to special characters in order to reserve a 2x2 square space
     * for the hole.
     * 
     * @param lines is the Arraylist of type String containing all the lines inside
     * of the level file that is being read.
     */
    public void editLines(ArrayList<String> lines) { 
        for (int i = 0; i < (lines.size()-1); i++) {
            String currentLine = lines.get(i);
            char[] chars = currentLine.toCharArray(); 
            for (int j = 0; j < chars.length; j++) {
                if (chars[j] == 'H') {
                    String newLine = lines.get(i + 1);
                    char[] newChars = newLine.toCharArray(); 
                    if (j < newChars.length - 1) {
                        newChars[j] = 'Z';
                        newChars[j + 1] = 'Y';
                        lines.set(i + 1, new String(newChars));
                    }
                } 
            }
        }
    }

    /**
     * Receive key pressed signal from the keyboard
     * and perform an action accordingly.
     * 
     * @param event which is the key event that the 
     * method is responding to.
     */
	@Override
    public void keyPressed(KeyEvent event){
        if (event.getKeyCode() == 82) {
            allLines.clear();
            balls.clear();
            this.score = 0;
            this.setup();
        }
        if (event.getKeyCode() == 32) {
            this.pauseFlag = !this.pauseFlag;
        }
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){
        
    }

    /**
     * Receive the mouse pressed signal from the mouse and
     * perform an action accordingly.
     * 
     * @param e is the mouse event that the method is responding to.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (this.gameEnd==true) {
            return;
        }
        boolean pressed = e.isControlDown();
        boolean rightClicked = (e.getButton() == PConstants.LEFT && pressed) && this.drawn;
        if (e.getButton() == PConstants.RIGHT || rightClicked) {
            for (int i = allLines.size() - 1; i >= 0; i--) {
                List<PVector> currentLine = allLines.get(i);
                for (int j = 0; j < currentLine.size() - 1; j++) {
                    PVector start = currentLine.get(j);
                    PVector end = currentLine.get(j + 1);
                    if (onLine(new PVector(e.getX(), e.getY()), start, end)) {
                        allLines.remove(i); 
                        return; 
                    }
                }
            }
        } 

        if (e.getButton() == PConstants.LEFT && !pressed) {
            currentLine = new ArrayList<>(); 
            currentLine.add(new PVector(e.getX(), e.getY())); //The coordinates at which the line is starting to be drawn are stored.
            this.drawing = true; 
        }
    }

    /**
     * Checks to see if the mouse's vector is on the line and if it is,
     * return true, and if not then returns false.
     * 
     * @param point is the PVector at which the mouse currently is.
     * @param start is the staring PVector of the line we are checking.
     * @param end is the ending PVector of the line we are checking.
     * @return true if the point is on the line or return false otherwise
     */
    public boolean onLine(PVector point, PVector start, PVector end) { 
        float crossProduct = (point.y - start.y) * (end.x - start.x) - (point.x - start.x) * (end.y - start.y);
        if (crossProduct > 0.01) { //If the result is close to  0 then that means that the points are colinear and point is on the line.
            return false;
        }
        return true;
    }

    /**
     * Handles the mouse being dragged event and performs the
     * necessary action.
     * 
     * @param e is the mouse event that is being handled.
     */
	@Override 
    public void mouseDragged(MouseEvent e) { 
        if (this.gameEnd == true) {
            return;
        } 
        if (e.getButton() == PConstants.LEFT && this.drawing==true) {
            currentLine.add(new PVector(e.getX(), e.getY())); 
        }
    }

    /**
     * Handles the mouse being released event and
     * performs the necessary action.
     * 
     * @param e is the mouse event that is being handled.
     */
    @Override
    public void mouseReleased(MouseEvent e) { 
        if (this.gameEnd==true){
            return;
        }
        if (e.getButton() == PConstants.LEFT && this.drawing==true) {
            allLines.add(currentLine); 
            this.drawing = false; 
            this.drawn = true; 
        }
    }

    /**
     * This function displays the message required when
     * the time is up for an event.
     */
    public void displayTimeEndMessage() {
        textSize(25);
        fill(0);
        text("=== TIME’S UP ===", WIDTH-360, App.TOPBAR-28);
    }

    /**
     * This function displays the message required when the
     * game has been paused.
     */
    public void displayPauseMessage() {
        textSize(25);
        fill(0);
        text("*** PAUSED ***", WIDTH-360, App.TOPBAR-28);
    }

    /**
     * This function draws the bar on the top left which
     * contains the queue of balls to be spawned that was extracted from the 
     * JSON object file.
     * 
     * @param x is the float x coordinate where the function is going to draw
     * the black bar
     * @param y is the float y coordinate where the function is going to draw
     * the black bar
     * @param width is the float width of the black bar to draw
     * @param height is the float height of the black bar to draw
     */
    public void drawBar(float x, float y, float width, float height) { 
        fill(0); 
        rect(x, y, width, height); 
        int maxBalls = 5;
        int spacing = (int) (width / maxBalls);
        int finalOffset = App.CELLSIZE;  

        if (this.spawnedFlag == true) {
            offset += 1;  
            if (offset >= finalOffset) {
                if (!configBalls.isEmpty()) {
                    configBalls.remove(0); 
                }
                offset = 0;
                this.spawnedFlag = false;
            }
        }

        for (int i = 0; i < this.configBalls.size() && i < maxBalls; i++) {
            char element = configBalls.get(i);
            PImage ball = this.getSprite("ball0");
            switch (element) {
                case 'A':
                    ball = this.getSprite("ball0");
                    break;
                case 'C':
                    ball = this.getSprite("ball1");
                    break;
                case 'D':
                    ball = this.getSprite("ball2");
                    break;
                case 'E':
                    ball = this.getSprite("ball3");
                    break;
                case 'F':
                    ball = this.getSprite("ball4");
                    break;
            }
            float ballX = x + (i * App.CELLSIZE);
            if (this.spawnedFlag) {
                if (i == 0) {
                    continue;
                } else {
                    ballX -= offset;
                }
            }
            float ballY = y + height / 2 - ball.height / 2;
            this.image(ball, ballX, ballY);
        }
    }  

    /**
     * Once the spawn interval timer has reached 0, this function will
     * spawn a ball from the existing ball queue (extracted from JSON object file).
     * 
     * @param count is the index at which the ball exists in the configballs arraylist.
     */
    public void spawnBall(int count) {
        int randomIndex = random.nextInt(entryPoints.size()); 
        Entrypoint randomElement = entryPoints.get(randomIndex);
        int x_coordinate = randomElement.getX();
        int y_coordinate = randomElement.getY();
        if (count < configBalls.size()) {
            char ball_label = configBalls.get(count);
            Ball ball = new Ball(x_coordinate*App.CELLSIZE, y_coordinate*App.CELLSIZE+App.TOPBAR, ball_label);
            balls.add(ball);
            this.spawnedFlag = true;
        }
    }

    /**
     * Goes through the lines read from the level file and makes objects
     * accordingly as per the characters in the line by calling the makeObjects
     * method.
     */
    public void objectsInLevel() {
        if (lines.size() != 0) {
            for (int i = 0; i < this.board.length; i++) {
                String currentLine = lines.get(i);
                makeObjects(currentLine, i);
            }
        }
    }

    /**
     * Goes through the current line from the file that is being read, 
     * and make objects at the appropriate coordinates.
     * @param currentLine is the string line that is being parsed through.
     * @param i is the index at which we are are making the object.
     */
    public void makeObjects(String currentLine, int i) { 
        boolean ballFlag = false;
        boolean holeFlag = false;
        int fileX = 0;

        for (int i2 = 0; i2 < this.board[i].length; i2++) {
            char ch = currentLine.charAt(fileX);
            if (ch=='B' && !ballFlag) {
                if (fileX<this.board[i].length) {
                    int tempX = fileX+1;
                    char tempCh = currentLine.charAt(tempX);
                    char passedInCh = ' ';
                    if (tempCh=='0') {
                        passedInCh = 'A';
                    } else if (tempCh=='1') {
                        passedInCh = 'C';
                    } else if (tempCh=='2') {
                        passedInCh = 'D';
                    } else if (tempCh=='3') {
                        passedInCh = 'E';
                    } else {
                        passedInCh = 'F';
                    }
                    ballFlag = true;
                    Ball ball = new Ball(i2*App.CELLSIZE, i*App.CELLSIZE+App.TOPBAR, passedInCh);
                    balls.add(ball);
                }
            } else if (ch=='H') { 
                holeFlag = true;
            } else if (ch=='Y') {
                Hole hole = new Hole(i2*App.CELLSIZE, i*App.CELLSIZE+App.TOPBAR);
                holes.add(hole);
            } else if (ch=='T') {
                TimedTile tile = new TimedTile(i2*App.CELLSIZE, i*App.CELLSIZE+App.TOPBAR, "timed_tile0");
                timedTiles.add(tile);
            } else {
                if (ch=='S') {
                    Entrypoint entrypoint = new Entrypoint(i2, i);
                    entryPoints.add(entrypoint);
                } else if (holeFlag == true) {
                    holeColors.add(ch);
                } else if (!holeFlag && !ballFlag){
                    Tile tile = new Tile(i2*App.CELLSIZE, i*App.CELLSIZE+App.TOPBAR, ch);
                    tiles.add(tile);
                }
                ballFlag = false;
                holeFlag = false;
            }
            fileX++;
        }
        setHoleColor();
    }

    /**
     * Sets the colors of the holes.
     */
    public void setHoleColor() {
        if (this.holeColors.size()==this.holes.size()) {
            for (int i=0; i<holeColors.size(); i++) {
                char ch = holeColors.get(i);
                Hole hole = holes.get(i);
                hole.setColour(ch);
            }
        }
    }


    /**
     * Goes through the list of timedTiles and sees which of the tiles needs 
     * to behave like a regular tile at its current state. If its not a blank tile,
     * it acts like a regular tile and gets added to the arraylist of tiles,
     * if it is then it gets removed from the tiles list.
     * 
     * @param timedTiles is the Arraylist of timedtiles that the function is 
     * iterating through.
     * @param tiles is the Arraylist of tiles that the function is iterating
     * through.
     */
    public void isTile(ArrayList<TimedTile> timedTiles, ArrayList<Tile> tiles) {
        for (TimedTile timedTile : timedTiles) {
            int timedTileX = timedTile.getX();
            int timedTileY = timedTile.getY();
            if ("tile".equals(timedTile.getSpriteName())) {
                for (int i = 0; i < tiles.size(); i++) {
                    Tile tile = tiles.get(i);
                    if (tile.getX() == timedTileX && tile.getY() == timedTileY) {
                        tiles.remove(i);
                        break;
                    }
                }
            } else {
                boolean alreadyTile = false;
                for (Tile tile : tiles) {
                    if (tile.getX() == timedTileX && tile.getY() == timedTileY) {
                        alreadyTile = true;
                        break;
                    }
                }
                if (!alreadyTile) {
                    Tile tile = new Tile(timedTileX, timedTileY, 'X');
                    tiles.add(tile);
                }
            }
        }
    }

    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {
        background(200, 200, 200);

        if (this.time==0) {
            this.timeEnd = true;
            this.gameEnd = true;
        }

        //----------------------------------
        //display Board for current level:
        //----------------------------------
        //TODO

        drawBar(WIDTH-560, App.TOPBAR-50, 150, 30);
        
        textSize(25);
        fill(0);
        text(String.format("%.1f", spawnInterval), WIDTH - 400, TOPBAR - 20);

        if (lines.size() != 0) {
            for (int i = 0; i < this.board.length; i++) {
                String current_line = lines.get(i);
                drawLine(current_line, i);
            }
        }
        
        stroke(0);
        strokeWeight(10);

        for (TimedTile tile : timedTiles) {
            tile.draw(this);
            isTile(this.timedTiles, this.tiles);
        }

        for (Ball ball : balls) {
            ball.draw(this);
        }

        for (Hole hole: holes) {
            hole.isOnHole(this.balls, this);
        }


        for (List<PVector> line : allLines) { 
            for (int i = 0; i < line.size() - 1; i++) {
                PVector start = line.get(i);
                PVector end = line.get(i + 1);
                line(start.x, start.y, end.x, end.y); 
            }
        }

        if (this.gameEnd == false || this.pauseFlag == false) { 
            if (drawing && currentLine.size() > 1) {
                for (int i = 0; i < currentLine.size() - 1; i++) {
                    PVector start = currentLine.get(i);
                    PVector end = currentLine.get(i + 1);
                    line(start.x, start.y, end.x, end.y);
                }
            }

        }


        //----------------------------------
        //display score
        //----------------------------------
        //TODO

        textSize(25);
        fill(0);
        text("Time: "+this.time, WIDTH-150, App.TOPBAR-7);
        textSize(25);
        fill(0);
        text("Score: "+String.format("%.0f", this.score), WIDTH-150, App.TOPBAR-32);
        
		//----------------------------------
        //----------------------------------
		//display game end message

        if (this.timeEnd) {
            displayTimeEndMessage(); 
            return; 
        } else {
            if (this.pauseFlag==false) {
                if (frameCount % App.FPS == 0) {
                    this.time -= 1;
                }
                if (frameCount % 300 == 0) {
                    for (TimedTile tile : timedTiles) {
                        tile.changeSprite();
                    }
                }
                if (this.spawnInterval > 0) {
                    if (frameCount % 3 == 0) {
                        this.spawnInterval -= 0.1;
                        this.spawnInterval = Math.max(0, this.spawnInterval);
                    } 
                } else if (this.spawnInterval==0) {
                    spawnBall(this.count);
                    this.spawnInterval = this.originalSpawnInterval;
                } else {
                    this.spawnInterval = this.originalSpawnInterval;
                }
                for (Ball ball : balls) {
                    ball.updatePosition(this.tiles, this.holes);
                }
            } else {
                displayPauseMessage();
            }
        }

        if (balls.size() == 0 && configBalls.size() == 0) {
            levelChange();
            handleScore();
            if (this.time == 0) {
                setup();
            }
        }

    }

    /**
     * Adds the remaining time to the score count at a 
     * rate of 1 unit per 0.067 seconds.
     */
    public void handleScore() {
        int timeLeft = this.time;
        int counts = (int)(timeLeft / 0.067); 
        this.score += counts; 
        this.time -= counts * 0.067; 
        if (this.time < 0) {
            this.time = 0; 
        }
    }

    /**
     * Goes through the currentLine from the file and draws each
     * sprite accordingly.
     * 
     * @param current_line is the line from the file that is parsed.
     * @param i is the coordinate at which we are drawing objects.
     */
    public void drawLine(String currentLine, int i) {
        boolean ballFlag = false;
        boolean holeFlag = false;
        int fileX = 0;
        for (int i2 = 0; i2 < this.board[i].length; i2++) {
            char ch = currentLine.charAt(fileX);
            if (ch=='B' && !ballFlag) {
                if (fileX<this.board[i].length) {
                    int tempX = fileX+1;
                    char tempCh = currentLine.charAt(tempX);
                    char passedInCh = ' ';
                    if (tempCh=='0') {
                        passedInCh = 'A';
                    } else if (tempCh=='1') {
                        passedInCh = 'C';
                    } else if (tempCh=='2') {
                        passedInCh = 'D';
                    } else if (tempCh=='3') {
                        passedInCh = 'E';
                    } else {
                        passedInCh = 'F';
                    }
                    ballFlag = true;
                    this.board[i][i2].draw(this, ' ', ballFlag, holeFlag);
                }
            } else if (ch=='H' && !holeFlag) {
                if (fileX<this.board[i].length) {
                    int tempX = fileX+1;
                    char tempCh = currentLine.charAt(tempX);
                    char passedInCh = ' ';
                    if (tempCh=='0') {
                        passedInCh = 'G';
                    } else if (tempCh=='1') {
                        passedInCh = 'H';
                    } else if (tempCh=='2') {
                        passedInCh = 'I';
                    } else if (tempCh=='3') {
                        passedInCh = 'J';
                    } else {
                        passedInCh = 'K';
                    } 
                    this.board[i][i2].draw(this, passedInCh, ballFlag, holeFlag);
                    holeFlag = true;
                } 
            } else {
                this.board[i][i2].draw(this, ch, ballFlag, holeFlag);
                ballFlag = false;
                holeFlag = false;
            }
            fileX++;
        }
    }

    public static void main(String[] args) {
        PApplet.main("inkball.App");
    }

}