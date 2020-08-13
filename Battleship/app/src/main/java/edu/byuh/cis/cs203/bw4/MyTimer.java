package edu.byuh.cis.cs203.bw4;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

/**
 * Created by Ibrahim on 1/19/2016.
 */


public class MyTimer extends Handler {

    private ArrayList<TickListener> tickArray;

    public MyTimer() {
        tickArray = new ArrayList<TickListener>();
        sendMessageDelayed(obtainMessage(0), 100);
    }

    @Override
    public void handleMessage(Message m) {

        for (TickListener t : tickArray) {
            t.tick();
        }
        sendMessageDelayed(obtainMessage(0), 100);
    }

    /**
     * adds tick listeners
     *
     * @param a
     */
    public void reg(TickListener a) {
        tickArray.add(a);
    }

    /**
     * removes tick listeners
     *
     * @param b
     */
    public void deReg(TickListener b) {
        tickArray.remove(b);
    }

}

