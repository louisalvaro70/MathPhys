/*
	Matfis pertemuan 5
	Circular motion and pendulum
	TODO:
	 1. Separate drawing area from the frame (done)
	 2. Add more pendulums (done)
	 3. Take care of the collision among pendulum balls. Remember, the ball moves before detecting any collisions
	 4. Minigame: hitting target with pendulum(s)
 */

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.awt.event.MouseEvent;

public class Pendulum extends JFrame 
{
		private DrawingArea drawingArea;	//Declare drawing area
		private JFrame frame;
		
		//the ball to be drawn
		ArrayList<Ball> balls = new ArrayList<>();
		
		// the line to be drawn
		ArrayList<Rope> ropes = new ArrayList<>();
		
		BufferedImage dbImage;	
		
		private int canvasHeight;
		private int canvasStartY;
		
		private Ball selectedBall;
		private Rope selectedRope;
		
				
		private boolean isPressed;
		private double mousePressedX;
		private double mousePressedY;

		private int maxBalls = 2;	
		private double ropeLength = 400;
		private double ballSize = 40;
		private double firstRopeX = 600;
		private double incX = 0.1;
		
		public static final double GRAVITY = 0.098;
		
		public Pendulum(){
			setExtendedState(MAXIMIZED_BOTH);
			setBackground(Color.WHITE);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setVisible(true);
			canvasHeight = getHeight() - getInsets().top;
			canvasStartY = getInsets().top;
				
			drawingArea = new DrawingArea(getWidth(), canvasHeight, balls, ropes);
			this.add(drawingArea);
			drawingArea.start();
			
			for(int i=0; i<maxBalls; i++)
				addPendulum();

			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					//get the coordinate where the mouse is clicked
					mousePressedX = (double)(e.getX());
					mousePressedY = (double)(e.getY());

					//get the ball at that coordinate
					for(Ball b: balls)
						if(b.isInside(mousePressedX, mousePressedY)){
							selectedBall = b;
							selectedRope = b.getRope();
						}

					//get which ball is pressed
					if(selectedBall != null){
						drawingArea.setPress(true);		//Set mouse press condition as TRUE on DrawingArea
						selectedBall.setVx(0);
						selectedBall.setVy(0);
					}
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					super.mouseReleased(e);
					//indicating that the mouse is not pressed anymore
					drawingArea.setPress(false);	//Set mouse press condition as FALSE on DrawingArea
					selectedBall = null;
					selectedRope = null;
				}
			});

			addMouseMotionListener(new MouseAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					super.mouseDragged(e);
					//get the coordinate where the mouse is located
					int mouseMovedX = e.getX();
					int mouseMovedY = e.getY();

					if(selectedBall != null){
						Vector v = new Vector(mouseMovedX - selectedRope.getX1(), mouseMovedY - selectedRope.getY1());
						v = v.unitVector();
						v.multiply(selectedRope.getLength());

						if((selectedRope.getY1() + v.getY() - selectedBall.getRadius()) > 0){
							selectedBall.move(selectedRope.getX1() + v.getX(), selectedRope.getY1() + v.getY());
						}
					}
				}
			});

		}
		
		public void addPendulum(){
			if(balls.size() < maxBalls){				
				//create the ropes
				ropes.add(new Rope(firstRopeX + 2*balls.size()*(ballSize + incX), 0,
						firstRopeX + 2*balls.size()*(ballSize + incX), ropeLength, Color.blue));
				//create the balls
				balls.add(new Ball(0, 0, ballSize, 0, 0, Color.RED, 4));
				//attach the ball to the rope
				ropes.get(ropes.size()-1).attach(balls.get(balls.size()-1));				
			}
		}

		public static void main(String[] args){
			EventQueue.invokeLater(Pendulum::new);
		}
}