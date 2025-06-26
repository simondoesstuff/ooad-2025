package ooad.project3.model.store;

import ooad.project3.ItemFactory;
import ooad.project3.model.item.Item;
import ooad.project3.model.Inventory;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Encapsulates basic Store utilties and maintains an Inventory set of Items
 * and a set of sold items. Tracks a list of pending orders and a cash register.
 */
public class Store {
    @Getter
    private final Inventory inventory = new Inventory();
    @Getter
    private final ArrayList<Item> soldItems = new ArrayList<>();
    @Getter
    private final CashRegister cashRegister = new CashRegister();
    @Getter
    private final ArrayList<Order> orders = new ArrayList<>();

    public Store() {}

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    /**
     * Marks the item as sold, removes it from the inventory,
     * and adds the item's salePrice to the register.
     */
    public void sellItem(Item item, double salePrice, int daySold) {
        item.markSold(salePrice, daySold);
        inventory.remove(item);
        soldItems.add(item);
        cashRegister.add(salePrice);
    }

    /**
     * Filters for orders that have arrived
     */
    public List<Order> getReadyOrders(int today) {
        return this.getOrders().stream()
             .filter(order -> order.getArrivalDay() <= today)
             .collect(Collectors.toList());
    }

    /**
     * the set of available item types
     */
    public Set<Class<? extends Item>> getAvailableItemTypes() {
        return this.getInventory().stream()
                .map(item -> item.getClass())
                .distinct()
                .collect(Collectors.toSet());
    }

    /**
     * Create a random set of items with a reasonable purchase and list price.
     * Specifies newOrUsed as new.
     * As items are created, their purchase price is withdrawn from the register.
     * If the register cannot sustain the cost, the already purchased items are returned
     * and the process finishes with a failure announcement.
     */
    public Item.Builder<?> makeRandomItem(Class<? extends Item> itemType, int arrivalDay) {
        var price = ThreadLocalRandom.current().nextDouble(1, 50);
        return ItemFactory.buildRandom(itemType)
                .purchasePrice(price)  // TODO: more intelligent prices
                .listPrice(price * 2)
                .newOrUsed(true)
                .dayArrived(arrivalDay);
    }
}
