package simon_swift;
import swiftbot.SwiftBotAPI;
import swiftbot.Button;

public class ButtonInputHandler {

    private final SwiftBotAPI swiftBot;
    private GameColour lastColourPressed;

    public ButtonInputHandler(SwiftBotAPI swiftBot) {
        this.swiftBot = swiftBot;
        this.lastColourPressed = null;

        //disble all buttons initially
        swiftBot.disableAllButtons();

        enableButtons();
    }

    private void enableButtons() {

        // A -> RED
        swiftBot.enableButton(Button.A, () -> {
            lastColourPressed = GameColour.RED;
            //System.out.println("Button A (RED) pressed");
        });

        // B -> GREEN
        swiftBot.enableButton(Button.B, () -> {
            lastColourPressed = GameColour.GREEN;
            //System.out.println("Button B (GREEN) pressed");
        });

        // X -> BLUE
        swiftBot.enableButton(Button.X, () -> {
            lastColourPressed = GameColour.BLUE;
            //System.out.println("Button X (BLUE) pressed");
        });

        // Y -> YELLOW
        swiftBot.enableButton(Button.Y, () -> {
            lastColourPressed = GameColour.YELLOW;
            //System.out.println("Button Y (YELLOW) pressed");
        });
    }

    public GameColour waitForPress() {
        lastColourPressed = null;

        while (lastColourPressed == null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.err.println("LED sleep interrupted: " + e.getMessage());
            }
        }
        
        return lastColourPressed;
    }
}
