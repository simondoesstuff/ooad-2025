package ooad.project2.model.item.music.players;

public class MP3Player extends Players {
    public MP3Player(Builder builder) {
        super(builder);
    }

    public static class Builder extends Players.Builder<Builder> {

		@Override
		public MP3Player build() {
            return new MP3Player(this);
		}

		@Override
		protected Builder self() {
            return this;
		}
    }
}

