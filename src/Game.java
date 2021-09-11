import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;
import java.util.List;

/**
 * Game, which is a class implementing Runnable, will be the main class to run
 * It will set up the entire window as well as the game for the user to play
 * @author garychen
 *
 */
public class Game implements Runnable {

    public static final String OPENING_FILE_PATH = "files/BrickBreakerOpening.txt";
    /**
     * run is the methods that creates most of the components needed for the game.
     * It will have a main JFrame with a game court
     * It will also have a controlPanel with three buttons:
     * 1. restart, which will restart the game if the game is ended or will restart the current game
     * if the game is paused
     * 2. pause, which will pause the game the instance the button is pressed
     * 3. customize, which will open another JFrame so that the user can customize his map.
     * In the customize frame, there will be:
     * 1. JPanel, which will be the main workspace for the user to customize the map.
     * 2. controlPanel2, which will be the place to hold the two buttons
     * The two buttons will be as follows:
     * 1. apply, which will close the window and apply the user's map into the game. If apply, the 
     * game will be restarted
     * 2. reset, which will reset the JPanel so that the user can draw again
     * It will also have another panel named statusPanel, which will display statuses like:
     * 1. life, the life of the user in the current game
     * 2. score, the score of the user in the current game
     * 3. records, the highest scores that any user have payed since "The Creation" of the game
     * 4. status, whether the game is running, or if the user has won or lost.
     * It will then wait for the user's instruction to start the game.
     */
    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(OPENING_FILE_PATH));
            String message = "";
            String next = br.readLine();
            while (next != null) {
                message += next + "\n";
                next = br.readLine();
            }
            br.close();
            JOptionPane.showMessageDialog(null, message, "Hello from BrickBreakerGod", 
                    JOptionPane.INFORMATION_MESSAGE);
            runGame();
        } catch (NullPointerException e) {
            System.err.println("Null Error at initial message");
        } catch (IOException e) {
            System.err.println("IOException at initial message");
        }
    }
    
    private void runGame() {
        // Create the frame
        final JFrame frame = new JFrame("BrickBreaker");
        frame.setLocation(600, 600);
        frame.setResizable(false); // Not allow to risize
        
        // Status panel, which will appear on the right of the main frame
        // It will contains multiple labels
        // Basically, there will be two labels on each row of the status_panel, with one display
        // This implementation will avoid reading the current text of the label or setting 
        // additional string variables to store the line.
        // the label of the number and another one is the number of records itself.
        final JPanel statusPanel = new JPanel(); // Initialize the status panel
        statusPanel.setLayout(new GridLayout(4, 2));
        statusPanel.setPreferredSize(new Dimension(200, 600));
        frame.add(statusPanel, BorderLayout.EAST);
        
        // Initialize the line that label the user's life
        final JLabel life = new JLabel("Life: ");
        life.setVerticalAlignment(JLabel.TOP);
        statusPanel.add(life);
        final JLabel lifeCount = new JLabel();
        lifeCount.setVerticalAlignment(JLabel.TOP);
        statusPanel.add(lifeCount);
        
        // Initialize the line that label the user's score
        final JLabel score = new JLabel("Score: ");
        score.setVerticalAlignment(JLabel.TOP);
        statusPanel.add(score);
        final JLabel scoreLabel = new JLabel("3");
        scoreLabel.setVerticalAlignment(JLabel.TOP);
        statusPanel.add(scoreLabel);
        
        // Initialize the line that label the records
        final JLabel highScore = new JLabel("Record: ");
        highScore.setVerticalAlignment(JLabel.TOP);
        statusPanel.add(highScore);
        final JLabel highScoreLabel = new JLabel("0");
        highScoreLabel.setVerticalAlignment(JLabel.TOP);
        statusPanel.add(highScoreLabel);
        
        // Initilaize the status. The line of status does not need to have two lines.
        final JLabel status = new JLabel("Running...");
        status.setVerticalAlignment(JLabel.TOP);
        statusPanel.add(status);
        
        
        // Initialize the game court
        final GameCourt court = new GameCourt(scoreLabel, lifeCount, highScoreLabel, status);
        frame.add(court, BorderLayout.CENTER);
        
        // ControlPanel
        final JPanel controlPanel = new JPanel();
        frame.add(controlPanel, BorderLayout.SOUTH);
        
        // Restart button
        final JButton restart = new JButton("Restart");
        restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.start(); // Start the game
            }
        });
        controlPanel.add(restart);
        
        // Pause button
        final JButton pause = new JButton("Pause");
        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.pause(); // pause the game
            }
        });
        controlPanel.add(pause);
        
        // Customize button, which will call to create a frame that is the workspace of the user
        final JButton customize = new JButton("Customize");
        customize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Pause the current game
                court.pause();
                // Initialize the JPanel
                Customizer customizer = new Customizer();
                
                // Initialize the frame
                JFrame custom = new JFrame("Customize");
                custom.setResizable(false);
                custom.setLocation(600, 600);
                custom.add(customizer);
                
                // Add another JPanel
                JPanel controlPanel2 = new JPanel();
                custom.add(controlPanel2, BorderLayout.SOUTH);
                
                // reset button
                final JButton reset = new JButton("Reset");
                reset.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        customizer.reset(); // reset the customizer
                    }
                });
                controlPanel2.add(reset);
                
                // apply button
                final JButton apply = new JButton("Apply");
                apply.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        List<Brick> brickList = customizer.getMap(false);
                        custom.dispose(); // close the window
                        
                        // Hidden part: if one didn't customize any map, an angry message will 
                        // appear and give the user the worst penalty!!! -> imagine if you have the 
                        // the most tedious brick breaker with full size bricks and no super power.
                        if (brickList.isEmpty()) {
                            String message = "Young man/woman! Stop being lazy!\n"
                                    + "You are penalized!!!";
                            JOptionPane.showMessageDialog(null, message, "Angry Message", 
                                    JOptionPane.INFORMATION_MESSAGE);
                            brickList = customizer.getMap(true);
                        }
                        court.load(brickList); // transfer the list of brick to the court
                                               // and will also restart the game
                        court.start(); // start the game
                        
                    }
                });
                controlPanel2.add(apply);
                
                custom.pack();                
                custom.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                custom.setVisible(true);
            }
        });
        controlPanel.add(customize);
        
        // If the window is closed, the program will automatically write the records
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                court.writeRecords();
            }
        });
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        court.start();
    }

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}