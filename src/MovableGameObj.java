import java.awt.Color;

/**
 * MovableGameObj is an abstract class as well as a subclass of GameObj that add the property of 
 * velocity and movable area, i.e. courtWidth and courtHeight.
 * This class will count the general movement of the object, which each object will move from the 
 * current position in the direction and distance of the velocity vector.
 * This class will also account the situation when the game object is out of the current bound.
 * @author garychen
 *
 */
public abstract class MovableGameObj extends GameObj {

    private int vx; // x-value of the velocity of a Movable Game Object
    private int vy; // x-value of the velocity of a Movable Game Object
    private int courtWidth; // the width of the court for clipping
    private int courtHeight; // the height of the court for clipping
    
    /**
     * Constructor of the class. It receives the x- and y- position of the object, the width 
     * and height of the object, the Color, the x- and y- velocity of the object, and both the width
     * and the height of the court.
     * It will call the constructor of the super class first and assign the rest of the variables.
     * Since the court width and court height represents the area the object is allowed to go, we 
     * subtract the value with the object's width and height respectively for convenience from later
     * calculation.
     * It also will handle resetting the width to a different value. In this case, the court width 
     * will have to be recalculated. The setWidth is mainly used by the paddle, which is also the 
     * reason why there isn't setHeight method.
     * @param px
     * @param py
     * @param width
     * @param height
     * @param color
     * @param vx
     * @param vy
     * @param courtWidth
     * @param courtHeight
     */
    public MovableGameObj(int px, int py, int width, int height, Color color, int vx, int vy, 
                           int courtWidth, int courtHeight) {
        super(px, py, width, height, color);
        this.vx = vx;
        this.vy = vy;
        this.courtWidth = courtWidth - width;
        this.courtHeight = courtHeight - height;
    }
    
    /*** GETTERS **********************************************************************************/
    /**
     * return the x-value of the velocity of this Movable Game Object
     * @return return the x-value of the velocity as an int
     */
    public int getVx() {
        return this.vx;
    }
    
    /**
     * return the y-value of the velocity of this Movable Game Object
     * @return return the y-value of the velocity as an int
     */
    public int getVy() {
        return this.vy;
    }
    
    /**
     * return the court width that this Movable Game Object is assign
     * @return return the court width as an int
     */
    public int getCourtWidth() {
        return this.courtWidth;
    }
    
    /**
     * return the court width that this Movable Game Object is assign
     * @return return the court width as an int
     */
    public int getCourtHeight() {
        return this.courtHeight;
    }
    
    /*** SETTERS **********************************************************************************/
    /**
     * set the x-value of the velocity with the given int value
     * @param vx int value of x-value of the velocity
     */
    public void setVx(int vx) {
        this.vx = vx;
    }
    
    /**
     * set the y-value of the velocity with the given int value
     * @param vy int value of y-value of the velocity
     */
    public void setVy(int vy) {
        this.vy = vy;
    }
    
    /**
     * set the width of the object with the given width in int
     * note that when a width is reset, the court width, i.e. the movable area of the object
     *  will needed to be recalculated.
     * @param width int value
     */
    public void setWidth(int width) {
        int oldCourtWidth = this.courtWidth + this.getWidth();
        super.setWidth(width);
        this.courtWidth = oldCourtWidth - this.getWidth();
    }
    
    /**
     * move the object.
     * By moving the object, we set the x- and y- value of the position of the object to be the sum
     * of the current x-, y-value of the position with the x-,  y-value of the velocity respectively
     * It also calls on the abstract method clip in order to catch if this object is out of its
     * movable area, i.e. courtWidth and courtHeight
     */
    public void move() {
        this.setPx(this.getPx() + vx);
        this.setPy(this.getPy() + vy);
        clip();
    }
    
    /**
     * The abstract method clip will account the object's position if the object is out of bounds
     * Since Circle and Paddle have different movement, i.e. paddle will "stick" to a wall if it 
     * reach the wall the the velocity is still moving the paddle to the wall, but Circle will 
     * bounce off the wall.
     */
    public abstract void clip();
    
    public abstract void bounce(Direction d);
    
    public abstract Direction hitObj(GameObj that);
}