package ooad.project4.model.item.clothing;

import ooad.project4.model.item.BuildableItem;

public abstract class Clothing extends BuildableItem {
    public Clothing(Builder<?> builder) {
        super(builder);
    }

    public static abstract class Builder<T extends Builder<T>> extends BuildableItem.Builder<T> {

    }
}
