import java.awt.Color;
import java.awt.Graphics;

public class Wall{
    private double x1, y1, x2, y2;

    public Wall (double x1, double y1, double x2, double y2){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public void draw(Graphics g){
        Color tempColor = g.getColor();
        g.setColor(Color.BLACK);
        g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        g.setColor(tempColor);
    }

    public Vector normalLine(){
        //normal vector to be returned
        Vector normVector = new Vector();
        double normVectorX, normVectorY;
        //wall vector
        Vector wallVector = new Vector();
        wallVector.setX(x2-x1);
        wallVector.setY(y2-y1);

        //calculate the normal vector, then calculate the unit vector
        normVectorY = 1;
        normVectorX = (-1)*(wallVector.getY()/wallVector.getX());
  
        //System.out.println(normVector.getX() + " - " + normVector.getY());
        double normVectorLength = Math.sqrt(normVectorX*normVectorX + normVectorY*normVectorY);
        normVector.setX(normVectorX/normVectorLength);
        normVector.setY(normVectorY/normVectorLength);
        return normVector;
    }

    public double distanceFromPoint(double xPoint, double yPoint){
        double distance, b, a;
        b = (x2-x1);
        a = (y2-y1);
        distance = Math.abs(a*xPoint + (-b)*yPoint + (b*y1 - a*x1));
        distance = distance / Math.sqrt(a*a + b*b);
        return distance;
    }

    public Vector nearestPoint(double xPoint, double yPoint){
        //point to be returned
        Vector nearestPoint = new Vector();
        //the x and y coordinate in the line which is nearest from the point given
        double nearestX, nearestY;

        //calculating
        double a = y2-y1, b = x2-x1;
        nearestX = (-b*(-b*xPoint - a*yPoint) - a*(b*y1 - a*x1));
        nearestX = nearestX/(a*a + b*b);
        nearestY = (a*(b*xPoint + a*yPoint) - (-b)*(b*y1 - a*x1));
        nearestY = nearestY/(a*a + b*b);

        //return the point after setting it up
        nearestPoint.setX(nearestX);
        nearestPoint.setY(nearestY);
        return nearestPoint;
    }

    public double getHeight(){
        return y2-y1;
    }

    public double getWidth(){
        return x2-x1;
    }
}