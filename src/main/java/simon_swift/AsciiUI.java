package simon_swift;
public class AsciiUI {
    public static void printWelcomeMessage() {
        System.out.println("+--------------------------------------+");
        System.out.println("|            SIMON SWIFT              |");
        System.out.println("+--------------------------------------+");
        System.out.println("| Watch the colours carefully...      |");
        System.out.println("| Repeat them using the buttons:      |");
        System.out.println("|   A = RED   B = GREEN               |");
        System.out.println("|   X = BLUE  Y = YELLOW              |");
        System.out.println("+--------------------------------------+");
        System.out.println();
    }

    public static void printRoundHeader(int round, int score) {
        System.out.println();
        System.out.println("+--------------------------------------+");
        System.out.printf ("|              ROUND %2d               |%n", round);
        System.out.println("+--------------------------------------+");
        System.out.printf ("| Current score: %-20d |%n", score);
        System.out.println("+--------------------------------------+");
        System.out.println();
    }

    public static void printWatchSequence() {
        System.out.println(">> WATCH THE SEQUENCE...");
    }

    public static void printYourTurn() {
        System.out.println(">> YOUR TURN!");
        System.out.println("   Repeat the sequence using buttons A, B, X, Y.");
    }

    public static void printWaitingForInput(int index, int total) {
        System.out.println("   - Waiting for input " + index + " of " + total + "...");
    }

    public static void printSequenceCorrect() {
        System.out.println();
        System.out.println("+---------------------------+");
        System.out.println("|   SEQUENCE IS CORRECT!    |");
        System.out.println("+---------------------------+");
        System.out.println("Get ready for the next round...");
    }

    public static void printSequenceIncorrect(GameColour expected, GameColour actual, int position) {
        System.out.println();
        System.out.println("+---------------------------+");
        System.out.println("|    SEQUENCE INCORRECT     |");
        System.out.println("+---------------------------+");
        System.out.println("Mismatch at position " + position + ":");
        System.out.println("  Expected: " + expected);
        System.out.println("  Got     : " + actual);
        System.out.println();
    }

    public static void printGameOverMessage() {
        System.out.println();
        System.out.println("+---------------------------+");
        System.out.println("|         GAME OVER!        |");
        System.out.println("+---------------------------+");
    }

    public static void printGameSummary(int score) {
        System.out.println();
        System.out.println("+======================================+");
        System.out.println("|             GAME SUMMARY             |");
        System.out.println("+======================================+");
        System.out.printf ("| Final score: %-23d |%n", score);
        System.out.println("| Thanks for playing Simon Swift!      |");
        System.out.println("+======================================+");
        System.out.println();
    }

    public static void playAgainMessage() {
        System.out.println();
        System.out.println("+======================================+");
        System.out.println("|              PLAY AGAIN?             |");
        System.out.println("+======================================+");
        System.out.println("|    Do you want to play again? (y/n)  |");
        System.out.println("+======================================+");
        System.out.println();
    }
}

