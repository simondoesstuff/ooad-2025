package ooad.project3.events.store;

import ooad.project3.model.store.Clerk;

public class LeaveTheStoreEvent extends StoreEvent {
    public LeaveTheStoreEvent(Clerk clerk) {
        super(clerk);
    }
}
