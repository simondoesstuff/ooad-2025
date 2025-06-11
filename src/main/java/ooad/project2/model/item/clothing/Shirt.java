package ooad.project2.model.item.clothing;

public class Shirt extends Clothing {
    private ShirtSize shirtSize;

    public Shirt(Builder builder) {
        super(builder);
        this.shirtSize = builder.shirtSize;
    }

    // --------------------------
    //  Inner classes
    // --------------------------

    public static enum ShirtSize {
        // TODO: shirt sizes are lacking
        XS, S, M, L, XL
    }

    public static class Builder extends Clothing.Builder<Builder> {
        private ShirtSize shirtSize;

        public Builder shirtSize(ShirtSize shirtSize) {
            this.shirtSize = shirtSize;
            return self();
        }

		@Override
		public Shirt build() {
            return new Shirt(this);
		}

		@Override
		protected Builder self() {
            return this;
		}
    }
}

