package ooad.project3.events.store;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ooad.project3.model.store.Clerk;
import ooad.project3.model.store.Order;

@Data
@EqualsAndHashCode(callSuper = true)
public class PlaceAnOrderEvent extends StoreEvent {
    private final Order order;

    public PlaceAnOrderEvent(Clerk clerk, Order order) {
        super(clerk);
        this.order = order;
    }
}

