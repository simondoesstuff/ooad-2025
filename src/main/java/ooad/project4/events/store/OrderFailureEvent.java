package ooad.project4.events.store;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ooad.project4.model.item.Item;
import ooad.project4.model.store.Clerk;
import ooad.project4.model.store.Order;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderFailureEvent extends StoreEvent {
    private final Class<? extends Item> type;
    private final int failQuantity;

    public OrderFailureEvent(Clerk clerk, Class<? extends Item> type, int failQuantity) {
        super(clerk);
        this.type = type;
        this.failQuantity = failQuantity;
    }
}


