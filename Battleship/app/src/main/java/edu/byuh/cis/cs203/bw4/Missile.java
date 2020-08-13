package edu.byuh.cis.cs203.bw4;


import android.graphics.Canvas;

public class Missile extends Sprite {

	private Direction dir;
	private Battleship instance = Battleship.getInstance();
	private static boolean readyR, readyL;

	public Missile() {
		super();
		readyR = true;
		readyL = true;
	}

	public Missile(Direction d) {
		super();
		dir = d;
		velocity.y = -GameView.getScreenHeight()/20;
		velocity.x = velocity.y;
		if (dir == Direction.LEFT_TO_RIGHT) {
			velocity.x = -velocity.x;
			readyR = true;
		}
		readyL = true;
	}

	@Override
	protected float relativeWidth() {
		return 0.03f;
	}

	@Override
	protected float relativeHeight() {
		return 0.05f;
	}

	@Override
	public void draw(Canvas c) {
		if (dir == Direction.RIGHT_TO_LEFT) {
			c.drawLine(bounds.right, bounds.bottom, bounds.left, bounds.top, paint);
		} else {
			c.drawLine(bounds.left, bounds.bottom, bounds.right, bounds.top, paint);
		}
	}

	@Override
	public void scaleThyself(int w, int h) {
		int newWidth = (int)(w * relativeWidth());
		int newHeight = (int)(h * relativeHeight());
		bounds.right = bounds.left + newWidth;
		bounds.bottom = bounds.top + newHeight;
	}

	@Override
	public void setLocation(float x, float y) {
		if (this.dir == Direction.RIGHT_TO_LEFT) {
			x = instance.bounds.left;
			y = instance.bounds.top;
			bounds.offsetTo(x+50, y);
		} else {
			x = instance.bounds.right;
			y = instance.bounds.top;
			bounds.offsetTo(x-71, y);
		}
	}


	public boolean isVisible() {

		return (bounds.bottom > 0);
	}

	public void setReady (boolean r){
		if(getDir() == Direction.LEFT_TO_RIGHT) {
			readyR = r;
		}
		else {
			readyL = r;
		}
	}

	public boolean isReady() {
		if (getDir() == Direction.LEFT_TO_RIGHT) {
			return readyR;
		} else {
			return readyL;
		}
	}
	//get direction of missle
	public Direction getDir() {
		return dir;
	}
	//returns lost score for frugality
	public int miss(String s) {
		if(s.equals("Easy")) {
			return 0;
		}
		else if(s.equals("Medium")) {
			return -10;
		}
		else if(s.equals("Hard")) {
			return -50;
		}
		else {
			return -200;
		}
	}

}
