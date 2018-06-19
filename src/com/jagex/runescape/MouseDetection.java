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

final class MouseDetection implements Runnable {

	private Client clientInstance;

	public final Object syncObject;

	public final int[] coordsY;
	public boolean running;
	public final int[] coordsX;
	public int coordsIndex;

	public MouseDetection(Client client1) {
		syncObject = new Object();
		coordsY = new int[500];
		running = true;
		coordsX = new int[500];
		clientInstance = client1;
	}

	@Override
	public void run() {
		while (running) {
			synchronized (syncObject) {
				if (coordsIndex < 500) {
					coordsX[coordsIndex] = clientInstance.mouseX;
					coordsY[coordsIndex] = clientInstance.mouseY;
					coordsIndex++;
				}
			}
			try {
				Thread.sleep(50L);
			} catch (Exception _ex) {
			}
		}
	}
}
