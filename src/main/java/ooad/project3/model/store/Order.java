package ooad.project3.model.store;

import ooad.project3.model.item.Item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a pending order of items that will arrive at the store on a future day.
 */
public class Order {
    private final int arrivalDay;
    private final List<Item> items;
    private final Set<Class<? extends Item>> itemTypes;

    public Order(List<Item> items, int arrivalDay) {
        this.items = items;
        this.arrivalDay = arrivalDay;

        // set of classes associated with this.items
        itemTypes = new HashSet<Class<? extends Item>>(
            items.stream()
            .map(x -> x.getClass())
            .toList());
    }

    public List<Item> getItems() {
        return items;
    }

    /**
     * @return true, if the order contains an item of the same class (ignoring field details)
     */
    public boolean hasLike(Item item) {
        var base = item.getClass();
        return itemTypes.contains(base);
    }

    public int getArrivalDay() {
        return arrivalDay;
    }
}
