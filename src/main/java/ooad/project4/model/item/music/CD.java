package ooad.project4.model.item.music;

import ooad.project4.model.item.BuildableItem;

public class CD extends Music {
    public CD(Builder builder) {
        super(builder);
    }

    public static class Builder extends Music.Builder<Builder> {

		@Override
		public BuildableItem build() {
            return new CD(this);
		}

		@Override
		protected Builder self() {
            return this;
		}
    }
}
