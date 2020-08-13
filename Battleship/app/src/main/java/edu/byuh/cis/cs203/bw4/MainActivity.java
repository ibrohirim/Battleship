package edu.byuh.cis.cs203.bw4;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;


public class MainActivity extends Activity {

	private GameView gv;
	private int savedTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gv = new GameView(this);
		setContentView(gv);
	}

	/**
	 * pause game and save the time
	 */
	@Override
	public void onPause() {
		super.onPause();
		savedTime = gv.saveTimer();
	}

	/**
	 * resume game and reload saved time
	 */

	@Override
	public void onResume() {
		super.onResume();
		gv.resumeTimer(savedTime);
	}

	/**
	 * checks whether the game should end or restart
	 * @param requestCode
	 * @param resultCode
	 * @param data
     */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == GameOver.ID) {
			if (resultCode == Activity.RESULT_OK) {
				boolean playAgain = data.getBooleanExtra(GameOver.PLAY_AGAIN, false);
				if(playAgain) {
					gv.restart();
				} else {
					finish();
				}
			}
		}
	}

	/**
	 * stop timer when activity closes
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		gv.stopTimer();
	}
}
