package ooad.project4.model.item.music.instruments.wind;

public class Saxophone extends Wind {
    public Saxophone(Builder builder) {
        super(builder);
    }

    public static class Builder extends Wind.Builder<Builder> {
        @Override
        public Saxophone build() {
            return new Saxophone(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
