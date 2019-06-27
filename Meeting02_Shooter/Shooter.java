import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;
import javax.swing.JTextArea;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.*;
import java.util.ArrayList;

/*
    MatFis pertemuan 2
    Note that every dimension-related are measured in pixel
    Except for angle, which is measured in radian
    Explain how parabolic motion of projectile works.
    What is the difference between mapping code in Cartesian coordinates and pixel coordinates?
    
    TODO:
     1. Add a text field to adjust bullet's velocity
     2. Make cannon able to shoot more than one bullet
     3. Limit the amount of bullet in the cannon
     4. Add wind force, with its direction (which impacts acceleration on x-axis and y-axis; use Newton's second law)
     5. Make a shooter game with simple moving target (yes, over-achievers, I need SIMPLE)
	 
    Extra:
    Q: Does this mean I can make a bullet hell game for my final project?
    A: Yes, but since the concept is already explained in class, you won't get Liv's extra brownie point.
 */


class Shooter {
    private JFrame frame;
    private JTextField bulletVelocity;

    // game area
    private Bullet bullet = null;
    double time = 0;
    double timeIncrement = 0.05;
    double bulletVelocity = 50.0;
    int bulletCount = 10;
    int angle = 0;
    int wind = 10;
    int mass = 1;
  

    private static final String INSTRUCTION = "Welcome to Cannon Simulation!\n" + 
                    "\nMove cannon's position = W A S D\n" +
                    "Move shooting direction = Left | Right \n" +
                    "Launch bullet = Space\n" +
                    "\nThere can only one bullet at a time";

                    private int cpSize = 230;      // set control panel's width

                    public Shooter() {
                        // setup the frame
                        frame = new JFrame("Graphing App");
                        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setLayout(null);
                        frame.setFocusable(true);
                        frame.setVisible(true);
                
                        frame2 = new JFrame("Speed changer");
                        frame2.setSize(600,400);
                        frame2.setLayout(null);
                        
                        JLabel speedLabel = new JLabel("Bullet Velocity");
                        speedLabel.setBounds(30,80,100,20);
                        frame2.add(speedLabel);
                
                        JTextField speedField = new JTextField();
                        speedField.setBounds(160,80,150,20);
                        frame2.add(speedField);
                
                        JLabel massLabel= new JLabel("Bullet Mass");
                        massLabel.setBounds(30,110,100,20);
                        frame2.add(massLabel);
                
                        JTextField massField = new JTextField();
                        massField.setBounds(160,110,150,20);
                        frame2.add(massField);
                
                        JLabel windLabel = new JLabel("Wind speed");
                        windLabel.setBounds(30,140,150,20);
                        frame2.add(windLabel);
                
                        JTextField windField = new JTextField();
                        windField.setBounds(160,140,150,20);
                        frame2.add(windField);
                
                        JLabel angleLabel = new JLabel("Wind Angle");
                        angleLabel.setBounds(30,170,150,20);
                        frame2.add(angleLabel);
                
                        JTextField angleField = new JTextField();
                        angleField.setBounds(160,170,150,20);
                        frame2.add(angleField);
                
                        
                
                        JButton speed = new JButton("Apply properties");
                        speed.setBounds(200,240,140,30);
                        frame2.add(speed);
                
                        speed.addActionListener(new ActionListener()
                        {
                            @Override
                            public void actionPerformed(ActionEvent e)
                            {
                                bulletVelocity = Double.parseDouble(speedField.getText());
                                mass = Integer.parseInt(massField.getText());
                                wind = Integer.parseInt(windField.getText());
                                angle = Integer.parseInt(angleField.getText());
                            }
                        });
                        
                
                        frame2.setVisible(true);
                
                        // setup control panel itself
                        JTextArea instruction = new JTextArea(INSTRUCTION);
                        instruction.setBounds(5, 5, cpSize - 5, frame.getHeight());
                        frame.add(instruction);
                
                        // setup drawing area
                        DrawingArea drawingArea = new DrawingArea(frame.getWidth(), frame.getHeight(), cpSize);
                        cannon = new Cannon(drawingArea.GRAPH_SCALE / 2, drawingArea.getOriginX(), drawingArea.getOriginY());
                        drawingArea.setCannon(cannon);
                        frame.add(drawingArea);
                
                        // Keyboard shortcuts
                        frame.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyPressed(KeyEvent e) {
                                super.keyPressed(e);
                                switch (e.getKeyCode()) {
                                    case KeyEvent.VK_SPACE:
                                        if (bulletCount != 0)
                                        {
                                            bullet = new Bullet(cannon.getBarrelWidth() / 2, (int) cannon.getBarrelMouthX(), (int) cannon.getBarrelMouthY(), cannon.getAngle());
                                            drawingArea.setBullet(bullet);
                                            bullet.shoot();
                                            bulletCount--;
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println("Out of ammo");
                                        }
                                    case KeyEvent.VK_LEFT:
                                        cannon.rotateLeft();
                                        break;
                                    case KeyEvent.VK_RIGHT:
                                        cannon.rotateRight();
                                        break;
                                    case KeyEvent.VK_W:
                                        cannon.moveUp();
                                        break;
                                    case KeyEvent.VK_A:
                                        cannon.moveLeft();
                                        break;
                                    case KeyEvent.VK_S:
                                        cannon.moveDown();
                                        break;
                                    case KeyEvent.VK_D:
                                        cannon.moveRight();
                                        break;
                                }
                            }
                        });
                
                        drawingArea.start();
                    }
                
                    public static void main(String[] args) {
                        EventQueue.invokeLater(Shooter::new);
                    }
                }