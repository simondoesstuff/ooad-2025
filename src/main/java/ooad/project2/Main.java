package ooad.project2;

import ooad.project2.model.store.Store;
import ooad.project2.musicLand.MusicLandSimulator;

/**
 * Main entry point for the MusicLand simulation.
 * This class sets up the simulation and redirects output to a file as required.
 */
public class Main {
    public static void main(String[] args) {
        MusicLandSimulator simulator = new MusicLandSimulator(new Store());
        simulator.run();
    }
}
