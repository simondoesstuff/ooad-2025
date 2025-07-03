package ooad.project4.model.store;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import ooad.project4.ItemFactory;
import ooad.project4.Utils;
import ooad.project4.Utils.BoolRet;
import ooad.project4.Utils.IntRet;
import ooad.project4.events.TheEventBus;
import ooad.project4.events.store.ArriveAtStoreEvent;
import ooad.project4.events.store.CheckRegisterEvent;
import ooad.project4.events.store.CleanTheStoreEvent;
import ooad.project4.events.store.DoInventoryEvent;
import ooad.project4.events.store.GoToBankEvent;
import ooad.project4.events.store.LeaveTheStoreEvent;
import ooad.project4.events.store.OpenTheStoreEvent;
import ooad.project4.events.store.OrderFailureEvent;
import ooad.project4.events.store.PlaceAnOrderEvent;
import ooad.project4.model.Bank;
import ooad.project4.model.item.Condition;
import ooad.project4.model.item.BuildableItem;
import ooad.project4.model.item.Item;
import ooad.project4.model.Customer;
import ooad.project4.model.Inventory;
import ooad.project4.model.item.BuildableItem;
import ooad.project4.model.item.clothing.Clothing;
import ooad.project4.model.item.music.accessories.Cable;
import ooad.project4.model.item.music.accessories.GigBag;
import ooad.project4.model.item.music.accessories.PracticeAmp;
import ooad.project4.model.item.music.accessories.Strings;
import ooad.project4.model.item.music.instruments.stringed.Stringed;
import ooad.project4.model.item.music.instruments.wind.Wind;
import ooad.project4.model.item.music.players.Players;
import ooad.project4.model.store.Clerk;
import ooad.project4.model.store.tuning.Tuner;
import ooad.project4.StoreNotAssignedException;
import com.google.common.eventbus.EventBus;

/**
 * A "Clerk" is responsible for mutating the Store based on a set of actions.
 * The Clerk requires knowledge of which Store they are assigned, as well as a
 * a few other attributes necessary to their behavior.
 */
public class Clerk {
    private static final Random rand = ThreadLocalRandom.current();

    @Getter
    private final double cleaningDamageChance;
    @Getter
    private String name;
    @Getter
    private int workStreak = 0;
    @Getter @Setter
    private Store store;
    @Getter @Setter
    private int today;
    @Getter @Setter
    private boolean sick;

    private final Tuner tuner;

    public Clerk(String name, double cleaningDamageChance, Tuner tuner) {
        this.name = name;
        this.cleaningDamageChance = cleaningDamageChance;
        this.tuner = tuner;
    }

    public void increaseWorkStreak() {
        this.workStreak++;
    }

    public void resetWorkStreak() {
        this.workStreak = 0;
    }

    private EventBus getBus() {
        return TheEventBus.getInstance().getBus();
    }

    private void verifyStoreExists() {
        if (store == null) throw new StoreNotAssignedException();
    }

    /**
     * Clerk may find items waiting that have been delivered from a prior day.
     * These items are put into the inventory of merchandise items for the store
     */
    public void arriveAtStore() {
        verifyStoreExists();

        var orders = store.getReadyOrders(today);
        getBus().post(new ArriveAtStoreEvent(this, orders));
        store.getOrders().removeAll(orders);

        for (var order : orders) {
            for (Item item : order.getItems()) {
                store.addItem(item);
            }
        }
    }

    /**
     * Checks the amount of money in the register.
     * Announces the amount of money currently in the register.
     * @returns true if there is "sufficient" money in the register
     */
    public boolean checkRegister() {
        verifyStoreExists();

        var cash = store.getCashRegister().getCash();
        getBus().post(new CheckRegisterEvent(this));
        return cash >= 75;
    }

    /**
     * go to the bank, withdraw $1000, and put the money in the store register
     */
    public void goToBank() {
        verifyStoreExists();

        double oldCash = store.getCashRegister().getCash();
        double withdrawalAmount = 1000d;
        Bank.getInstance().getAccount(store.getName()).withdraw(withdrawalAmount);
        store.getCashRegister().add(withdrawalAmount);
        double newCash = store.getCashRegister().getCash();
        getBus().post(new GoToBankEvent(this, oldCash, newCash));
    }

    /**
     * any of the item subclasses has a count of 0 (for instance, there are 0 CD player
     * items in inventory), the Clerk perform the PlaceAnOrder Action for
     * each missing item type
     */
    public void doInventory() {
        verifyStoreExists();
        var startedClothingBan = store.tryClothingBan();

        var types = store.getAvailableItemTypes();
        Inventory itemsDamaged = new Inventory();
        Inventory itemsDestroyed = new Inventory();

        for (var item : store.getInventory()) {
            if (tuner.tryTune(item)) { // if damaged
                itemsDamaged.add(item);

                if (item.damage()) { // if destroyed
                    itemsDestroyed.add(item);
                }
            }
        }

        for (var item : itemsDestroyed) {
            store.getInventory().remove(item);
        }

        // Check for out-of-stock
        for (Class<? extends BuildableItem> itemType : ItemFactory.getAllItemTypes()) {
            // dont buy clothes on ban
            if (store.isClothingBan()) {
                if (Clothing.class.isAssignableFrom(itemType)) continue;
            }

            if (!types.contains(itemType)) {
                placeAnOrder(itemType);
            }
        }

        getBus().post(new DoInventoryEvent(this, itemsDamaged, startedClothingBan));
    }

    /**
     * Clerk will order 3 of those items, each with a random purchase price and other randomly
     * determined characteristics (similar to adding items on Day 0). The purchase price of each
     * of these items should be paid for by removing the funds from the Cash Register. These items
     * should arrive at the store in the next 1 to 3 days
     */
    public void placeAnOrder(Class<? extends BuildableItem> itemType) {
        verifyStoreExists();

        var deliveryTime = rand.nextInt(1, 4); // 1-3 days, exclusive bound
        var itemQueue = new ArrayList<Item>();

        for (int i = 0; i < 3; i++) {
            var item = store.makeRandomItem(itemType, today + deliveryTime).build();
            var price = item.getPurchasePrice();

            if (!store.getCashRegister().withdraw(price)) {
                getBus().post(new OrderFailureEvent(this, itemType, 3-i));
                break;
            }

            itemQueue.add(item);
        }

        var order = new Order(itemQueue, today + deliveryTime);
        store.addOrder(order);
        getBus().post(new PlaceAnOrderEvent(this, order));
    }

    /**
     * Clerk will now respond to arriving customers. Customers will come
     * in to either buy or sell a single item
     */
    public void openTheStore() {
        verifyStoreExists();

        int numBuyingCustomers = rand.nextInt(4, 11); // (4-10, exclusive upper)
        int numSellingCustomers = rand.nextInt(1, 5);

        int sales = 0;
        int purchases = 0;

        for (int i = 0; i < numBuyingCustomers + numSellingCustomers; i++) {
            final int custId = i + 1;
            if (handleBuyingCustomer(new Customer(custId))) sales++;
        }

        for (int i = 0; i < numSellingCustomers; i++) {
            final int custId = numBuyingCustomers + i + 1;
            if (handleSellingCustomer(new Customer(custId))) purchases++;
        }

        getBus().post(new OpenTheStoreEvent(this, numBuyingCustomers, numSellingCustomers, sales, purchases));
    }

    /**
     * a utiltiy method for openTheStore(). Handles bartering regarding customers
     * looking to buy.
     */
    public boolean handleBuyingCustomer(Customer customer) {
        verifyStoreExists();

        Class<? extends BuildableItem> desiredType = ItemFactory.getRandomItemType();

        // matching items to desiredType
        var available = store.getInventory().stream()
                .filter(i -> i.getClass().equals(desiredType))
                .collect(Collectors.toList());

        if (available.isEmpty()) {
            System.out.printf(" - %s wanted to buy a %s but none were in inventory, so they left.\n",
                    customer, desiredType.getSimpleName());
            return false;
        }

        Item itemToBuy = available.get(rand.nextInt(available.size()));
        double allure = 0;

        // certain well kept items are more likely to be bought
        switch (itemToBuy) {
            case Players i:
                if (i.isEqualized()) allure += .10;
                break;
            case Stringed i:
                if (i.isTuned()) allure += .15;
                break;
            case Wind i:
                if (i.isAdjusted()) allure += .20;
                break;
            default:
                break;
        }

        double listPrice = itemToBuy.getListPrice();
        boolean purchased = false;
        double finalPrice = 0;

        if (rand.nextDouble() < 0.50 + allure) { // 50% chance to buy at list price
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
            trySellExtras(itemToBuy);
        }

        return purchased;
    }

    private void trySellExtras(Item item) {
        if (!(item instanceof Stringed)) return;

        double offset = 0;

        if (!((Stringed) item).isElectric()) offset -= 10;

        var rand = ThreadLocalRandom.current();

        if (rand.nextDouble(1) <= .2 + offset) {
            System.out.printf("- sold a GigBag in a bundle!\n");
            trySell(GigBag.class);
        }

        if (rand.nextDouble(1) <= .25 + offset) {
            System.out.printf("- sold a PracticeAmp in a bundle!\n");
            trySell(PracticeAmp.class);
        }

        if (rand.nextDouble(1) <= .3 + offset) {
            for (int i = 0; i < rand.nextInt(2); i++) {
                System.out.printf("- sold some Cable in a bundle!\n");
                trySell(Cable.class);
            }
        }

        if (rand.nextDouble(1) <= .4 + offset) {
            for (int i = 0; i < rand.nextInt(1, 4); i++) {
                System.out.printf("- sold some Strings in a bundle!\n");
                trySell(Strings.class);
            }
        }
    }

    // TODO: announce
    private boolean trySell(Class<? extends Item> desiredType) {
        // matching items to desiredType
        var available = store.getInventory().stream()
                .filter(i -> i.getClass().equals(desiredType))
                .collect(Collectors.toList());

        if (available.isEmpty()) {
            return false;
        }

        var choice = available.get(rand.nextInt(available.size()));
        store.sellItem(choice, choice.getPurchasePrice(), today);
        return true;
    }

    /**
     * a utiltiy method for openTheStore(). Handles bartering regarding customers
     * looking to sell.
     */
    public boolean handleSellingCustomer(Customer customer) {
        verifyStoreExists();

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

        Class<? extends BuildableItem> itemTypeToSell = ItemFactory.getRandomItemType();

        // dont buy clothes on ban
        if (store.isClothingBan() && Clothing.class.isAssignableFrom(itemTypeToSell)) {
            System.out.printf(" - %s wanted to sell clothing, but was refused on principle.\n", customer);
            return false;
        }

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

        return bought;
    }

    public void cleanTheStore() {
        Inventory damaged = new Inventory();
        Inventory destroyed = new Inventory();

        for (var item : store.getInventory()) {
            if (store.getInventory().isEmpty()) {
                break;
            }

            if (rand.nextDouble() < getCleaningDamageChance()) {
                damaged.add(item);

                if (item.damage()) {
                    destroyed.add(item);
                }
            }
        }

        for (var item : destroyed) {
            store.getInventory().remove(item);
            System.out.printf(" - the item was already in POOR condition and has now been destroyed.\n");
        }

        getBus().post(new CleanTheStoreEvent(this, damaged));
    }

    public void leaveTheStore() {
        getBus().post(new LeaveTheStoreEvent(this));
    }
}
