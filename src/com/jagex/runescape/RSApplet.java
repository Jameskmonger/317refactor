package com.jagex.runescape;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class RSApplet extends Applet
    implements Runnable, MouseListener, MouseMotionListener, KeyListener, FocusListener, WindowListener {

    private int gameState;

    private int delayTime;

    int minDelay;

    private final long[] otims;

    int fps;

    boolean debugRequested;

    int width;

    int height;

    Graphics gameGraphics;

    RSImageProducer fullGameScreen;

    RSFrame gameFrame;

    private boolean clearScreen;

    boolean awtFocus;

    int idleTime;

    int mouseButton;

    public int mouseX;

    public int mouseY;

    private int eventMouseButton;

    private int eventClickX;

    private int eventClickY;

    private long eventClickTime;

    int clickType;

    int clickX;

    int clickY;

    long clickTime;

    final int[] keyStatus;

    private final int[] inputBuffer;

    private int readIndex;

    private int writeIndex;

    RSApplet() {
        this.delayTime = 20;
        this.minDelay = 1;
        this.otims = new long[10];
        this.debugRequested = false;
        this.clearScreen = true;
        this.awtFocus = true;
        this.keyStatus = new int[128];
        this.inputBuffer = new int[128];
    }

    void cleanUpForQuit() {
    }

    final void createClientFrame(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.gameFrame = new RSFrame(this, this.width, this.height);
        this.gameGraphics = this.getGameComponent().getGraphics();
        this.fullGameScreen = new RSImageProducer(this.width, height, this.getGameComponent());
        this.startRunnable(this, 1);
    }

    @Override
    public final void destroy() {
        this.gameState = -1;
        try {
            Thread.sleep(5000L);
        } catch (final Exception _ex) {
        }
        if (this.gameState == -1) {
            this.exit();
        }
    }

    void drawLoadingText(final int percentage, final String s) {
        while (this.gameGraphics == null) {
            this.gameGraphics = this.getGameComponent().getGraphics();
            try {
                this.getGameComponent().repaint();
            } catch (final Exception _ex) {
            }
            try {
                Thread.sleep(1000L);
            } catch (final Exception _ex) {
            }
        }
        final Font helveticaBold = new Font("Helvetica", Font.BOLD, 13);
        final FontMetrics fontmetrics = this.getGameComponent().getFontMetrics(helveticaBold);
        final Font helvetica = new Font("Helvetica", Font.PLAIN, 13);
        this.getGameComponent().getFontMetrics(helvetica);
        if (this.clearScreen) {
            this.gameGraphics.setColor(Color.black);
            this.gameGraphics.fillRect(0, 0, this.width, this.height);
            this.clearScreen = false;
        }
        final Color color = new Color(140, 17, 17);
        final int centerHeight = this.height / 2 - 18;
        this.gameGraphics.setColor(color);
        this.gameGraphics.drawRect(this.width / 2 - 152, centerHeight, 304, 34);
        this.gameGraphics.fillRect(this.width / 2 - 150, centerHeight + 2, percentage * 3, 30);
        this.gameGraphics.setColor(Color.black);
        this.gameGraphics.fillRect((this.width / 2 - 150) + percentage * 3, centerHeight + 2, 300 - percentage * 3, 30);
        this.gameGraphics.setFont(helveticaBold);
        this.gameGraphics.setColor(Color.white);
        this.gameGraphics.drawString(s, (this.width - fontmetrics.stringWidth(s)) / 2, centerHeight + 22);
    }

    private void exit() {
        this.gameState = -2;
        this.cleanUpForQuit();
        if (this.gameFrame != null) {
            try {
                Thread.sleep(1000L);
            } catch (final Exception _ex) {
            }
            try {
                System.exit(0);
            } catch (final Throwable _ex) {
            }
        }
    }

    @Override
    public final void focusGained(final FocusEvent focusevent) {
        this.awtFocus = true;
        this.clearScreen = true;
        this.redraw();
    }

    @Override
    public final void focusLost(final FocusEvent focusevent) {
        this.awtFocus = false;
        for (int key = 0; key < 128; key++) {
            this.keyStatus[key] = 0;
        }

    }

    Component getGameComponent() {
        if (this.gameFrame != null) {
            return this.gameFrame;
        } else {
            return this;
        }
    }

    final void initClientFrame(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.gameGraphics = this.getGameComponent().getGraphics();
        this.fullGameScreen = new RSImageProducer(this.width, this.height, this.getGameComponent());
        this.startRunnable(this, 1);
    }

    @Override
    public final void keyPressed(final KeyEvent keyevent) {
        this.idleTime = 0;
        final int keyCode = keyevent.getKeyCode();
        int keyChar = keyevent.getKeyChar();

        if (keyChar < 30) {
            keyChar = 0;
        }
        if (keyCode == 37) // Left
        {
            keyChar = 1;
        }
        if (keyCode == 39) // Right
        {
            keyChar = 2;
        }
        if (keyCode == 38) // Up
        {
            keyChar = 3;
        }
        if (keyCode == 40) // Down
        {
            keyChar = 4;
        }
        if (keyCode == 17) // CTRL
        {
            keyChar = 5;
        }
        if (keyCode == 8) // Backspace
        {
            keyChar = 8;
        }
        if (keyCode == 127) // Delete
        {
            keyChar = 8;
        }
        if (keyCode == 9) // Meant to be tab but doesn't work
        {
            keyChar = 9;
        }
        if (keyCode == 10) // Enter / return
        {
            keyChar = 10;
        }
        if (keyCode >= 112 && keyCode <= 123) // F keys
        {
            keyChar = (1008 + keyCode) - 112;
        }
        if (keyCode == 36) // Home
        {
            keyChar = 1000;
        }
        if (keyCode == 35) // End
        {
            keyChar = 1001;
        }
        if (keyCode == 33) // Page up
        {
            keyChar = 1002;
        }
        if (keyCode == 34) // Page down
        {
            keyChar = 1003;
        }
        if (keyChar > 0 && keyChar < 128) {
            this.keyStatus[keyChar] = 1;
        }
        if (keyChar > 4) {
            this.inputBuffer[this.writeIndex] = keyChar;
            this.writeIndex = this.writeIndex + 1 & 0x7f;
        }
    }

    @Override
    public final void keyReleased(final KeyEvent keyevent) {
        this.idleTime = 0;
        final int keyCode = keyevent.getKeyCode();
        char keyChar = keyevent.getKeyChar();
        if (keyChar < '\036') {
            keyChar = '\0';
        }
        if (keyCode == 37) {
            keyChar = '\001';
        }
        if (keyCode == 39) {
            keyChar = '\002';
        }
        if (keyCode == 38) {
            keyChar = '\003';
        }
        if (keyCode == 40) {
            keyChar = '\004';
        }
        if (keyCode == 17) {
            keyChar = '\005';
        }
        if (keyCode == 8) {
            keyChar = '\b';
        }
        if (keyCode == 127) {
            keyChar = '\b';
        }
        if (keyCode == 9) {
            keyChar = '\t';
        }
        if (keyCode == 10) {
            keyChar = '\n';
        }
        if (keyChar > 0 && keyChar < '\200') {
            this.keyStatus[keyChar] = 0;
        }
    }

    @Override
    public final void keyTyped(final KeyEvent keyevent) {
    }

    @Override
    public final void mouseClicked(final MouseEvent mouseevent) {
    }

    @Override
    public final void mouseDragged(final MouseEvent mouseevent) {
        int x = mouseevent.getX();
        int y = mouseevent.getY();
        if (this.gameFrame != null) {
            x -= 4;
            y -= 22;
        }
        this.idleTime = 0;
        this.mouseX = x;
        this.mouseY = y;
    }

    @Override
    public final void mouseEntered(final MouseEvent mouseevent) {
    }

    @Override
    public final void mouseExited(final MouseEvent mouseevent) {
        this.idleTime = 0;
        this.mouseX = -1;
        this.mouseY = -1;
    }

    @Override
    public final void mouseMoved(final MouseEvent mouseevent) {
        int x = mouseevent.getX();
        int y = mouseevent.getY();
        if (this.gameFrame != null) {
            x -= 4;
            y -= 22;
        }
        this.idleTime = 0;
        this.mouseX = x;
        this.mouseY = y;
    }

    @Override
    public final void mousePressed(final MouseEvent mouseEvent) {
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();
        if (this.gameFrame != null) {
            x -= 4;
            y -= 22;
        }
        this.idleTime = 0;
        this.eventClickX = x;
        this.eventClickY = y;
        this.eventClickTime = System.currentTimeMillis();
        if (mouseEvent.isMetaDown()) {
            this.eventMouseButton = 2;
            this.mouseButton = 2;
        } else {
            this.eventMouseButton = 1;
            this.mouseButton = 1;
        }
    }

    @Override
    public final void mouseReleased(final MouseEvent mouseevent) {
        this.idleTime = 0;
        this.mouseButton = 0;
    }

    @Override
    public final void paint(final Graphics g) {
        if (this.gameGraphics == null) {
            this.gameGraphics = g;
        }
        this.clearScreen = true;
        this.redraw();
    }

    void processDrawing() {
    }

    void processGameLoop() {
    }

    final int readCharacter() {
        int character = -1;
        if (this.writeIndex != this.readIndex) {
            character = this.inputBuffer[this.readIndex];
            this.readIndex = this.readIndex + 1 & 0x7f;
        }
        return character;
    }

    void redraw() {
    }

    @Override
    public void run() {
        this.getGameComponent().addMouseListener(this);
        this.getGameComponent().addMouseMotionListener(this);
        this.getGameComponent().addKeyListener(this);
        this.getGameComponent().addFocusListener(this);
        if (this.gameFrame != null) {
            this.gameFrame.addWindowListener(this);
        }
        this.drawLoadingText(0, "Loading...");
        this.startUp();
        int opos = 0;
        int ratio = 256;
        int delay = 1;
        int count = 0;
        int intex = 0;
        for (int otim = 0; otim < 10; otim++) {
            this.otims[otim] = System.currentTimeMillis();
        }

        while (this.gameState >= 0) {
            if (this.gameState > 0) {
                this.gameState--;
                if (this.gameState == 0) {
                    this.exit();
                    return;
                }
            }
            final int i2 = ratio;
            final int j2 = delay;
            ratio = 300;
            delay = 1;
            final long currentTime = System.currentTimeMillis();
            if (this.otims[opos] == 0L) {
                ratio = i2;
                delay = j2;
            } else if (currentTime > this.otims[opos]) {
                ratio = (int) (2560 * this.delayTime / (currentTime - this.otims[opos]));
            }
            if (ratio < 25) {
                ratio = 25;
            }
            if (ratio > 256) {
                ratio = 256;
                delay = (int) (this.delayTime - (currentTime - this.otims[opos]) / 10L);
            }
            if (delay > this.delayTime) {
                delay = this.delayTime;
            }
            this.otims[opos] = currentTime;
            opos = (opos + 1) % 10;
            if (delay > 1) {
                for (int otim = 0; otim < 10; otim++) {
                    if (this.otims[otim] != 0L) {
                        this.otims[otim] += delay;
                    }
                }

            }
            if (delay < this.minDelay) {
                delay = this.minDelay;
            }
            try {
                Thread.sleep(delay);
            } catch (final InterruptedException _ex) {
                intex++;
            }
            for (; count < 256; count += ratio) {
                this.clickType = this.eventMouseButton;
                this.clickX = this.eventClickX;
                this.clickY = this.eventClickY;
                this.clickTime = this.eventClickTime;
                this.eventMouseButton = 0;
                this.processGameLoop();
                this.readIndex = this.writeIndex;
            }

            count &= 0xff;
            if (this.delayTime > 0) {
                this.fps = (1000 * ratio) / (this.delayTime * 256);
            }
            this.processDrawing();
            if (this.debugRequested) {
                System.out.println("ntime:" + currentTime);
                for (int i = 0; i < 10; i++) {
                    final int otim = ((opos - i - 1) + 20) % 10;
                    System.out.println("otim" + otim + ":" + this.otims[otim]);
                }

                System.out.println("fps:" + this.fps + " ratio:" + ratio + " count:" + count);
                System.out.println("del:" + delay + " deltime:" + this.delayTime + " mindel:" + this.minDelay);
                System.out.println("intex:" + intex + " opos:" + opos);
                this.debugRequested = false;
                intex = 0;
            }
        }
        if (this.gameState == -1) {
            this.exit();
        }
    }

    final void setFrameRate(final int frameRate) {
        this.delayTime = 1000 / frameRate;
    }

    @Override
    public final void start() {
        if (this.gameState >= 0) {
            this.gameState = 0;
        }
    }

    public void startRunnable(final Runnable runnable, final int priority) {
        final Thread thread = new Thread(runnable);
        thread.start();
        thread.setPriority(priority);
    }

    void startUp() {
    }

    @Override
    public final void stop() {
        if (this.gameState >= 0) {
            this.gameState = 4000 / this.delayTime;
        }
    }

    @Override
    public final void update(final Graphics g) {
        if (this.gameGraphics == null) {
            this.gameGraphics = g;
        }
        this.clearScreen = true;
        this.redraw();
    }

    @Override
    public final void windowActivated(final WindowEvent windowevent) {
    }

    @Override
    public final void windowClosed(final WindowEvent windowevent) {
    }

    @Override
    public final void windowClosing(final WindowEvent windowevent) {
        this.destroy();
    }

    @Override
    public final void windowDeactivated(final WindowEvent windowevent) {
    }

    @Override
    public final void windowDeiconified(final WindowEvent windowevent) {
    }

    @Override
    public final void windowIconified(final WindowEvent windowevent) {
    }

    @Override
    public final void windowOpened(final WindowEvent windowevent) {
    }
}
