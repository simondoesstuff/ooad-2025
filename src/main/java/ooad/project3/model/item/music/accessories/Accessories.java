package ooad.project3.model.item.music.accessories;

import ooad.project3.model.item.BuildableItem;

public abstract class Accessories extends BuildableItem {
    public Accessories(Builder<?> builder) {
        super(builder);
    }

    public static abstract class Builder<T extends Builder<T>> extends BuildableItem.Builder<T> {
    }
}
