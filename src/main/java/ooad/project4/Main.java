package ooad.project4;

import ooad.project4.model.store.Store;

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
