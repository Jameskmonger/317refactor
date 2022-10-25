package com.jagex.runescape;

import com.jagex.runescape.collection.Linkable;

final class GameObjectSpawnRequest extends Linkable {

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

    GameObjectSpawnRequest() {
        this.delayUntilRespawn = -1;
    }
}
