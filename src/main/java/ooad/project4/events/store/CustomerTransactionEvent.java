package ooad.project4.events.store;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ooad.project4.model.Inventory;
import ooad.project4.model.store.Clerk;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerTransactionEvent extends StoreEvent {
    private final Inventory recentlyPurchased;
    private final Inventory recentlySold;

    public CustomerTransactionEvent(Clerk clerk, Inventory recentlyPurchased, Inventory recentlySold) {
        super(clerk);
        this.recentlyPurchased = recentlyPurchased;
        this.recentlySold = recentlySold;
    }
}


