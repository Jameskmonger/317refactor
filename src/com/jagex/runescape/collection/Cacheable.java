package com.jagex.runescape.collection;

public class Cacheable extends Linkable {

	public Cacheable previousCacheable;
	public Cacheable nextCacheable;

	public final void unlinkCacheable() {
		if (nextCacheable == null) {
			return;
		}
		
		nextCacheable.previousCacheable = previousCacheable;
		previousCacheable.nextCacheable = nextCacheable;
		previousCacheable = null;
		nextCacheable = null;
	}
}
