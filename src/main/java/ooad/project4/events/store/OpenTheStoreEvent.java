package ooad.project4.events.store;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ooad.project4.model.store.Clerk;

@Data
@EqualsAndHashCode(callSuper = true)
public class OpenTheStoreEvent extends StoreEvent {
    private final int buyingCustomers;
    private final int sellingCustomers;
    private final int totalSales;
    private final int totalPurchases;

    public OpenTheStoreEvent(Clerk clerk,
                            int buyingCustomers,
                            int sellingCustomers,
                            int totalSales,
                            int totalPurchases) {
        super(clerk);
        this.buyingCustomers = buyingCustomers;
        this.sellingCustomers = sellingCustomers;
        this.totalSales = totalSales;
        this.totalPurchases = totalPurchases;
    }
}

