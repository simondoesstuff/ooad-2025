package ooad.project4.events.store;

import ooad.project4.model.store.Clerk;

public class LeaveTheStoreEvent extends StoreEvent {
    public LeaveTheStoreEvent(Clerk clerk) {
        super(clerk);
    }
}
