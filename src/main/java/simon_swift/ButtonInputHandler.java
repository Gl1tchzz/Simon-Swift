package simon_swift;

import swiftbot.SwiftBotAPI;
import swiftbot.Button;

public class ButtonInputHandler {

    private SwiftBotAPI api;
    private GameColour lastColourPressed;

    public ButtonInputHandler(SwiftBotAPI api) {
        this.api = api;
        this.lastColourPressed = null;

        //disble all buttons initially
        api.disableAllButtons();

        enableButtons();
    }

    public void enableButtons() {

        // A -> RED
        api.enableButton(Button.A, () -> {
            lastColourPressed = GameColour.RED;
            System.out.println("Button A (RED) pressed");
        });

        // B -> GREEN
        api.enableButton(Button.B, () -> {
            lastColourPressed = GameColour.GREEN;
            System.out.println("Button B (GREEN) pressed");
        });

        // X -> BLUE
        api.enableButton(Button.X, () -> {
            lastColourPressed = GameColour.BLUE;
            System.out.println("Button X (BLUE) pressed");
        });

        // Y -> YELLOW
        api.enableButton(Button.Y, () -> {
            lastColourPressed = GameColour.YELLOW;
            System.out.println("Button Y (YELLOW) pressed");
        });
    }

    public GameColour waitForPress() {
        lastColourPressed = null;

        while (lastColourPressed == null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        return lastColourPressed;
    }
}
