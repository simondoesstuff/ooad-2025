package ooad.project3.events.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ooad.project3.model.Inventory;
import ooad.project3.model.store.Clerk;

@Data
@EqualsAndHashCode(callSuper = true)
public class CleanTheStoreEvent extends StoreEvent {
    private final Inventory recentlyDamaged;

    public CleanTheStoreEvent(Clerk clerk, Inventory recentlyDamaged) {
        super(clerk);
        this.recentlyDamaged = recentlyDamaged;
    }
}

