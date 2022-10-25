package com.jagex.runescape;

final class MouseDetection implements Runnable {

    private final Client clientInstance;

    public final Object syncObject;

    public final int[] coordsY;
    public boolean running;
    public final int[] coordsX;
    public int coordsIndex;

    public MouseDetection(final Client client1) {
        this.syncObject = new Object();
        this.coordsY = new int[500];
        this.running = true;
        this.coordsX = new int[500];
        this.clientInstance = client1;
    }

    @Override
    public void run() {
        while (this.running) {
            synchronized (this.syncObject) {
                if (this.coordsIndex < 500) {
                    this.coordsX[this.coordsIndex] = this.clientInstance.mouseX;
                    this.coordsY[this.coordsIndex] = this.clientInstance.mouseY;
                    this.coordsIndex++;
                }
            }
            try {
                Thread.sleep(50L);
            } catch (final Exception _ex) {
            }
        }
    }
}
