package edu.byuh.cis.cs203.bw4;


public class DepthCharge extends Sprite {

	public DepthCharge() {
		super();
		image = GameView.myBitmapLoader(R.drawable.depth_charge);
		velocity.y = GameView.getScreenHeight()/100;
	}

	@Override
	protected float relativeWidth() {
		return 0.025f;
	}

	@Override
	protected float relativeHeight() {
		return 0.02f;
	}
	
	public boolean isVisible() {
		return bounds.top <= GameView.getScreenHeight();
	}
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
