import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import swiftbot.SwiftBotAPI;

public class SimonSwiftGame {

    private final SwiftBotAPI swiftBot;

    private final LEDController ledController;
    private final ButtonInputHandler buttonInputHandler;
    private final SequenceManager sequenceManager;
    private final CelebrationController celebrationController;
    private final SequenceValidator sequenceValidator;

    private int score;
    private int round;

    public SimonSwiftGame() {
        swiftBot = SwiftBotAPI.INSTANCE;

        ledController = new LEDController(swiftBot);
        buttonInputHandler = new ButtonInputHandler(swiftBot);
        sequenceManager = new SequenceManager();
        celebrationController = new CelebrationController(swiftBot);
        sequenceValidator = new SequenceValidator();

        score = 0;
        round = 0;
    }

    public void startGame() {
        AsciiUI.printWelcomeMessage();

        sequenceManager.resetSequence();
        score = 0;
        round = 0;

        boolean playing = true;
        try (Scanner scanner = new Scanner(System.in)) {

            while (playing) {
                boolean sucsess = playRound();

                if (!sucsess) {
                    AsciiUI.printGameOverMessage();
                    playing = false;
                } else {
                    score++;

                    if (round % 5 == 0) {
                        System.out.println("You reached round " + round + ". Do you want to continue? (y/n): ");
                        String answer = scanner.nextLine();

                        if (answer.equalsIgnoreCase("n")) {
                            System.out.println("See you again champ!");
                            AsciiUI.printGameSummary(score);
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
        AsciiUI.printRoundHeader(round, score);

        // Add new random colour to the sequence
        sequenceManager.addNewColour();
        List<GameColour> sequence = sequenceManager.getSequence();

        // Show the sequence to the player
        AsciiUI.printWatchSequence();
        for (GameColour colour : sequence) {
            ledController.blinkUnderlight(colour, 500);
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
        AsciiUI.printYourTurn();

        List<GameColour> userInput = new ArrayList<>();
        for (int i = 0; i < sequence.size(); i++) {
            AsciiUI.printWaitingForInput(i, sequence.size());
            GameColour pressed = buttonInputHandler.waitForPress();
            userInput.add(pressed);
            ledController.blinkUnderlight(pressed, 300); // Briefly show the pressed colour
        }

        // Validate the input
        boolean correct = sequenceValidator.validate(sequence, userInput);

        if(!correct) {
            
            return false;
        }

        AsciiUI.printSequenceCorrect();
        ledController.CorrectSequenceLights();
        return true;
    }

    public void endGame() {
        if (score >= 5) {
            celebrationController.celebrate(score);
        } else {
            ledController.GameOverLights();
        }

        AsciiUI.printGameSummary(score);

        // Prompt to play again
        AsciiUI.playAgainMessage();
        try (Scanner scanner = new Scanner(System.in)) {
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("y")) {
                startGame();
            } else {
                System.out.println("Goodbye!");
                System.exit(0);
            }
        }

        ledController.clearLights();
        swiftBot.disableAllButtons();
    }
}
