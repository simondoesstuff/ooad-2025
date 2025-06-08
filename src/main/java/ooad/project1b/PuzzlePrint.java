package ooad.project1b;

import java.util.ArrayList;

public class PuzzlePrint {

    /**
     * Prints the entire puzzle, including tokens, clues, and the answer key.
     */
    public void printPuzzle(ArrayList<String> tokens, ArrayList<String> words, ArrayList<String> clues) {
        printTokens(tokens);
        printClues(clues);
        printAnswerKey(words);
    }

    private void printTokens(ArrayList<String> tokens) {
        System.out.println("Tokens");

        for (int i = 0; i < tokens.size(); i++) {
            System.out.print(tokens.get(i) + "\t");

            if ((i + 1) % 4 == 0) {
                System.out.println();
            }
        }

        System.out.println("\n");
    }

    private void printClues(ArrayList<String> clues) {
        System.out.println("Clues");

        for (String clue : clues) {
            System.out.println(clue);
        }

        System.out.println("\n");
    }

    private void printAnswerKey(ArrayList<String> words) {
        System.out.println("Answer Key");

        for (String word : words) {
            System.out.println(word);
        }
    }
}
