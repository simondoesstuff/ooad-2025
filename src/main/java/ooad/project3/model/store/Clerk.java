package ooad.project3.model.store;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import ooad.project3.ItemFactory;
import ooad.project3.Utils;
import ooad.project3.model.Bank;
import ooad.project3.model.item.Condition;
import ooad.project3.model.Customer;
import ooad.project3.model.item.Item;
import ooad.project3.model.store.Clerk;

public class Clerk {
    private static final Random rand = ThreadLocalRandom.current();

    @Getter
    private String name;
    @Getter
    private final double cleaningDamageChance;
    @Getter
    private int workStreak = 0;
    @Getter
    private final Store store;
    @Getter @Setter
    private int today;

    public Clerk(String name, double cleaningDamageChance, Store store) {
        this.name = name;
        this.cleaningDamageChance = cleaningDamageChance;
        this.store = store;
    }

    public void increaseWorkStreak() {
        this.workStreak++;
    }

    public void resetWorkStreak() {
        this.workStreak = 0;
    }

    /**
     * Clerk may find items waiting that have been delivered from a prior day.
     * These items are put into the inventory of merchandise items for the store
     */
    public void arriveAtStore() {
        System.out.printf("%s arrives at the store.\n", getName());

        var orders = store.getReadyOrders(today);
        store.getOrders().removeAll(orders);

        if (orders.size() != 0) System.out.println(" - new deliveries!");

        for (var order : orders) {
            for (Item item : order.getItems()) {
                store.addItem(item);
                System.out.printf(" - added delivered item to inventory: %s\n", item.getName());
            }
        }
    }

    /**
     * Checks the amount of money in the register.
     * Announces the amount of money currently in the register.
     * @returns true if there is "sufficient" money in the register
     */
    public boolean checkRegister() {
        var cash = store.getCashRegister().getCash();
        System.out.printf("%s counts the register. Current cash: $%.2f\n", getName(), cash);
        return cash >= 75;
    }

    /**
     * go to the bank, withdraw $1000, and put the money in the store register
     */
    public void goToBank() {
        System.out.printf("Cash is low. %s is going to the bank.\n", getName());
        double withdrawalAmount = 1000d;
        Bank.getInstance().withdraw(withdrawalAmount);
        store.getCashRegister().add(withdrawalAmount);
        System.out.printf(" - withdrew $%.2f from the bank. New register total: $%.2f\n",
                withdrawalAmount, store.getCashRegister().getCash());
    }

    /**
     * any of the item subclasses has a count of 0 (for instance, there are 0 CD player
     * items in inventory), the Clerk perform the PlaceAnOrder Action for
     * each missing item type
     */
    public void doInventory() {
        System.out.printf("%s is doing inventory. Total purchase value of items: $%.2f\n",
                getName(), store.getInventoryValue());

        var types = store.getAvailableItemTypes();

        // Check for out-of-stock
        for (Class<? extends Item> itemType : ItemFactory.getAllItemTypes()) {
            if (!types.contains(itemType)) {
                System.out.printf(" - inventory missing: %s.\n", itemType.getSimpleName());
                placeAnOrder(itemType);
            }
        }
    }

    /**
     * Clerk will order 3 of those items, each with a random purchase price and other randomly
     * determined characteristics (similar to adding items on Day 0). The purchase price of each
     * of these items should be paid for by removing the funds from the Cash Register. These items
     * should arrive at the store in the next 1 to 3 days
     */
    public void placeAnOrder(Class<? extends Item> itemType) {
        System.out.printf("%s is considering ordering %s\n",
                getName(), itemType.getSimpleName());

        var deliveryTime = rand.nextInt(1, 4); // 1-3 days, exclusive bound
        var itemQueue = new ArrayList<Item>();

        for (int i = 0; i < 3; i++) {
            var item = store.makeRandomItem(itemType, today + deliveryTime).build();
            var price = item.getPurchasePrice();

            if (!store.getCashRegister().withdraw(price)) {
                System.out.printf(" - failed to order %d %s(s) because there was not enough cash in the register.\n",
                        (3-i), itemType.getSimpleName());
                break;
            }

            itemQueue.add(item);
            System.out.printf(" - added a %s to the order for $%.2f.\n",
                    itemType.getSimpleName(), price);
        }

        var order = new Order(itemQueue, today + deliveryTime);
        store.addOrder(order);
        System.out.printf(" - placed order, to arrive on day: %s\n",
                    today + deliveryTime);
    }

    /**
     * Clerk will now respond to arriving customers. Customers will come
     * in to either buy or sell a single item
     */
    public void openTheStore() {
        System.out.printf("%s opens the store.\n", getName());

        int numBuyingCustomers = rand.nextInt(4, 11); // (4-10, exclusive upper)
        int numSellingCustomers = rand.nextInt(1, 5);

        System.out.printf(" - there are %d buying and %d selling customers today.\n", numBuyingCustomers, numSellingCustomers);

        // Handle customers in a random order
        List<Runnable> customerActions = new ArrayList<>();

        for (int i = 0; i < numBuyingCustomers; i++) {
            final int custId = i + 1;
            customerActions.add(() -> handleBuyingCustomer(new Customer(custId)));
        }

        for (int i = 0; i < numSellingCustomers; i++) {
            final int custId = numBuyingCustomers + i + 1;
            customerActions.add(() -> handleSellingCustomer(new Customer(custId)));
        }

        Collections.shuffle(customerActions);
        customerActions.forEach(Runnable::run);
    }

    /**
     * a utiltiy method for openTheStore(). Handles bartering regarding customers
     * looking to buy.
     */
    private void handleBuyingCustomer(Customer customer) {
        Class<? extends Item> desiredType = ItemFactory.getRandomItemType();

        // matching items to desiredType
        var available = store.getInventory().stream()
                .filter(i -> i.getClass().equals(desiredType))
                .collect(Collectors.toList());

        if (available.isEmpty()) {
            System.out.printf(" - %s wanted to buy a %s but none were in inventory, so they left.\n",
                    customer, desiredType.getSimpleName());
            return;
        }

        Item itemToBuy = available.get(rand.nextInt(available.size()));
        double listPrice = itemToBuy.getListPrice();
        boolean purchased = false;
        double finalPrice = 0;

        if (rand.nextDouble() < 0.50) { // 50% chance to buy at list price
            purchased = true;
            finalPrice = listPrice;
            System.out.printf(" - %s sold a %s to %s for $%.2f.\n",
                    getName(), itemToBuy.getClass().getSimpleName(), customer, finalPrice);
        } else {
            System.out.printf(" - %s offered a %s to %s for $%.2f, but they hesitated.\n",
                    getName(), itemToBuy.getClass().getSimpleName(), customer, listPrice);
            double discountPrice = listPrice * 0.90;

            if (rand.nextDouble() < 0.75) { // 75% chance to buy with 10% discount
                purchased = true;
                finalPrice = discountPrice;
                System.out.printf(" - %s sold a %s to %s for $%.2f after a 10%% discount.\n",
                        getName(), itemToBuy.getClass().getSimpleName(), customer, finalPrice);
            } else {
                System.out.printf(" - %s would not buy the %s, even with a discount, and left.\n",
                        customer, itemToBuy.getClass().getSimpleName());
            }
        }

        if (purchased) {
            store.sellItem(itemToBuy, finalPrice, today);
        }
    }

    /**
     * a utiltiy method for openTheStore(). Handles bartering regarding customers
     * looking to sell.
     */
    private void handleSellingCustomer(Customer customer) {
        // TODO: intelligent clerk price offers
        double baseOffer = rand.nextDouble(1, 35); // Base random price

        // Determine purchase price based on condition
        Condition condition = Utils.getRandomEnum(Condition.class);
        double purchasePrice = switch (condition) {
            case Condition.POOR -> baseOffer * 0.5;
            case Condition.FAIR -> baseOffer * 0.7;
            case Condition.GOOD -> baseOffer * 1.0;
            case Condition.VERY_GOOD -> baseOffer * 1.2;
            case Condition.EXCELLENT ->  baseOffer * 1.5;
            default -> baseOffer;
        };

        Class<? extends Item> itemTypeToSell = ItemFactory.getRandomItemType();
        boolean bought = false;
        double finalPrice = 0;

        System.out.printf(" - %s wants to sell a %s condition used %s.\n", customer, condition, itemTypeToSell.getSimpleName());

        if (rand.nextDouble() < 0.50) { // 50% chance to accept initial offer
            bought = true;
            finalPrice = purchasePrice;
        } else {
            double increasedOffer = purchasePrice * 1.10;
            System.out.printf(" - %s rejected the initial offer of $%.2f. %s offered 10%% more: $%.2f.\n",
                    customer, purchasePrice, getName(), increasedOffer);

            if (rand.nextDouble() < 0.75) { // 75% chance to accept new offer
                bought = true;
                finalPrice = increasedOffer;
            }
        }

        if (bought) {
            if (store.getCashRegister().withdraw(finalPrice)) {
                // Override random values with determined ones
                Item itemToSell = ItemFactory.buildRandom(itemTypeToSell)
                        .purchasePrice(finalPrice)
                        .listPrice(finalPrice * 2)
                        .newOrUsed(false)
                        .dayArrived(today)
                        .condition(condition)
                        .build();

                store.addItem(itemToSell);

                System.out.printf(" - %s bought a %s condition used %s from %s for $%.2f.\n",
                        getName(), condition, itemTypeToSell.getSimpleName(), customer, finalPrice);
            } else {
                System.out.printf(" - %s accepted the offer, but the store did not have enough cash to purchase the %s.\n",
                        customer, itemTypeToSell.getSimpleName());
            }
        } else {
            System.out.printf(" - %s decided not to sell their %s and left.\n", customer, itemTypeToSell.getSimpleName());
        }
    }

    public void cleanTheStore() {
        System.out.printf("%s is cleaning the store.\n", getName());

        if (rand.nextDouble() < getCleaningDamageChance()) {
            if (store.getInventory().isEmpty()) {
                System.out.println(" - no items in inventory to damage.");
                return;
            }

            Item itemToDamage = store.getInventory().get(rand.nextInt(store.getInventory().size()));
            System.out.printf(" - Oh no! %s damaged an item while cleaning: %s.\n", getName(), itemToDamage.getName());

            Condition originalCondition = itemToDamage.getCondition();
            boolean destroyed = itemToDamage.damage();

            if (destroyed) {
                store.getInventory().remove(itemToDamage);
                System.out.printf(" - the item was already in POOR condition and has now been destroyed.\n");
            } else {
                System.out.printf(" - the item's condition was lowered from %s to %s and its list price was reduced by 20%%.\n",
                        originalCondition, itemToDamage.getCondition());
            }
        }
    }

    public void leaveTheStore() {
        System.out.printf("%s locks up the store and goes home.\n", getName());
    }
}
