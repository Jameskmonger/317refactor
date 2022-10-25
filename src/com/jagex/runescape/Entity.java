package com.jagex.runescape;

import com.jagex.runescape.definition.AnimationSequence;

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
        this.waypointX = new int[10];
        this.waypointY = new int[10];
        this.interactingEntity = -1;
        this.degreesToTurn = 32;
        this.runAnimationId = -1;
        this.height = 200;
        this.standAnimationId = -1;
        this.standTurnAnimationId = -1;
        this.hitArray = new int[4];
        this.hitMarkTypes = new int[4];
        this.hitsLoopCycle = new int[4];
        this.queuedAnimationId = -1;
        this.graphicId = -1;
        this.animation = -1;
        this.loopCycleStatus = -1000;
        this.textCycle = 100;
        this.boundaryDimension = 1;
        this.dynamic = false;
        this.waypointRan = new boolean[10];
        this.walkAnimationId = -1;
        this.turnAboutAnimationId = -1;
        this.turnRightAnimationId = -1;
        this.turnLeftAnimationId = -1;
    }

    public boolean isVisible() {
        return false;
    }

    public final void move(final boolean flag, final int direction) {
        int x = this.waypointX[0];
        int y = this.waypointY[0];
        if (direction == 0) {
            x--;
            y++;
        }
        if (direction == 1) {
            y++;
        }
        if (direction == 2) {
            x++;
            y++;
        }
        if (direction == 3) {
            x--;
        }
        if (direction == 4) {
            x++;
        }
        if (direction == 5) {
            x--;
            y--;
        }
        if (direction == 6) {
            y--;
        }
        if (direction == 7) {
            x++;
            y--;
        }
        if (this.animation != -1 && AnimationSequence.animations[this.animation].precedenceWalking == 1) {
            this.animation = -1;
        }
        if (this.waypointCount < 9) {
            this.waypointCount++;
        }
        for (int l = this.waypointCount; l > 0; l--) {
            this.waypointX[l] = this.waypointX[l - 1];
            this.waypointY[l] = this.waypointY[l - 1];
            this.waypointRan[l] = this.waypointRan[l - 1];
        }
        this.waypointX[0] = x;
        this.waypointY[0] = y;
        this.waypointRan[0] = flag;
    }

    public final void resetPath() {
        this.waypointCount = 0;
        this.stepsRemaining = 0;
    }

    public final void setPos(final int x, final int y, final boolean teleported) {
        if (this.animation != -1 && AnimationSequence.animations[this.animation].precedenceWalking == 1) {
            this.animation = -1;
        }
        if (!teleported) {
            final int distanceX = x - this.waypointX[0];
            final int distanceY = y - this.waypointY[0];
            if (distanceX >= -8 && distanceX <= 8 && distanceY >= -8 && distanceY <= 8) {
                if (this.waypointCount < 9) {
                    this.waypointCount++;
                }
                for (int waypoint = this.waypointCount; waypoint > 0; waypoint--) {
                    this.waypointX[waypoint] = this.waypointX[waypoint - 1];
                    this.waypointY[waypoint] = this.waypointY[waypoint - 1];
                    this.waypointRan[waypoint] = this.waypointRan[waypoint - 1];
                }

                this.waypointX[0] = x;
                this.waypointY[0] = y;
                this.waypointRan[0] = false;
                return;
            }
        }
        this.waypointCount = 0;
        this.stepsRemaining = 0;
        this.stepsDelayed = 0;
        this.waypointX[0] = x;
        this.waypointY[0] = y;
        this.x = this.waypointX[0] * 128 + this.boundaryDimension * 64;
        this.y = this.waypointY[0] * 128 + this.boundaryDimension * 64;
    }

    public final void updateHitData(final int type, final int damage, final int currentTime) {
        for (int hit = 0; hit < 4; hit++) {
            if (this.hitsLoopCycle[hit] <= currentTime) {
                this.hitArray[hit] = damage;
                this.hitMarkTypes[hit] = type;
                this.hitsLoopCycle[hit] = currentTime + 70;
                return;
            }
        }
    }
}
