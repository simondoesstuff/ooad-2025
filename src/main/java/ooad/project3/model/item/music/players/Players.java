package ooad.project3.model.item.music.players;

import lombok.Getter;
import ooad.project3.model.item.Item;

public abstract class Players extends Item {
    @Getter
    private boolean isEqualized;

    public Players(Builder<?> builder) {
        super(builder);
        this.isEqualized = builder.isEqualized;
    }

    public static abstract class Builder<T extends Builder<T>> extends Item.Builder<T> {
        private boolean isEqualized = false;

        public T isEqualized(boolean isEqualized) {
            this.isEqualized = isEqualized;
            return self();
        }
    }
}
