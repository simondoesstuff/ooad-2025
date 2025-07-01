package ooad.project4.model.item;

import lombok.Getter;

/**
 * A decorator for Item that provides
 * details related to the sale
 */
public class SoldItem implements Item {
    private final Item base;
    @Getter
    private final double salePrice;
    @Getter
    private final int daySold;

    public SoldItem(Item base, double salePrice, int daySold) {
        this.base = base;
        this.salePrice = salePrice;
        this.daySold = daySold;
    }

    public String getName() { return base.getName(); }
    public double getPurchasePrice() { return base.getPurchasePrice(); }
    public double getListPrice() { return base.getListPrice(); }
    public int getDayArrived() { return base.getDayArrived(); }
    public Condition getCondition() { return base.getCondition(); }
    public boolean damage() { return base.damage(); }
}
