package simon_swift;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import swiftbot.SwiftBotAPI;

public class SimonSwiftGame {

    private SwiftBotAPI api;

    private LEDController ledController;
    private ButtonInputHandler buttonInputHandler;
    private SequenceManager sequenceManager;
    private CelebrationController celebrationController;
    private SequenceValidator sequenceValidator;

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
        Scanner scanner = new Scanner(System.in);

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
        scanner.close();
    }

    public boolean playRound() {
        //Increment Round
        round++;

        //Display Round Info
        System.out.println("----------------------");
        System.out.println("Round: " + round);
        System.out.println("Current score: " + score);
        System.out.println("----------------------");

        // Generate the Colour Sequence
        

        // Show the sequence to the player
       

        // Get player input

        
        // Validate the input
        
    }

    public void endGame() {
        
    }
}
