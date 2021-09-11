import java.awt.Color;
import java.awt.Graphics;

/**
 * Paddle class extends the MovableGameObj and represents the paddle in the brick breaker game. It 
 * only have movement along the x-axis and is required only to not move out of bound.
 * It also needs to be drawn if specific method is called.
 * @author garychen
 *
 */
public class Paddle extends MovableGameObj {

    /**
     * The constructor of this class will receive all the variable the constructor of the super 
     * class need.
     * 
     * @param px
     * @param py
     * @param width
     * @param height
     * @param color
     * @param vx
     * @param courtWidth
     * @param courtHeight
     */
    public Paddle(int px, int py, int width, 
                  int height, Color color, int vx, 
                  int courtWidth, int courtHeight) {
        // Paddel cannot move up and down
        super(px, py, width, height, color, vx, 0, courtWidth, courtHeight); 
    }
    
    /**
     * Overriding the draw method, which will set the color of the pen and will draw the rectangle 
     * with the given x-, y- value of the position and the width and height of the object.
     */
    @Override
    public void draw(Graphics g) {
        if (g != null) {
            g.setColor(this.getColor());
            g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
        }
    }

    /**
     * Overriding the clip method, which will only take care of the paddle's movement along the 
     * x-axis because the paddle is supposed to move horizontally.
     */
    @Override
    public void clip() {
        int px = this.getPx();
        int courtWidth = this.getCourtWidth();
        if (px > courtWidth) {
            this.setPx(courtWidth);
        } else if (px < 0) {
            this.setPx(0);
        }
    }

    /**
     * The method bounce is used by Circle and not by Paddle
     */
    @Override
    public void bounce(Direction d) {
    }

    /**
     * The method hitObj is used by Circle and not by Paddle
     */
    @Override
    public Direction hitObj(GameObj that) {
        // TODO Auto-generated method stub
        return null;
    }
}