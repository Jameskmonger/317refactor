package com.jagex.runescape;

public class Animable extends QueueLink {
	VertexNormal vertexNormals[];
	public int modelHeight;

	Animable() {
		modelHeight = 1000;
	}

	Model getRotatedModel() {
		return null;
	}

	public void renderAtPoint(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2) {
		Model model = getRotatedModel();
		if (model != null) {
			modelHeight = model.modelHeight;
			model.renderAtPoint(i, j, k, l, i1, j1, k1, l1, i2);
		}
	}
}