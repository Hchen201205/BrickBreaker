/**
 * Game State represents 5 states that the game can be.
 * @author garychen
 *
 */
public enum GameState {
               // Assume 2 boolean variables: playing (P) and reset (R)
               // such that playing represents whether a game is started,
               // reset represents whether reset is needed in a game.
    STARTING,  // !P && !R
               // Starting happens when the user starts a game
    HALTING,   // Halt and wait for decision from the player. This is out of the four condition to 
               // Account for unexpected situation.
    PLAYING,   //  P && !R
    RESETING,  //  P &&  R
               // Reseting happens when the user loses a life but not yet loses
    ENDING;    // !P &&  R
}