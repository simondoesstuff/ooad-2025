package ooad.project2.model.store;

/**
 * prevents cash from becoming negative
 */
public class CashRegister {
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
