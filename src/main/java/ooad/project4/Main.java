package ooad.project4;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

/**
 * Main entry point for the MusicLand simulation.
 * This class sets up the simulation and redirects output to a file as required.
 */
public class Main {
    public static void main(String[] args) {
        var logDir = "./src/main/java/ooad/project4/assets/logs";
        MusicLandSimulator simulator = new MusicLandSimulator(logDir);
        simulator.run();
    }
}
