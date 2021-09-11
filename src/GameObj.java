import java.awt.Color;
import java.awt.Graphics;

/**
 * GameObj serves as both an abstract class and also the superclass of two subclasses, 
 * Brick and MoveableGameObj. This class represents what a general game object in this game would be
 * Specifically, a game object needs to have a x-y position, a width and height value, and a color.
 * It does not need to be specified in shape as the subclasses will do.
 * @author garychen
 *
 */
public abstract class GameObj {
    private int px; // x-value of the position of Game Object
    private int py; // y-value of the position of Game Object
    
    private int width; // width of the Game Object
    private int height; // height of the Game Object
    private Color color; // color of the Game Object
    
    /**
     * The Constructor of a GameObj takes in and record all five parameters as 
     * private instance variable
     * If the color passed in is null, set it to the default color, which is white
     * 
     * @param px
     * @param py
     * @param width
     * @param height
     * @param color
     */
    public GameObj(int px, int py, int width, int height, Color color) {
        this.px = px;
        this.py = py;
        this.width = width;
        this.height = height;
        if (color != null) {
            this.color = color;
        } else {
            this.color = Color.white;
        }
    }
    
    /*** GETTERS **********************************************************************************/
    /**
     * return the x-value of the position of this Game Object
     * @return the x-value of the position as an int
     */
    public int getPx() {
        return this.px;
    }

    /**
     * return the y-value of the position of this Game Object
     * @return the y-value of the position as an int
     */
    public int getPy() {
        return this.py;
    }
    
    /**
     * return the width of this Game Object
     * @return the width as an int
     */
    public int getWidth() {
        return this.width;
    }
    
    /**
     * return the height of this Game Object
     * @return the height as an int
     */
    public int getHeight() {
        return this.height;
    }
    
    /**
     * return the Color of this Game Object
     * @return the Color as a color in the Color class.
     */
    public Color getColor() {
        return color;
    }
    
    /*** SETTERS **********************************************************************************/
    
    /**
     * set the x-value of the position of this Game Object
     * @param px as an int
     */
    public void setPx(int px) {
        this.px = px;
    }
    
    /**
     * set the y-value of the position of this Game Object
     * @param py as an int
     */
    public void setPy(int py) {
        this.py = py;
    }
    
    /**
     * set the width of this Game Object
     * @param width as an int
     */
    public void setWidth(int width) {
        this.width = width;
    }
    
    /**
     * set the Color of this Game Object
     * @param color as a value of the Color class.
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * Abstract method, given a Graphics parameter g, represents the repaint of each Game Object
     * @param g
     */
    public abstract void draw(Graphics g);
}