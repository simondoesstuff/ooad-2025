package ooad.project3.model.item.music.accessories;

import ooad.project3.model.item.Item;

public abstract class Accessories extends Item {
    public Accessories(Builder<?> builder) {
        super(builder);
    }

    public static abstract class Builder<T extends Builder<T>> extends Item.Builder<T> {
    }
}
