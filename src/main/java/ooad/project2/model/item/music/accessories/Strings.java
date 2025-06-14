package ooad.project2.model.item.music.accessories;

import ooad.project2.model.item.Item;

public class Strings extends Item {
    private StringType type;

    public Strings(Builder builder) {
        super(builder);
        this.type = builder.type;
    }

    // --------------------------
    //  Inner classes
    // --------------------------

    public static enum StringType {
        GUITAR, BASS, MANDOLIN
    }

    public static class Builder extends Item.Builder<Builder> {
        private StringType type;

        public Builder type(StringType type) {
            this.type = type;
            return self();
        }

		@Override
		public Strings build() {
            return new Strings(this);
		}

		@Override
		protected Builder self() {
            return this;
		}
    }
}


