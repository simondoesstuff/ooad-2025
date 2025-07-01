package ooad.project4.model.item.music.players;

public class CassettePlayer extends Players {
    public CassettePlayer(Builder builder) {
        super(builder);
    }

    public static class Builder extends Players.Builder<Builder> {
        @Override
        public CassettePlayer build() {
            return new CassettePlayer(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}

