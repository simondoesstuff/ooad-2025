package ooad.project3.events.store;

import ooad.project3.model.store.Clerk;

public class CheckRegisterEvent extends StoreEvent {
    public CheckRegisterEvent(Clerk clerk) {
        super(clerk);
    }
}
