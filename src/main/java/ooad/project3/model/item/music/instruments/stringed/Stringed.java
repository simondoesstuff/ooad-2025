package ooad.project3.model.item.music.instruments.stringed;

import ooad.project3.model.item.music.instruments.Instruments;

public abstract class Stringed extends Instruments {
    protected final boolean isElectric;

    protected Stringed(Builder<?> builder) {
        super(builder);
        this.isElectric = builder.isElectric;
    }

    public abstract static class Builder<T extends Builder<T>> extends Instruments.Builder<T> {
        private boolean isElectric = false;

        public T isElectric(boolean isElectric) {
            this.isElectric = isElectric;
            return self();
        }
    }
}
