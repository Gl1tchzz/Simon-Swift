package simon_swift;
import swiftbot.SwiftBotAPI;

public class App {
    public static void main( String[] args ){
        SwiftBotAPI.INSTANCE.move(100, 100, 1000);
    }
}
