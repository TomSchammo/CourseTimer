package com.tomschammo.coursetimer.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tomschammo.coursetimer.Activities.SettingsActivity;
import com.tomschammo.coursetimer.Activities.StandardPopUpActivity;
import com.tomschammo.coursetimer.HelperClasses.SQLHelper;
import com.tomschammo.coursetimer.R;
import com.tomschammo.coursetimer.UserEvents.StopWatch;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.NoArgsConstructor;

import static android.app.Activity.RESULT_OK;

/**
 * Fragment to handle the Stopwatch tab / Main tab. <br>
 *
 * Provides all the logic and visual items of the Stopwatch tab.
 *
 */

@NoArgsConstructor
public class StopWatchFragment extends Fragment {

    private StopWatch stopWatch;

    private SQLHelper sqlHelper;

    public static final int POPUP_CODE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqlHelper = new SQLHelper(getContext());
    }

    /**
     * Sets the visual layout for the login activity and initializes the important visual items. <br>
     *
     * When the start button is pushed, a new {@link StopWatch} object is created and the counter is started
     * using the {@link StopWatch#start(Activity, TextView)} method and updates the button icon. <br>
     *
     * When the stop button is pushed, the {@link StopWatch#stop()} method is called, which stops the counter.
     *
     * And the button icon is updated again. <br>
     *
     * The settings button is set as well to open the {@link SettingsActivity} on click.
     *
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        ImageButton start = view.findViewById(R.id.startButton);
        ImageButton stop = view.findViewById(R.id.stopButton);
        TextView showCounter = view.findViewById(R.id.showCounter);
        FloatingActionButton floatingSettingsButton = view.findViewById(R.id.floatingSettingsButton);

        start.setOnClickListener(view1 -> {

            stopWatch = new StopWatch();
            stopWatch.start(getActivity(), showCounter);

            start.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);

        });

        stop.setOnClickListener(view1 -> {

            stopWatch.stop();


            Intent intent = new Intent(getActivity(), StandardPopUpActivity.class);
            startActivityForResult(intent, POPUP_CODE);

            stop.setVisibility(View.GONE);
            start.setVisibility(View.VISIBLE);

        });

        floatingSettingsButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        });

        return view;
    }

    /**
     * After the name has been retrieved from the PopUp Activity, the new Session will be added
     * to the database with all the data. <br>
     *
     * If the resultCode is not equal to {@code RESULT_OK} or the {@link Intent} is equal to null,
     * the client name would be null.
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        double duration = stopWatch.getTotal(stopWatch.getStart(), stopWatch.getStop());
        double moneyEarned = ((int)((duration/60.0) * sqlHelper.getPay() * 100))/100.0;

        if (resultCode == RESULT_OK) {

            if (data != null && data.getExtras() != null) {
                String name = data.getExtras().getString("name");
                sqlHelper.addSession(date, stopWatch.getStartTime(), stopWatch.getEndTime(), stopWatch.getTime(stopWatch.getStart(), stopWatch.getStop()), name, moneyEarned);
            }

            else
                sqlHelper.addSession(date, stopWatch.getStartTime(), stopWatch.getEndTime(), stopWatch.getTime(stopWatch.getStart(), stopWatch.getStop()), null, moneyEarned);
        }

        else
            sqlHelper.addSession(date, stopWatch.getStartTime(), stopWatch.getEndTime(), stopWatch.getTime(stopWatch.getStart(), stopWatch.getStop()), null, moneyEarned);
    }
}
