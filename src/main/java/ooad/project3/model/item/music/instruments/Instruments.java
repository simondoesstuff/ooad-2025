package ooad.project3.model.item.music.instruments;

import ooad.project3.model.item.Item;

// New package location
public abstract class Instruments extends Item {
    protected Instruments(Builder<?> builder) {
        super(builder);
    }

    public abstract static class Builder<T extends Builder<T>> extends Item.Builder<T> {
        // pass
    }
}
