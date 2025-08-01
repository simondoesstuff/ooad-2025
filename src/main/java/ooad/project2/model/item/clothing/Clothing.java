package ooad.project2.model.item.clothing;

import ooad.project2.model.item.Item;

public abstract class Clothing extends Item {
    public Clothing(Builder<?> builder) {
        super(builder);
    }

    public static abstract class Builder<T extends Builder<T>> extends Item.Builder<T> {

    }
}
