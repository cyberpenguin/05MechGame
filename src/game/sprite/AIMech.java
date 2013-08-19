package game.sprite;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import engine.GameDisplay;
import engine.GameEngine;
import engine.interfaces.ColisionInterface;
import engine.interfaces.ImageInterface;
@SuppressWarnings("unused")
public class AIMech implements ColisionInterface{

	private Font f1;
	private Dimension displayBounds;

	private final double notch = 0.1;

	boolean rtArm;
	boolean ltArm;
	boolean rtPowerArm;
	boolean ltPowerArm;
	boolean rtGrab;
	boolean ltGrab;

	private double maxLegSpeed = 1;
	private double minLegSpeed = -1;
	private long currentTime = GameEngine.getCurrentTime();
	private long rtAlarm = GameEngine.getCurrentTime();
	private long ltAlarm = rtAlarm;
	
	double angle = 0;
	private double doubleX = 100;
	private double doubleY = 100;
	private int x = 100;
	private int y = 100;
	private int width = 20;
	private int height = 20;
	private double xVel;
	private double yVel;
	private double rightLegSpeed;
	private double leftLegSpeed;
	private double speed;



	public AIMech() {
		displayBounds = GameDisplay.getBounds();
		f1 = new Font("Times New Roman", Font.BOLD, (int) 12);

		doubleX = displayBounds.width / 3;
		doubleY = displayBounds.height / 2;
	}

	@Override
	public void update() {
		// TODO: calculate angle
		currentTime = GameEngine.getCurrentTime();
		// TODO: calculate speed
		changeSpeed();
		recalcAngle();
		calcMovement();
		checkFrameCollision();
	}

	@Override
	public void draw(Graphics2D g) {

		g.rotate(-angle, x, y);// mech
		g.setColor(Color.RED);
		g.fillRect(x - width / 2, y - height / 2, width, height);

		// top left
		if (leftLegSpeed > 0)
			g.setColor(new Color((int) Math.abs((int) (leftLegSpeed * 231)),
					155, 155));
		else
			g.setColor(new Color(0, 155, 155));
		g.fillOval(x - width / 2, y - height / 2, 10, 10);

		// botom left
		if (leftLegSpeed <= 0)
			g.setColor(new Color((int) Math.abs((int) (leftLegSpeed * 231)),
					155, 155));
		else
			g.setColor(new Color(0, 155, 155));
		g.fillOval(x - width / 2, y, 10, 10);

		// top right
		if (rightLegSpeed > 0)
			g.setColor(new Color((int) Math.abs((int) (rightLegSpeed * 231)),
					155, 155));
		else
			g.setColor(new Color(0, 155, 155));
		g.fillOval(x, y - height / 2, 10, 10);

		// botom right
		if (rightLegSpeed <= 0)
			g.setColor(new Color((int) Math.abs((int) (rightLegSpeed * 231)),
					155, 155));
		else
			g.setColor(new Color(0, 155, 155));
		g.fillOval(x, y, 10, 10);

		g.rotate(angle, x, y);// end mech

		g.setColor(Color.BLACK);
		g.drawLine(x, y, x - (int) (xVel * 20), y - (int) (yVel * 20)); // movement
																		// direction
		g.setColor(Color.blue);
		g.drawLine(x, y, (int) (x - Math.sin(angle) * 10),
				(int) (y - Math.cos(angle) * 10)); // facing direction

		// debug
		{
			g.setFont(f1);
			g.setColor(Color.RED);
			g.drawRect(x + 99, y + 89, 200, 200);
			g.drawLine(x + width/2, y + height/2, x + 99, y + 89);
			g.drawString(
					"Right Speed: " + String.format("%.3f", rightLegSpeed * 10),
					x + 100, y + 100);
			g.drawString(
					"Left Speed:  " + String.format("%.3f", leftLegSpeed * 10),
					x + 100, y + 110);
			g.drawString("Angle: " + (int) Math.toDegrees(angle) + " Degrees",
					x + 100, y + 120);
			g.drawString("Speed: " + Math.round(speed * 10), x + 100, y + 130);
			g.drawString("X Velocity: " + String.format("%.3f", xVel * 10),
					x + 100, y + 140);
			g.drawString("Y Velocity: " + String.format("%.3f", yVel * 10),
					x + 100, y + 150);
		}
	}

	private void changeSpeed() {

					rightLegSpeed = notch * 2;

					leftLegSpeed = notch * 6;
	}

	private void recalcAngle() {

		speed = Math.abs(rightLegSpeed + leftLegSpeed);

		angle += (Math.atan(rightLegSpeed - leftLegSpeed))
				* (Math.abs(rightLegSpeed) + Math.abs(leftLegSpeed)) / 100;
		if (rightLegSpeed + leftLegSpeed < 0) {
			speed *= -1;
		}

		// check for degrees
		if (angle > Math.PI * 2)
			angle = 0;
		if (angle < -Math.PI * 2)
			angle = 0;
		xVel = Math.sin(angle) * speed;
		yVel = Math.cos(angle) * speed;
	}

	private void calcMovement() {
		doubleX -= xVel;
		doubleY -= yVel;
		x = (int) doubleX;
		y = (int) doubleY;
	}

	private void checkFrameCollision() {
		/* Check for right collision */

		if ((x + width / 2) >= displayBounds.width) {
			doubleX = (displayBounds.width - width / 2 - 1);
		}

		/* Check for left collision */

		if (x <= 0) {
			doubleX = (0);
		}

		/* Check for bottom collision */

		if ((y + height / 2) >= displayBounds.height) {
			doubleY = (displayBounds.height - height / 2 - 1);
		}

		/* Check for top collision */

		if (y <= 0) {
			doubleY = (0);
		}
	}

	@Override
	public void checkCollision(ColisionInterface obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public Rectangle intersects(Rectangle boundingBox) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(ImageInterface compareImage) {
		return this.getLayer() - ((ImageInterface) compareImage).getLayer();
	}

	@Override
	public int getLayer() {
		return 100;
	}
}