package ooad.project4.model.item.music.instruments.wind;

public class Flute extends Wind {
    private FluteMaterial type;

    public Flute(Builder builder) {
        super(builder);
        this.type = builder.type;
    }

    // ----------------------
    //     Inner classes
    // ----------------------

    public static enum FluteMaterial {
        SILVER, WOOD, GOLD
    }

    public static class Builder extends Wind.Builder<Builder> {
        private FluteMaterial type;

        public Builder type(FluteMaterial type) {
            this.type = type;
            return self();
        }

		@Override
		public Flute build() {
            return new Flute(this);
		}

		@Override
		protected Builder self() {
            return this;
		}
    }
}
