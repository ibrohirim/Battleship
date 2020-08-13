package edu.byuh.cis.cs203.bw4;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by Ibrahim on 1/25/2016.
 */
public class Prefs extends PreferenceActivity {

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        addPreferencesFromResource(R.xml.prefs);
    }

    public static int getNumberPlanes(Context c) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString("numberPlanes", "3"));
    }

    public static int getNumberSubs(Context c) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString("numberSubs", "3"));
    }

    public static int getTime(Context c) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString("time", "180"));
    }

}
