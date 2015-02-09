package com.jagex.runescape;

/*
 * This file is part of the RuneScape client
 * revision 317, which was publicly released
 * on the 13th of June 2005.
 * 
 * This file has been refactored in order to
 * restore readability to the codebase for
 * educational purposes, primarility to those
 * with an interest in game development.
 * 
 * It may be a criminal offence to run this
 * file. This file is the intellectual property
 * of Jagex Ltd.
 */

/* 
 * This file was renamed as part of the 317refactor project.
 */

public class Animable extends QueueLink {
	VertexNormal vertexNormals[];
	public int modelHeight;

	Animable() {
		modelHeight = 1000;
	}

	Model getRotatedModel() {
		return null;
	}
	public void renderAtPoint(int i, int j, int k, int l, int i1, int j1,
			int k1, int l1, int i2) {
		Model model = getRotatedModel();
		if (model != null) {
			modelHeight = model.modelHeight;
			model.renderAtPoint(i, j, k, l, i1, j1, k1, l1, i2);
		}
	}
}