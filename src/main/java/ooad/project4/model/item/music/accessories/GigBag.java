package ooad.project4.model.item.music.accessories;

import ooad.project4.model.item.BuildableItem;

public class GigBag extends BuildableItem {
    public GigBag(Builder builder) {
        super(builder);
    }

    public static class Builder extends BuildableItem.Builder<Builder> {
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

