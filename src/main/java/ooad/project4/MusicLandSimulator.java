package ooad.project4;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ooad.project4.model.store.tuning.HaphazardTuning;
import ooad.project4.model.store.tuning.ManualTuning;
import ooad.project4.model.store.tuning.ElectronicTuning;
import ooad.project4.model.customers.CommandLineCustomer;
import ooad.project4.model.item.BuildableItem;
import ooad.project4.model.store.Clerk;
import ooad.project4.model.store.Store;

/**
 * Manages system-wide things like the day cycle & logger.
 * Controls all Stores in the overall simulation.
 */
public class MusicLandSimulator {
    private final StoreManager northern, southern;
    private final ClerkPool pool = new ClerkPool();
    private final String logDir;

    public MusicLandSimulator(String logDir) {
        this.logDir = logDir;

        addStaff();
        northern = new StoreManager(setupStore(new Store("North")), pool);
        southern = new StoreManager(setupStore(new Store("South")), pool);
    }

    private void addStaff() {
        // Add staff
        pool.add(new Clerk("Fred", 0.20, new ManualTuning()));
        pool.add(new Clerk("Ginger", 0.05, new ElectronicTuning()));
        pool.add(new Clerk("Rita", 0.95, new HaphazardTuning()));

        pool.add(new Clerk("Gigatron-XV", 0.90, new ElectronicTuning()));
        pool.add(new Clerk("Zomzar-X", 0.91, new ElectronicTuning()));
        pool.add(new Clerk("DOM-I", 0.92, new ElectronicTuning()));
    }

    /**
     * Initializes the store with proper items.
     * @returns argument
     */
    private Store setupStore(Store store) {
        // Initialize inventory with 3 of each item type
        for (Class<? extends BuildableItem> itemType : ItemFactory.getAllItemTypes()) {
            var batch = Stream.generate(() -> store.makeRandomItem(itemType, 0).build())
                    .limit(3)
                    .collect(Collectors.toList());
            batch.forEach(item -> store.addItem(item));
        }

        System.out.println("Initial store inventory has been created.");
        return store;
    }

    public void run() {
        DayLogger log = null;

        for (int today = 1; today <= 30; today++) {
            // reset the logger
            if (log != null) log.close();
            log = new DayLogger(logDir, today);

            log.logf("\n========================================\n");
            log.logf("================ Day %-2d ================\n", today);
            log.logf("========================================\n");

            // ClerkPool#rest assigns the day to all Clerks
            //   and resets work streaks
            pool.rest(today);

            // the stores are closed on sunday
            if (today % 7 == 0) {
                log.logf("All MusicLand stores are closed on Sunday.\n");
                continue;
            }

            // reserve some workers as being sick
            var sick = new ArrayList<Clerk>();

            // there can only be at most 2 sick workers for some reason
            while (ThreadLocalRandom.current().nextDouble(0, 1) <= .1 && sick.size() < 2) {
                var clerk = pool.assign();
                // TODO: extract SickClerkEvent
                log.logf("Oh no! %s was sick and cannot work today.\n", clerk.getName());
                sick.add(clerk);
            }

            // put all stores to work
            northern.runDailyActions(today, log);
            southern.runDailyActions(today, log);

            // at the end of the day, consider sick workers now rested
            for (var clerk : sick) {
                pool.makeAssignable(clerk);
            }
        }

        // now that the simulation has run for 30 days, the 31st day will
        // be dedicated to the summary and the CLI for manual customer interaction
        log.close();
        log = new DayLogger(logDir, 31);

        // Customer CLI...
        log.logf("\n");
        var activeClerks = new ArrayList<Clerk>();
        activeClerks.add(northern.getActiveClerk());
        activeClerks.add(southern.getActiveClerk());
        new CommandHandler(activeClerks).run();

        printSummary(log);
    }

    public void printSummary(DayLogger log) {
        log.logf("\n########################################");
        log.logf("###### 30-DAY SIMULATION COMPLETE ######");
        log.logf("########################################\n");

        log.logf("\n---- Northern Store ----\n");
        northern.printSummary();

        log.logf("\n---- Southern Store ----\n");
        southern.printSummary();
    }
}
