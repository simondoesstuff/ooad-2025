package ooad.project3.model.item.music.players;

import lombok.Getter;
import lombok.Setter;
import ooad.project3.model.item.BuildableItem;

public abstract class Players extends BuildableItem {
    @Getter @Setter
    private boolean isEqualized;

    public Players(Builder<?> builder) {
        super(builder);
        this.isEqualized = builder.isEqualized;
    }

    public static abstract class Builder<T extends Builder<T>> extends BuildableItem.Builder<T> {
        private boolean isEqualized = false;

        public T isEqualized(boolean isEqualized) {
            this.isEqualized = isEqualized;
            return self();
        }
    }
}
