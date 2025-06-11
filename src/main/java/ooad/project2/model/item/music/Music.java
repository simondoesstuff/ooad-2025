package ooad.project2.model.item.music;

import ooad.project2.model.item.Item;

public abstract class Music extends Item {
    protected String band;
    protected String album;

    protected Music(Builder<?> builder) {
        super(builder);
        this.band = builder.band;
        this.album = builder.album;
    }

    public abstract static class Builder<T extends Builder<T>> extends Item.Builder<T> {
        private String band;
        private String album;

        public T band(String band) {
            this.band = band;
            return self();
        }

        public T album(String album) {
            this.album = album;
            return self();
        }
    }
}
