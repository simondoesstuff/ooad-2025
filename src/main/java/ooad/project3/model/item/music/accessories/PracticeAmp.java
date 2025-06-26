package ooad.project3.model.item.music.accessories;

import ooad.project3.model.item.BuildableItem;

public class PracticeAmp extends BuildableItem {
    private double wattage;

    public PracticeAmp(Builder builder) {
        super(builder);
        this.wattage = builder.wattage;
    }

    public static class Builder extends BuildableItem.Builder<Builder> {
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


