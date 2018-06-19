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

public class Entity extends Animable {

	public int entScreenX;

	public int entScreenY;

	public final int index = -1;

	public final int[] waypointX;

	public final int[] waypointY;
	public int interactingEntity;
	int stepsDelayed;

	int degreesToTurn;

	int runAnimationId;

	public String overheadTextMessage;
	public int height;
	public int turnDirection;
	int standAnimationId;
	int standTurnAnimationId;
	int chatColour;
	final int[] hitArray;
	final int[] hitMarkTypes;
	final int[] hitsLoopCycle;
	int queuedAnimationId;
	int queuedAnimationFrame;
	int queuedAnimationDuration;
	int graphicId;
	int currentAnimationId;
	int currentAnimationTimeRemaining;
	int graphicEndCycle;
	int graphicHeight;
	int waypointCount;
	public int animation;
	int currentAnimationFrame;
	int currentAnimationDuration;
	int animationDelay;
	int currentAnimationLoopCount;
	int chatEffect;
	public int loopCycleStatus;
	public int currentHealth;
	public int maxHealth;
	int textCycle;
	int lastUpdateTick;
	int faceTowardX;
	int faceTowardY;
	int boundaryDimension;
	boolean dynamic;
	int stepsRemaining;
	int startX;
	int endX;
	int startY;
	int endY;
	int tickStart;
	int tickEnd;
	int direction;
	public int x;
	public int y;
	int currentRotation;
	final boolean[] waypointRan;
	int walkAnimationId;
	int turnAboutAnimationId;
	int turnRightAnimationId;
	int turnLeftAnimationId;

	Entity() {
		waypointX = new int[10];
		waypointY = new int[10];
		interactingEntity = -1;
		degreesToTurn = 32;
		runAnimationId = -1;
		height = 200;
		standAnimationId = -1;
		standTurnAnimationId = -1;
		hitArray = new int[4];
		hitMarkTypes = new int[4];
		hitsLoopCycle = new int[4];
		queuedAnimationId = -1;
		graphicId = -1;
		animation = -1;
		loopCycleStatus = -1000;
		textCycle = 100;
		boundaryDimension = 1;
		dynamic = false;
		waypointRan = new boolean[10];
		walkAnimationId = -1;
		turnAboutAnimationId = -1;
		turnRightAnimationId = -1;
		turnLeftAnimationId = -1;
	}

	public boolean isVisible() {
		return false;
	}

	public final void move(boolean flag, int direction) {
		int x = waypointX[0];
		int y = waypointY[0];
		if (direction == 0) {
			x--;
			y++;
		}
		if (direction == 1)
			y++;
		if (direction == 2) {
			x++;
			y++;
		}
		if (direction == 3)
			x--;
		if (direction == 4)
			x++;
		if (direction == 5) {
			x--;
			y--;
		}
		if (direction == 6)
			y--;
		if (direction == 7) {
			x++;
			y--;
		}
		if (animation != -1 && AnimationSequence.animations[animation].precedenceWalking == 1)
			animation = -1;
		if (waypointCount < 9)
			waypointCount++;
		for (int l = waypointCount; l > 0; l--) {
			waypointX[l] = waypointX[l - 1];
			waypointY[l] = waypointY[l - 1];
			waypointRan[l] = waypointRan[l - 1];
		}
		waypointX[0] = x;
		waypointY[0] = y;
		waypointRan[0] = flag;
	}

	public final void resetPath() {
		waypointCount = 0;
		stepsRemaining = 0;
	}

	public final void setPos(int x, int y, boolean teleported) {
		if (animation != -1 && AnimationSequence.animations[animation].precedenceWalking == 1)
			animation = -1;
		if (!teleported) {
			int distanceX = x - waypointX[0];
			int distanceY = y - waypointY[0];
			if (distanceX >= -8 && distanceX <= 8 && distanceY >= -8 && distanceY <= 8) {
				if (waypointCount < 9)
					waypointCount++;
				for (int waypoint = waypointCount; waypoint > 0; waypoint--) {
					waypointX[waypoint] = waypointX[waypoint - 1];
					waypointY[waypoint] = waypointY[waypoint - 1];
					waypointRan[waypoint] = waypointRan[waypoint - 1];
				}

				waypointX[0] = x;
				waypointY[0] = y;
				waypointRan[0] = false;
				return;
			}
		}
		waypointCount = 0;
		stepsRemaining = 0;
		stepsDelayed = 0;
		waypointX[0] = x;
		waypointY[0] = y;
		this.x = waypointX[0] * 128 + boundaryDimension * 64;
		this.y = waypointY[0] * 128 + boundaryDimension * 64;
	}

	public final void updateHitData(int type, int damage, int currentTime) {
		for (int hit = 0; hit < 4; hit++)
			if (hitsLoopCycle[hit] <= currentTime) {
				hitArray[hit] = damage;
				hitMarkTypes[hit] = type;
				hitsLoopCycle[hit] = currentTime + 70;
				return;
			}
	}
}
