package ooad.project2.model.item.music.instruments.stringed;

public class Bass extends Stringed {
    public Bass(Builder builder) {
        super(builder);
    }

    public static class Builder extends Stringed.Builder<Builder> {
        public Bass build() {
            return new Bass(this);
        }

        public Builder self() {
            return this;
        }
    }
}

