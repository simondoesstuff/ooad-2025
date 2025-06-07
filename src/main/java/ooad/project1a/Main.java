package ooad.project1a;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Quantity of random values to generate (or press Enter to quit): ");
            String input = sc.nextLine();

            if (input.isEmpty()) break;

            try {
                int numRands = Integer.parseInt(input);

                if (numRands <= 0) {
                    System.out.println("Expected a positive integer, got: " + input);
                    continue;
                }

                Creator creator = new Creator(numRands);
                Analyzer analyzer = new Analyzer();

                System.out.println("\n---------------------------------------------------------------------------------");
                System.out.printf("%-25s | %-10s | %-10s | %-15s | %-10s | %-10s\n", "Random Function", "Samples", "Mean", "Std. Dev.", "Min", "Max");
                System.out.println("---------------------------------------------------------------------------------");

                analyzer.analyzeAndPrint(creator.getUtilRandoms(), "java.util.Random", numRands);
                analyzer.analyzeAndPrint(creator.getMathRandoms(), "Math.random", numRands);
                analyzer.analyzeAndPrint(creator.getThreadLocalRandoms(), "...ThreadLocalRandom", numRands);

                System.out.println("---------------------------------------------------------------------------------\n");

            } catch (NumberFormatException e) {
                System.out.println("Please enter an integer");
            }
        }

        sc.close();
    }
}
