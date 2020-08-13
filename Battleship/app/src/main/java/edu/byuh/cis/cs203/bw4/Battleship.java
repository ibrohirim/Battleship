package edu.byuh.cis.cs203.bw4;





public class Battleship extends Sprite {

	private static Battleship singleton;
	
	private Battleship() {
		super();
		image = GameView.myBitmapLoader(R.drawable.battleship);
	}
	
	public static Battleship getInstance() {
		if (singleton == null) {
			singleton = new Battleship();
		}
		return singleton;
	}

	@Override
	protected float relativeWidth() {
		return 0.4f;
	}

	@Override
	protected float relativeHeight() {
		return 0.125f;
	}

}
