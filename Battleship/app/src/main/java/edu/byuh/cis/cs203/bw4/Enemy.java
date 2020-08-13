package edu.byuh.cis.cs203.bw4;

import android.graphics.Canvas;

public abstract class Enemy extends Sprite {

	protected Size size;
	protected Direction dir;
	protected boolean check;

	public Enemy() {
		super();
		check = false;
	}

	@Override
	public void move() {
		super.move();
		if (Math.random() < 0.1) {
			velocity.x = getRandomVelocity() * Math.signum(velocity.x);
		}
		if (bounds.right < 0 || bounds.left > GameView.getScreenWidth()) {
			resetRandom();
		}

	}

	public static Size getRandomSize() {
		double r2 = Math.random();
		Size newSize;
		if (r2 < 0.3333) {
			newSize = Size.LARGE;
		} else if (r2 < 0.667) {
			newSize = Size.MEDIUM;
		} else {
			newSize = Size.SMALL;
		}
		return newSize;
	}

	protected void resetRandom() {
		double r1 = Math.random();
		Direction newDirection;
		Size newSize = getRandomSize();
		if (r1 < 0.5) {
			newDirection = Direction.RIGHT_TO_LEFT;
		} else {
			newDirection = Direction.LEFT_TO_RIGHT;
		}
		reset(newSize, newDirection);
	}

	protected float getRandomVelocity() {
		return (float)(Math.random() * 20);
	}

	@Override
	public void draw(Canvas c) {
		super.draw(c);
	}

	protected abstract float getRandomHeight();

	protected void reset(Size s, Direction d) {
		if (d == Direction.RIGHT_TO_LEFT) {
			velocity.x = -1 * getRandomVelocity();
			bounds.offsetTo(GameView.getScreenWidth(), getRandomHeight());
		} else {
			velocity.x = getRandomVelocity();
			bounds.offsetTo(-image.getWidth(), getRandomHeight());
		}

	}

	protected abstract int explodingImage();

	protected void explode() {
		image = GameView.myBitmapLoader(explodingImage());
		velocity.x = 0;
		velocity.y = 0;
		check = true;
	}

	public abstract int getPointValue();

}
