import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

class DrawingArea extends JPanel {
    public final static int GRAPH_SCALE = 30;
    private double time = 0;
    private final static double TIME_INCREMENT = 0.05;
    private int width;
    private int height;
    private int originX;        // the origin points (0, 0)
    private int originY;
    private int lengthX;        // how many numbers shown along absis and ordinate
    private int lengthY;
    private Image drawingArea;
    private Thread animator;    // thread to draw the
    private Cannon cannon;
    private Bullet bullet;

    // setup the drawing area
    public DrawingArea(int width, int height, int cpSize) {
        super(null);
        this.width = width - cpSize;
        this.height = height;
        setBounds(cpSize, 0, this.width, this.height);
        drawingArea = createImage(this.width, this.height);

        originX = this.width / 4;
        originY = this.height / 4;
        lengthX = (this.width - originX) / GRAPH_SCALE;
        lengthY = (this.height - originY) / GRAPH_SCALE;

        // trigger drawing process
        drawingArea = createImage(this.width, this.height);
        animator = new Thread(this::eventLoop);
    }

    public void start() {
        animator.start();
    }

    public void setCannon(Cannon cannon) {
        this.cannon = cannon;
    }

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
        this.bullet.setTime(time);
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    private void eventLoop() {
        drawingArea = createImage(width, height);
        while (true) {
            update();
            render();
            printScreen();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }

    private void update() {
        time += TIME_INCREMENT;
        if (bullet != null && bullet.isShot()) {
            bullet.move(time);
            if (bullet.getPositionY() > getHeight()) {
                bullet.stopShoot();
            }
        }
    }

    private void render() {
        if (drawingArea != null) {
            //get graphics of the image where coordinate and function will be drawn
            Graphics g = drawingArea.getGraphics();

            // clear screen
            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.black);
            //draw the x-axis and y-axis
            g.drawLine(0, originY, getWidth(), originY);
            g.drawLine(originX, 0, originX, getHeight());

            //print numbers on the x-axis and y-axis, based on the scale
            for (int i = 0; i < lengthX; i++) {
                g.drawString(Integer.toString(i), (originX + (i * GRAPH_SCALE)), originY);
                g.drawString(Integer.toString(-1 * i), (originX + (-i * GRAPH_SCALE)), originY);
            }
            for (int i = 0; i < lengthY; i++) {
                g.drawString(Integer.toString(-1 * i), originX, (originY + (i * GRAPH_SCALE)));
                g.drawString(Integer.toString(i), originX, (originY + (-i * GRAPH_SCALE)));
            }

            // draw cannon and bullet
            cannon.draw(g);
            if (bullet != null && bullet.isShot()) {
                bullet.draw(g);
            }
        }
    }

    private void printScreen()
    {
        try
        {
            Graphics g = getGraphics();
            if(drawingArea != null && g != null)
            {
                g.drawImage(drawingArea, 0, 0, null);
            }

            // Sync the display on some systems.
            // (on Linux, this fixes event queue problems)
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        }
        catch(Exception ex)
        {
            System.out.print("Graphics error: ");
            ex.printStackTrace();
        }
    }
}