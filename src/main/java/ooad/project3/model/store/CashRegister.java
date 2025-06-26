package ooad.project3.model.store;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * prevents cash from becoming negative
 */
public class CashRegister {
    @Getter
    private double cash;

    public CashRegister() {
        this.cash = 0d;
    }

    public void add(double amount) {
        if (amount > 0) {
            this.cash += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && this.cash >= amount) {
            this.cash -= amount;
            return true;
        }

        return false;
    }

    public double getCash() {
        return cash;
    }
}
