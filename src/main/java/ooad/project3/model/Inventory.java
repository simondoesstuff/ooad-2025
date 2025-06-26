package ooad.project3.model;

import java.util.ArrayList;

import ooad.project3.model.item.Item;

public class Inventory extends ArrayList<Item> {
    public double getTotalPurchasePrice() {
        return this.stream().mapToDouble(Item::getPurchasePrice).sum();
    }
}
