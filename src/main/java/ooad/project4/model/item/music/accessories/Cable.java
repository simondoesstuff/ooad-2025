package ooad.project4.model.item.music.accessories;

import ooad.project4.model.item.BuildableItem;

public class Cable extends BuildableItem {
    private double length;

    public Cable(Builder builder) {
        super(builder);
        this.length = builder.length;
    }

    public static class Builder extends BuildableItem.Builder<Builder> {
        private double length;

        public Builder length(double length) {
            this.length = length;
            return self();
        }

		@Override
		public Cable build() {
            return new Cable(this);
		}

		@Override
		protected Builder self() {
            return this;
		}
    }
}

