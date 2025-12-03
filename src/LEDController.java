import swiftbot.SwiftBotAPI;
import swiftbot.Underlight;
import java.util.Random;

public class LEDController {

    private final SwiftBotAPI swiftBot;
    private static final Random RAND = new Random();

    // RGB definitions
    static final int[] RED = new int[]{255, 0, 0};
    static final int[] GREEN = new int[]{0, 255, 0};
    static final int[] BLUE = new int[]{0, 0, 255};
    static final int[] YELLOW = new int[]{255, 255, 0};
    static final int[] OFF = new int[]{0, 0, 0};
    
    static final Underlight[] LEDS = new Underlight[] {
        Underlight.FRONT_LEFT,   // index 0 -> RED
        Underlight.FRONT_RIGHT,  // index 1 -> BLUE
        Underlight.BACK_LEFT,  // index 2 -> GREEN
        Underlight.BACK_RIGHT  // index 3 -> YELLOW
    };

    static final int[][] COLOURS = new int[][] { RED, GREEN, BLUE, YELLOW };

    public LEDController(SwiftBotAPI swiftBot) {
        this.swiftBot = swiftBot;
    }

    private int colourToIndex(GameColour colour) {
        return switch (colour) {
            case RED -> 0;
            case BLUE -> 1;
            case GREEN -> 2;
            case YELLOW-> 3;
        };
    }

    public void blinkUnderlight(GameColour colour, int durationMs) {
        int idx = colourToIndex(colour);
        Underlight led = LEDS[idx];
        int[] rgb = COLOURS[idx];

        try {
            swiftBot.setUnderlight(led, rgb);
            if (durationMs > 0) {
                Thread.sleep(durationMs);
                // turn off
                swiftBot.setUnderlight(led, OFF);
            }
        } catch (InterruptedException e) {
            System.err.println("LED sleep interrupted");
            Thread.currentThread().interrupt();
        }
    }

    public void GameOverLights() {
        int[] red = RED;

        // Flash all lights red three times
        for (int i = 0; i < 3; i++) {
            // Turn all Lights Red
            for (Underlight underlight : Underlight.values()) {
                swiftBot.setUnderlight(underlight, red);
            }

            // wait 500ms
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
               System.err.println("LED sleep interrupted");
               Thread.currentThread().interrupt();
            }

            // Turn off all Lights
            swiftBot.disableUnderlights();

            // wait another 500ms to loop
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
               System.err.println("LED sleep interrupted");
               Thread.currentThread().interrupt();
            }
        }
    }

    public void CorrectSequenceLights() {
        int[] green = GREEN;

        // Light all LEDs green for 1 second
        for (Underlight underlight : Underlight.values()) {
            swiftBot.setUnderlight(underlight, green);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
           System.err.println("LED sleep interrupted");
           Thread.currentThread().interrupt();
        }

        swiftBot.disableUnderlights();
    }

    public void clearLights() {
        try {
            swiftBot.disableUnderlights();
        } catch (Exception e) {
            System.err.println("Error disabling underlights: " + e.getMessage());
        }
    }

    public void RandomLightSequence(int msPerColour) {
        boolean[] used = new boolean[4];
        int remaining = 4;
        while (remaining > 0) {
            int idx = RAND.nextInt(4);
            if (used[idx]) continue;
            used[idx] = true;
            remaining--;

            try {
                // set all underlights to selected colour (his fillUnderlights idea)
                swiftBot.fillUnderlights(COLOURS[idx]);
                Thread.sleep(msPerColour);
            } catch (InterruptedException e) {
                System.err.println("Error blinking colours: " + e.getMessage());
            }
        }
        try {
            swiftBot.disableUnderlights();
        } catch (Exception ignored) {}
    }
}