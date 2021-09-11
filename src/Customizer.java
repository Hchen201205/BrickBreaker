import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.LinkedList;


import javax.swing.JPanel;

/**
 * Customizer, a class extending JPanel, will be the class for the user to create their own brick 
 * breaker map through clicking at the panel. When the user click the panel, the brick where he 
 * click will switch color such that the user can see what he just click.
 * Also, the customizer will be in charge of resetting the panel when method is called
 * It will also have a method to provide the bricks that the user selected as a list form.
 * @author garychen
 *
 */
public class Customizer extends JPanel {
    
    private static final int BOARD_WIDTH = GameCourt.COURT_WIDTH; // The width of the customizer 
                                                                  //board
    private static final int BOARD_HEIGHT = GameCourt.COURT_HEIGHT / 3 * 2; // The length of the
                                                                            // customizer board
    private static final long serialVersionUID = 1L; // default serial number
    // We use 2D-array instead of Collections like what we used in Game Court because we need to
    // account for the user's mouse x and y position, and it will be more convenient to do some
    // arithmetic operation that will be explained laer to locate which brick to change quicker
    // Also, we create bricks at the very first stage, so it will be more time consuming to look
    // over every single brick to find which brick to implement.
    private Brick[][] blockArray;// the 2D-array of the brick
    /**
     * The constructor that will create and fill the 2D-array with bricks
     * It will also set up the JPanel for the user to use.
     */
    public Customizer() {
        // Create the 2D-array with the exact number of bricks on each row and column
        blockArray = new Brick[BOARD_HEIGHT / GameCourt.BRICK_HEIGHT]
                              [BOARD_WIDTH / GameCourt.BRICK_WIDTH];
        setBackground(Color.black);
        setFocusable(true); // Change the focus to this panel
        
        // Hear the mouse event the user has
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // Get the x and y position of the mouse
                int mouseX = e.getX();
                int mouseY = e.getY();
                // Locate the bricks by using the current mouse to divide either the height or width
                // of the brick
                Brick b = 
                    blockArray[mouseY / GameCourt.BRICK_HEIGHT][mouseX / GameCourt.BRICK_WIDTH];
                if (b.getColor().equals(Color.black)) {
                    b.setColor(Color.white);
                } else {
                    b.setColor(Color.black);
                }
                repaint();
            }
        });
        reset();
    }
    
    /**
     * Reset method will fill the 2D-array with the bricks that are initially black and are each in 
     * a specific position according to their position in the 2D-array.
     */
    public void reset() {
        for (int i = 0; i < blockArray.length; i++) {
            for (int j = 0; j < blockArray[i].length; j++) {
                blockArray[i][j] = new Brick(j * GameCourt.BRICK_WIDTH, i * GameCourt.BRICK_HEIGHT,
                        GameCourt.BRICK_WIDTH, GameCourt.BRICK_HEIGHT, Color.BLACK, null);
            }
        }
        requestFocusInWindow(); // Request the focus
        repaint();
    }
    
    /**
     * This method will set the size of this JPanel
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

    /**
     * This method will paint all the bricks
     * For reference and for the convenience of the user, this method also paint lines that draw the
     * approximate position of each brick.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw the bricks
        for (int i = 0; i < blockArray.length; i++) {
            for (int j = 0; j < blockArray[i].length; j++) {
                blockArray[i][j].draw(g);
            }
        }
        
        // Draw the horizontal line
        g.setColor(Color.white);
        for (int i = 0; i < blockArray.length; i++) {
            int y = i * GameCourt.BRICK_HEIGHT;
            g.drawLine(0,y, BOARD_WIDTH, y);
        }
        
        // Draw the vertical line.
        for (int i = 0; i < blockArray[0].length; i++) {
            int x = i * GameCourt.BRICK_WIDTH;
            g.drawLine(x, 0, x, BOARD_HEIGHT);
        }
    }
    
    /**
     * return the map the user creates as a list
     * We uses a list for this method because the Game Court uses List, and the essential purpose of
     * this method is to update the bricks to the main class, which will pass it to the court.
     * @return
     */
    public List<Brick> getMap(boolean penalized) {
        List<Brick> brickList = new LinkedList<Brick>();
        if (penalized) { // Return a list with all the blocks in it
            for (Brick[] ba: blockArray) {
                for (Brick b: ba) {
                    b.setColor(Color.white);
                    brickList.add(b);
                }
            }
        } else {
            // Put the bricks into the list if the bricks have a different color than the background
            // which will compose the map the user creates
            for (int i = 0; i < blockArray.length; i++) {
                for (int j = 0; j < blockArray[i].length; j++) {
                    Brick b = blockArray[i][j];
                    if (b.getColor() != GameCourt.COURT_COLOR) {
                        brickList.add(b);
                    }
                }
            }
        }
        return brickList;
    }
}