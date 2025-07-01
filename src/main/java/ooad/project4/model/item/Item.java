package ooad.project4.model.item;

/**
 * Base class for the item inheritance hierarchy.
 * Represents an item that can be bought/sold at a Store.
 */
public interface Item {
    public String getName();
    public double getPurchasePrice();
    public double getListPrice();
    public int getDayArrived();
    public Condition getCondition();

    /**
     * lowers condition by 1 level
     * @returns true if the item was at POOR which indicates
     * it should be now considered destroyed
     */
    public boolean damage();
}
