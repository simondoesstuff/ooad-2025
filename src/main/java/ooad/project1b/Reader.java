package ooad.project1b;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * Singleton, houses word/clue pairs.
 */
public class Reader {
    private static Reader inst;
    private final ArrayList<String> words = new ArrayList<>();
    private final ArrayList<String> clues = new ArrayList<>();

    private final String[] initialWords = {"printer", "shadow", "volcano", "camera", "meeting", "politics"};
    private final String[] initialClues = {
            "My hungry mouth consumes paper and ink, crying out when I'm empty.",
            "I follow you all day but flee when the sun bids farewell.",
            "I am a mountain with a fiery temper.",
            "I am a time machine for frozen memories.",
            "The place where minutes are taken and hours are lost.",
            "The art of looking for trouble and misdiagnosing the remedy."
    };
    private Reader() {
      this.createLists();
    }

    public static Reader getInst() {
      if (inst == null) inst = new Reader();
      return inst;
    }

    /**
     * Populates the words and clues ArrayLists from constant arrays.
     * Converts words to uppercase.
     */
    private void createLists() {
        for (String word : initialWords) {
            words.add(word.toUpperCase());
        }

        clues.addAll(Arrays.asList(initialClues));
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public ArrayList<String> getClues() {
        return clues;
    }
}
