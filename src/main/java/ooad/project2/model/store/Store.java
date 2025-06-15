package ooad.project2.model.store;

import ooad.project2.model.item.Item;

import java.util.ArrayList;
import java.util.List;

// INFO: --> OO Term:  Polymorphism
//  The store maintains the inventory List<Item> and operates on it without
//  knowing anything about a specific subtype, yet remains compatible.

/**
 * Encapsulates basic Store utilties and maintains an Inventory set of Items
 * and a set of sold items. Tracks a list of pending orders and a cash register.
 */
public class Store {
    private final List<Item> inventory;
    private final List<Item> soldItems;
    private final CashRegister cashRegister;
    private final List<Clerk> staff;
    private final List<Order> pendingOrders;

    public Store() {
        this.inventory = new ArrayList<>();
        this.soldItems = new ArrayList<>();
        this.cashRegister = new CashRegister();
        this.staff = new ArrayList<>();
        this.pendingOrders = new ArrayList<>();
    }

    public void addStaff(Clerk clerk) {
        staff.add(clerk);
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void addOrder(Order order) {
        pendingOrders.add(order);
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
     * Get the total inventory sum purchasePrice
     */
    public double getInventoryValue() {
        return inventory.stream().mapToDouble(Item::getPurchasePrice).sum();
    }

    // Getters
    public List<Item> getInventory() { return inventory; }
    public List<Item> getSoldItems() { return soldItems; }
    public CashRegister getCashRegister() { return cashRegister; }
    public List<Clerk> getStaff() { return staff; }
    public List<Order> getPendingOrders() { return pendingOrders; }
}
