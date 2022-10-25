package com.jagex.runescape.collection;

public class Cacheable extends Linkable {

    public Cacheable nextCacheable;
    public Cacheable previousCacheable;

    public final void unlinkCacheable() {
        if (this.previousCacheable == null) {
            return;
        }

        this.previousCacheable.nextCacheable = this.nextCacheable;
        this.nextCacheable.previousCacheable = this.previousCacheable;
        this.nextCacheable = null;
        this.previousCacheable = null;
    }
}
