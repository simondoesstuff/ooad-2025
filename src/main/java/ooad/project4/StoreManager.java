package ooad.project4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;
import ooad.project4.model.Bank;
import ooad.project4.model.item.BuildableItem;
import ooad.project4.model.item.SoldItem;
import ooad.project4.model.store.Clerk;
import ooad.project4.model.store.Store;

/**
 * Associates Clerks with Stores and manages the logic associated
 * with them overworking or getting sick.
 */
public class StoreManager {
    private static final Random rand = ThreadLocalRandom.current();

    private final Store store;
    private final ClerkPool pool;
    private int today = 0;
    @Getter
    private Clerk activeClerk;

    public StoreManager(Store store, ClerkPool pool) {
        this.store = store;
        this.pool = pool;
    }

    /**
     * Decides on a Clerk to put on duty and performs their daily actions
     */
    public void runDailyActions(int today, DayLogger log) {
        this.today = today;

        // assign an initial clerk
        if (activeClerk == null) assignClerk();

        // 1. overworked clerks rest
        if (activeClerk.getWorkStreak() >= 3) {
            pool.makeAssignable(activeClerk);
            assignClerk();
        }

        // 3. working clerks do daily actions
        activeClerk.arriveAtStore();
        if (!activeClerk.checkRegister())
            activeClerk.goToBank();
        activeClerk.doInventory();
        activeClerk.openTheStore();
        activeClerk.cleanTheStore();
        activeClerk.leaveTheStore();
    }

    /**
     * Gets a Clerk from the ClerkPool & assigns them to the Store
     */
    private void assignClerk() {
        activeClerk = pool.assign();
        activeClerk.setStore(store);
    }

    /**
     * Summary of the final items in the inventory,
     * the total items sold, and the financial summary.
     */
    public void printSummary() {
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
        var acc = Bank.getInstance().getAccount(store.getName());
        System.out.printf("Total money withdrawn from the Bank: $%.2f\n", acc.getWithdrawn());
    }
}
