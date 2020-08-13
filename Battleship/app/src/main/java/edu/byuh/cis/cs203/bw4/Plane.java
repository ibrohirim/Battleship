package edu.byuh.cis.cs203.bw4;


public class Plane extends Enemy {

	public Plane(Direction dir) {
		super();
		reset(getRandomSize(), dir);
	}

	public void reset(Size s, Direction d) {
		size = s;
		dir = d;
		switch (size) {
			case SMALL:
				if (dir == Direction.RIGHT_TO_LEFT) {
					image = GameView.myBitmapLoader(R.drawable.little_airplane);
				} else {
					image = GameView.myBitmapLoader(R.drawable.little_airplane_flip);
				}
				break;
			case MEDIUM:
				if (dir == Direction.RIGHT_TO_LEFT) {
					image = GameView.myBitmapLoader(R.drawable.medium_airplane);
				} else {
					image = GameView.myBitmapLoader(R.drawable.medium_airplane_flip);
				}
				break;
			case LARGE:
			default:
				if (dir == Direction.RIGHT_TO_LEFT) {
					image = GameView.myBitmapLoader(R.drawable.big_airplane);
				} else {
					image = GameView.myBitmapLoader(R.drawable.big_airplane_flip);
				}
				break;
		}
		scaleThyself(GameView.getScreenWidth(), GameView.getScreenHeight());
		super.reset(size, dir);
	}

	@Override
	protected float relativeWidth() {
		switch (size) {
			case SMALL:
				return 0.05f;
			case MEDIUM:
				return 0.083f;
			case LARGE:
			default:
				return 0.1f;
		}
	}

	@Override
	protected float relativeHeight() {
		switch (size) {
			case SMALL:
				return 0.05f;
			case MEDIUM:
				return 0.067f;
			case LARGE:
			default:
				return 0.067f;
		}
	}

	@Override
	public float getRandomHeight() {
		return (float)(Math.random() * 0.25 * GameView.getScreenHeight());
	}

	@Override
	protected int explodingImage() {
		return R.drawable.airplane_explosion;
	}

	@Override
	protected void resetRandom() {
		super.resetRandom();
		check = false;
	}

	@Override
	public int getPointValue() {
		int value = 0;
		switch(size) {
			case LARGE:
				value = 25;
				break;
			case MEDIUM:
				value = 40;
				break;
			case SMALL:
				value = 150;
				break;
		}
		return value;
	}
}
