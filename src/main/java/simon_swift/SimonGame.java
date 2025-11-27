package org.example;
import swiftbot.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * SimonGame for SwiftBot
 *
 *
 * Mapping (default in this code - you can change if you prefer):
 *  - Button.A -> Underlight.FRONT_LEFT  -> RED
 *  - Button.B -> Underlight.FRONT_RIGHT -> GREEN
 *  - Button.X -> Underlight.MIDDLE_LEFT -> BLUE
 *  - Button.Y -> Underlight.MIDDLE_RIGHT-> YELLOW
 */
public class SimonGame {
    static SwiftBotAPI swiftBot;
    static final Random RAND = new Random();

    // RGB definitions
    static final int[] RED = new int[]{255, 0, 0};
    static final int[] GREEN = new int[]{0, 255, 0};
    static final int[] BLUE = new int[]{0, 0, 255};
    static final int[] YELLOW = new int[]{255, 255, 0};
    static final int[] OFF = new int[]{0, 0, 0};

    // Game mapping: colours -> underlights -> buttons
    // Order is important so we can choose a random colour by index 0..3
    static final Underlight[] LEDS = new Underlight[] {
            Underlight.FRONT_LEFT,   // index 0 -> RED
            Underlight.FRONT_RIGHT,  // index 1 -> GREEN
            Underlight.BACK_LEFT,  // index 2 -> BLUE
            Underlight.BACK_RIGHT  // index 3 -> YELLOW
    };
    static final int[][] COLOURS = new int[][] { RED, GREEN, BLUE, YELLOW };
    static final Button[] BUTTONS = new Button[] { Button.A, Button.X, Button.B, Button.Y };

    // Synchronization for button callbacks
    static final Object buttonLock = new Object();
    static volatile Button lastButtonPressed = null;

    public static void main(String[] args) throws InterruptedException {
        try {
            swiftBot = SwiftBotAPI.INSTANCE;
        } catch (Exception e) {
            System.out.println("I2C disabled or SwiftBotAPI unavailable!");
            System.exit(5);
        }

        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println("Welcome to SwiftBot Simon Game!");
        System.out.println("Buttons: A, B, X, Y. Repeat the sequences shown by the robot.");

        List<Integer> sequence = new ArrayList<>(); // holds indices 0..3 for colours
        int round = 0;
        int score = 0;

        // main game loop
        gameLoop:
        while (true) {
            // start next round
            round++;
            // add a random colour to sequence
            sequence.add(RAND.nextInt(4));

            // display current score and round
            System.out.printf("\n--- Round %d ---  Current score: %d\n", round, score);

            // display the sequence to user (blink each mapped underlight)
            displaySequence(sequence);

            // now read user inputs, comparing against sequence
            for (int i = 0; i < sequence.size(); i++) {
                int expected = sequence.get(i); // 0..3

                // wait for a button press and get which button was pressed
                Button pressed = waitForButtonPress();
                if (pressed == null) {
                    // Shouldn't happen normally, but treat as incorrect
                    System.out.println("No button press detected. Game Over!");
                    break gameLoop;
                }

                // check mapping: which button corresponds to expected index?
                Button expectedButton = BUTTONS[expected];

                // give brief feedback: blink pressed button's underlight quickly
                int pressedIndex = buttonToIndex(pressed);
                if (pressedIndex >= 0) {
                    // quick blink the corresponding underlight
                    blinkUnderlight(LEDS[pressedIndex], COLOURS[pressedIndex], 150);
                }

                if (pressed != expectedButton) {
                    // incorrect input -> game over
                    System.out.println("\nGame Over!");
                    break gameLoop;
                }
            }

            // if we reach here, user got the sequence correct
            score++;
            System.out.printf("Correct! Score now: %d\n", score);

            // after every 5th round (5, 10, 15, ...) offer the choice to quit
            while (true) {
                if (round % 5 == 0) {
                    System.out.print("You've completed round " + round + ". Do you want to quit? (y/n): ");
                    String ans = sc.next().trim().toLowerCase();

                    if (ans.equals("y") || ans.equals("yes")) {
                        System.out.println("\nSee you again champ!");
                        break gameLoop; // Ends the whole game
                    } else if (ans.equals("n") || ans.equals("no")) {
                        break; // continues to the next round
                    } else {
                        System.out.println();
                        System.out.println("Invalid input. Please enter 'y' or 'n'");
                    }
                }
            }
            // small pause before next round
            Thread.sleep(500);
        }

        // final reporting
        System.out.printf("Final score: %d\nLast completed round: %d\n", score, round - (score==round?0:0));
        // The above prints the last round number attempted. If the user completed round N then quit,
        // round value equals N; if they failed during round N, we still print N as last round attempted.

        // celebration dive if score >= 5
        if (score >= 5) {
            celebrationDive(score);
        }

        // cleanup: turn off underlights and buttons
        try { swiftBot.disableUnderlights(); } catch (Exception ignored) {}
        try { swiftBot.disableAllButtons(); } catch (Exception ignored) {}
        System.out.println("Thanks for playing!");
        System.exit(0);
    }

    // Show the sequence by blinking the corresponding underlights (colour, off)
    static void displaySequence(List<Integer> sequence) throws InterruptedException {
        for (Integer idx : sequence) {
            Underlight led = LEDS[idx];
            int[] rgb = COLOURS[idx];
            // light on
            swiftBot.setUnderlight(led, rgb);
            Thread.sleep(600);
            // turn off
            swiftBot.setUnderlight(led, OFF);
            Thread.sleep(250);
        }
    }

    // Blink a single underlight for a short duration (ms)
    static void blinkUnderlight(Underlight led, int[] rgb, int ms) {
        try {
            swiftBot.setUnderlight(led, rgb);
            Thread.sleep(ms);
            swiftBot.setUnderlight(led, OFF);
        } catch (Exception e) {
            // ignore for small feedback
        }
    }

    // Waits for a single button press and returns which button was pressed.
    // Uses enableButton with callbacks to set lastButtonPressed and a wait/notify.
    static Button waitForButtonPress() {
        lastButtonPressed = null;

        // Define callbacks for each button
        try {
            swiftBot.enableButton(Button.A, () -> {
                synchronized (buttonLock) {
                    lastButtonPressed = Button.A;
                    buttonLock.notifyAll();
                }
                swiftBot.disableButton(Button.A);
            });
            swiftBot.enableButton(Button.B, () -> {
                synchronized (buttonLock) {
                    lastButtonPressed = Button.B;
                    buttonLock.notifyAll();
                }
                swiftBot.disableButton(Button.B);
            });
            swiftBot.enableButton(Button.X, () -> {
                synchronized (buttonLock) {
                    lastButtonPressed = Button.X;
                    buttonLock.notifyAll();
                }
                swiftBot.disableButton(Button.X);
            });
            swiftBot.enableButton(Button.Y, () -> {
                synchronized (buttonLock) {
                    lastButtonPressed = Button.Y;
                    buttonLock.notifyAll();
                }
                swiftBot.disableButton(Button.Y);
            });
        } catch (Exception e) {
            System.out.println("ERROR: unable to enable buttons.");
            return null;
        }

        // wait until one of the callbacks sets lastButtonPressed
        synchronized (buttonLock) {
            while (lastButtonPressed == null) {
                try {
                    buttonLock.wait();
                } catch (InterruptedException e) {
                    // ignore and continue waiting
                }
            }
        }

        // disable all buttons now that we've read the press
        try { swiftBot.disableAllButtons(); } catch (Exception ignored) {}
        return lastButtonPressed;
    }

    // helper: map Button to its index in BUTTONS array (A=0,B=1,X=2,Y=3)
    static int buttonToIndex(Button b) {
        for (int i = 0; i < BUTTONS.length; i++) {
            if (BUTTONS[i] == b) return i;
        }
        return -1;
    }

    // Celebration: blink all four colours in random order, then do a 'V' shaped movement,
    // then blink again in random order.
    static void celebrationDive(int score) {
        System.out.println("Performing celebration dive!");
        // blink before move
        blinkColoursRandomOrder(300);

        // compute speed percent
        int speedPercent = score * 10;
        if (score < 5) speedPercent = 40;
        if (score >= 10) speedPercent = 100;

        // Move in a 'V' shape: left arm then right arm. Each arm length is 30 cm.
        // The SwiftBot move API takes (leftPercent, rightPercent, timeMs).
        // The API does not give us a direct conversion from percent to cm/s, so we
        // use an approximation:
        //
        // ASSUMPTION (adjustable):
        //  - At 100% velocity, robot moves approximately baseSpeedCmPerSec cm/s.
        //  - Choose baseSpeedCmPerSec = 20 cm/s (tweak for your robot).
        //
        // timeMs = (distanceCm / (baseSpeed * speedPercent/100)) * 1000
        //
        double baseSpeedCmPerSec = 20.0; // tweak if you measure actual speed
        double speedFactor = (speedPercent / 100.0) * baseSpeedCmPerSec;
        if (speedFactor <= 0) speedFactor = 1.0;
        long timeMs = (long) Math.ceil((30.0 / speedFactor) * 1000.0);

        try {
            // left arm: left wheel forward, right wheel forward a bit slower to curve right
            swiftBot.move(speedPercent, speedPercent / 2, (int) timeMs);
            Thread.sleep(250);

            // small pause
            swiftBot.move(0, 0, 200);

            // right arm: right wheel forward, left wheel forward a bit slower to curve left
            swiftBot.move(speedPercent / 2, speedPercent, (int) timeMs);
            Thread.sleep(250);
        } catch (Exception e) {
            System.out.println("Warning: movement error during celebration (ignored).");
        }

        // blink after move
        blinkColoursRandomOrder(300);
    }

    // Blink the 4 colours (RED,GREEN,BLUE,YELLOW) in random order once each.
    static void blinkColoursRandomOrder(int msPerColour) {
        boolean[] used = new boolean[4];
        int remaining = 4;
        while (remaining > 0) {
            int idx = RAND.nextInt(4);
            if (used[idx]) continue;
            used[idx] = true;
            remaining--;

            try {
                swiftBot.fillUnderlights(COLOURS[idx]); // set all underlights to selected colour
                Thread.sleep(msPerColour);
            } catch (Exception e) {
                // ignore blinking errors
            }
        }
        try { swiftBot.disableUnderlights(); } catch (Exception ignored) {}
    }
}