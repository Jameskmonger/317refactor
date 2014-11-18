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

final class GameObjectSpawnRequest extends Node {

    GameObjectSpawnRequest()
    {
        delayUntilRespawn = -1;
    }

    public int id2;
    public int face2;
    public int type2;
    public int delayUntilRespawn;
    public int z;
    public int objectType;
    public int x;
    public int y;
    public int id;
    public int face;
    public int type;
    public int delayUntilSpawn;
}
