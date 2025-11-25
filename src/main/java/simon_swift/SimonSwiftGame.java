package simon_swift;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import swiftbot.SwiftBotAPI;

public class SimonSwiftGame {

    private final SwiftBotAPI api;

    private final LEDController ledController;
    private final ButtonInputHandler buttonInputHandler;
    private final SequenceManager sequenceManager;
    private final CelebrationController celebrationController;
    private final SequenceValidator sequenceValidator;

    private int score;
    private int round;

    public SimonSwiftGame() {
        api = SwiftBotAPI.INSTANCE;

        ledController = new LEDController(api);
        buttonInputHandler = new ButtonInputHandler(api);
        sequenceManager = new SequenceManager();
        celebrationController = new CelebrationController(api);
        sequenceValidator = new SequenceValidator();

        score = 0;
        round = 0;
    }

    public void startGame() {
        System.out.println("Welcome to Simon Swift!");
        System.out.println("Watch the colours, then repeat them using the buttons A, B, X, Y.");
        System.out.println();

        sequenceManager.resetSequence();
        score = 0;
        round = 0;

        boolean playing = true;
        try (Scanner scanner = new Scanner(System.in)) {

            while (playing) {
                boolean sucsess = playRound();

                if (!sucsess) {
                    System.out.println("Game Over!");
                    playing = false;
                } else {
                    score++;

                    if (round % 5 == 0) {
                        System.out.println("You reached round " + round + ". Do you want to continue? (y/n): ");
                        String answer = scanner.nextLine();

                        if (answer.equalsIgnoreCase("n")) {
                            System.out.println("Thanks for playing! Your final score is: " + score);
                            playing = false;
                        }
                    }
                }
            }
            
            endGame();
        }
    }

    public boolean playRound() {
        //Increment Round
        round++;

        //Display Round Info
        System.out.println("----------------------");
        System.out.println("Round: " + round);
        System.out.println("Current score: " + score);
        System.out.println("----------------------");

        // Add new random colour to the sequence
        sequenceManager.addNewColour();
        List<GameColour> sequence = sequenceManager.getSequence();

        // Show the sequence to the player
        System.out.println("Watch the sequence...");
        for (GameColour colour : sequence) {
            ledController.showColour(colour, 500);
            try {
                Thread.sleep(250); // brief pause between colours
            } catch (InterruptedException e) {
               System.err.println("LED sleep interrupted");
               Thread.currentThread().interrupt();
            }
        }

        // Brief pause before player input
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            System.err.println("Sleep interrupted");
        }

        // Get player input
        System.out.println("Your turn! Repeat the sequence using buttons A, B, X, Y.");

        List<GameColour> userInput = new ArrayList<>();
        for (int i = 0; i < sequence.size(); i++) {
            System.out.println("Waiting for input " + (i + 1) + " of " + sequence.size() + "...");
            GameColour pressed = buttonInputHandler.waitForPress();
            userInput.add(pressed);
            ledController.showColour(pressed, 300); // Briefly show the pressed colour
        }

        // Validate the input
        boolean correct = sequenceValidator.validate(sequence, userInput);

        if(!correct) {
            
            return false;
        }

        System.out.println("Correct! Get ready for the next round.");
        return true;
    }

    public void endGame() {
        if (score >= 5) {
            celebrationController.celebrate(2500);
        } else {
            ledController.GameOverLights();
        }

        System.out.println();
        System.out.println("===== GAME SUMMARY =====");
        System.out.println("Final Score: " + score);
        System.out.println("Thank you for playing Simon Swift!");
        System.out.println("========================");

        ledController.clearLights();
        api.disableAllButtons();
    }
}
