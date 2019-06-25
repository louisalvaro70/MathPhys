public class Vector{
    double x;
    double y;

    public Vector(){
        x = 0;
        y = 0;
    }
    
    public Vector (double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public double x(){
        return x;
    }
    
    public double y(){
        return y;
    }
    
    public void setX(double newX){
        x = newX;
    }
    
    public void setY(double newY){
        y = newY;
    }
}