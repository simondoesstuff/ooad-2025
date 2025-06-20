package ooad.project3.musicLand;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ooad.project3.ItemFactory;
import ooad.project3.model.Bank;
import ooad.project3.model.item.Item;
import ooad.project3.model.store.Clerk;
import ooad.project3.model.store.Store;

/**
 * Main simulation class for MusicLand.
 * This class orchestrates the entire simulation, including the daily loop,
 * clerk scheduling, and executing all daily actions.
 */
public class MusicLandSimulator {
    private static final Random rand = ThreadLocalRandom.current();

    private final StoreActions actions;
    private int today = 0;

    public MusicLandSimulator(Store store) {
        this.actions = new StoreActions(store);
        setupStore(this.actions);
    }

    /**
     * Initializes the store with proper works and items.
     * Impure
     */
    private void setupStore(StoreActions actions) {
        // Add staff
        actions.getStore().addStaff(new Clerk("Fred", 0.20));
        actions.getStore().addStaff(new Clerk("Ginger", 0.05));

        // Initialize inventory with 3 of each item type
        for (Class<? extends Item> itemType : ItemFactory.getAllItemTypes()) {
            var batch = Stream.generate(() -> actions.makeRandomItem(itemType, 0).build())
                    .limit(3)
                    .collect(Collectors.toList());
            batch.forEach(item -> actions.getStore().addItem(item));
        }

        System.out.println("Initial store inventory has been created.");
    }

    /**
     * Main simulation loop that runs for 30 days.
     */
    public void run() {
        actions.marchDay(null); // day zero is sunday, but we want the simulation
                                // to start on a monday.

        for (today = 1; today <= 30; today++) {
            System.out.println("\n========================================");
            System.out.printf("================ Day %-2d ================\n", today);
            System.out.println("========================================");

            // is it sunday?
            if (today % 7 == 0) {
                System.out.println("Store is closed on Sunday.");
                actions.marchDay(null); // null: no active clerk today
                continue;
            }

            var clerk = selectClerk();

            if (clerk == null) continue;

            actions.marchDay(clerk);
            runDailyActions();
        }

        printFinalSummary();
    }

    private Clerk selectClerk() {
        var clerks = actions.getAvailableClerks();

        if (clerks.size() == 0) {
            System.out.println("All clerks are overworked so the store can't open today.");
            return null;
        }

        return clerks.get(rand.nextInt(clerks.size()));
    }

    private void runDailyActions() {
        actions.arriveAtStore();
        if (!actions.checkRegister())
            actions.goToBank();
        actions.doInventory();
        actions.openTheStore();
        actions.cleanTheStore();
        actions.leaveTheStore();
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

        var store = actions.getStore();

        if (store.getInventory().isEmpty()) {
            System.out.println("No items left in inventory.");
        } else {
            store.getInventory().forEach(System.out::println);
        }

        System.out.printf("\nTotal value of remaining inventory (by purchase price): $%.2f\n", store.getInventoryValue());

        System.out.println("\n--- Items Sold ---");
        double totalSales = 0;

        if (store.getSoldItems().isEmpty()) {
            System.out.println("No items were sold during the simulation.");
        } else {
            for (Item item : store.getSoldItems()) {
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
