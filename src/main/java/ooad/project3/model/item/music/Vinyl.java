package ooad.project3.model.item.music;

public class Vinyl extends Music {
    public Vinyl(Builder builder) {
        super(builder);
    }

    public static class Builder extends Music.Builder<Builder> {

		@Override
		public Vinyl build() {
            return new Vinyl(this);
		}

		@Override
		protected Builder self() {
            return this;
		}
    }
}

