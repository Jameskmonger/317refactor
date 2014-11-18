package com.jagex.runescape;

/*
 * This file is part of the RuneScape client
 * revision 317, which was publicly released
 * on the 10th of April 2006.
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

public final class ItemDefinition {

	public static ItemDefinition getDefinition(int id) {
		for (int i = 0; i < 10; i++)
			if (cache[i].id == id)
				return cache[i];

		cacheIndex = (cacheIndex + 1) % 10;
		ItemDefinition definition = cache[cacheIndex];
		stream.currentOffset = streamOffsets[id];
		definition.id = id;
		definition.setDefaults();
		definition.readValues(stream);
		if (definition.noteTemplateId != -1)
			definition.toNote();
		if (!membersWorld && definition.membersObject) {
			definition.name = "Members Object";
			definition.description = "Login to a members' server to use this object."
					.getBytes();
			definition.groundActions = null;
			definition.actions = null;
			definition.teamId = 0;
		}
		return definition;
	}

	public static Sprite getSprite(int itemId, int itemAmount, int type) {
		if (type == 0) {
			Sprite sprite = (Sprite) spriteCache.get(itemId);
			if (sprite != null && sprite.maxHeight != itemAmount
					&& sprite.maxHeight != -1) {
				sprite.remove();
				sprite = null;
			}
			if (sprite != null)
				return sprite;
		}
		ItemDefinition definition = getDefinition(itemId);
		if (definition.stackableIds == null)
			itemAmount = -1;
		if (itemAmount > 1) {
			int stackedId = -1;
			for (int amount = 0; amount < 10; amount++)
				if (itemAmount >= definition.stackableAmounts[amount]
						&& definition.stackableAmounts[amount] != 0)
					stackedId = definition.stackableIds[amount];

			if (stackedId != -1)
				definition = getDefinition(stackedId);
		}
		Model model = definition.getAmountModel(1);
		if (model == null)
			return null;
		Sprite noteSprite = null;
		if (definition.noteTemplateId != -1) {
			noteSprite = getSprite(definition.noteId, 10, -1);
			if (noteSprite == null)
				return null;
		}
		Sprite itemSprite = new Sprite(32, 32);
		int textureCentreX = Texture.centreX;
		int textureCentreY = Texture.centreY;
		int lineOffsets[] = Texture.lineOffsets;
		int pixels[] = DrawingArea.pixels;
		int width = DrawingArea.width;
		int height = DrawingArea.height;
		int topX = DrawingArea.topX;
		int bottomX = DrawingArea.bottomX;
		int topY = DrawingArea.topY;
		int bottomY = DrawingArea.bottomY;
		Texture.textured = false;
		DrawingArea.initDrawingArea(32, 32, itemSprite.pixels);
		DrawingArea.drawFilledRectangle(0, 0, 32, 32, 0);
		Texture.setDefaultBounds();
		int zoom = definition.modelZoom;
		if (type == -1)
			zoom = (int) (zoom * 1.5D);
		if (type > 0)
			zoom = (int) (zoom * 1.04D);
		int l3 = Texture.SINE[definition.modelRotationX] * zoom >> 16;
		int i4 = Texture.COSINE[definition.modelRotationX] * zoom >> 16;
		model.renderSingle(definition.modelRotationY,
				definition.modelRotationZ, definition.modelRotationX,
				definition.modelOffset1, l3 + model.modelHeight / 2
						+ definition.modelOffset2, i4 + definition.modelOffset2);
		for (int _x = 31; _x >= 0; _x--) {
			for (int _y = 31; _y >= 0; _y--)
				if (itemSprite.pixels[_x + _y * 32] == 0)
					if (_x > 0 && itemSprite.pixels[(_x - 1) + _y * 32] > 1)
						itemSprite.pixels[_x + _y * 32] = 1;
					else if (_y > 0
							&& itemSprite.pixels[_x + (_y - 1) * 32] > 1)
						itemSprite.pixels[_x + _y * 32] = 1;
					else if (_x < 31 && itemSprite.pixels[_x + 1 + _y * 32] > 1)
						itemSprite.pixels[_x + _y * 32] = 1;
					else if (_y < 31
							&& itemSprite.pixels[_x + (_y + 1) * 32] > 1)
						itemSprite.pixels[_x + _y * 32] = 1;

		}

		if (type > 0) {
			for (int _x = 31; _x >= 0; _x--) {
				for (int _y = 31; _y >= 0; _y--)
					if (itemSprite.pixels[_x + _y * 32] == 0)
						if (_x > 0
								&& itemSprite.pixels[(_x - 1) + _y * 32] == 1)
							itemSprite.pixels[_x + _y * 32] = type;
						else if (_y > 0
								&& itemSprite.pixels[_x + (_y - 1) * 32] == 1)
							itemSprite.pixels[_x + _y * 32] = type;
						else if (_x < 31
								&& itemSprite.pixels[_x + 1 + _y * 32] == 1)
							itemSprite.pixels[_x + _y * 32] = type;
						else if (_y < 31
								&& itemSprite.pixels[_x + (_y + 1) * 32] == 1)
							itemSprite.pixels[_x + _y * 32] = type;

			}

		} else if (type == 0) {
			for (int _x = 31; _x >= 0; _x--) {
				for (int _y = 31; _y >= 0; _y--)
					if (itemSprite.pixels[_x + _y * 32] == 0 && _x > 0
							&& _y > 0
							&& itemSprite.pixels[(_x - 1) + (_y - 1) * 32] > 0)
						itemSprite.pixels[_x + _y * 32] = 0x302020;

			}

		}
		if (definition.noteTemplateId != -1) {
			int _maxWidth = noteSprite.maxWidth;
			int _maxHeight = noteSprite.maxHeight;
			noteSprite.maxWidth = 32;
			noteSprite.maxHeight = 32;
			noteSprite.drawImage(0, 0);
			noteSprite.maxWidth = _maxWidth;
			noteSprite.maxHeight = _maxHeight;
		}
		if (type == 0)
			spriteCache.put(itemSprite, itemId);
		DrawingArea.initDrawingArea(height, width, pixels);
		DrawingArea.setDrawingArea(bottomY, topX, bottomX, topY);
		Texture.centreX = textureCentreX;
		Texture.centreY = textureCentreY;
		Texture.lineOffsets = lineOffsets;
		Texture.textured = true;
		if (definition.stackable)
			itemSprite.maxWidth = 33;
		else
			itemSprite.maxWidth = 32;
		itemSprite.maxHeight = itemAmount;
		return itemSprite;
	}

	public static void load(Archive streamLoader) {
		stream = new Stream(streamLoader.getFile("obj.dat"));
		Stream stream = new Stream(streamLoader.getFile("obj.idx"));
		itemCount = stream.getUnsignedLEShort();
		streamOffsets = new int[itemCount];
		int offset = 2;
		for (int item = 0; item < itemCount; item++) {
			streamOffsets[item] = offset;
			offset += stream.getUnsignedLEShort();
		}

		cache = new ItemDefinition[10];
		for (int definition = 0; definition < 10; definition++)
			cache[definition] = new ItemDefinition();

	}

	public static void nullLoader() {
		modelCache = null;
		spriteCache = null;
		streamOffsets = null;
		cache = null;
		stream = null;
	}

	private byte equipModelTranslationFemale;

	public int value;

	private int[] modifiedModelColors;

	public int id;

	static MRUNodes spriteCache = new MRUNodes(100);

	public static MRUNodes modelCache = new MRUNodes(50);

	private int[] originalModelColors;

	public boolean membersObject;

	private int femaleEquipModelIdEmblem;

	private int noteTemplateId;

	private int femaleEquipModelIdSecondary;
	private int maleEquipModelIdPrimary;
	private int maleDialogueHatModelId;
	private int modelScaleX;
	public String groundActions[];
	private int modelOffset1;
	public String name;
	private static ItemDefinition[] cache;
	private int femaleDialogueHatModelId;
	private int modelId;
	private int maleDialogueModelId;
	public boolean stackable;
	public byte description[];
	private int noteId;
	private static int cacheIndex;
	public int modelZoom;
	public static boolean membersWorld = true;
	private static Stream stream;
	private int shadowModifier;
	private int maleEquipModelIdEmblem;
	private int maleEquipModelIdSecondary;
	public String actions[];
	public int modelRotationX;
	private int modelScaleZ;
	private int modelScaleY;
	private int[] stackableIds;
	private int modelOffset2;
	private static int[] streamOffsets;
	private int lightModifier;
	private int femaleDialogueModelId;
	public int modelRotationY;
	private int femaleEquipModelIdPrimary;
	private int[] stackableAmounts;
	public int teamId;
	public static int itemCount;
	private int modelRotationZ;
	private byte equipModelTranslationMale;
	private ItemDefinition() {
		id = -1;
	}
	public boolean equipModelCached(int gender) {
		int equipModelIdPrimary = maleEquipModelIdPrimary;
		int equipModelIdSecondary = maleEquipModelIdSecondary;
		int equipModelIdEmblem = maleEquipModelIdEmblem;
		if (gender == 1) {
			equipModelIdPrimary = femaleEquipModelIdPrimary;
			equipModelIdSecondary = femaleEquipModelIdSecondary;
			equipModelIdEmblem = femaleEquipModelIdEmblem;
		}
		if (equipModelIdPrimary == -1)
			return true;
		boolean cached = true;
		if (!Model.isCached(equipModelIdPrimary))
			cached = false;
		if (equipModelIdSecondary != -1
				&& !Model.isCached(equipModelIdSecondary))
			cached = false;
		if (equipModelIdEmblem != -1 && !Model.isCached(equipModelIdEmblem))
			cached = false;
		return cached;
	}
	public Model getAmountModel(int amount) {
		if (stackableIds != null && amount > 1) {
			int stackableId = -1;
			for (int i = 0; i < 10; i++)
				if (amount >= stackableAmounts[i] && stackableAmounts[i] != 0)
					stackableId = stackableIds[i];

			if (stackableId != -1)
				return getDefinition(stackableId).getAmountModel(1);
		}
		Model stackedModel = (Model) modelCache.get(id);
		if (stackedModel != null)
			return stackedModel;
		stackedModel = Model.getModel(modelId);
		if (stackedModel == null)
			return null;
		if (modelScaleX != 128 || modelScaleY != 128 || modelScaleZ != 128)
			stackedModel.scaleT(modelScaleX, modelScaleZ, modelScaleY);
		if (modifiedModelColors != null) {
			for (int l = 0; l < modifiedModelColors.length; l++)
				stackedModel.recolour(modifiedModelColors[l],
						originalModelColors[l]);

		}
		stackedModel.applyLighting(64 + lightModifier, 768 + shadowModifier,
				-50, -10, -50, true);
		stackedModel.singleTile = true;
		modelCache.put(stackedModel, id);
		return stackedModel;
	}
	public Model getDialogueModel(int gender) {
		int dialogueModelId = maleDialogueModelId;
		int dialogueHatModelId = maleDialogueHatModelId;
		if (gender == 1) {
			dialogueModelId = femaleDialogueModelId;
			dialogueHatModelId = femaleDialogueHatModelId;
		}
		if (dialogueModelId == -1)
			return null;
		Model dialogueModel = Model.getModel(dialogueModelId);
		if (dialogueHatModelId != -1) {
			Model dialogueHatModel = Model.getModel(dialogueHatModelId);
			Model dialogueModels[] = { dialogueModel, dialogueHatModel };
			dialogueModel = new Model(2, dialogueModels);
		}
		if (modifiedModelColors != null) {
			for (int c = 0; c < modifiedModelColors.length; c++)
				dialogueModel.recolour(modifiedModelColors[c],
						originalModelColors[c]);

		}
		return dialogueModel;
	}
	public Model getEquippedModel(int gender) {
		int equipModelIdPrimary = maleEquipModelIdPrimary;
		int equipModelIdSecondary = maleEquipModelIdSecondary;
		int equipModelIdEmblem = maleEquipModelIdEmblem;
		if (gender == 1) {
			equipModelIdPrimary = femaleEquipModelIdPrimary;
			equipModelIdSecondary = femaleEquipModelIdSecondary;
			equipModelIdEmblem = femaleEquipModelIdEmblem;
		}
		if (equipModelIdPrimary == -1)
			return null;
		Model modelPrimary = Model.getModel(equipModelIdPrimary);
		if (equipModelIdSecondary != -1)
			if (equipModelIdEmblem != -1) {
				Model modelSecondary = Model.getModel(equipModelIdSecondary);
				Model modelEmblem = Model.getModel(equipModelIdEmblem);
				Model models[] = { modelPrimary, modelSecondary, modelEmblem };
				modelPrimary = new Model(3, models);
			} else {
				Model modelSecondary = Model.getModel(equipModelIdSecondary);
				Model models[] = { modelPrimary, modelSecondary };
				modelPrimary = new Model(2, models);
			}
		if (gender == 0 && equipModelTranslationMale != 0)
			modelPrimary.translate(0, equipModelTranslationMale, 0);
		if (gender == 1 && equipModelTranslationFemale != 0)
			modelPrimary.translate(0, equipModelTranslationFemale, 0);
		if (modifiedModelColors != null) {
			for (int c = 0; c < modifiedModelColors.length; c++)
				modelPrimary.recolour(modifiedModelColors[c],
						originalModelColors[c]);

		}
		return modelPrimary;
	}
	public Model getInventoryModel(int amount) {
		if (stackableIds != null && amount > 1) {
			int stackableId = -1;
			for (int i = 0; i < 10; i++)
				if (amount >= stackableAmounts[i] && stackableAmounts[i] != 0)
					stackableId = stackableIds[i];

			if (stackableId != -1)
				return getDefinition(stackableId).getInventoryModel(1);
		}
		Model stackedModel = Model.getModel(modelId);
		if (stackedModel == null)
			return null;
		if (modifiedModelColors != null) {
			for (int c = 0; c < modifiedModelColors.length; c++)
				stackedModel.recolour(modifiedModelColors[c],
						originalModelColors[c]);

		}
		return stackedModel;
	}
	public boolean isDialogueModelCached(int gender) {
		int dialogueModelId = maleDialogueModelId;
		int dialogueHatModelId = maleDialogueHatModelId;
		if (gender == 1) {
			dialogueModelId = femaleDialogueModelId;
			dialogueHatModelId = femaleDialogueHatModelId;
		}
		if (dialogueModelId == -1)
			return true;
		boolean cached = true;
		if (!Model.isCached(dialogueModelId))
			cached = false;
		if (dialogueHatModelId != -1 && !Model.isCached(dialogueHatModelId))
			cached = false;
		return cached;
	}
	private void readValues(Stream stream) {
		do {
			int attribute = stream.getUnsignedByte();
			if (attribute == 0)
				return;
			if (attribute == 1)
				modelId = stream.getUnsignedLEShort();
			else if (attribute == 2)
				name = stream.getString();
			else if (attribute == 3)
				description = stream.readBytes();
			else if (attribute == 4)
				modelZoom = stream.getUnsignedLEShort();
			else if (attribute == 5)
				modelRotationX = stream.getUnsignedLEShort();
			else if (attribute == 6)
				modelRotationY = stream.getUnsignedLEShort();
			else if (attribute == 7) {
				modelOffset1 = stream.getUnsignedLEShort();
				if (modelOffset1 > 32767)
					modelOffset1 -= 0x10000;
			} else if (attribute == 8) {
				modelOffset2 = stream.getUnsignedLEShort();
				if (modelOffset2 > 32767)
					modelOffset2 -= 0x10000;
			} else if (attribute == 10)
				stream.getUnsignedLEShort();
			else if (attribute == 11)
				stackable = true;
			else if (attribute == 12)
				value = stream.getInt();
			else if (attribute == 16)
				membersObject = true;
			else if (attribute == 23) {
				maleEquipModelIdPrimary = stream.getUnsignedLEShort();
				equipModelTranslationMale = stream.get();
			} else if (attribute == 24)
				maleEquipModelIdSecondary = stream.getUnsignedLEShort();
			else if (attribute == 25) {
				femaleEquipModelIdPrimary = stream.getUnsignedLEShort();
				equipModelTranslationFemale = stream.get();
			} else if (attribute == 26)
				femaleEquipModelIdSecondary = stream.getUnsignedLEShort();
			else if (attribute >= 30 && attribute < 35) {
				if (groundActions == null)
					groundActions = new String[5];
				groundActions[attribute - 30] = stream.getString();
				if (groundActions[attribute - 30].equalsIgnoreCase("hidden"))
					groundActions[attribute - 30] = null;
			} else if (attribute >= 35 && attribute < 40) {
				if (actions == null)
					actions = new String[5];
				actions[attribute - 35] = stream.getString();
			} else if (attribute == 40) {
				int colourCount = stream.getUnsignedByte();
				modifiedModelColors = new int[colourCount];
				originalModelColors = new int[colourCount];
				for (int c = 0; c < colourCount; c++) {
					modifiedModelColors[c] = stream.getUnsignedLEShort();
					originalModelColors[c] = stream.getUnsignedLEShort();
				}

			} else if (attribute == 78)
				maleEquipModelIdEmblem = stream.getUnsignedLEShort();
			else if (attribute == 79)
				femaleEquipModelIdEmblem = stream.getUnsignedLEShort();
			else if (attribute == 90)
				maleDialogueModelId = stream.getUnsignedLEShort();
			else if (attribute == 91)
				femaleDialogueModelId = stream.getUnsignedLEShort();
			else if (attribute == 92)
				maleDialogueHatModelId = stream.getUnsignedLEShort();
			else if (attribute == 93)
				femaleDialogueHatModelId = stream.getUnsignedLEShort();
			else if (attribute == 95)
				modelRotationZ = stream.getUnsignedLEShort();
			else if (attribute == 97)
				noteId = stream.getUnsignedLEShort();
			else if (attribute == 98)
				noteTemplateId = stream.getUnsignedLEShort();
			else if (attribute >= 100 && attribute < 110) {
				if (stackableIds == null) {
					stackableIds = new int[10];
					stackableAmounts = new int[10];
				}
				stackableIds[attribute - 100] = stream.getUnsignedLEShort();
				stackableAmounts[attribute - 100] = stream.getUnsignedLEShort();
			} else if (attribute == 110)
				modelScaleX = stream.getUnsignedLEShort();
			else if (attribute == 111)
				modelScaleY = stream.getUnsignedLEShort();
			else if (attribute == 112)
				modelScaleZ = stream.getUnsignedLEShort();
			else if (attribute == 113)
				lightModifier = stream.get();
			else if (attribute == 114)
				shadowModifier = stream.get() * 5;
			else if (attribute == 115)
				teamId = stream.getUnsignedByte();
		} while (true);
	}
	private void setDefaults() {
		modelId = 0;
		name = null;
		description = null;
		modifiedModelColors = null;
		originalModelColors = null;
		modelZoom = 2000;
		modelRotationX = 0;
		modelRotationY = 0;
		modelRotationZ = 0;
		modelOffset1 = 0;
		modelOffset2 = 0;
		stackable = false;
		value = 1;
		membersObject = false;
		groundActions = null;
		actions = null;
		maleEquipModelIdPrimary = -1;
		maleEquipModelIdSecondary = -1;
		equipModelTranslationMale = 0;
		femaleEquipModelIdPrimary = -1;
		femaleEquipModelIdSecondary = -1;
		equipModelTranslationFemale = 0;
		maleEquipModelIdEmblem = -1;
		femaleEquipModelIdEmblem = -1;
		maleDialogueModelId = -1;
		maleDialogueHatModelId = -1;
		femaleDialogueModelId = -1;
		femaleDialogueHatModelId = -1;
		stackableIds = null;
		stackableAmounts = null;
		noteId = -1;
		noteTemplateId = -1;
		modelScaleX = 128;
		modelScaleY = 128;
		modelScaleZ = 128;
		lightModifier = 0;
		shadowModifier = 0;
		teamId = 0;
	}
	private void toNote() {
		ItemDefinition noteTemplateDefinition = getDefinition(noteTemplateId);
		modelId = noteTemplateDefinition.modelId;
		modelZoom = noteTemplateDefinition.modelZoom;
		modelRotationX = noteTemplateDefinition.modelRotationX;
		modelRotationY = noteTemplateDefinition.modelRotationY;

		modelRotationZ = noteTemplateDefinition.modelRotationZ;
		modelOffset1 = noteTemplateDefinition.modelOffset1;
		modelOffset2 = noteTemplateDefinition.modelOffset2;
		modifiedModelColors = noteTemplateDefinition.modifiedModelColors;
		originalModelColors = noteTemplateDefinition.originalModelColors;

		ItemDefinition noteDefinition = getDefinition(noteId);
		name = noteDefinition.name;
		membersObject = noteDefinition.membersObject;
		value = noteDefinition.value;
		String prefix = "a";
		char firstCharacter = noteDefinition.name.charAt(0);
		if (firstCharacter == 'A' || firstCharacter == 'E'
				|| firstCharacter == 'I' || firstCharacter == 'O'
				|| firstCharacter == 'U')
			prefix = "an";
		description = ("Swap this note at any bank for " + prefix + " "
				+ noteDefinition.name + ".").getBytes();
		stackable = true;
	}

}
