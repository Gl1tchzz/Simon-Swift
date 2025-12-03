package simon_swift;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class SequenceManager {

    private final Random rand;
    private final List<Integer> sequenceList;

    //to initialise sequence
    public SequenceManager() {
        sequenceList = new ArrayList<>();
        rand = new Random();
    }

    //generates and appends new colourIndex to list
    public void nextColour() {
        int newColour = rand.nextInt(0,4);
        sequenceList.add(newColour);
    }

    //returns current sequence of colours
    public List<GameColour> getSequence() {
        List<GameColour> colourSequence = new ArrayList<>();
        GameColour[] colours = GameColour.values();
        for (Integer i : sequenceList) {
            colourSequence.add(colours[i]);
        }
        return colourSequence;
    }

    //resets the sequence
    public void resetSequence() {
        sequenceList.clear();
    }

}
