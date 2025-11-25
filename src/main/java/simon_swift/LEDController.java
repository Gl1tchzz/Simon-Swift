package simon_swift;

import swiftbot.SwiftBotAPI;
import swiftbot.Underlight;

public class LEDController {

    private final SwiftBotAPI api;

    public LEDController(SwiftBotAPI api) {
        this.api = api;
    }

    // Mapping colours to LEDs
    private Underlight getUnderlightForColour(GameColour colour) {
        return switch (colour) {
            case RED -> Underlight.FRONT_LEFT;
            case BLUE -> Underlight.FRONT_RIGHT;
            case GREEN -> Underlight.BACK_LEFT;
            case YELLOW -> Underlight.BACK_RIGHT;
        };
    }

    // Mapping colours to RGB values
    private int[] getRgbForColour(GameColour colour) {

        return switch (colour) {
            case RED -> new int[]{255, 0, 0};
            case BLUE -> new int[]{0, 0, 255};
            case GREEN -> new int[]{0, 255, 0};
            case YELLOW -> new int[]{255, 255, 0};
        };
    }

    public void showColour(GameColour colour, int duration) {
        Underlight led = getUnderlightForColour(colour);
        int[] rgb = getRgbForColour(colour);
        
        // Display the colour
        api.setUnderlight(led, rgb);

        // Turn off after duration cooldown if specified
        if (duration > 0) {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
               System.err.println("LED sleep interrupted");
               Thread.currentThread().interrupt();
            }

            api.disableUnderlight(led);
        }
    }

    public void GameOverLights() {
        int[] RED = getRgbForColour(GameColour.RED);

        // Flash all lights red three times
        for (int i = 0; i < 3; i++) {
            // Turn all Lights Red
            for (Underlight underlight : Underlight.values()) {
                api.setUnderlight(underlight, RED);
            }

            // wait 500ms
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
               System.err.println("LED sleep interrupted");
            }
        
            // Turn off all Lights
            api.disableUnderlights();
         
            //wait another 500ms to loop
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
               System.err.println("LED sleep interrupted");
            }
        }
    }

    // Clear all lights
    public void clearLights() {
        api.disableUnderlights();
    }
}
