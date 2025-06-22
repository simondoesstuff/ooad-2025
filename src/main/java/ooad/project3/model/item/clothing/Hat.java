package ooad.project3.model.item.clothing;

public class Hat extends Clothing {
    private double hatSize;

    public Hat(Builder builder) {
        super(builder);
        this.hatSize = builder.hatSize;
    }

    public static class Builder extends Clothing.Builder<Builder> {
        private double hatSize;

        public Builder hatSize(double hatSize) {
            this.hatSize = hatSize;
            return self();
        }

		@Override
		public Hat build() {
            return new Hat(this);
		}

		@Override
		protected Builder self() {
            return this;
		}
    }
}
