package com.jagex.runescape;

import com.jagex.runescape.collection.Cache;

public final class SpotAnimation {

	public static void load(Archive archive) {
		Buffer buffer = new Buffer(archive.decompressFile("spotanim.dat"));
		int count = buffer.getUnsignedLEShort();
		if (cache == null)
			cache = new SpotAnimation[count];
		for (int anim = 0; anim < count; anim++) {
			if (cache[anim] == null)
				cache[anim] = new SpotAnimation();
			cache[anim].id = anim;
			cache[anim].read(buffer);
		}

	}

	public static SpotAnimation cache[];

	private int id;

	private int modelId;

	private int animationId;
	public AnimationSequence sequences;
	private final int[] originalModelColours;
	private final int[] modifiedModelColours;
	public int scaleXY;
	public int scaleZ;
	public int rotation;
	public int modelLightFalloff;
	public int modelLightAmbient;
	public static Cache modelCache = new Cache(30);

	private SpotAnimation() {
		animationId = -1;
		originalModelColours = new int[6];
		modifiedModelColours = new int[6];
		scaleXY = 128;
		scaleZ = 128;
	}

	public Model getModel() {
		Model model = (Model) modelCache.get(id);
		if (model != null)
			return model;
		model = Model.getModel(modelId);
		if (model == null)
			return null;
		for (int colour = 0; colour < 6; colour++)
			if (originalModelColours[0] != 0)
				model.recolour(originalModelColours[colour], modifiedModelColours[colour]);

		modelCache.put(model, id);
		return model;
	}

	private void read(Buffer stream) {
		do {
			int opcode = stream.getUnsignedByte();
			if (opcode == 0)
				return;
			if (opcode == 1)
				modelId = stream.getUnsignedLEShort();
			else if (opcode == 2) {
				animationId = stream.getUnsignedLEShort();
				if (AnimationSequence.animations != null)
					sequences = AnimationSequence.animations[animationId];
			} else if (opcode == 4)
				scaleXY = stream.getUnsignedLEShort();
			else if (opcode == 5)
				scaleZ = stream.getUnsignedLEShort();
			else if (opcode == 6)
				rotation = stream.getUnsignedLEShort();
			else if (opcode == 7)
				modelLightFalloff = stream.getUnsignedByte();
			else if (opcode == 8)
				modelLightAmbient = stream.getUnsignedByte();
			else if (opcode >= 40 && opcode < 50)
				originalModelColours[opcode - 40] = stream.getUnsignedLEShort();
			else if (opcode >= 50 && opcode < 60)
				modifiedModelColours[opcode - 50] = stream.getUnsignedLEShort();
			else
				System.out.println("Error unrecognised spotanim config code: " + opcode);
		} while (true);
	}
}