package ooad.project3.model.item.music.accessories;

import ooad.project3.model.item.Item;

public class GigBag extends Item {
    public GigBag(Builder builder) {
        super(builder);
    }

    public static class Builder extends Item.Builder<Builder> {
        @Override
        public GigBag build() {
            return new GigBag(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}

