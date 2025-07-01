package ooad.project4.events.store;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ooad.project4.model.store.Clerk;

@Data
@EqualsAndHashCode(callSuper = true)
public class GoToBankEvent extends StoreEvent {
    private final double oldAmntInRegister;
    private final double newAmntInRegister;

    public GoToBankEvent(Clerk clerk, double oldAmntInRegister, double newAmntInRegister) {
        super(clerk);
        this.oldAmntInRegister = oldAmntInRegister;
        this.newAmntInRegister = newAmntInRegister;
    }
}
