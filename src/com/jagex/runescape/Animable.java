package com.jagex.runescape;

import com.jagex.runescape.collection.Cacheable;

public class Animable extends Cacheable {
	public VertexNormal vertexNormals[];
	public int modelHeight;

	Animable() {
        this.modelHeight = 1000;
	}

	Model getRotatedModel() {
		return null;
	}

	public void renderAtPoint(final int i, final int j, final int k, final int l, final int i1, final int j1, final int k1, final int l1, final int i2) {
		final Model model = this.getRotatedModel();
		if (model != null) {
            this.modelHeight = model.modelHeight;
			model.renderAtPoint(i, j, k, l, i1, j1, k1, l1, i2);
		}
	}
}