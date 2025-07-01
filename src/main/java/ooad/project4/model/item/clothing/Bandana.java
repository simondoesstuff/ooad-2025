package ooad.project4.model.item.clothing;

public class Bandana extends Clothing {
    public Bandana(Builder builder) {
        super(builder);
    }

    public static class Builder extends Clothing.Builder<Builder> {
		@Override
		public Bandana build() {
            return new Bandana(this);
		}

		@Override
		protected Builder self() {
            return this;
		}
    }
}

