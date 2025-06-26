package ooad.project3.events.store;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ooad.project3.model.store.Clerk;
import ooad.project3.model.store.Order;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArriveAtStoreEvent extends StoreEvent {
     private final List<Order> recentlyDelivered;

     public ArriveAtStoreEvent(Clerk clerk, List<Order> recentlyDelivered) {
          super(clerk);
          this.recentlyDelivered = recentlyDelivered;
     }
}
