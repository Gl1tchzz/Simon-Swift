package simon_swift;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class SequenceGenerator {

    private Random rand;
    private List<Integer> sequenceList;

    //to initialise and reset sequence
    public void init() {
        sequenceList = new ArrayList<>();
        rand = new Random();
    }

    //generates and appends new colour to list and returns new list
    public List<Integer> nextColour() {
        int newColour = rand.nextInt(0,4);
        sequenceList.add(newColour);
        return sequenceList;
    }

    public List<Integer> getCurrentList(){
        return sequenceList;
    }

}
