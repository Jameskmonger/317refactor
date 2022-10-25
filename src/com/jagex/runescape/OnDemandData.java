package com.jagex.runescape;

import com.jagex.runescape.collection.Cacheable;

public final class OnDemandData extends Cacheable {

    int dataType;

    byte[] buffer;
    int id;
    boolean incomplete;
    int loopCycle;

    public OnDemandData() {
        this.incomplete = true;
    }
}
