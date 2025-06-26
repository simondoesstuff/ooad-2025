package ooad.project3.model;

/**
 * singleton to track total money withdrawn from the bank.
 */
public class Bank {
    private static Bank instance;
    private double totalWithdrawn;

    private Bank() {
        this.totalWithdrawn = 0.0;
    }

    public static Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    public void withdraw(double amount) {
        if (amount > 0) {
            this.totalWithdrawn += amount;
        }
    }

    public double getTotalWithdrawn() {
        return totalWithdrawn;
    }
}
