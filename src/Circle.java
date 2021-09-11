import java.awt.Color;
import java.awt.Graphics;

/**
 * Circle is a class extending Movalbe Game Object. It represents the sphere in the brick breaker 
 * game. As a result, it is responsible for the movement itself, drawing of the oval, as well as 
 * bouncing
 * @author garychen
 *
 */
public class Circle extends MovableGameObj {

    /**
     * The constructor that takes in all the variable that the Movable Game Object needs
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
    public Circle(int px, int py, int width, 
                  int height, Color color, int vx, int vy, 
                  int courtWidth, int courtHeight) {
        super(px, py, width, height, color, vx, vy, courtWidth, courtHeight);
    }

    /**
     * bounce is a method that receive a Direction parameter, which represents the direction at 
     * which the game object meets another game object. It will change:
     * x-value to the absolute value of it if this object encounters another object on the left.
     * x-value to the negative of the absolute value of it if this object encounters another object 
     * on the right.
     * y-value to the absolute value of it if this object encounters another object on the top.
     * x-value to the negative of the absolute value of it if this object encounters another object 
     * on the bottom.
     * @param d
     */
    public void bounce(Direction d) {
        if (d != null) {
            switch (d) {
                case UP:
                    this.setVy(Math.abs(this.getVy()));
                    break;
                case DOWN:
                    this.setVy(-Math.abs(this.getVy()));
                    break;
                case LEFT:
                    this.setVx(Math.abs(this.getVx()));
                    break;
                case RIGHT:
                    this.setVx(-Math.abs(this.getVx()));
                    break;
                default:
                    break;
            }
        } 
    }

    /**
     * overriding the draw method of the super class
     * This draw method will set the color to the color of this object and will draw oval with the
     * given x,y value of the position and the width and the height of this object.
     */
    @Override
    public void draw(Graphics g) {
        if (g != null) {
            g.setColor(this.getColor());
            g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
        }
    }

    /**
     * htObj is a method that will take in a Game Object. it will return the direction of that 
     * object with respect to Circle itself if the collide (if isIn is true)
     * If it hit the paddle, the direction is automatically Down because circle, when hitting the 
     * paddle, can only go up.
     * If the direction return is null, Circle did not hit that.
     */
    public Direction hitObj(GameObj that) {
        Direction d = null;
        if (that != null) {
            int cR = this.getWidth() / 2;
            d = isIn(that, getPx(), getPy(), cR);
            if (d != null) {
                if (that.getClass() == Paddle.class) { // Check if that is paddle
                    int pVx = ((Paddle)that).getVx();
                    if (pVx > 0) {
                        setVx(Math.max(getVx() - 1, -5)); // Minor direction change according to the
                                                        // velocity of the paddle. It works ]
                                                        // like a simplify version of friction
                                                        // if the circle hit the paddle and the 
                                                        // paddle is moving left, the "friction" 
                                                        // will make the circle to go to right more.
                    } else if (pVx < 0) {
                        setVx(Math.min(getVx() + 1, 5));
                    }
                    d = Direction.DOWN;
                }
            }
        }
        return d;
    }
    
    /**
     * Assume that the GameObj input in is not null, the method will determine in which direction, 
     * if any, did Circle hits that Game Object. Basically, to see if each point 
     * (left, right, up, down), of the circle is in the area of that GameObj.
     * If none of the point of the Circle is in, the method will return null.
     * @param that
     * @param px
     * @param py
     * @param width
     * @param height
     * @return
     */
    private Direction isIn(GameObj that, int px, int py, int r) {
        int tPx = that.getPx();
        int tPy = that.getPy();
        int tW = that.getWidth();
        int tH = that.getHeight();
        // If the bottom of the circle hits an object
        if ((px + r <= tPx + tW && px + r >= tPx) 
            && (py + 2 * r <= tPy + tH && py + 2 * r >= tPy)) {
            return Direction.DOWN;
        // If the top of the circle hits an object
        } else if ((px + r <= tPx + tW && px + r >= tPx) 
                   && (py <= tPy + tH && py >= tPy)) {
            return Direction.UP;
        // If the right of the circle hits an object
        } else if ((px + 2 * r <= tPx + tW && px + 2 * r >= tPx) 
                   && (py + r <= tPy + tH && py + r >= tPy)) {
            return Direction.RIGHT;
        // If the left of the circle hits an object
        } else if ((px <= tPx + tW && px >= tPx) 
                   && (py + r <= tPy + tH && py + r >= tPy)) {
            return Direction.LEFT;
        }
        //if none of the above is true
        return null;
    }

    /**
     * Overriding the clip method from Movable Game Object
     * It will set both the x- and y- value of the position to be in bound, except when it is at the
     * court Width, at which case the user will lose a life.
     * It will then account whether the circle needs to bounce from the wall.
     * 
     */
    @Override
    public void clip() {
        // TODO Auto-generated method stub
        this.setPx(Math.min(Math.max(this.getPx(), 0), this.getCourtWidth()));
        this.setPy(Math.max(this.getPy(), 0)); // Don't need to clip for the lower bound
        bounce(hitWall());
    }
    
    /**
     * This method will get the x-,y- value of the position of the circle. It will then check 
     * whether the x-value is in the bound and whether the y-value is larger than the ceiling, which
     * is 0. If not, it will return a direction indicating where did the circle hits.
     * @return Direction as a value from the enum class
     */
    public Direction hitWall() {
        int px = this.getPx();
        int py = this.getPy();
        if (px == 0) {
            return Direction.LEFT;
        } else if (px == this.getCourtWidth()) {
            return Direction.RIGHT;
        }
        if (py == 0) {
            return Direction.UP;
        }
        return null;
    }
}
