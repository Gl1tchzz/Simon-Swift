package simon_swift;
import swiftbot.SwiftBotAPI;

public class CelebrationController {

    private final SwiftBotAPI swiftBot;
    private final LEDController ledController;


    public CelebrationController(SwiftBotAPI swiftBot) {
        this.swiftBot = swiftBot;

        ledController = new LEDController(swiftBot);
    }

    /**
     * Celebration based on your group member's celebrationDive:
     * - Blink random colours on all underlights
     * - Do a 'V' shaped movement
     * - Blink random colours again
     *
     * The duration parameter is kept to match your existing call,
     * but the pattern itself is fixed like his original.
     */
    public void celebrate(int score) {
        System.out.println("Performing celebration dive!");
        // blink before move
        ledController.blinkColoursRandomOrder(300);

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
        ledController.blinkColoursRandomOrder(300);
    }
}
