package ooad.project2.model.item.music.instruments.stringed;

public class Mandolin extends Stringed {
    public Mandolin(Builder builder) {
        super(builder);
    }

    public static class Builder extends Stringed.Builder<Builder> {
        public Mandolin build() {
            return new Mandolin(this);
        }

        public Builder self() {
            return this;
        }
    }
}
