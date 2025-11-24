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
        
    }

    // Mapping colours to RGB values
    private int[] getRgbForColour(GameColour colour) {
        
    }

    public void showColour(GameColour colour, int duration) {
        
        // Display the colour
       

        // Turn off after duration cooldown
        
    }

    // Clear all lights
    public void clearLights() {
      
    }
}
