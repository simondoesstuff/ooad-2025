package ooad.project2.model;

// INFO: --> OO Term:  Cohesion
//  All methods & attributes of Bank cohere to the function of the Bank.
//  Also, the existence of Bank separates Bank related functions from other
//  classes which allows them to have better cohesion themselves.

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
