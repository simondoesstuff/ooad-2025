package ooad.project3.model.item.music.players;

public class CDPlayer extends Players {
    public CDPlayer(Builder builder) {
        super(builder);
    }

    public static class Builder extends Players.Builder<Builder> {

		@Override
		public CDPlayer build() {
            return new CDPlayer(this);
		}

		@Override
		protected Builder self() {
            return this;
		}
    }
}


