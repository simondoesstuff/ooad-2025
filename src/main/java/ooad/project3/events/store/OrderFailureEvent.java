package ooad.project3.events.store;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ooad.project3.model.item.Item;
import ooad.project3.model.store.Clerk;
import ooad.project3.model.store.Order;

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


