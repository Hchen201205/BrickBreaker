import java.awt.Color;
import java.awt.Graphics;

/**
 * Brick class is one of the three Game Object in this game.
 * It represents the brick that will be broken by the Circle.
 * Brick class, unlike Circle and Paddle, extends GameObj because it's not supposed to move.
 * @author garychen
 *
 */
public class Brick extends GameObj {
    private SuperPower sp; // Superpower represents as a value of enum SuperPower
    
    /**
     * constructor of the class. It takes in the x- and y-value of the position, width, height,
     * Color, and SuperPower that this instance variable has.
     * It first calls the constructor of the super class and then assign the rest of the variables.
     * 
     * @param px
     * @param py
     * @param width
     * @param height
     * @param color
     * @param sp
     */
    public Brick(int px, int py, int width, int height, Color color, SuperPower sp) {
        super(px, py, width, height, color);
        this.sp = sp;
    }
    
    /**
     * return SuperPower of this Game Object as a value of SuperPower enum.
     * @return SuperPower as a value of the enum class.
     */
    public SuperPower getSP() {
        return sp;
    }
    
    /**
     * set
     * @param sp
     */
    /*
    public void setSP(SuperPower sp) {
        this.sp = sp;
    }*/
    
    /**
     * draw method overriding that in the GameObj class.
     * This method will draw the brick as a rectangle with the given color
     */
    @Override
    public void draw(Graphics g) {
        if (g != null) {
            g.setColor(this.getColor());
            g.fillRect(this.getPx() + 1, this.getPy() + 1, 
                       this.getWidth() - 1, this.getHeight() - 1);
            // -1 to make each of the brick distinguishable.
        }
    }
}