package ooad.project4.model.item.music;

public class Cassette extends Music {
    public Cassette(Builder builder) {
        super(builder);
    }

    public static class Builder extends Music.Builder<Builder> {

		@Override
		public Cassette build() {
            return new Cassette(this);
		}

		@Override
		protected Builder self() {
            return this;
		}
    }
}


