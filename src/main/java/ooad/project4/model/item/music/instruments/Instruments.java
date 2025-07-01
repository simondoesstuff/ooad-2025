package ooad.project4.model.item.music.instruments;

import ooad.project4.model.item.BuildableItem;

// New package location
public abstract class Instruments extends BuildableItem {
    protected Instruments(Builder<?> builder) {
        super(builder);
    }

    public abstract static class Builder<T extends Builder<T>> extends BuildableItem.Builder<T> {
        // pass
    }
}
