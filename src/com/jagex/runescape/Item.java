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

final class Item extends Animable {

	public int itemId;

	public int x;

	public int y;
	public int itemCount;

	public Item() {
	}

	@Override
	public final Model getRotatedModel() {
		ItemDefinition itemDef = ItemDefinition.getDefinition(itemId);
		return itemDef.getAmountModel(itemCount);
	}
}
