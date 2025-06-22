package ooad.project3.model.item.music.instruments.stringed;

public class Guitar extends Stringed {
    private Guitar(Builder builder) {
        super(builder);
    }

    public static class Builder extends Stringed.Builder<Builder> {
        @Override
        public Guitar build() {
            return new Guitar(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
