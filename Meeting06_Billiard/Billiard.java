/*
    Matfis Pertemuan 6
    Half-fledged billiard
    To do:
    1. create billiard balls, use 8-ball rules
    2. assingn one ball to be the hitter (preferably not colored white)
    3. create guideline to help aiming the hitter
    4. add collision for ball against wall and ball against balls
    5. create holes for the balls
    6. add additional mechanics for the game and scoring system
*/

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class Billiard{
    private JFrame frame;
    private int frameHeight;

    //the collection of walls to be drawn
    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Ball> balls = new ArrayList<>();

    private Ball hitter;
    private Vector destination;

    private Billiard(){
        //configure the main canvas
        frame = new JFrame ("Billiard");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        frame.setLayout(null);
        frame.setVisible(true);
        frameHeight = frame.getHeight() - frame.getInsets().top;

        createObjects();
        hitter = new Ball((frame.getWidth() * 0.66) - 400.0, frame.getHeight() / 2.0, 30.0, 10.0, Color.RED);
        balls.add(new Ball(140, 245, 50.0, 1.0, Color.BLACK));
        balls.add(new Ball(frame.getWidth() / 2.0, 250, 50.0, 1.0, Color.BLACK));
        balls.add(new Ball(1800, 250, 50.0, 1.0, Color.BLACK));
        balls.add(new Ball(140, 760, 50.0, 1.0, Color.BLACK));
        balls.add(new Ball(frame.getWidth() / 2.0, 760, 50.0, 1.0, Color.BLACK));
        balls.add(new Ball(1800, 760, 50.0, 1.0, Color.BLACK));
        balls.add(hitter);
        destination = new Vector(hitter.getPositionX(), hitter.getPositionY());

        DrawingArea drawingArea = new DrawingArea (frame.getWidth(), frameHeight, balls, walls, balls.size() - 1, destination);
        frame.add(drawingArea);

        frame.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                super.mousePressed(e);
                drawingArea.setPress(true);
                destination.setX((double) e.getX());
                destination.setY((double) e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e){
                super.mouseReleased(e);

                double distanceX = e.getX() - hitter.getPositionX();
                double distanceY = e.getY() - hitter.getPositionY();

                hitter.setVelocityX(drawingArea.getTime() * distanceX / distance);
                hitter.setVelocityY(drawingArea.getTime() * distanceY / distance);

                drawingArea.setPress(false);
            }
        });

        frame.addMouseMotionListener(new MouseAdapter(){
            @Override
            public void mouseMoved(MouseEvent e){
                super.mouseMoved(e);
                destination.setX((double) e.getX());
                destination.setY((double) e.getY());
            }
        });
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

        double x = frame.getWidth() * 0.66;             
        double cy = frame.getHeight() / 2.0;

        createBalls(x, cy);

        /*
            Pseudocode

            procedure(x, cy) //cy = starting coordinate of y-axis

                for i = 1 to 5
                    y = cy
                    for j = 0 to i
                        create ball(x, y)
                        y = y + 2r
                    x = x + r (sqrt(3))
                    y = y - (i - 1)r
        */

        //create balls
        public void createBalls(double x, double cy){
            double y;
            Random randomGenerator = new Random();
            for (int i = 0; i <= 5; i++){
                y = cy;
                x = x + (30.0 * Math.sqrt(3.0));
                y = y - (i - 1) * 30.0;
                for (int j = 0; j < i; j++){
                    Color color = new Color(randomGenerator.nextInt(255), randomGenerator.nextInt(255), randomGenerator.nextInt(255));
                    balls.add(new Ball(x, y, 30.0, 10.0, color));
                    y = y + (2*30.0);
                }
            }
        }

    public static void main(String[] args){
        EventQueue.invokeLater(Billiard::new);
    }
}