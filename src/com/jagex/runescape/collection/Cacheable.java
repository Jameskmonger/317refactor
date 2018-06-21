package com.jagex.runescape.collection;

public class Cacheable extends Linkable {

	public Cacheable nextCacheable;
	public Cacheable previousCacheable;

	public final void unlinkCacheable() {
		if (previousCacheable == null) {
			return;
		}
		
		previousCacheable.nextCacheable = nextCacheable;
		nextCacheable.previousCacheable = previousCacheable;
		nextCacheable = null;
		previousCacheable = null;
	}
}
