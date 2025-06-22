package ooad.project3.model;

/**
 * Represents a customer who can buy or sell items.
 */
public class Customer {
    private final int id;

    public Customer(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Customer " + id;
    }
}
