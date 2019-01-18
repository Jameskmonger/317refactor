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

	public void renderAtPoint(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2) {
		Model model = this.getRotatedModel();
		if (model != null) {
            this.modelHeight = model.modelHeight;
			model.renderAtPoint(i, j, k, l, i1, j1, k1, l1, i2);
		}
	}
}