package ooad.project3.events.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import ooad.project3.model.Inventory;
import ooad.project3.model.store.Clerk;
import ooad.project3.model.store.Store;

@Data
@AllArgsConstructor
public class StoreEvent {
    private final Clerk clerk;

    public Store getStore() {
        return clerk.getStore();
    }

    public Inventory getInventory() {
        return getStore().getInventory();
    }

    public String getClerkName() {
        return clerk.getName();
    }
}
