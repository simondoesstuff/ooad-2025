package ooad.project2.model.item.music.instruments.wind;

public class Harmonica extends Wind {
    private HarmonicaKey key;

    public Harmonica(Builder builder) {
        super(builder);
        this.key = builder.key;
    }

    // ----------------------
    //     Inner classes
    // ----------------------

    public static enum HarmonicaKey {
        A, C, F, G,  // TODO: add more musical keys
    }

    public static class Builder extends Wind.Builder<Builder> {
        private HarmonicaKey key;

        public Builder key(HarmonicaKey key) {
            this.key = key;
            return self();
        }

		@Override
		public Harmonica build() {
            return new Harmonica(this);
		}

		@Override
		protected Builder self() {
            return this;
		}
    }
}
