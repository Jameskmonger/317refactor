import java.awt.*;

final class RSFrame extends Frame
{

    public RSFrame(RSApplet applet, int width, int height)
    {
        this.applet = applet;
        setTitle("Jagex");
        setResizable(false);
        setVisible(true);
        toFront();
        setSize(width + 8, height + 28);
    }

    public Graphics getGraphics()
    {
        Graphics graphics = super.getGraphics();
        graphics.translate(4, 24);
        return graphics;
    }

    public void update(Graphics graphics)
    {
        applet.update(graphics);
    }

    public void paint(Graphics graphics)
    {
        applet.paint(graphics);
    }

    private final RSApplet applet;
}
