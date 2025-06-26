package ooad.project3.model.item.music.instruments.wind;

import lombok.Getter;
import ooad.project3.model.item.music.instruments.Instruments;

public abstract class Wind extends Instruments {
    @Getter
    protected final boolean isAdjusted;

    public Wind(Builder<?> builder) {
        super(builder);
        this.isAdjusted = builder.isAdjusted;
    }

    public static abstract class Builder<T extends Builder<T>> extends Instruments.Builder<T> {
        private boolean isAdjusted;

        public T isAdjusted(boolean isAdjusted) {
            this.isAdjusted = isAdjusted;
            return self();
        }
    }
}
