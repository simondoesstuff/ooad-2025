package ooad.project4.model.item.music;

import ooad.project4.model.item.BuildableItem;

public abstract class Music extends BuildableItem {
    protected String band;
    protected String album;

    protected Music(Builder<?> builder) {
        super(builder);
        this.band = builder.band;
        this.album = builder.album;
    }

    public abstract static class Builder<T extends Builder<T>> extends BuildableItem.Builder<T> {
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
