package ooad.project3.model.item.music;

import ooad.project3.model.item.Item;

public class CD extends Music {
    public CD(Builder builder) {
        super(builder);
    }

    public static class Builder extends Music.Builder<Builder> {

		@Override
		public Item build() {
            return new CD(this);
		}

		@Override
		protected Builder self() {
            return this;
		}
    }
}
