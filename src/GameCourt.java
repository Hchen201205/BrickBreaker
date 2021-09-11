import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.*;

/**
 * GameCourt is a class implementing JPanel that will be the main court of the game. The user will 
 * mainly use the paddle created in the court to bounce the circle and try to break the brickes to 
 * win the game.
 * It has a Timer to help the continuation of the game
 * It also implementing methods according to the five state of the game so that the game can be run 
 * systematically
 * It also is in charge of a few JLabel that is passed into the constructor as parameter.
 * It will be displaying the current score, life, as well as the ranking that the game is having.
 * The reason why we put ranking in this class is because if the score is strong enough to change
 * the ranking, we want to make sure that the implementation of the new ranking would be easy and
 * stable, which means having score and ranking in the same class.
 * Also, this class will be in charge of the interaction between circle, bricks, paddle and score:
 * 1. if the circle hits a paddle, the circle should bounce back
 * 2. if the circle hits a brick, the brick should be broken and the circle should bounce back, and
 * the score should change.
 * The court also account for the user's key events, such that if the user presses left, the paddle
 * will shift left, and if the user presses right, the paddle will shift right. If the user is not 
 * pressing anything, i.e. key released, the paddle should not move.
 * If the user presses left or right at halting stage, the circle will follow the paddle's movement
 * until the user presses space, in which the circle will be shooted out and the game will start.
 * @author garychen
 *
 */
public class GameCourt extends JPanel {
    
    private GameState gamestate; // The state of the game
    
    private int life; // Number of life the user have in a game as an int
    private int score; // Number of points the user have in a game as an int
    private Map<Integer, List<String>> records; // The records that a game has been played since 
                                                // THE Creation. It has the score as the key and 
                                                // the list of the username, who reaches the score
                                                //as the value
    private List<Integer> ranking; // The ranking that list the integer value of all the score the 
                                   // records account from the highest to the lowest.
    
    private static final long serialVersionUID = 1L; // Default serial ID
    public static final int COURT_WIDTH = 600; // The width of the court as a final static variable
    public static final int COURT_HEIGHT = 600; // The height of the court
    public static final Color COURT_COLOR = Color.black; // THe Color of the background of the 
                                                         // Court
    public static final int SCOREINCREMENT = 10; // The increment of score when a brick is broken
    
    public static final String USERNAME_REGEX = "[a-zA-Z0-9_]{1,10}"; // The regex of a valid
                                                                      // user name
    public static final String RECORD_FILEPATH = "files/BrickBreakerRecords.txt"; // The path of the
                                                                                  // file for 
                                                                                  // storing records
    
    public static final int INTERVAL = 40; // The time interval the Timer has
    public static final int RANDOMSP = 20; // The reciprocal of the probability of getting a 
                                           // super power
    
    // Brick
    public static final int BRICK_LOWER_LIMIT = 300; // The lower limit of what the brick can 
                                                     // create in default
    public static final Color BRICK_COLOR = Color.white; // The color of the regular brick
    public static final Color SPBRICK_COLOR = Color.red; // The color the brick with super power
    public static final int BRICK_WIDTH = 30; // The width of the brick
    public static final int BRICK_HEIGHT = 10; // The height of the brick
    
    public static final int PADDLE_WIDTH = 60; // The width of the paddle
    public static final int PADDLE_LONGERWIDTH = 80; // The width of the longer paddle
    public static final int PADDLE_SHORTERWIDTH = 40; // The height of the shorter paddle
    public static final int PADDLE_HEIGHT = 10; // The height of the paddle
    public static final int PADDLE_X = 600 / 2 - PADDLE_WIDTH; // The initial x-value of the paddle
    public static final int PADDLE_Y = 500; // The y-position of the paddle
    public static final int PADDLE_VX = 6; //The x-value of the paddle in velocity
    public static final Color PADDLE_COLOR = Color.white; // The color of the paddle
    
    public static final int CIRCLERADIUS = 20; // The radius of the circle
    public static final int CIRCLE_VX = 4; // The x-value of the velocity of the circle
    public static final int CIRCLE_VY = -4; // The x-value of the velocity of the circle. It's 
                                            // negative because the circle needs to move up 
                                            // initially so that it doesn't catch user off guard.
    public static final Color CIRCLE_COLOR = Color.white; // The color of the circle.
    
    private List<Brick> brickList; // The list of brick
    private MovableGameObj paddle; // The paddle
    private MovableGameObj circle; // The circle
    private Timer timer; // The Timer
    private JLabel scoreLabel; // The JLabel that display the current score
    private JLabel lifeCount; // The JLabel that display the current life
    private JLabel highScoreLabel; // The JLabel that display the top five highest score and the 
                                   // record keepers
    private JLabel status; // The current status of the game: running..., win, or lose
    private ScoreRecorder sr; // The ScoreRecorder variable that read and write the records
    
    /**
     * This constructor will assign the instance variable.
     * It will also create the Timer, which is responsible for the continuation of the game.
     * It will also create mouse listener that accounts for the situation:
     * 1. When the user is playing, in which only the paddle can be controlled by the user.
     * 2. When the user is not playing, in which both the circle and the paddle will stay together 
     * and will be controlled by the user.
     * It also accounts for the situation when the key is released. Specifically, when the key is
     * released, the paddle, and sometime the circle, should not be moving.
     * 
     * @param scoreLabel
     * @param lifeCount
     * @param highScoreLabel
     * @param status
     */
    public GameCourt(JLabel scoreLabel, 
                     JLabel lifeCount,
                     JLabel highScoreLabel,
                     JLabel status) {
        // Assing JLabels
        this.scoreLabel = scoreLabel;
        this.lifeCount = lifeCount;
        this.highScoreLabel = highScoreLabel;
        this.status = status;
        
        // Creating the new ScoreRecorder and read the records to display
        sr = new ScoreRecorder(RECORD_FILEPATH);
        this.records = sr.getRecords();
        this.ranking = sr.gerRanking();
        displayRanking(); // Display the ranking
        
        // The properties of the game.
        this.gamestate = GameState.STARTING; // The initial state of the game is starting
        this.score = 0; // The initial score is 0.
        this.life = 3; // The initial life is 3
        
        // Declaring the objects used in the game court
        this.brickList = new LinkedList<Brick>(); 
        this.paddle = new Paddle(PADDLE_X, PADDLE_Y, 
                                 PADDLE_WIDTH, PADDLE_HEIGHT, PADDLE_COLOR, 
                                 0, COURT_WIDTH, COURT_HEIGHT);
        this.circle = new Circle(0, 0, CIRCLERADIUS, CIRCLERADIUS, 
                                 CIRCLE_COLOR, CIRCLE_VX, CIRCLE_VY, 
                                 COURT_WIDTH, COURT_HEIGHT);
        // Set the look of the JPanel
        setBorder(BorderFactory.createLineBorder(COURT_COLOR));
        setBackground(Color.black);
        setFocusable(true);
        
        // Set the timer, which will help the continuation of the game.
        timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        
        // Add key listeners such that only left key, right key, and, sometimes, space will be used.
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (gamestate) {
                    // In the case of playing, only the left and right key will work to control 
                    // paddle.
                    case PLAYING:
                        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                            paddle.setVx(-PADDLE_VX);
                        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            paddle.setVx(PADDLE_VX);
                        }
                        break;
                    // In the case of halting, both the left, right, and space key will work to 
                    // control both the circle and the paddle.
                    case HALTING:
                        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                            paddle.setVx(-PADDLE_VX);
                            circle.setVx(-PADDLE_VX); // circle move with the paddle
                        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            paddle.setVx(PADDLE_VX);
                            circle.setVx(PADDLE_VX); // circle move with the paddle
                        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                            gamestate = GameState.PLAYING;
                            // Reset the velocity of the circle
                            // Circle will leave the paddle
                            circle.setVx(CIRCLE_VX);
                            circle.setVy(CIRCLE_VY); 
                        }
                        break;
                    default:
                        break;
                }
            }
            
            // When the key is released, the paddle should be stop
            public void keyReleased(KeyEvent e) {
                paddle.setVx(0);
                if (gamestate == GameState.HALTING) {
                    circle.setVx(0); // the circle will be stop in halting stage
                }
            }
        });
    }
    
    /**
     * a method that will generate a SuperPower when called at random based on the probability given
     * as a static final variable.
     * @return
     */
    private SuperPower rollSP() {
        int ran = (int) (Math.random() * 20);
        SuperPower sp = null;
        if (ran == 1) {
            sp = SuperPower.LONGERPADDLE; // 1/20 chance will Longer Paddle be the super power
        } else if (ran == 2) {
            sp = SuperPower.SHORTERPADDLE;// 1/20 chance will shorter Paddle be the super power
        }
        return sp;
    }
    
    /**
     * restart is a method that will only be called when the current game state is starting.
     * Specifically, it will declare a new bricklist, it will also, depending on the boolean 
     * parameter, generate and store bricks by default.
     * It then will set the initial position, velocity, and width of the paddle and circle
     * It will also set the properties and JLabel in the court to be default.
     * @param defaultBricks
     */
    private void restart(boolean defaultBricks) {
        brickList = new LinkedList<Brick>();
        if (defaultBricks) {
            for (int i = 0; i < BRICK_LOWER_LIMIT / BRICK_HEIGHT; i++) {
                for (int j = 0; j < COURT_WIDTH / BRICK_WIDTH; j++) {
                    SuperPower sp = rollSP(); // Roll a super power
                    Color brickColor;
                    if (sp == null) { // If no superPower is added, the brick color will be default.
                        brickColor = BRICK_COLOR;
                    } else { // If superPower is added, the brick color will be special color.
                        brickColor = SPBRICK_COLOR;
                    }
                    brickList.add(new Brick(j * BRICK_WIDTH, i * BRICK_HEIGHT,
                            BRICK_WIDTH, BRICK_HEIGHT, brickColor, 
                            sp));
                }
            } 
        }
        
        // set paddle to default position, width, height, and velocity
        paddle.setPx(PADDLE_X);
        paddle.setPy(PADDLE_Y);
        paddle.setWidth(PADDLE_WIDTH);
        paddle.setVx(0);
        
        // Reset the properties in the court
        life = 3; 
        score = 0;
        
        // reset the labels in the court
        status.setText("Running...");
        scoreLabel.setText("" + score);
        lifeCount.setText("" + life);     
        reset(); // reset circle and change the game state to halting
        requestFocusInWindow(); // request the focus on window.
    }
    
    /**
     * This method will only be called if the game state is playing.
     * Specifically, the paddle and the circle will move, it will also account of bounce and hitting
     * objects
     */
    private void play() {
        // Move both the paddle and circle
        paddle.move();
        circle.move();
        
        // Accounts for the situation when the circle is below the y-bound, in which the user loses 
        // a life and possibly lose the game if he has no life.
        if (circle.getPy() >= COURT_HEIGHT) {
            life--;
            lifeCount.setText("" + life); // reset the label for number of life
            if (life > 0) {
                gamestate = GameState.RESETING;
            } else {
                gamestate = GameState.ENDING;
            }
        } else {
            Direction d = circle.hitObj(paddle);
            if (d == null) { // if the circle did not hit the paddle
                for (int i = 0; i < brickList.size(); i++) {
                    Brick b = brickList.get(i);
                    d = circle.hitObj(brickList.get(i));
                    if (d != null) { // if the paddle hit the brick
                        SuperPower sp = b.getSP();
                        // Assign super power
                        if (sp == SuperPower.LONGERPADDLE) {
                            paddle.setWidth(PADDLE_LONGERWIDTH);
                            
                        } else if (sp == SuperPower.SHORTERPADDLE) {
                            paddle.setWidth(PADDLE_SHORTERWIDTH);
                        }
                        brickList.remove(i);
                        score += SCOREINCREMENT;
                        scoreLabel.setText("" + score);
                        break;
                    }
                }
            }
            circle.bounce(d);
            if (brickList.isEmpty()) { // Determine if the user wins or not.
                gamestate = GameState.ENDING;
            }
        }
    }
    
    /**
     * This method is only called during the reseting game state or at starting state as the 
     * position of the circle will be reset.
     */
    private void reset() {
        circle.setPx(paddle.getPx() + PADDLE_WIDTH / 2 - CIRCLERADIUS / 2);
        circle.setPy(paddle.getPy() - CIRCLERADIUS);
        circle.setVx(0);// Initially, ball should be on the board
        circle.setVy(0);
        gamestate = GameState.HALTING; // State will change to halting to wait for user's move
    }
    
    /**
     * This method is only called when the game ends.
     * When the game ends, the timer is stop, and the game will ask the user for the username in 
     * order to record the score.
     */
    private void end() {
        timer.stop();
        
        // Determine whether the player wins or not and will print message accordingly
        String message = "Please enter your name to save your record.";
        if (brickList.isEmpty()) {
            status.setText("You Win!");
            message = "Congratulation! You Win!\n" + message;
        } else {
            status.setText("You Lose!");
            message = "You Lose!\n" + message;
        }
        
        // Acquire the username
        String scoreUser = JOptionPane.showInputDialog(message);
        // Reask the username if what the user input is not valid
        while (scoreUser != null && !scoreUser.matches(USERNAME_REGEX)) {
            scoreUser = JOptionPane.showInputDialog("Please try again\n"
                    + "your username should contains only alphabets, numbers, and underscores "
                    + "\nand should be no more than 10 characters");
        }
        
        // If the user click cancel or exit that prevents him from entering the user name, the
        // score will not be counted
        
        if (scoreUser != null) { // If the user successfully enters the username
            if (records.containsKey(score)) { // If the scores already exists in records
                List<String> l = records.get(score);
                l.add(scoreUser);
                Collections.sort(l); // the name list will be sort in alphabetical order
            } else {
                // Add a new name list if there isn't a current score on the records.
                List<String> l = new LinkedList<String>();
                l.add(scoreUser);
                records.put(score, l);
                ranking.add(score);
                Collections.sort(ranking, Collections.reverseOrder()); 
                // the name list will be sort in alphabetical order
            }
            displayRanking();
        }
    }
    
    /**
     * The main method of the court for distributing the operation 
     * according to the current game state.
     */
    private void tick() {
        switch (gamestate) {
            case STARTING:
                restart(true);
                break;
            case PLAYING:
                play();
                break;
            case RESETING:
                reset();
                break;
            case ENDING:
                end();
                break;
            case HALTING: // When halt, both paddle and circle will move according to the vx, vy of 
                          // the two objects so that they can move simultaneously
                paddle.move();
                int px = paddle.getPx();
                if (px == 0 || px + GameCourt.PADDLE_WIDTH == GameCourt.COURT_WIDTH) {
                    circle.setPx(px + GameCourt.PADDLE_WIDTH / 2 - GameCourt.CIRCLERADIUS / 2);
                } else {
                    circle.move();
                }
                break;
            default:
                break;
        }
        repaint();
    }
    
    /**
     * Paint methods for painting each of the components
     */
    @Override
    public void paintComponent(Graphics g) {
        if (g != null) {
            super.paintComponent(g);
            for (Brick b: brickList) {
                b.draw(g);
            }
            paddle.draw(g);
            circle.draw(g);
        }
    }
    
    /**
     * Overriding the JPanel method for adjusting the size of the JPanel
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
    
    /**
     * Stop the timer and thus the game
     */
    public void pause() {
        timer.stop();
    }
    
    /**
     * Start the timer and start the game by either continuing to play or start a new game
     */
    public void start() {
        timer.start();
        requestFocusInWindow();
        if (gamestate == GameState.ENDING) { // Start a new game if needed.
            restart(true);
        }
    }
    
    /**
     * Given a list of bricks, load them into the court to play.
     * @param lb
     */
    public void load(List<Brick> lb) {
        if (lb != null) {
            restart(false);
            brickList = lb;
        }
    }
    
    /**
     * A helper method for displaying the ranking.
     * It will only display the top five scores and all the names of users who get that score
     */
    private void displayRanking() {
        // JLabel's indentation using html.
        String displayRanking = "<html>";
        for (int i = 0; i < Math.min(5, ranking.size()); i++) {
            int displayScore = ranking.get(i);
            displayRanking += 
                String.format("%d.%s %d<br/>", i + 1, records.get(displayScore), displayScore);
        }
        displayRanking += "<html>";
        highScoreLabel.setText(displayRanking);
    }
    
    /**
     * Use the ScoreRecorder to write the records back into the file.
     */
    public void writeRecords() {
        sr.writeRecords(records);
    }
}