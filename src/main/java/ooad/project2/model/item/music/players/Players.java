package ooad.project2.model.item.music.players;

import ooad.project2.model.item.Item;

public abstract class Players extends Item {
    public Players(Builder<?> builder) {
        super(builder);
    }

    public static abstract class Builder<T extends Builder<T>> extends Item.Builder<T> {
        // pass
    }
}
