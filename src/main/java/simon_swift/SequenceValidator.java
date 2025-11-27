package simon_swift;

import java.util.List;

public class SequenceValidator {
    public boolean validate(List<GameColour> expected, List<GameColour> userInput) {
        
        // Checks if Sequence Lengths Match
        if (expected.size() != userInput.size()) {
            System.out.println("Size of sequences do not match");
            return false;
        }

        // Compares Each Colour in Sequence
        for (int i = 0; i < expected.size(); i++) {
            //return false if any colour does not match
            if (expected.get(i) != userInput.get(i)) {
                System.out.println("Mismatch at position " + i + ": expected " + expected.get(i) + " but got " + userInput.get(i));
                return false;
            }
        }

        // If all colours match return true
       // System.out.println("Sequence is correct!");
        return true;
    }
}
