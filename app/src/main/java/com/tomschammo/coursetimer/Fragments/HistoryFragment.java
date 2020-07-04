package com.tomschammo.coursetimer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tomschammo.coursetimer.Activities.AddSessionActivity;
import com.tomschammo.coursetimer.HelperClasses.RecyclerViewAdapter;
import com.tomschammo.coursetimer.HelperClasses.SQLHelper;
import com.tomschammo.coursetimer.R;
import com.tomschammo.coursetimer.UserEvents.Session;

import java.util.ArrayList;

import lombok.NoArgsConstructor;

import static android.app.Activity.RESULT_OK;

/**
 * Fragment to handle the history tab. <br>
 *
 * Provides logic and visual items of the history tab. <br>
 *
 * {@link RecyclerViewAdapter} is used to handle all the list data and appearance of list containing all the sessions.
 *
 */

@NoArgsConstructor
public class HistoryFragment extends Fragment {

    private TextView totalTime, moneyEarned;

    private RecyclerView history;

    private SQLHelper sqlHelper;

    private ArrayList<Session> sessions = null;


    private static final int CODE_WAITING_FOR_CALLBACK = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqlHelper = new SQLHelper(getContext());
    }

    /**
     * Sets the visual layout for the login activity and initializes the important visual items. <br>
     *
     * Swipe behavior is added here as well using the {@link android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback} method. <br>
     *
     * If the user swipes the session away, he gets prompted using a {@link Snackbar} if he is sure that he wants to delete it. <br>
     *
     * If the 'UNDO' button is pressed, the session will not be deleted from the database and it will be restored, else it will and can not be restored.
     *
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        moneyEarned = view.findViewById(R.id.money_earned);
        totalTime = view.findViewById(R.id.total_time);

        history = view.findViewById(R.id.historyList);

        FloatingActionButton addSession = view.findViewById(R.id.floatingAddSessionButton);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        history.setLayoutManager(layoutManager);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            /**
             * If an item is swiped, its removed from the list of items and a {@link Snackbar} appears,
             * that asks the user whether he wants to delete that item or not. <br>
             *
             * If the 'UNDO' button is pressed, the item will be restored. <br>
             *
             * Else if the user either dismissed the {@link Snackbar} or if it times out,
             * the item will be removed from the database and can no longer be restored.
             */
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                int adapterPosition = viewHolder.getAdapterPosition();

                // the session gets removed from the variable only
                if (adapterPosition != -1)
                    sessions.remove(adapterPosition);


                Snackbar.make(view, "Session deleted", Snackbar.LENGTH_SHORT).setAction("UNDO", view1 -> {

                    // The 'UNDO' button has been pressed so the session will not be deleted from the database and therefore will be restored, when the 'sessions' variable gets reinitialized
                    Log.i("HistoryFragment", "Object restored");

                }).addCallback(new Snackbar.Callback() {

                    // the button has not been pressed

                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {

                        super.onDismissed(transientBottomBar, event);

                        // the item will be deleted if the snackbar notification has been dismissed, or has timed out, etc
                        if (event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_SWIPE || event == DISMISS_EVENT_MANUAL || event == DISMISS_EVENT_CONSECUTIVE) {

                            // the session gets completely removed from the database and can no longer be restored
                            if (adapterPosition != -1) {
                                sqlHelper.deleteSession(sqlHelper.getSessions().get(adapterPosition));
                                Log.i("HistoryFragment", "Object deleted");
                            }

                            else
                                Log.e("HistoryFragment", "Something has gone wrong, the adapterPosition equals -1");

                        }

                        // the object will not be deleted from the database, so it will be restored, when the 'sessions' variable will be reinitialized
                        else
                            Log.i("HistoryFragment", "Not going to delete the object");

                        // Session data will be reinitialized
                        initData();

                    }
                }).show();
            }

        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(history);

        addSession.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), AddSessionActivity.class);
            startActivityForResult(intent, CODE_WAITING_FOR_CALLBACK);
        });

        initData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            initData();
        }
    }

    /**
     * Initialises the list data by pulling the data from the database and
     * displays it by using the {@link RecyclerViewAdapter}. <br>
     *
     * Updates total time and total earnings as well.
     */
    private void initData() {

        sessions = sqlHelper.getSessions();

        double money = sqlHelper.getTotalTime()/60 * sqlHelper.getPay();

        moneyEarned.setText((String.format("%.2f", money) + " " + sqlHelper.getCurrency()));

        totalTime.setText((Math.round(sqlHelper.getTotalTime()*100)/100.0 + " minutes"));

        RecyclerViewAdapter listAdapter = new RecyclerViewAdapter(sessions);

        history.setAdapter(listAdapter);
    }


}
