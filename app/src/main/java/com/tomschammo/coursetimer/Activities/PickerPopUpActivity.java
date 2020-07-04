package com.tomschammo.coursetimer.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.tomschammo.coursetimer.R;


/**
 *
 * Activity to handle Popups with time / DatePickers.
 *
 */

public class PickerPopUpActivity extends Activity {

    /**
     * Sets the visual layout for the login activity and initializes the important visual items. <br>
     *
     * Depending on the extras sent in the intent, a timePicker or a datePicker will be displayed. <br>
     *
     * Uses either the {@link PickerPopUpActivity#getDateInFormat(int, int, int)} or {@link PickerPopUpActivity#getTimeInFormat(int, int)}
     * method to format the time/date that has been entered by the user and pass it back when the button is clicked
     * and this activity gets terminated.
     *
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_popup_picker);


        TimePicker timePicker = findViewById(R.id.picker_time);

        DatePicker datePicker = findViewById(R.id.session_date_picker);


        boolean dt = getIntent().getBooleanExtra("dt", false);

        if (dt)
            timePicker.setVisibility(View.VISIBLE);


        else
            datePicker.setVisibility(View.VISIBLE);


        Button setButton = findViewById(R.id.button_set);

        setButton.setOnClickListener(view -> {

            Intent intent = new Intent();

            intent.putExtra("data", dt ? getTimeInFormat(timePicker.getHour(), timePicker.getMinute()) : getDateInFormat(datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear()));

            setResult(RESULT_OK, intent);

            finish();
        });
    }

    /**
     * Creates date String in dd/mm/yy format using day, month and year provided in parameters. <br>
     *
     * Used to pass a formatted String back when this activity is stopped. <br>
     *
     * @param day   int day of month.
     * @param month int month of year.
     * @param year  int year.
     * <br>
     * @return String representing a date in dd/mm/yy format.
     */
    private String getDateInFormat(int day, int month, int year) {

        String s = "";

        s += day < 10 ? "0" + day : day;
        s += "/";
        s += month < 10 ? "0" + month : month;
        s += "/" + year;

        return s;
    }

    /**
     * Creates time String in hh:mm using hour and minute provided in parameters. <br>
     *
     * Used to pass a formatted String back when this activity is stopped. <br>
     *
     * @param hour      int hour.
     * @param minute    int minute.
     * <br>
     * @return String representing a time in hh:mm format.
     */
    private String getTimeInFormat(int hour, int minute) {

        String s = "";

        s += hour < 10 ? "0" + hour : hour;
        s += ":";
        s += minute < 10 ? "0" + minute : minute;
        s += ":00";

        return s;
    }
}
