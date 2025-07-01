package ooad.project4.model.item.music.instruments.stringed;

import lombok.Getter;
import lombok.Setter;
import ooad.project4.model.item.music.instruments.Instruments;

public abstract class Stringed extends Instruments {
    @Getter
    protected final boolean isElectric;
    @Getter @Setter
    protected boolean isTuned;

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
