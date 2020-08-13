package edu.byuh.cis.cs203.bw4;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Ibrahim on 1/20/2016.
 */
public class SplashActivity extends Activity {

    private ImageView imageView;
    //constructor
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.splash);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(imageView);
    }
    //logic for about and start buttons.
    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if(m.getAction() == MotionEvent.ACTION_DOWN) {
            float x = m.getX();
            float y = m.getY();
            if (x < imageView.getWidth()*0.09 && y > imageView.getHeight()*0.86) {
                AlertDialog.Builder ab = new AlertDialog.Builder(this);
                ab.setMessage(getApplicationContext().getString(R.string.splash_description))
                        .setTitle(getApplicationContext().getString(R.string.app_name))
                        .setNeutralButton("OK", null);
                ab.show();
            }
            if ( x < imageView.getWidth()*0.8 && y < imageView.getHeight()*0.2) {
                Intent prefs = new Intent(this, Prefs.class);
                startActivity(prefs);
            }
            if(x > imageView.getWidth()*0.4 && y > imageView.getHeight()*0.43 && x < imageView.getWidth()*0.59 && y < imageView.getHeight()*0.6) {
                Intent start = new Intent(this, MainActivity.class);
                startActivity(start);
                finish();
            }
        }

        return true;
    }

}
