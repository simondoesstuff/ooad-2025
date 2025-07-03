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
import ooad.project4.model.Inventory;
import ooad.project4.model.customers.*;
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

        System.out.printf("%s: %s opens the store.\n", store.getName(), getName());

        int numBuyingCustomers = rand.nextInt(4, 11); // (4-10, exclusive upper)
        int numSellingCustomers = rand.nextInt(1, 5);

        int sales = 0;
        int purchases = 0;

        for (int i = 0; i < numBuyingCustomers + numSellingCustomers; i++) {
            final int custId = i + 1;
            if (handleBuyingCustomer(new RandomCustomer(custId))) sales++;
        }

        for (int i = 0; i < numSellingCustomers; i++) {
            final int custId = numBuyingCustomers + i + 1;
            if (handleSellingCustomer(new RandomCustomer(custId))) purchases++;
        }

        getBus().post(new OpenTheStoreEvent(this, numBuyingCustomers, numSellingCustomers, sales, purchases));
    }

    /**
     * a utiltiy method for openTheStore(). Handles bartering regarding customers
     * looking to buy.
     */
    public boolean handleBuyingCustomer(Customer customer) {
        verifyStoreExists();

        Item item = customer.getPurchaseInterest(store.getInventory());

        if (item == null) {
            System.out.printf(" - %s didn't see anything in the inventory of interest so they left.\n",
                    customer);
            return false;
        }

        if (!store.getInventory().contains(item)) {
            System.out.printf(" - %s tried to purchase an item that wasn't in the Inventory.\n",
                    customer);
            return false;
        }


        double listPrice = item.getListPrice();
        boolean sale = false;

        if (!trySell(customer, item, listPrice)) {
            System.out.printf(" - %s offered a %s to %s for $%.2f, but they hesitated.\n",
                    getName(), item.getClass().getSimpleName(), customer, listPrice);
            listPrice *= .9;

            if (!trySell(customer, item, listPrice)) {
                System.out.printf(" - %s would not buy the %s, even with a discount, and left.\n",
                        customer, item.getClass().getSimpleName());
            } else sale = true;
        } else sale = true;

        if (sale) {
            if (item instanceof Stringed) {
                System.out.printf(" - since the customer bought a Stringed instrument, perhaps they want extras?\n");
                trySell(customer, store.getInventory().getFirstLike(GigBag.class));
                trySell(customer, store.getInventory().getFirstLike(PracticeAmp.class));
                trySell(customer, store.getInventory().getFirstLike(Cable.class));
                trySell(customer, store.getInventory().getFirstLike(Strings.class));
            }
        }

        return sale;
    }

    private boolean trySell(Customer customer, Item item) {
        if (item == null) return false;
        return trySell(customer, item, item.getListPrice());
    }

    private boolean trySell(Customer customer, Item item, double offer) {
        if (!customer.acceptPurchase(item, offer)) return false;
        store.sellItem(item, offer, today);
        System.out.printf(" - %s sold a %s to %s for $%.2f.\n",
                getName(), item.getClass().getSimpleName(), customer, offer);
        return true;
    }

    /**
     * a utiltiy method for openTheStore(). Handles bartering regarding customers
     * looking to sell.
     */
    public boolean handleSellingCustomer(Customer customer) {
        verifyStoreExists();

        var item = customer.getSaleOffer();

        if (item == null) {
            System.out.printf(" - %s seemed like they wanted to cut a deal but got cold feet and left.\n",
                customer);
            return false;
        }

        var condition = item.getCondition();

        // Determine purchase price based on condition
        double offer = rand.nextDouble(1, 35); // Base random price
        double purchasePrice = switch (condition) {
            case Condition.POOR -> offer * 0.5;
            case Condition.FAIR -> offer * 0.7;
            case Condition.GOOD -> offer * 1.0;
            case Condition.VERY_GOOD -> offer * 1.2;
            case Condition.EXCELLENT ->  offer * 1.5;
            default -> offer;
        };

        var type = item.build().getClass();

        // dont buy clothes on ban
        if (store.isClothingBan() && Clothing.class.isAssignableFrom(type)) {
            System.out.printf(" - %s wanted to sell clothing, but was refused on principle.\n", customer);
            return false;
        }

        System.out.printf(" - %s wants to sell a %s condition used %s.\n", customer, condition, type.getSimpleName());

        if (!customer.acceptSale(item.purchasePrice(offer).build())) {
            offer *= 1.1;
            System.out.printf(" - %s didn't take the deal so a 10%% increase was offered.\n", customer);

            if (!customer.acceptSale(item.purchasePrice(offer).build())) {
                System.out.printf(" - %s still didn't take the deal and left insulted.\n", customer);
                return false;
            }
        }

        store.addItem(item
                .newOrUsed(false)
                .listPrice(item.getPurchasePrice() * 2)
                .dayArrived(today)
                .build());

        System.out.printf(" - %s bought a %s condition used %s from %s for $%.2f.\n",
                getName(), condition, type.getSimpleName(), customer, offer);

        return true;
    }

    private void tryBuy(Customer customer, Item item) {
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

        getBus().post(new CleanTheStoreEvent(this, damaged));

        for (var item : destroyed) {
            store.getInventory().remove(item);
            System.out.printf(" - the item was already in POOR condition and has now been destroyed.\n");
        }
    }

    public void leaveTheStore() {
        getBus().post(new LeaveTheStoreEvent(this));
    }
}
