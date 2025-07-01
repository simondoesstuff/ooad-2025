package ooad.project4.events.store;

import ooad.project4.model.store.Clerk;

public class CheckRegisterEvent extends StoreEvent {
    public CheckRegisterEvent(Clerk clerk) {
        super(clerk);
    }
}
