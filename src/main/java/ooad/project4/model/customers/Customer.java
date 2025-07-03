package ooad.project4.model.customers;

import java.util.List;

import ooad.project4.model.item.BuildableItem;
import ooad.project4.model.item.Item;

public interface Customer {
    /**
     * Lets Customers choose to initiate item buying from Store.
     * @returns choice Item of otions, null on no choice
     */
    public Item getPurchaseInterest(List<Item> options);

    /**
     * Completes a Store to Customer, "purchase transaction"
     * @returns true on accepting of the Store's offer
     */
    public boolean acceptPurchase(Item item, double offer);

    /**
     * Lets Customers offer an Item for sale to the Store.
     * The Store will make an offer price in subsequent calls.
     * Return type is a Builder so that the Store can fill in
     * final details such as dayArrived upon purchase.
     * @returns Builder, for sale
     */
    public BuildableItem.Builder<?> getSaleOffer();

    /**
     * Completes a Customer to Store, "sale transaction".
     * The Store's offer is located in the purchase field
     * of the Item.
     * @returns true on accepting of the Store's offer
     */
    public boolean acceptSale(Item item);
}
