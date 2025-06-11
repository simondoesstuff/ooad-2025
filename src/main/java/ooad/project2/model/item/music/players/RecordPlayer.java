package ooad.project2.model.item.music.players;

public class RecordPlayer extends Players {
    public RecordPlayer(Builder builder) {
        super(builder);
    }

    public static class Builder extends Players.Builder<Builder> {

		@Override
		public RecordPlayer build() {
            return new RecordPlayer(this);
		}

		@Override
		protected Builder self() {
            return this;
		}
    }
}

