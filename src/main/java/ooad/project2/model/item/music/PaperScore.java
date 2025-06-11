package ooad.project2.model.item.music;

public class PaperScore extends Music {
    public PaperScore(Builder builder) {
        super(builder);
    }

    public static class Builder extends Music.Builder<Builder> {

		@Override
		public PaperScore build() {
            return new PaperScore(this);
		}

		@Override
		protected Builder self() {
            return this;
		}
    }
}

