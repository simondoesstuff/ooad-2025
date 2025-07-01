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
}
