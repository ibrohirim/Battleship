package edu.byuh.cis.cs203.bw4;

import android.content.Context;
import android.content.res.ColorStateList;
import android.provider.CalendarContract;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ibrahim on 2/10/2016.
 */
public class HighScore implements Comparable<HighScore> {
    private String playerName;
    private int score;
    public static final String SCORE = "score";
    public static final String NAME = "name";

    /**
     * constructor for the new score
     * @param s
     */
    public HighScore(String n, int s) {
        score = s;
        playerName = n;
    }

    public HighScore(int s) {
        this(null, s);
    }

    /**
     * constructor for json object
     * @param j get's the score and name from json object
     * @throws JSONException
     */
    public HighScore(JSONObject j) throws JSONException {
        score = j.getInt(SCORE);//score and name are keys
        playerName = j.getString(NAME);
    }

    public void setPlayerName(String n) {
        playerName = n;
    }

    /**
     * gets the score
     * @return returns the score
     */
    public int getScore() {
        return score;
    }

    /**
     * wraps to jsonobject
     * @return returns the hason object
     * @throws JSONException
     */
    public JSONObject saveState() throws JSONException{
        JSONObject j = new JSONObject();
        j.put(NAME, playerName);
        j.put(SCORE, score);
        return j;
    }

    /**
     * compares the scores
     * @param t
     * @return
     */
    @Override
    public int compareTo(HighScore t) {
        return t.score - this.score;
    }

    /**
     * return string variant of HighScore
     * @return
     */
    public String log() {
        return "Score " + score + " name" + playerName;
    }
    public String getPlayerName() {
        return playerName;
    }

    public TableRow getRow(GameOver go) {
        final TextView left, right;
        if(playerName == null) {
            left = new EditText(go);
            left.setHint(R.string.hint);
            go.setText(left);
        } else {
            left = new TextView(go);
            left.setText(playerName + "     ");
        }
        right = new TextView(go);
        right.setText("" + score);
        TableRow row = new TableRow(go);
        row.addView(left);
        row.addView(right);
        return row;
    }
}
