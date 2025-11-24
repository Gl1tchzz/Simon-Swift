package simon_swift;

import swiftbot.SwiftBotAPI;
import swiftbot.Underlight;

public class LEDController {

    private SwiftBotAPI api;

    public LEDController(SwiftBotAPI api) {
        this.api = api;
    }

    // Mapping colours to LEDs
    private Underlight getUnderlightForColour(GameColour colour) {
        switch (colour) {
            case RED:
                return Underlight.FRONT_LEFT;
            case BLUE:
                return Underlight.FRONT_RIGHT;
            case GREEN:
                return Underlight.BACK_LEFT;
            case YELLOW:
                return Underlight.BACK_RIGHT;
            default:
                throw new AssertionError("Unknown colour: " + colour);
        }
    }

    // Mapping colours to RGB values
    private int[] getRgbForColour(GameColour colour) {
        switch (colour) {
            case RED:
                return new int[]{255, 0, 0};
            case BLUE:
                return new int[]{0, 0, 255};
            case GREEN:
                return new int[]{0, 255, 0};
            case YELLOW:
                return new int[]{255, 255, 0};
            default:
                throw new AssertionError("Unknown colour: " + colour);
        }
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
               e.printStackTrace();
            }

            api.disableUnderlight(led);
        }
    }

    // Clear all lights
    public void clearLights() {
        api.disableUnderlights();
    }
}
