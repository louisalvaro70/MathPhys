import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

public class Billiard{
    private JFrame frame;
    private int frameHeight;

    //the collection of walls to be drawn
    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Ball> balls = new ArrayList<>();

    private Billiard(){
        //configure the main canvas
        frame = new JFrame ("Billiard");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
        frameHeight = frame.getHeight() - frame.getInsets().top;

        createObjects();

        DrawingArea drawingArea = new DrawingArea (frame.getWidth(), frameHeight, balls, walls);
        frame.add(drawingArea);

        drawingArea.start();
    }
    private void createObjects(){
        int wallWidth = (int) (frame.getWidth() * 0.9);
        int wallHeight = (int) (frame.getHeight() * 0.6);
        int wallX = (int) (frame.getWidth() * 0.05);
        int wallY = (int) (frame.getHeight() * 0.2);

        //vertical wall must be defined in clockwise direction
        //horizontal wall must be defined in counter clockwise direction 
        walls.add(new Wall(wallWidth + wallX, wallY, wallX, wallY));                            //top wall
        walls.add(new Wall(wallX, wallHeight + wallY, wallX, wallY));                           //left wall
        walls.add(new Wall(wallWidth + wallX, wallY, wallWidth + wallX, wallHeight + wallY));   //bottom wall
        walls.add(new Wall(wallWidth + wallX, wallHeight + wallY, wallX, wallHeight + wallY));  //right wall

        Random randomGenerator = new Random();

        for (int i = 0; i < 3; i++){
            int positionX = randomGenerator.nextInt(wallWidth - (int) (Ball.RADIUS)) + wallX + (int) Ball.RADIUS;
            int positionY = randomGenerator.nextInt(wallHeight - (int) (Ball.RADIUS)) + wallY + (int) Ball.RADIUS;
            Color color = new Color (randomGenerator.nextInt(255), randomGenerator.nextInt(255), randomGenerator.nextInt(255));
            balls.add(new Ball(positionX, positionY, color));
        }
    }
    public static void main(String[] args){
        EventQueue.invokeLater(Billiard::new);
    }
}