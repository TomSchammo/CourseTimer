package com.tomschammo.coursetimer.Activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tomschammo.coursetimer.HelperClasses.SQLHelper;
import com.tomschammo.coursetimer.R;


/**
 *
 * Activity provides functions and interface to add sessions manually to the database.
 *
 */

public class AddSessionActivity extends Activity {

    private SQLHelper sqlHelper = new SQLHelper(this);

    private TextView pickDateButton, pickStartTimeButton, pickEndTimeButton;

    private EditText clientName;

    private String name;
    private String start, stop;
    private String date;

    private static final int PICK_DATE = 0, PICK_START = 1, PICK_END = 2;

    /**
     * Sets the visual layout for the login activity and initializes the important visual items. <br>
     *
     * Adds button listeners to the various buttons and makes sure that the right Activity is started for the
     * Buttons that are responsible for collecting user input data. <br>
     *
     * If the addSessionButton has been clicked and the user has entered all the necessary data, the session gets added to the database.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_session);

        Button addSessionButton = findViewById(R.id.addSession);

        pickDateButton = findViewById(R.id.edit_text_pick_session_date);
        pickStartTimeButton = findViewById(R.id.edit_text_pick_start_time);
        pickEndTimeButton = findViewById(R.id.edit_text_pick_end_time);


        clientName = findViewById(R.id.client_name);


        pickDateButton.setOnClickListener(view -> {
            Intent intent = new Intent(AddSessionActivity.this, PickerPopUpActivity.class);

            // specifies that the Activity should give back a date as Result
            intent.putExtra("dt", false);

            startActivityForResult(intent, PICK_DATE);
        });

        pickStartTimeButton.setOnClickListener(view -> {

            Intent intent = new Intent(AddSessionActivity.this, PickerPopUpActivity.class);

            // specifies that the Activity should give back a time as Result
            intent.putExtra("dt", true);

            startActivityForResult(intent, PICK_START);
        });

        pickEndTimeButton.setOnClickListener(view -> {

            Intent intent = new Intent(AddSessionActivity.this, PickerPopUpActivity.class);

            // specifies that the Activity should give back a time as Result
            intent.putExtra("dt", true);

            startActivityForResult(intent, PICK_END);

        });


        addSessionButton.setOnClickListener(view -> {

            if (!(clientName.getText().toString().isEmpty() && pickDateButton.getText().toString().isEmpty() && pickStartTimeButton.getText().toString().isEmpty() && pickEndTimeButton.getText().toString().isEmpty())) {

                name = clientName.getText().toString();
                date = pickDateButton.getText().toString();
                start = pickStartTimeButton.getText().toString();
                stop = pickEndTimeButton.getText().toString();

                int total = getTotal(Integer.parseInt(start.split(":")[0]), Integer.parseInt(start.split(":")[1]), Integer.parseInt(stop.split(":")[0]), Integer.parseInt(stop.split(":")[1]));

                Log.i("AddSessionActivity", "Adding session with: ");
                Log.i("AddSessionActivity", "Date = " + date);
                Log.i("AddSessionActivity", "start: " + start + ", stop: " + stop);
                Log.i("AddSessionActivity", "Total = " + total);

                sqlHelper.addSession(date, start, stop, total + " minutes", name, ((int)((total/60.0) * sqlHelper.getPay() * 100))/100.0);

                finish();
            }

            else {
                Toast.makeText(this, "Please enter valid data for all the required fields", Toast.LENGTH_SHORT).show();
            }

        });

    }

    /**
     * Sets the result data to the according visual element, specified by the requestCode, if the activity has been terminated properly. <br>
     *
     * @param requestCode   Used to specify what the result data belongs to.
     * @param resultCode    Result code of the activity. Passes the information whether the activity has been terminated properly.
     * @param data          The data that has been passed on from the Activity that has been stopped that caused this method to be called.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_DATE:
                    pickDateButton.setText(data.getStringExtra("data")); break;

                case PICK_START:
                    pickStartTimeButton.setText(data.getStringExtra("data")); break;

                case PICK_END:
                    pickEndTimeButton.setText(data.getStringExtra("data")); break;

            }
        }

        else
            Log.w("AddSessionActivity", "The result code does not not match 'RESULT_OK'!");
    }


    /**
     * Returns the total time between the start time and the end time. <br>
     *
     * @param h1 Hours of start time.
     * @param m1 Minutes of start time.
     * @param h2 Hours of end time.
     * @param m2 Minutes of end time.
     * <br>
     * @return The time difference in minutes.
     */
    private int getTotal(int h1, int m1, int h2, int m2) {
        return h1 > h2 ? (h1 - h2) * 60 + (m1 - m2) + 24 * 60 : (h2 - h1) * 60 + (m2 - m1);
    }

}
