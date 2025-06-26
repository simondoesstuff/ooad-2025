package ooad.project3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ooad.project3.model.store.tuning.HaphazardTuning;
import ooad.project3.model.store.tuning.ManualTuning;
import ooad.project3.model.store.tuning.ElectronicTuning;
import ooad.project3.model.Bank;
import ooad.project3.model.item.BuildableItem;
import ooad.project3.model.item.BuildableItem;
import ooad.project3.model.item.SoldItem;
import ooad.project3.model.store.Clerk;
import ooad.project3.model.store.Store;

/**
 * Main simulation class for MusicLand.
 * This class orchestrates the entire simulation, including the daily loop,
 * clerk scheduling, and executing all daily actions.
 */
public class MusicLandSimulator {
    private static final Random rand = ThreadLocalRandom.current();

    private final Store store;
    private ArrayList<Clerk> staff = new ArrayList<>();
    private Clerk activeClerk;
    private int today = 0;
    private DayLogger log;
    private String logDir = "./src/main/java/ooad/project3/assets/logs";

    public MusicLandSimulator(Store store) {
        this.store = store;
        setupStore(this.store);
    }

    /**
     * Initializes the store with proper works and items.
     * Impure
     */
    private void setupStore(Store store) {
        // Add staff
        staff.add(new Clerk(store, "Fred", 0.20, new ManualTuning()));
        staff.add(new Clerk(store, "Ginger", 0.05, new ElectronicTuning()));
        staff.add(new Clerk(store, "Rita", 0.95, new HaphazardTuning()));

        // Initialize inventory with 3 of each item type
        for (Class<? extends BuildableItem> itemType : ItemFactory.getAllItemTypes()) {
            var batch = Stream.generate(() -> store.makeRandomItem(itemType, 0).build())
                    .limit(3)
                    .collect(Collectors.toList());
            batch.forEach(item -> store.addItem(item));
        }

        System.out.println("Initial store inventory has been created.");
    }

    /**
     * Main simulation loop that runs for 30 days.
     */
    public void run() {
        marchDay(null); // day zero is sunday, but we want the simulation
                        // to start on a monday.

        for (today = 1; today <= 30; today++) {
            System.out.println("\n========================================");
            System.out.printf("================ Day %-2d ================\n", today);
            System.out.println("========================================");

            if (log != null) log.close();
            log = new DayLogger(logDir, today);

            // is it sunday?
            if (today % 7 == 0) {
                System.out.println("Store is closed on Sunday.");
                marchDay(null); // null: no active clerk today
            }

            var clerk = selectClerk();

            if (clerk == null) continue;

            marchDay(clerk);
            runDailyActions(clerk);
        }

        printFinalSummary();
    }

    /**
     * Go to the next day and specify a new Clerk.
     * Inactive Clerk's days worked streaks are reset.
     */
    private void marchDay(Clerk todaysClerk) {
        activeClerk = todaysClerk;

        for (var clerk : staff) {
            if (clerk != activeClerk) {
                clerk.resetWorkStreak();
            }

            clerk.setToday(today);
        }
    }

    /**
     * Filters the store's Clerks for ones not overworked
     */
    public List<Clerk> getAvailableClerks() {
        return staff.stream()
            .filter(c -> c.getWorkStreak() < 3)
            .collect(Collectors.toList());
    }

    private Clerk selectClerk() {
        var clerks = getAvailableClerks();

        if (clerks.size() == 0) {
            System.out.println("All clerks are overworked so the store can't open today.");
            return null;
        }

        var clerk = clerks.get(rand.nextInt(clerks.size()));

        if (rand.nextDouble(0, 1) <= .1) {
            System.err.printf("Oh no! %s was sick and cannot work today.\n", clerk.getName());
            clerk.resetWorkStreak();
            clerk = selectClerk();  // reselect
        }

        return clerk;
    }

    private void runDailyActions(Clerk clerk) {
        clerk.arriveAtStore();
        if (!clerk.checkRegister())
            clerk.goToBank();
        clerk.doInventory();
        clerk.openTheStore();
        clerk.cleanTheStore();
        clerk.leaveTheStore();
    }

    /**
     * Summary of the final items in the inventory,
     * the total items sold, and the financial summary.
     */
    private void printFinalSummary() {
        System.out.println("\n########################################");
        System.out.println("###### 30-DAY SIMULATION COMPLETE ######");
        System.out.println("########################################\n");

        System.out.println("--- Final Inventory ---");

        if (store.getInventory().isEmpty()) {
            System.out.println("No items left in inventory.");
        } else {
            store.getInventory().forEach(System.out::println);
        }

        System.out.printf("\nTotal value of remaining inventory (by purchase price): $%.2f\n", store.getInventory().getTotalPurchasePrice());

        System.out.println("\n--- Items Sold ---");
        double totalSales = 0;

        if (store.getSoldItems().isEmpty()) {
            System.out.println("No items were sold during the simulation.");
        } else {
            for (SoldItem item : store.getSoldItems()) {
                System.out.printf("%s sold on day %d for $%.1f\n",
                        item.getName(), item.getDaySold(), item.getSalePrice());
                totalSales += item.getSalePrice();
            }
        }

        System.out.printf("\nTotal of all sales: $%.2f\n", totalSales);

        System.out.println("\n--- Financial Summary ---");
        System.out.printf("Final money in Cash Register: $%.2f\n", store.getCashRegister().getCash());
        System.out.printf("Total money withdrawn from the Bank: $%.2f\n", Bank.getInstance().getTotalWithdrawn());
    }
}
