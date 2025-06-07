package ooad.project1b;

import java.util.ArrayList;
import java.util.Collections;

public class Tokenizer {
    private final ArrayList<String> tokens = new ArrayList<>();

    /**
     * Creates tokens from a list of words, adds them to a single list, and randomizes the order.
     * @param words The list of words to tokenize.
     */
    public void createTokens(ArrayList<String> words) {
        for (String word : words) {
            for (int i = 0; i < word.length(); i += 2) {
                if (word.length() % 2 != 0 && i == word.length() - 3) {
                    tokens.add(word.substring(i, i + 3));
                    break;
                }

                tokens.add(word.substring(i, i + 2));
            }
        }

        Collections.shuffle(tokens);
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }
}
