import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DrawingArea extends JPanel
{
    private int height;
    private int width;
    private boolean press = false;
    private ArrayList<Ball> balls;
    private ArrayList<Rope> ropes;
    private Thread animator;
    private BufferedImage dbImage;

    public DrawingArea(int width, int height, ArrayList<Ball> balls, ArrayList<Rope> ropes)
    {
        super(null);
        this.height = height;
        this.width = width;
        setBounds(0,0,width,height);
        this.balls = balls;
        this.ropes = ropes;
        animator = new Thread(this::eventLoop);
    }

    public void start()
    {
        animator.start();
    }

    public boolean isPressed()
    {
        return press;
    }

    public void setPress(boolean press)
    {
        this.press = press;
    }

    public void eventLoop() 
    {
        dbImage = (BufferedImage) createImage(width,height);
        while(true)
        {
            update();
            render();
            printScreen();
        }		
    }
    
    public void update()
    {
        //update the rope if no mouse is pressed
        if(!press)
        {
            for(Ball b: balls)
            {					
                b.move();
                b.checkCollision(balls);
            }
        }
    }
    
    public void render()
    {
        if(dbImage != null)
        {				
            //get graphics of the image where coordinate and function will be drawn
            Graphics g = dbImage.getGraphics();
        
            //clear screen
            g.setColor(new Color(200,200,150));
            g.fillRect(0, 0, getWidth(), getHeight());
            
            //draw the balls
            for(Ball b: balls)
            {
                if(b != null)
                      b.draw(g);
            }
            
            //draw the ropes				
            for(Rope r: ropes)
            {				
                r.draw(g);
            }							
        }
    }
    
    public void printScreen()
    {
        try
        {
            Graphics g = getGraphics();
            if(dbImage != null && g != null)
            {
                g.drawImage(dbImage, 0, 0, null);
            }
            
            // Sync the display on some systems.
            // (on Linux, this fixes event queue problems)
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        } 
        catch(Exception ex)
        {
            System.out.println("Graphics error: " + ex);  
        }		
    }
}
