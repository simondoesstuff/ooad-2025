package ooad.project3.model.item;

// INFO: --> OO Term:  Inheritance
//  classes in the items package follow an inheritance hierarchy.

// INFO: --> OO Term:  Identity
//  as Item in the hierarchy are instantiated, each has a unique
//  identity even if it has identical attributes. This lets
//  items be stored or sold as individuals despite potentially
//  having identical state.

/**
 * Base class for the item inheritance hierarchy.
 * Represents an item that can be bought/sold at a Store.
 */
public abstract class Item {
    protected final String name;
    protected final double purchasePrice;
    protected double listPrice; // decreases due to damage
    protected final boolean newOrUsed;
    protected final int dayArrived;
    protected Condition condition; // decreases due to damage
    protected double salePrice; // updates on sale
    protected int daySold; // updates on sale

    protected Item(Builder<?> builder) {
        this.name = builder.name;
        this.purchasePrice = builder.purchasePrice;
        this.listPrice = builder.listPrice;
        this.newOrUsed = builder.newOrUsed;
        this.dayArrived = builder.dayArrived;
        this.condition = builder.condition;
    }

    public String getName() { return name; }
    public double getPurchasePrice() { return purchasePrice; }
    public double getListPrice() { return listPrice; }
    public double getSalePrice() { return salePrice; }
    public int getDayArrived() { return dayArrived; }
    public int getDaySold() { return daySold; }
    public Condition getCondition() { return condition; }

    /**
     * combined setter for salePrice & daySold
     */
    public void markSold(double salePrice, int daySold) {
        this.salePrice = salePrice;
        this.daySold = daySold;
    }

    /**
     * lowers condition by 1 level
     * @returns true if the item was at POOR which indicates
     * it should be now considered destroyed
     */
    public boolean damage() {
        if (this.condition == Condition.POOR) {
            return true;
        }

        this.condition = Condition.values()[this.condition.ordinal() - 1];
        this.listPrice *= 0.80;
        return false;
    }

    @Override
    public String toString() {
        return String.format("%-25s | Condition: %-10s | Purchase Price: $%.2f",
                name, condition, purchasePrice);
    }

    // ------------------------
    //      Builder
    // ------------------------

    /**
     * The base Builder class. Uses a self-referential generic type (CRTP)
     * to allow for a fluent interface in subclass Builders.
     * Part of an overall delegate builder pattern.
     * INFO: see https://www.baeldung.com/java-builder-pattern-inheritance
     *
     * @param <T> The type of the concrete Builder subclass.
     */
    public abstract static class Builder<T extends Builder<T>> {
        private String name;
        private double purchasePrice;
        private double listPrice;
        private boolean newOrUsed = true;
        private int dayArrived = 0;
        private Condition condition = Condition.GOOD;

        public T name(String name) {
            this.name = name;
            return self();
        }

        public T purchasePrice(double purchasePrice) {
            this.purchasePrice = purchasePrice;
            return self();
        }

        public T listPrice(double listPrice) {
            this.listPrice = listPrice;
            return self();
        }

        public T newOrUsed(boolean isNew) {
            this.newOrUsed = isNew;
            return self();
        }

        public T dayArrived(int dayArrived) {
            this.dayArrived = dayArrived;
            return self();
        }

        public T condition(Condition condition) {
            this.condition = condition;
            return self();
        }

        public abstract Item build();
        protected abstract T self();
    }
}
