package ooad.project3.events.store;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ooad.project3.model.Inventory;
import ooad.project3.model.store.Clerk;

@Data
@EqualsAndHashCode(callSuper = true)
public class DoInventoryEvent extends StoreEvent {
    private final Inventory recentlyDamaged;
    private final boolean startedClothingBan;

    public DoInventoryEvent(Clerk clerk, Inventory recentlyDamaged, boolean startedClothingBan) {
        super(clerk);
        this.recentlyDamaged = recentlyDamaged;
        this.startedClothingBan = startedClothingBan;
    }
}
