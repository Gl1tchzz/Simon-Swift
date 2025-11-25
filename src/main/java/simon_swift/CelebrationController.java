package simon_swift;

import swiftbot.SwiftBotAPI;
import java.util.Random;


public class CelebrationController {

    private final SwiftBotAPI api;
    private final Random random;
    private final LEDController ledController;

    public CelebrationController(SwiftBotAPI api) {
        this.api = api;

        ledController = new LEDController(api);
        random = new Random();
    }

    public void celebrate(long duration) {
        
        long startTime = System.currentTimeMillis();
        
        while (System.currentTimeMillis() - startTime < duration) {
            GameColour[] colours = GameColour.values();
            int index = random.nextInt(colours.length);
            GameColour newColour = colours[index];

            ledController.showColour(newColour, 250);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println("LED sleep interrupted: " + e.getMessage());
            }
        }
    }
}
