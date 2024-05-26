package com.samsung.finalproject;

import android.widget.TextView;

public class TimeThread extends Thread {
    private SolarTime solarTime;
    private TextView timeView;
    public boolean running = true;

    public TimeThread(SolarTime solarTime, TextView timeView) {
        this.solarTime = solarTime;
        this.timeView = timeView;
    }

    @Override
    public void run() {
        while (running) {
            String time = solarTime.toString();
            timeView.post(new Runnable() {
                @Override
                public void run() {
                    timeView.setText(time);
                }
            });
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
