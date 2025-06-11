package ooad.project2.model.item.music.accessories;

import ooad.project2.model.item.Item;

public class PracticeAmp extends Item {
    private double wattage;

    public PracticeAmp(Builder builder) {
        super(builder);
        this.wattage = builder.wattage;
    }

    public static class Builder extends Item.Builder<Builder> {
        private double wattage;

        public Builder wattage(double wattage) {
            this.wattage = wattage;
            return self();
        }

		@Override
		public PracticeAmp build() {
            return new PracticeAmp(this);
		}

		@Override
		protected Builder self() {
            return this;
		}
    }
}


