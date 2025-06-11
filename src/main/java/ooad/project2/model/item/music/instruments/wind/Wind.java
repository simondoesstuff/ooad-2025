package ooad.project2.model.item.music.instruments.wind;

import ooad.project2.model.item.music.instruments.Instruments;

public abstract class Wind extends Instruments {
    public Wind(Builder<?> builder) {
        super(builder);
    }

    public static abstract class Builder<T extends Builder<T>> extends Instruments.Builder<T> {
        // pass
    }
}
