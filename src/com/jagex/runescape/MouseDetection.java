package com.jagex.runescape;

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
