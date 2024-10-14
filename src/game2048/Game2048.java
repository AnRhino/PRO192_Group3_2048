// PeterNguyen

// Creating 2048 Game

// In this Project while creating this Game

// I have implemented
/*
    Java Collection including

    ArrayList
    Array
    LinkedList

    GUI including

    Colours
    Fonts
    Window
    Tiles
*/

/* 
    The Game Scenario

    When the Game starts we have a tile of 2 or 4
    On every step we move, one more tile gets added to the running game interface
    We have to combine the equal numbered tiles before the game ends
    We win the game if we get succeeded in having a tile of 2048 and a GAME WIN popup show
    The game continues as far as we do not get the game over
    screen only if all the spaces get occupied by numbered tiles

*/

/*
    What should I do

    I have to store the numbers so that the user can get the new one
    Parallel to this, I have to create the numbered tile within the limit
    I have to provide the move function
*/

package game2048;

// These are the libraries I have used in this project

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author PeterNguyen
 */

// This is the Game2048 Class which I extend from JPanel to implement the GUI
public class Game2048 extends JPanel {

    // Setting the Background Colour, Font and the Tile Sizes
    private static final Color BG_COLOR = new Color(0x0d5154);
    private static final String FONT_NAME = "Times new Roman";
    private static final int TILE_SIZE = 64;
    private static final int TILES_MARGIN = 13;

    // File saves the highest score
    private static final String HIGHEST_SCORE_FILE = "highest_score.txt";

    /* 
        Declaring the Array of the Tile type where Tile is the static class 
        which has some defined attributes
    */
    private Tile[] myTiles;

    // Declaring the basic variables
    boolean myWin = false;
    boolean myLose = false;
    int myScore = 0;
    int highestScore = readHighestScore();
    private boolean isNewRecord = false;
    private int target = 2048;


    // The Default Constructor
    public Game2048() {

        // Setting the Dimensions of the Windows form
        setPreferredSize(new Dimension(340, 410));
        setFocusable(true);

        // Implementing the KeyBoard Buttons to function accordingly
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {

                // Checking if the pressed Key is "ESC"
                if ((e.getKeyCode() == KeyEvent.VK_ESCAPE) || (e.getKeyCode() == KeyEvent.VK_R ) ){
                    // Game will be restarted
                    confirmReset(null);
                }

                // Checking if the pressed Key is "F1"
                if ((e.getKeyCode() == KeyEvent.VK_F1)){
                    // Help
                    JOptionPane.showMessageDialog(null, "Welcome to Help Center\nPress R or ESC to restart\nPress Arrow keys or WASD to move\n");
                }

                // Checking if the space left or not to declare Game over
                if (!canMove()){
                    myLose = true;
                }

                // Checking if we can press the directive keys or not
                if (!myWin && !myLose){
                    // We can move tiles only if the above conditions get true
                    // Move Up
                    if((e.getKeyCode() == KeyEvent.VK_W) || (e.getKeyCode() == KeyEvent.VK_UP) ){
                        up();
                    }

                    // Move Left
                    if((e.getKeyCode() == KeyEvent.VK_A) || (e.getKeyCode() == KeyEvent.VK_LEFT)){
                        left();
                    }

                    // Move Down
                    if((e.getKeyCode() == KeyEvent.VK_S) || (e.getKeyCode() == KeyEvent.VK_DOWN)){
                        down();
                    }

                    // Move Right
                    if((e.getKeyCode() == KeyEvent.VK_D) || (e.getKeyCode() == KeyEvent.VK_RIGHT)){
                        right();
                    }
                }

                // Checking if the Game Win or not
                if (myWin){
                    confirmContinue(null);
                }

                // Checking if the Game Over or not
                if (!myWin && !canMove()){
                    myLose = true;
                }


                // Built-in applet Method to update the windows form
                repaint();

            }
        });

        // Reset the Game
        resetGame();
    }

    // Method to Confirm Close or Reset
    public void confirmReset(WindowEvent e){
        int choice = JOptionPane.showConfirmDialog(null,"Do you want to RESTART the Game?");

        if( choice == JOptionPane.YES_OPTION){
            target = 2048;
            resetGame();
        }
    }

    // Method to Confirm Continue or Reset
    public void confirmContinue(WindowEvent e){
        int choice = JOptionPane.showConfirmDialog(null,"You win!\nDo you want to try Endless Mode?");

        if( choice == JOptionPane.YES_OPTION){
            target = 1;
            myWin = false;
        }
    }

    // Method to Reset the Game
    public void resetGame() {

        // Member Variables which get reset to their default values
        myScore = 0;
        myWin = false;
        myLose = false;
        isNewRecord = false;

        // Defining the Array
        myTiles = new Tile[4 * 4];
        for (int i = 0; i < myTiles.length; i++){
            myTiles[i] = new Tile();
        }

        // Calling that function twice to have two tiles at the same time
        addTile();
        addTile();
    }

    // Method to align the tiles to the left
    public void left() {
        boolean needAddTile = false;
        for (int i = 0; i < 4; i++){
            // Calling the getLine method and storing the value in the Tile Array
            // It will be called 4 times
            Tile[] line = getLine(i);
            Tile[] merged = mergeLine(moveLine(line));

            // Calling the SetLine Method by passing the index and the  merged Array
            setLine(i, merged);
            if (!needAddTile && !compare(line, merged)){
                needAddTile = true;
            }
        }

        // Add the new tile
        if (needAddTile){
            addTile();
        }
    }

    // Method to align the tiles to the right
    public void right() {
        
        /*
            This method first rotate the tiles exactly opposite  180 Degree
            Then calls the left method
            Then again rotate them to position them
        */

        myTiles = rotate(180);
        left();
        myTiles = rotate(180);
    }

    // Method to align the tiles to the Down
    public void down() {
        
        /*
            This method first rotate the tiles exactly vertical 90 Degree
            Then calls the left method
            Then again rotate them to position them
        */

        myTiles = rotate(90);
        left();
        myTiles = rotate(270);
    }

    // Method to align the tiles to the Up
    public void up() {
        
        /*
            This method first rotate the tiles exactly vertically opposite 270 Degree 
            Then calls the left method
            Then again rotate them to position them
        */

        myTiles = rotate(270);
        left();
        myTiles = rotate(90);
    }

    // Method to get the tile accordingly
    private Tile tileAt(int x, int y) {
        return myTiles[x + y * 4];
    }

    // Method to Add new tile
    private void addTile() {

        // List of Tile type
        // Calling the availableSpace method of the List Type 
        List<Tile> list = availableSpace();
        if (!availableSpace().isEmpty()){
            // If Space is not empty

            // Creating an Index to store the random generated value
            int index = (int) (Math.random() * list.size()) % list.size();

            // Instantiation of the Tile Class and adding the index to the list
            Tile emptyTime = list.get(index);

            // Generating the random number 2 or 4
            emptyTime.value = Math.random() < 0.9 ? 2 : 4;
        }
    }

    // availableSpace method of the List Type
    private List<Tile> availableSpace() {
        // Assigning the total number of tiles to iterate through
        final List<Tile> list = new ArrayList<Tile>(16);

        // Iterating through the tiles to check for the available space
        for (Tile t : myTiles) {
            if (t.isEmpty()) {
                list.add(t);
            }
        }
        // Returning the list which contains the  info if the space is available or not
        return list;
    }

    // Method to check if the available space is full or not
    private boolean isFull() {
        return availableSpace().isEmpty();
    }

    // Method to check if the tile can move
    boolean canMove(){
        // Checking if the PlayGround Interface has space left or not
        if (!isFull()){
            return true;
        }

        // Checking at which index the tiles has value
        for (int x = 0; x < 4; x++){
            for (int y = 0; y < 4; y++){
                // Calling the tileAt function if it has any value or not
                Tile t = tileAt(x, y);
                if ((x < 3 && t.value == tileAt(x + 1, y).value) || ((y < 3) && t.value == tileAt(x, y + 1).value)){
                    return true;
                }
            }
        }

        return false;
    }

    // Method to compare the Tile lines
    private boolean compare(Tile[] line1, Tile[] line2) {
        if (line1 == line2){
            return true;
        } else if (line1.length != line2.length){
            return false;
        }

        // Comparing the indexes of the lines
        for (int i = 0; i < line1.length; i++){
            if (line1[i].value != line2[i].value){
                return false;
            }
        }

        return true;
    }

    // Tile type method to rotate the Tiles so that we can reuse the left method to move the tiles
    private Tile[] rotate(int angle) {

        // Creating the new Array
        Tile[] newTiles = new Tile[4 * 4];
        int offsetX = 3, offsetY = 3;

        if (angle == 90){
            offsetY = 0;
        } else if (angle == 270){
            offsetX = 0;
        }

        // Converting the degrees into radians
        // Getting the angle from the method call
        double rad = Math.toRadians(angle);
        int cos = (int) Math.cos(rad);
        int sin = (int) Math.sin(rad);

        // Rotating the  tiles
        for (int x = 0; x < 4; x++){
            for (int y = 0; y < 4; y++){
                // OffSet value to balance the angle
                int newX = (x * cos) - (y * sin) + offsetX;
                int newY = (x * sin) + (y * cos) + offsetY;

                // Calling the  tileAt method giving the indexes of the loop as the parameters
                // It will be called 16 times
                newTiles[(newX) + (newY) * 4] = tileAt(x, y);
            }
        }

        return newTiles;

    }

    // Method of the Tile type to move the tiles
    private Tile[] moveLine(Tile[] oldLine) {

        // Linked list to store the sequence of the tiles
        LinkedList<Tile> tileLinkedList = new LinkedList<Tile>();
        for (int i = 0; i < 4; i++){
            // Checking if the old Line sequence of tiles is empty
            if (!oldLine[i].isEmpty()){
                // Adding the tile if the old line sequence is empty
                tileLinkedList.addLast(oldLine[i]);
            }
        }

        // Checking if the new linked list has size Zero means no space available
        if (tileLinkedList.isEmpty()) {
            return oldLine;
        } else{
            // Creating a new Array
            Tile[] newLine = new Tile[4];
            ensureSize(tileLinkedList, 4);

            // Placing the tiles out of the linked list
            for (int i = 0; i < 4; i++){
                newLine[i] = tileLinkedList.removeFirst();
            }

            return newLine;
        }
    }

    // Tile type method to merge the lines
    private Tile[] mergeLine(Tile[] oldLine) {

        // Linked list to store them consecutively
        LinkedList<Tile> list = new LinkedList<Tile>();

        // Traversing through the lines and adding to the score
        for (int i = 0; i < 4 && !oldLine[i].isEmpty(); i++){
            int num = oldLine[i].value;
            if (i < 3 && oldLine[i].value == oldLine[i + 1].value){
                num *= 2;
                myScore += num;

                // Declare the Game Win if the target achieved
                if (num == target){
                    myWin = true;
                }

                i++;
            }

            // Adding the number to the list to use it for adding to the score
            list.add(new Tile(num));
        }

        // Checking if the list size is zero or not
        if (list.isEmpty()){
            return oldLine;
        } else{
            ensureSize(list, 4);
            return list.toArray(new Tile[4]);
        }
    }

    // Creating the static method ensureSize taking List and the userdefined size as the parameters
    private static void ensureSize(List<Tile> l, int s) {

        // It will keep on adding to the tiles until the 
        // userdefined size becomes equal to the linked list size
        while (l.size() != s)
        {
            l.add(new Tile());
        }
    }

    // Method of the Tile type
    private Tile[] getLine(int index) {

        // Creating an Array of Tile type
        Tile[] result = new Tile[4];
        for (int i = 0; i < 4; i++){
            // Storing the values in result Array
            result[i] = tileAt(i, index);
        }

        // Returning the result Array where the method called
        return result;
    }

    // Method to copy the array
    private void setLine(int index, Tile[] re) {
        System.arraycopy(re, 0, myTiles, index * 4, 4);
    }

    // Built-in method paint the  windows attributes
    public void paint(Graphics g) {
        super.paint(g);

        // Painting the background Colour
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);

        // Draw the tiles
        for (int y = 0; y < 4; y++){
            for (int x = 0; x < 4; x++){
                drawTile(g, myTiles[x + y * 4], x, y);
            }
        }

        // Display the Score
        g.setFont(new Font(FONT_NAME, Font.BOLD, 20));
        g.setColor(Color.WHITE);
        g.drawString("Score: " + myScore, 200, 20);
        g.drawString("Highest Score: " + highestScore, 5, 20);

        // Display a NEW RECORD message
        if (isNewRecord) {
            g.setColor(Color.ORANGE);
            g.setFont(new Font(FONT_NAME, Font.BOLD, 18));
            g.drawString("NEW RECORD!", 100, 45);
        }

        // Declaring the Win or Lose
        if (myWin || myLose) {

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.BLACK);
            g.setFont(new Font(FONT_NAME, Font.BOLD, 36));

            String resultText = myWin ? "You Win!" : "Game Over!";
            FontMetrics fm = g.getFontMetrics();
            int resultWidth = fm.stringWidth(resultText);
            g.drawString(resultText, (getWidth() - resultWidth) / 2, getHeight() / 2 - 20);

            String finalScoreText = "SCORE: " + myScore;
            int scoreWidth = fm.stringWidth(finalScoreText);
            g.drawString(finalScoreText, (getWidth() - scoreWidth) / 2, getHeight() / 2 + 20);

            g.setFont(new Font(FONT_NAME, Font.BOLD, 24));
            String highestText = "Highest: " + highestScore;
            int highestWidth = fm.stringWidth(highestText);
            g.drawString(highestText, (getWidth() - highestWidth) / 2, getHeight() / 2 + 60);

            g.setFont(new Font(FONT_NAME, Font.ITALIC, 16));
            g.drawString("Press ESC to play again", 80, getHeight() - 50);

            if (isNewRecord) {
                g.setColor(Color.RED);
                g.setFont(new Font(FONT_NAME, Font.BOLD, 28));
                String newRecordText = "New Record!";
                int newRecordWidth = fm.stringWidth(newRecordText);
                g.drawString(newRecordText, (getWidth() - newRecordWidth) / 2, getHeight() - 90);
                saveHighestScore();
            }
        }
    }

    // Method to draw the tiles in the window frame
    private void drawTile(Graphics g2, Tile tile, int x, int y) {

        // Instantiation of Graphics2D Class extended from Graphics Class
        Graphics2D g = ((Graphics2D) g2);

        // Aliasing to update the tiles location
        // setRenderingHint to give it the high quality
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        int value = tile.value;

        // Setting the margins
        int xOffset = offsetCorns(x);
        int yOffset = offsetCorns(y) + 45;

        // Setting the background Colour
        g.setColor(tile.getBackground());

        // Setting the foreground colours of the tiles and their size
        g.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, 14, 14);
        g.setColor(tile.getForeground());
        final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;

        // Setting the font and the font size
        final Font font = new Font(FONT_NAME, Font.BOLD, size);
        g.setFont(font);

        String s = String.valueOf(value);

        // Instantiation of FontMetrics Class which encapsulates information about 
        // the rendering of a particular font on a particular screen.
        final FontMetrics fm = getFontMetrics(font);

        final int w = fm.stringWidth(s);
        final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];

        // Set the position of the number
        if (value != 0){
            g.drawString(s, xOffset + (TILE_SIZE - w) / 2, yOffset + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);
        }

        if (myScore > highestScore) {
            highestScore = myScore;
            isNewRecord = true;
        }

    }

    // Method to Set the margins
    private static int offsetCorns(int arg) {
        return arg * (TILES_MARGIN + TILE_SIZE) + TILES_MARGIN;
    }

    private int readHighestScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGHEST_SCORE_FILE))) {
            String line = reader.readLine();
            return line != null ? Integer.parseInt(line) : 0;
        } catch (IOException e) {
            return 0;
        }
    }

    private void saveHighestScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGHEST_SCORE_FILE))) {
            writer.write(String.valueOf(highestScore));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}   // End of the Game2048 Class