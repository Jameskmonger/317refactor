package com.jagex.runescape;

import com.jagex.runescape.collection.Cache;

public final class EntityDefinition {

	public static EntityDefinition getDefinition(int id) {
		for (int c = 0; c < 20; c++)
			if (EntityDefinition.cache[c].id == id)
				return EntityDefinition.cache[c];

		EntityDefinition.bufferIndex = (EntityDefinition.bufferIndex + 1) % 20;
		EntityDefinition definition = EntityDefinition.cache[EntityDefinition.bufferIndex] = new EntityDefinition();
		EntityDefinition.stream.position = EntityDefinition.streamOffsets[id];
		definition.id = id;
		definition.loadDefinition(EntityDefinition.stream);
		return definition;
	}

	public static void load(Archive streamLoader) {
		EntityDefinition.stream = new Buffer(streamLoader.decompressFile("npc.dat"));
		Buffer stream = new Buffer(streamLoader.decompressFile("npc.idx"));
		int size = stream.getUnsignedLEShort();
		EntityDefinition.streamOffsets = new int[size];
		int offset = 2;
		for (int n = 0; n < size; n++) {
			EntityDefinition.streamOffsets[n] = offset;
			offset += stream.getUnsignedLEShort();
		}

		EntityDefinition.cache = new EntityDefinition[20];
		for (int c = 0; c < 20; c++)
			EntityDefinition.cache[c] = new EntityDefinition();

	}

	public static void nullLoader() {
		modelCache = null;
		streamOffsets = null;
		cache = null;
		stream = null;
	}

	public int turnLeftAnimationId;

	private static int bufferIndex;

	private int varBitId;

	public int turnAboutAnimationId;

	private int settingId;

	private static Buffer stream;
	public int combatLevel;
	public String name;
	public String actions[];
	public int walkAnimationId;
	public byte boundaryDimension;
	private int[] originalModelColours;
	private static int[] streamOffsets;
	private int[] headModelIds;
	public int headIcon;
	private int[] modifiedModelColours;
	public int standAnimationId;
	public long id;
	public int degreesToTurn;
	private static EntityDefinition[] cache;
	public static Client clientInstance;
	public int turnRightAnimationId;
	public boolean clickable;
	private int brightness;
	private int scaleZ;
	public boolean visibleMinimap;
	public int childrenIDs[];
	public byte description[];
	private int scaleXY;
	private int contrast;
	public boolean visible;
	private int[] modelIds;
	public static Cache modelCache = new Cache(30);

	private EntityDefinition() {
		turnLeftAnimationId = -1;
		varBitId = -1;
		turnAboutAnimationId = -1;
		settingId = -1;
		combatLevel = -1;
		walkAnimationId = -1;
		boundaryDimension = 1;
		headIcon = -1;
		standAnimationId = -1;
		id = -1L;
		degreesToTurn = 32;
		turnRightAnimationId = -1;
		clickable = true;
		scaleZ = 128;
		visibleMinimap = true;
		scaleXY = 128;
		visible = false;
	}

	public EntityDefinition getChildDefinition() {
		int childId = -1;
		if (varBitId != -1) {
			VarBit varBit = VarBit.cache[varBitId];
			int configId = varBit.configId;
			int lsb = varBit.leastSignificantBit;
			int msb = varBit.mostSignificantBit;
			int bit = Client.BITFIELD_MAX_VALUE[msb - lsb];
			childId = clientInstance.interfaceSettings[configId] >> lsb & bit;
		} else if (settingId != -1)
			childId = clientInstance.interfaceSettings[settingId];
		if (childId < 0 || childId >= childrenIDs.length || childrenIDs[childId] == -1)
			return null;
		else
			return getDefinition(childrenIDs[childId]);
	}

	public Model getChildModel(int frameId2, int frameId1, int framesFrom2[]) {
		if (childrenIDs != null) {
			EntityDefinition childDefinition = getChildDefinition();
			if (childDefinition == null)
				return null;
			else
				return childDefinition.getChildModel(frameId2, frameId1, framesFrom2);
		}
		Model model = (Model) modelCache.get(id);
		if (model == null) {
			boolean notCached = false;
			for (int m = 0; m < modelIds.length; m++)
				if (!Model.isCached(modelIds[m]))
					notCached = true;

			if (notCached)
				return null;
			Model childModels[] = new Model[modelIds.length];
			for (int m = 0; m < modelIds.length; m++)
				childModels[m] = Model.getModel(modelIds[m]);

			if (childModels.length == 1)
				model = childModels[0];
			else
				model = new Model(childModels.length, childModels);
			if (modifiedModelColours != null) {
				for (int c = 0; c < modifiedModelColours.length; c++)
					model.recolour(modifiedModelColours[c], originalModelColours[c]);

			}
			model.createBones();
			model.applyLighting(64 + brightness, 850 + contrast, -30, -50, -30, true);
			modelCache.put(model, id);
		}
		Model childModel = Model.aModel_1621;
		childModel.replaceWithModel(model, Animation.isNullFrame(frameId1) & Animation.isNullFrame(frameId2));
		if (frameId1 != -1 && frameId2 != -1)
			childModel.mixAnimationFrames(framesFrom2, frameId2, frameId1);
		else if (frameId1 != -1)
			childModel.applyTransformation(frameId1);
		if (scaleXY != 128 || scaleZ != 128)
			childModel.scaleT(scaleXY, scaleXY, scaleZ);
		childModel.calculateDiagonals();
		childModel.triangleSkin = null;
		childModel.vertexSkin = null;
		if (boundaryDimension == 1)
			childModel.singleTile = true;
		return childModel;
	}

	public Model getHeadModel() {
		if (childrenIDs != null) {
			EntityDefinition definition = getChildDefinition();
			if (definition == null)
				return null;
			else
				return definition.getHeadModel();
		}
		if (headModelIds == null)
			return null;
		boolean someModelsNotCached = false;
		for (int i = 0; i < headModelIds.length; i++)
			if (!Model.isCached(headModelIds[i]))
				someModelsNotCached = true;

		if (someModelsNotCached)
			return null;
		Model headModels[] = new Model[headModelIds.length];
		for (int j = 0; j < headModelIds.length; j++)
			headModels[j] = Model.getModel(headModelIds[j]);

		Model headModel;
		if (headModels.length == 1)
			headModel = headModels[0];
		else
			headModel = new Model(headModels.length, headModels);
		if (modifiedModelColours != null) {
			for (int c = 0; c < modifiedModelColours.length; c++)
				headModel.recolour(modifiedModelColours[c], originalModelColours[c]);

		}
		return headModel;
	}

	private void loadDefinition(Buffer stream) {
		do {
			int attributeType = stream.getUnsignedByte();
			if (attributeType == 0)
				return;
			if (attributeType == 1) {
				int modelCount = stream.getUnsignedByte();
				modelIds = new int[modelCount];
				for (int m = 0; m < modelCount; m++)
					modelIds[m] = stream.getUnsignedLEShort();

			} else if (attributeType == 2)
				name = stream.getString();
			else if (attributeType == 3)
				description = stream.readBytes();
			else if (attributeType == 12)
				boundaryDimension = stream.get();
			else if (attributeType == 13)
				standAnimationId = stream.getUnsignedLEShort();
			else if (attributeType == 14)
				walkAnimationId = stream.getUnsignedLEShort();
			else if (attributeType == 17) {
				walkAnimationId = stream.getUnsignedLEShort();
				turnAboutAnimationId = stream.getUnsignedLEShort();
				turnRightAnimationId = stream.getUnsignedLEShort();
				turnLeftAnimationId = stream.getUnsignedLEShort();
			} else if (attributeType >= 30 && attributeType < 40) {
				if (actions == null)
					actions = new String[5];
				actions[attributeType - 30] = stream.getString();
				if (actions[attributeType - 30].equalsIgnoreCase("hidden"))
					actions[attributeType - 30] = null;
			} else if (attributeType == 40) {
				int colourCount = stream.getUnsignedByte();
				modifiedModelColours = new int[colourCount];
				originalModelColours = new int[colourCount];
				for (int c = 0; c < colourCount; c++) {
					modifiedModelColours[c] = stream.getUnsignedLEShort();
					originalModelColours[c] = stream.getUnsignedLEShort();
				}

			} else if (attributeType == 60) {
				int additionalModelCount = stream.getUnsignedByte();
				headModelIds = new int[additionalModelCount];
				for (int m = 0; m < additionalModelCount; m++)
					headModelIds[m] = stream.getUnsignedLEShort();

			} else if (attributeType == 90)
				stream.getUnsignedLEShort();
			else if (attributeType == 91)
				stream.getUnsignedLEShort();
			else if (attributeType == 92)
				stream.getUnsignedLEShort();
			else if (attributeType == 93)
				visibleMinimap = false;
			else if (attributeType == 95)
				combatLevel = stream.getUnsignedLEShort();
			else if (attributeType == 97)
				scaleXY = stream.getUnsignedLEShort();
			else if (attributeType == 98)
				scaleZ = stream.getUnsignedLEShort();
			else if (attributeType == 99)
				visible = true;
			else if (attributeType == 100)
				brightness = stream.get();
			else if (attributeType == 101)
				contrast = stream.get() * 5;
			else if (attributeType == 102)
				headIcon = stream.getUnsignedLEShort();
			else if (attributeType == 103)
				degreesToTurn = stream.getUnsignedLEShort();
			else if (attributeType == 106) {
				varBitId = stream.getUnsignedLEShort();
				if (varBitId == 65535)
					varBitId = -1;
				settingId = stream.getUnsignedLEShort();
				if (settingId == 65535)
					settingId = -1;
				int childCount = stream.getUnsignedByte();
				childrenIDs = new int[childCount + 1];
				for (int c = 0; c <= childCount; c++) {
					childrenIDs[c] = stream.getUnsignedLEShort();
					if (childrenIDs[c] == 65535)
						childrenIDs[c] = -1;
				}

			} else if (attributeType == 107)
				clickable = false;
		} while (true);
	}

}
