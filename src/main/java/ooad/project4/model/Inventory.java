package ooad.project4.model;

import java.util.ArrayList;

import ooad.project4.model.item.Item;

public class Inventory extends ArrayList<Item> {
    public Inventory() {
        super();
    }

    public double getTotalPurchasePrice() {
        return this.stream().mapToDouble(Item::getPurchasePrice).sum();
    }

    /**
     * @returns first Item of matching class type
     */
    public Item getFirstLike(Class<? extends Item> type) {
        return stream()
            .filter(x -> type.isAssignableFrom(x.getClass()))
            .findFirst()
            .orElse(null);
    }
}
