package com.tomschammo.coursetimer.UserEvents;

import android.app.Activity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import lombok.Getter;

/**
 *
 * Contains all the logic to handle time and the timer.
 *
 */
@Getter
public class StopWatch {

    // time of session start/end in milliseconds
    private long start, stop;

    // date & time of session start/end
    private String startTime, endTime;

    private boolean running = false;

    /**
     * Starts the timer by setting 'running' to true and setting the value of the start variable to the time of the function call. <br>
     *
     * Starts a thread which counts the time and updates the UI every second.
     */
    public void start(Activity activity, TextView counter) {
        running = true;
        start = System.currentTimeMillis();
        startTime = getCurrentTimeInFormat();

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (running) {
                        Thread.sleep(1000);
                        activity.runOnUiThread(() -> {
                            counter.setText(timeRunning());
                        });

                    }
                }

                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t.start();
    }

    /**
     * When a timer is running it returns the difference between the start time and the time of the function call. <br>
     *
     * Else the difference of the start time and the end time of the last timer will be returned.
     *
     * @return String in format 'hh:mm:ss'.
     */
    private String timeRunning() { return running ? getTime(getStart(), System.currentTimeMillis()) : getTime(start, stop); }

    /**
     * Stops timer by setting 'running' to false and sets the time of the function call as end time.
     */
    public void stop() {
        running = false;
        stop = System.currentTimeMillis();
        endTime =  getCurrentTimeInFormat();
    }

    /**
     * Takes two time stamps as input and returns the difference between the two in format.
     *
     * @param startValue    the start time.
     * @param stopValue     the time when the timer stopped.
     *
     * @return              String in format 'hh:mm:ss'.
     */
    public String getTime(long startValue, long stopValue) {
        String formattedTime;
        long time=(stopValue-startValue)/1000;                                                  //time in seconds   example:3700
        long h=time/3600;                                                                       //hours                     1
        long min=(time%3600)/60;                                                                //minutes                   1
        long sec=(time%3600)%60;                                                                //seconds                   40
        formattedTime= h<10 ? "0"+h+":" : h+":";                                                //formatting hours
        formattedTime= min<10 ? formattedTime+"0"+min+":" : formattedTime+min+":";              //formatting minutes
        formattedTime= sec<10 ? formattedTime+"0"+sec : formattedTime + sec;                    //formatting seconds
        return formattedTime;                                                                   //all in format             01:01:40
    }

    /**
     * Returns difference between two time stamps in minutes.
     *
     * @param startValue    start time.
     * @param stopValue     time when the timer stopped.
     *
     * @return              time difference in minutes.
     */
    public double getTotal(long startValue, long stopValue){
        return (stopValue-startValue)/60000.0;
    }

    /**
     * Returns current time in hh:mm:ss.
     *
     * @return String hh:mm:ss.
     */
    private String getCurrentTimeInFormat() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(calendar.getTime());
    }

}
