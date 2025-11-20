package simon_swift;

import swiftbot.SwiftBotAPI;

public class SimonSwiftGame {

    private SwiftBotAPI api;

    private LEDController ledController;
    private ButtonInputHandler buttonInputHandler;
    private SequenceManager sequenceManager;
    private CelebrationController celebrationController;

    private int score;
    private int round;
    private GameState gameState;

    public SimonSwiftGame() {
        // initialise objects here
    }

    public void startGame() {
        // start the game loop
    }

    public void playRound() {
        // play one round
    }

    public void endGame() {
        // finish the game
    }
}
