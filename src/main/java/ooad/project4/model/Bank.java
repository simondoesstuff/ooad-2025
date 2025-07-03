package ooad.project4.model;

import java.util.HashMap;

import lombok.Getter;

/**
 * singleton to track total money withdrawn from the bank.
 */
public class Bank {
    private static Bank instance;
    private HashMap<String, Account> accounts = new HashMap<>();

    private Bank() {}

    /**
     * always returns
     */
    public Account getAccount(String holder) {
        if (!accounts.containsKey(holder)) {
            accounts.put(holder, new Account());
        }

        return accounts.get(holder);
    }

    synchronized
    public static Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    // ---------------------
    //   Sub class
    // ---------------------

    public static class Account {
        @Getter
        private double withdrawn = 0;

        public void withdraw(double amount) {
            if (amount > 0) {
                withdrawn += amount;
            }
        }
    }
}
