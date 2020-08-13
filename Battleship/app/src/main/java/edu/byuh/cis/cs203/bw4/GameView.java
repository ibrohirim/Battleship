package edu.byuh.cis.cs203.bw4;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

public class GameView extends View implements TickListener {

	private Bitmap water;
	private Battleship battleship;
	private Missile m;
	private boolean firstTime;
	private Paint paint;
	private static Resources res;
	private static int screenWidth, screenHeight;
	private MyTimer tim;
	private List<DepthCharge> charges;
	private List<Missile> missiles;
	private List<Plane> planes;
	private List<Sub> subs;
	private MediaPlayer dcSound, leftSound, rightSound, subExplode, planeExplode;
	private List<Sprite> doomed;
	private int score;
	private int timer;
	private long timeBefore;
	private long timeNow;
	private int depthCount;
	private boolean depthReady;
	private boolean soundsOnOff;
	private boolean missileBarrage;
	private boolean rapidFire;
	private int input;
	private int inputSub;
	private boolean frugalityMode;
	private boolean gameOver;
	private int timerSetUp;
	private boolean paused;
	/**
	 * Constructor for our View subclass. Loads all the images
	 * @param context a reference to our main Activity class
	 */
	public GameView(Context context) {
		super(context);
		res = getResources();
		water = BitmapFactory.decodeResource(getResources(), R.drawable.water);
		battleship = Battleship.getInstance();
		firstTime = true;
		tim = new MyTimer();
		paint = new Paint();
		missiles = new ArrayList<Missile>();
		charges = new ArrayList<DepthCharge>();
		planes = new ArrayList<Plane>();
		subs = new ArrayList<Sub>();
		doomed = new ArrayList<Sprite>();
		dcSound = MediaPlayer.create(getContext(), R.raw.depth_charge);
		leftSound = MediaPlayer.create(getContext(), R.raw.left_gun);
		rightSound = MediaPlayer.create(getContext(), R.raw.right_gun);
		subExplode = MediaPlayer.create(getContext(), R.raw.sub_explode);
		planeExplode = MediaPlayer.create(getContext(), R.raw.plane_explode);
		depthReady = true;
		soundsOnOff = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("sounds", true);
		missileBarrage = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("barrage", true);
		rapidFire = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("rapidFire", true);
		m = new Missile();
		input = Prefs.getNumberPlanes(getContext());
		inputSub = Prefs.getNumberSubs(getContext());
		frugalityMode = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("frugality", false);
		timerSetUp = Prefs.getTime(getContext());
		paused = false;
	}

	/**
	 * loads bitmaps
	 * @param id of the image
	 * @return
     */
	public static Bitmap myBitmapLoader(int id) {
		return BitmapFactory.decodeResource(res, id);
	}

	/**
	 * returns screen width
	 * @return
     */
	public static int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * set up all objects on create
	 */
	public void setUp() {
		//set sizes
		score = 0;
		timer = timerSetUp;
		int w = getWidth();
		int h = getHeight();
		int waterW = (int) (w * 0.015);
		int waterH = (int) (h * 0.015);
		water = Bitmap.createScaledBitmap(water, waterW, waterH, true);
		screenWidth = w;
		screenHeight = h;
		for(int p = 0; p < input; p++) {
			planes.add(new Plane(Direction.RIGHT_TO_LEFT));
		}
		for(Plane p : planes) {
			tim.reg(p);
			p.scaleThyself(w, h);
		}
		for(int s = 0; s < inputSub; s++) {
			subs.add(new Sub(Direction.LEFT_TO_RIGHT));
		}
		for(Sub s : subs) {
			tim.reg(s);
			s.scaleThyself(w, h);
		}
		tim.reg(this);
		water = Bitmap.createScaledBitmap(water, w/50, h/30, true);
		battleship.scaleThyself(w, h);

		//set positions
		battleship.setLocation((w-battleship.getWidth())/2f,
				h/2-battleship.getHeight()+waterH);
		timeBefore = System.currentTimeMillis();
	}

	/**
	 * draws all objects
	 */
	@Override
	public void onDraw(Canvas c) {
		c.drawColor(Color.WHITE);
		c.drawRect((float)0, (float)getScreenHeight(), (float)getScreenWidth(),(float)0,paint);

//		c.drawColor(Color.BLUE);
		c.drawRect((float)0, (float)getScreenHeight()/2, (float)getScreenWidth()/2,(float)0, paint);

		paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
		paint.setTextSize(40);
		String time = getContext().getString(R.string.time);
		String scores = getContext().getString(R.string.score);
		int minutesInt = timer/60;
		int secondsInt = timer%60;
		String actualTime = String.format("%d:%02d", minutesInt, secondsInt);
		c.drawText(scores + "" + score,getScreenWidth()*.01f, getScreenHeight()*0.49f, paint);
		c.drawText(time + actualTime, getScreenWidth() - 220, getScreenHeight()*0.49f, paint);
		int w = c.getWidth();
		int h = c.getHeight();
		if (firstTime) {
			firstTime = false;
			setUp();
		}

		float waterX = 0;
		while (waterX < w) {
			c.drawBitmap(water, waterX, h/2, paint);
			waterX += water.getWidth();
		}

		for (int i = 0; i < input; i++) {
			Plane p = planes.get(i);
			p.draw(c);
			if (p.check) {
				p.resetRandom();
			}
		}
		for (Sub s : subs) {
			s.draw(c);
			if (s.check) {
				s.resetRandom();
			}
		}
		for (DepthCharge d : charges) {
			d.draw(c);
		}
		for (Missile m : missiles) {
			m.draw(c);
		}

		battleship.draw(c);
		delete();
	}

	public static int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * detects touches and instantiates missiles and subs
	 * @param me id of the on touch event
	 * @return
     */
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		if (me.getActionMasked() == MotionEvent.ACTION_DOWN || me.getActionMasked() == MotionEventCompat.ACTION_POINTER_DOWN) {
			for(int j = 0; j < me.getPointerCount(); j++) {
				float x = (int)MotionEventCompat.getX(me, me.getActionIndex());
				float y = (int)MotionEventCompat.getY(me, me.getActionIndex());
				if (y > screenHeight / 2) {
					if (rapidFire == false) {
						if (depthReady == true) {
							DepthCharge dc = new DepthCharge();
							depthReady = false;
							tim.reg(dc);
							dc.scaleThyself(screenWidth, screenHeight);
							dc.setLocation(screenWidth / 2, screenHeight * 0.48f);
							charges.add(dc);
						}
					} else {
						DepthCharge dc = new DepthCharge();
						tim.reg(dc);
						dc.scaleThyself(screenWidth, screenHeight);
						dc.setLocation(screenWidth / 2, screenHeight * 0.48f);
						charges.add(dc);
					}

				} else {
					if (x < screenWidth / 2) {
						if (missileBarrage == false) {
							if (m.isReady()) {
								m = new Missile(Direction.RIGHT_TO_LEFT);
								tim.reg(m);
								m.setLocation(screenWidth, screenHeight);
								if (soundsOnOff == true) {
									leftSound.start();
								}
								m.setReady(false);
							}
						} else {
							m = new Missile(Direction.RIGHT_TO_LEFT);
							tim.reg(m);
							m.setLocation(screenWidth, screenHeight);
							if (soundsOnOff == true) {
								leftSound.start();
							}
						}
					} else {
						if (missileBarrage == false) {
							if (m.isReady()) {
								m = new Missile(Direction.LEFT_TO_RIGHT);
								tim.reg(m);
								m.setLocation(screenWidth, screenHeight);
								if (soundsOnOff == true) {
									rightSound.start();
								}
								m.setReady(false);
							}
						} else {
							m = new Missile(Direction.LEFT_TO_RIGHT);
							tim.reg(m);
							m.setLocation(screenWidth, screenHeight);
							if (soundsOnOff == true) {
								rightSound.start();
							}
						}
					}
					m.scaleThyself(screenWidth, screenHeight);
					missiles.add(m);
				}
			}
		}
		return true;
	}

	/**
	 * detects collisons between missile and planes and subs and depth charges
	 */
	private void detectCollisions() {
		for (Plane p : planes) {
			for (Missile m : missiles) {
				if(p.collision(m)) {
					p.explode();
					doomed.add(m);
					score+=p.getPointValue();
					if(soundsOnOff == true) {
						planeExplode.start();
					}
					m.setReady(true);
				}
			}
		}
		for (Sub s : subs) {
			for (DepthCharge d : charges) {
				if(s.collision(d)) {
					s.explode();
					doomed.add(d);
					score+=s.getPointValue();
					if (soundsOnOff == true) {
						subExplode.start();
					}
					depthReady = true;
				}
			}
		}
		playDepthSound();
	}

	/**
	 * deletes objects
	 */
	private void delete() {
		for (DepthCharge d : charges) {
			if (!d.isVisible()) {
				doomed.add(d);
				tim.deReg(d);
				if(frugalityMode) {
					score+=d.miss("Hard");
				}
				depthReady = true;
			}
		}
		for (Sprite s : doomed) {
			charges.remove(s);
		}
		for (Missile b : missiles) {
			if (!b.isVisible() ) {
				doomed.add(b);
				tim.deReg(b);
				if(frugalityMode) {
					score+=b.miss("Hard");
				}
				b.setReady(true);
			}
		}
		for (Sprite s : doomed) {
			missiles.remove(s);
		}
		doomed.clear();
	}

	/**
	 * overidden tick method
	 */
	@Override
	public void tick() {
		if (!gameOver && !paused) {
			timer();
			detectCollisions();
			invalidate();
		}
	}
	//method to play the depthcharge sound
	private void playDepthSound() {
		if(soundsOnOff == true) {
			depthCount++;
			if (depthCount == 10 && charges.size() > 0) {
				dcSound.start();
				depthCount = 0;
			}
			if (depthCount > 10 && charges.size() == 0) {
				depthCount = 0;
			}
		}
	}

	/**
	 * method to countdown time
	 */
	private void timer() {
		timeNow = currentTimeMillis();
		long timeCheck = timeNow - timeBefore;
		if (timeCheck >= 1000) {
			timer-=1;
			timeBefore = timeNow;
		}
		if (timer == 0) {
			end();
		}

	}

	/**
	 * starts the Game Over Activity
	 */
	private void end() {
		gameOver = true;
		Intent end = new Intent(getContext(), GameOver.class);
		end.putExtra("Final Score", getScore());
		((MainActivity)getContext()).startActivityForResult(end, GameOver.ID);
	}

	/**
	 * method to return the score
	 * @return
     */
	public int getScore() {
		return score;
	}

	public void restart() {
		score = 0;
		timeBefore = System.currentTimeMillis();
		timer = timerSetUp;
		gameOver = false;
	}

	/**
	 * saves time to a variable in main activity on pause
	 * @return
     */

	public int saveTimer(){
		paused = true;
		if (timer > 0) {
			return timer;
		} else {
			return timerSetUp;
		}
	}

	/**
	 * reloads timer from varaible from main activity on resume
	 * @param t
     */
	public void resumeTimer(int t) {
		timer = t;
		paused = false;
	}

	/**
	 * stops timer completely on destroy
	 */
	public void stopTimer() {
		tim.removeMessages(0);
	}

}
