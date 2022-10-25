package com.jagex.runescape;

import com.jagex.runescape.definition.ItemDefinition;

final class Item extends Animable {

    public int itemId;

    public int x;

    public int y;
    public int itemCount;

    public Item() {
    }

    @Override
    public Model getRotatedModel() {
        final ItemDefinition itemDef = ItemDefinition.getDefinition(this.itemId);
        return itemDef.getAmountModel(this.itemCount);
    }
}
