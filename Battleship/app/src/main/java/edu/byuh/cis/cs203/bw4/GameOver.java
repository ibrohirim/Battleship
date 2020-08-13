package edu.byuh.cis.cs203.bw4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.TableLayout;
import edu.byuh.cis.cs203.bw4.R;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Ibrahim on 2/2/2016.
 */
public class GameOver extends Activity {

    private boolean playAgain;
    public static final int ID = 1337;
    public static final String PLAY_AGAIN = "playAgain";
    public static final String SCORE_KEY = "Final Score";
    public static final String FILE_NAME = "score.txt";
    private HighScore currentScore;
    private List<HighScore> highScores;
    private JSONArray ja;
    private TextView edit;
    private boolean top;

    private static final int MAX_ENTRIES = 5;

    /**
     * starts the game over activity
     *
     * @param b
     */
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        top = false;
        edit = null;
        highScores = new ArrayList<HighScore>();
        ja = new JSONArray();
        edit = new TextView(this);
        Intent over = getIntent();
        int score = over.getIntExtra(SCORE_KEY, 0);
        List<HighScore> temporary = loadScores();
        currentScore = new HighScore(score);
        temporary.add(currentScore);
        Collections.sort(temporary);
        TextView textView = new TextView(this);
        for (int i = 0; i < MAX_ENTRIES; ++i) {
            highScores.add(temporary.get(i));
        }
        if(highScores.contains(currentScore)) {
            if(score > highScores.get(0).getScore()) {
                textView.setText(getApplication().getString(R.string.highscore) + " " + score);
            } else {
                textView.setText(getApplicationContext().getString(R.string.top) + " " + score);
            }
            top = true;

        } else {
            textView.setText(getApplication().getString(R.string.nothighscore)+ " " + score);
        }

        TableLayout table = new TableLayout(this);

        for (HighScore h : highScores) {
            table.addView(h.getRow(this));
        }


        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
        LinearLayout buttonRow = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        buttonRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (0, ViewGroup.LayoutParams.MATCH_PARENT, 1);


        layout.addView(textView);
        playAgain = false;
        Button yes = new Button(this);
        yes.setText(R.string.playagain);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAgain = true;
                set();
            }
        });
        Button no = new Button(this);
        no.setText(R.string.quit);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain = false;
                set();
            }
        });

        buttonRow.addView(yes, params);
        buttonRow.addView(no, params);
        layout.addView(table);
        layout.addView(buttonRow);
        setContentView(layout);

    }

    /**
     * sets the intent
     */
    private void set() {
        Intent reset = new Intent();
        reset.putExtra(PLAY_AGAIN, playAgain);
        setResult(Activity.RESULT_OK, reset);
        saveScores();
        finish();
    }

    /**
     * loads scores from json
     * @return
     */
    private List<HighScore> loadScores() {
        List<HighScore> result = new ArrayList<HighScore>();
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            String jsonText = "";
            Scanner s = new Scanner(fis);
            jsonText = s.nextLine();
            s.close();
            JSONArray jo = new JSONArray(jsonText.toString());
            for (int i = 0; i < jo.length(); i++) {
                HighScore hs = new HighScore(jo.getJSONObject(i));
                result.add(hs);
            }
        } catch (Exception e) {
            result.add(new HighScore("John", 0));
            result.add(new HighScore("Ben", 0));
            result.add(new HighScore("Joseph", 0));
            result.add(new HighScore("Jen", 0));
            result.add(new HighScore("Wade", 0));
        }
        return result;
    }

    /**
     * saves scores to json
     */
    private void saveScores() {
        if (top) {
            try {
                if( edit != null) {
                    currentScore.setPlayerName(edit.getText().toString());
                }
                JSONArray jo = new JSONArray();
                for (HighScore h : highScores) {
                    jo.put(h.saveState());
                }
                String jsonString = jo.toString();
                FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                fos.write(jsonString.getBytes());
                fos.close();
            } catch (JSONException j) {
                j.printStackTrace();
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
    }

    /**
     * supposed to set the text for the name...not doing it
     * @param text
     */

    public void setText(TextView text) {
        this.edit = text;
    }


}
