/*To do #3*/

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/*
    MatFis pertemuan 3
    Collision between parabolically moving object against wall
    TODO:
     0. Review about elastic and inelastic collisions. What happened when you change the coefficient of resistution (COR)?
     1. Add more balls with different colors, sizes, and velocities - done
     2. Create UI to add new balls and delete some instances - done
     3. Add COR field to the UI, so user can choose between using different COR than the default or not - done
     4. Turn all balls into linearly moving ones (apply Newton's first law here).
     5. Create diagonal walls and modify the calculation to adjust with diagonal walls
     6. Create UI to customize the walls
 */


public class Dribble extends JFrame {
    private JFrame frame;
    private DrawingArea drawingArea;
    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Ball> balls = new ArrayList<>();
    private ArrayList<Double> corCount = new ArrayList<>();
    
    double cor = 0.7;
    double newCor;

    public Dribble() {
        //configure the main canvas
        frame = new JFrame("Dribbling Balls");

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JFrame commandFrame = new JFrame("Ball creator & killer");
        JLabel positionXLabel = new JLabel("Position (X-axis)");
        positionXLabel.setBounds(10,30,130,30);
        JLabel positionYLabel = new JLabel("Position (Y-axis)");
        positionYLabel.setBounds(10,70,130,30);
        JLabel setRadiusLabel = new JLabel("Radius");
        setRadiusLabel.setBounds(10,110,130,30);
        JLabel velocityXLabel = new JLabel("Velocity (X-axis)");
        velocityXLabel.setBounds(10,150,130,30);
        JLabel velocityYLabel = new JLabel("Velocity (Y-axis)");
        velocityYLabel.setBounds(10,190,130,30);
        JLabel setColorLabel = new JLabel("Color");
        setColorLabel.setBounds(10,230,130,30);

        commandFrame.add(positionXLabel);
        commandFrame.add(positionYLabel);
        commandFrame.add(setRadiusLabel);
        commandFrame.add(velocityXLabel);
        commandFrame.add(velocityYLabel);
        commandFrame.add(setColorLabel);
        commandFrame.add(setColorLabel);

        JTextField posX = new JTextField();
        posX.setBounds(130,30,100,30);
        JTextField posY = new JTextField();
        posY.setBounds(130,70,100,30);
        JTextField setRadius = new JTextField();
        setRadius.setBounds(130,110,100,30);
        JTextField velocityX = new JTextField();
        velocityX.setBounds(130,150,100,30);
        JTextField velocityY = new JTextField();
        velocityY.setBounds(130,190,100,30);
        
        JLabel corLabel = new JLabel ("Coefficient of Restitution");
        corLabel.setBounds(350, 30, 150, 30);
        commandFrame.add(corLabel);

        JLabel info = new JLabel ("If empty, the COR will remain at 0.7");
        info.setBounds(430, 70, 400, 50);
        commandFrame.add(info);

        JLabel info2 = new JLabel("the COR will take effect on the next ball");
        info2.setBounds(403, 90, 430, 50);
        commandFrame.add(info2);

        /* COR */
        JTextField corValue = new JTextField();
        corValue.setBounds(550, 30, 100, 30);
        commandFrame.add(corValue);

        JTextField setColor = new JTextField();
        setColor.setBounds(130, 230, 100, 30);
        commandFrame.add(posX);
        commandFrame.add(posY);
        commandFrame.add(setRadius);
        commandFrame.add(velocityX);
        commandFrame.add(velocityY);
        commandFrame.add(setColor);        
        
        JButton button = new JButton("Create new ball");                //create ball
        button.setBounds(100,300,100,20);                               //position x, position y, width, height

        JButton kill = new JButton("Kill a ball");                      //kill ball
        kill.setBounds(300,300,100,20);
        kill.addActionListener(new CustomActionListener());
       
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println(corValue.getText().isEmpty());     //check if the COR field is filled or not (returns true if its empty)

                //if not empty
                if (corValue.getText().isEmpty() == false){
                    newCor = Double.parseDouble(corValue.getText());  //parse COR text field into double
                    //if the COR is different than the original (0.7)
                    if (newCor != 0.7 && newCor >= 0.0 && newCor <= 100.0){
                        cor = newCor;
                    }
                }

                double ballCounter = balls.size();
                System.out.println(ballCounter);
                Color color2 = Color.black;                             //Default color
                double pX = Double.parseDouble(posX.getText());
                double pY = Double.parseDouble(posY.getText());
                double r = Double.parseDouble(setRadius.getText());
                double vX = Double.parseDouble(velocityX.getText());
                double vY = Double.parseDouble(velocityY.getText());
                String col = setColor.getText();
                switch(col.toLowerCase())
                {
                    case "black":
                        color2 = Color.black;
                        break;
                    case "blue":
                        color2 = Color.blue;
                        break;
                    case "red":
                        color2 = Color.red;
                        break;
                    case "green":
                        color2 = Color.green;
                        break;
                    case "yellow":
                        color2 = Color.yellow;
                        break;
                    case "orange":
                        color2 = Color.orange;
                        break;
                    case "gray":
                        color2 = Color.gray;
                        break;
                }
                balls.add(new Ball(pX,pY,r,vX,vY,color2));
            }
        });

        commandFrame.add(button);
        commandFrame.add(kill);
        commandFrame.setSize(700,400);
        commandFrame.setLayout(null);
        commandFrame.setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // create the walls
        createWalls();

        // create the ball
        balls.add(new Ball(300, 200, 50, 10, 10, Color.green));
        balls.add(new Ball(300, 100, 20, 3, -3, Color.green));  //Lizard ball
        //New balls
        balls.add(new Ball(200, 200, 10, 3, -3, Color.yellow));  
        balls.add(new Ball(300, 200, 50, 5, 5, Color.red));     
        balls.add(new Ball(200, 200, 50, 0, 3, Color.orange));
        balls.add(new Ball(400, 100, 60, 2, -2, Color.green));

        drawingArea = new DrawingArea(frame.getWidth(), frame.getHeight(), balls, walls);
        frame.add(drawingArea);
        drawingArea.start();
    }

    private void createWalls() {
        // vertical wall must be defined in clockwise direction
        // horizontal wall must be defined in counter clockwise direction
        walls.add(new Wall(1300, 100, 50, 100, Color.black));	        // horizontal top
        walls.add(new Wall(50, 600, 1300, 600, Color.black));           // horizontal bottom
        walls.add(new Wall(1300, 100, 1300, 600, Color.black));         // vertical right
        walls.add(new Wall(50, 600, 50, 100, Color.black));             // vertical left
    }

    //ActionListener to kill balls (corresponds to 'kill' button)
    class CustomActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            int counter = balls.size();
            if (counter != 0){
                balls.remove(counter-1);
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Dribble::new);
    }
}