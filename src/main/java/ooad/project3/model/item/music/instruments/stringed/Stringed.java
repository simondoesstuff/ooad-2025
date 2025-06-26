package ooad.project3.model.item.music.instruments.stringed;

import lombok.Getter;
import ooad.project3.model.item.music.instruments.Instruments;

public abstract class Stringed extends Instruments {
    @Getter
    protected final boolean isElectric;
    @Getter
    protected final boolean isTuned;

    protected Stringed(Builder<?> builder) {
        super(builder);
        this.isElectric = builder.isElectric;
        this.isTuned = builder.isTuned;
    }

    public abstract static class Builder<T extends Builder<T>> extends Instruments.Builder<T> {
        private boolean isElectric = false;
        private boolean isTuned = false;

        public T isElectric(boolean isElectric) {
            this.isElectric = isElectric;
            return self();
        }

        public T isTuned(boolean isTuned) {
            this.isTuned = isTuned;
            return self();
        }
    }
}
