package com.jagex.runescape;

final class GameObject extends Animable {

	private int frame;

	private final int[] childrenIds;

	private final int varBitId;

	private final int configId;
	private final int vertexHeightBottomLeft;
	private final int vertexHeightBottomRight;
	private final int vertexHeightTopRight;
	private final int vertexHeightTopLeft;
	private AnimationSequence animation;
	private int nextFrameTime;
	public static Client clientInstance;
	private final int objectId;
	private final int type;
	private final int orientation;

	public GameObject(int objectId, int orientation, int type, int vertexHeightBottomRight, int vertexHeightTopRight,
			int vertexHeightBottomLeft, int vertexHeightTopLeft, int animationId, boolean animating) {
		this.objectId = objectId;
		this.type = type;
		this.orientation = orientation;
		this.vertexHeightBottomLeft = vertexHeightBottomLeft;
		this.vertexHeightBottomRight = vertexHeightBottomRight;
		this.vertexHeightTopRight = vertexHeightTopRight;
		this.vertexHeightTopLeft = vertexHeightTopLeft;
		if (animationId != -1) {
			animation = AnimationSequence.animations[animationId];
			frame = 0;
			nextFrameTime = Client.tick;
			if (animating && animation.frameStep != -1) {
				frame = (int) (Math.random() * animation.frameCount);
				nextFrameTime -= (int) (Math.random() * animation.getFrameLength(frame));
			}
		}
		GameObjectDefinition definition = GameObjectDefinition.getDefinition(this.objectId);
		varBitId = definition.varBitId;
		configId = definition.configIds;
		childrenIds = definition.childIds;
	}

	private GameObjectDefinition getChildDefinition() {
		int child = -1;
		if (varBitId != -1) {
			VarBit varBit = VarBit.cache[varBitId];
			int configId = varBit.configId;
			int lsb = varBit.leastSignificantBit;
			int msb = varBit.mostSignificantBit;
			int bit = Client.BITFIELD_MAX_VALUE[msb - lsb];
			child = clientInstance.interfaceSettings[configId] >> lsb & bit;
		} else if (configId != -1)
			child = clientInstance.interfaceSettings[configId];
		if (child < 0 || child >= childrenIds.length || childrenIds[child] == -1)
			return null;
		else
			return GameObjectDefinition.getDefinition(childrenIds[child]);
	}

	@Override
	public Model getRotatedModel() {
		int animationId = -1;
		if (animation != null) {
			int step = Client.tick - nextFrameTime;
			if (step > 100 && animation.frameStep > 0)
				step = 100;
			while (step > animation.getFrameLength(frame)) {
				step -= animation.getFrameLength(frame);
				frame++;
				if (frame < animation.frameCount)
					continue;
				frame -= animation.frameStep;
				if (frame >= 0 && frame < animation.frameCount)
					continue;
				animation = null;
				break;
			}
			nextFrameTime = Client.tick - step;
			if (animation != null)
				animationId = animation.frame2Ids[frame];
		}
		GameObjectDefinition definition;
		if (childrenIds != null)
			definition = getChildDefinition();
		else
			definition = GameObjectDefinition.getDefinition(objectId);
		if (definition == null) {
			return null;
		} else {
			return definition.getModelAt(type, orientation, vertexHeightBottomLeft, vertexHeightBottomRight,
					vertexHeightTopRight, vertexHeightTopLeft, animationId);
		}
	}
}
