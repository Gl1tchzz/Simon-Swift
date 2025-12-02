package simon_swift;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SequenceManager {

    private final List<GameColour> sequence;
    private final Random random;

    public SequenceManager() {
        sequence = new ArrayList<>();
        random = new Random();
    }

    // Adds a new random colour to the sequence
    public void addNewColour() {
        GameColour[] colours = GameColour.values();
        int index = random.nextInt(colours.length);
        GameColour newColour = colours[index];
        sequence.add(newColour);
    }

    // Returns the current sequence
    public List<GameColour> getSequence() {
        return sequence;
    }

    public void resetSequence() {
        // clears the sequence
        sequence.clear();
    }
}
