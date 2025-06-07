package ooad.project1b;

public class Main {
    /**
     * Main method to run the puzzle creator program.
     * It instantiates the necessary classes and calls their methods to create and print the puzzle.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Reader reader = Reader.getInst();
        Tokenizer tokenizer = new Tokenizer();
        PuzzlePrint puzzlePrint = new PuzzlePrint();

        tokenizer.createTokens(reader.getWords());
        puzzlePrint.printPuzzle(tokenizer.getTokens(), reader.getWords(), reader.getClues());
    }
}
