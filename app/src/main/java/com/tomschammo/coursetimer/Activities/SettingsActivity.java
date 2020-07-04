package com.tomschammo.coursetimer.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.tomschammo.coursetimer.HelperClasses.SQLHelper;
import com.tomschammo.coursetimer.R;

/**
 * Settings Activity of CourseTimer app. <br>
 *
 * Provides visual layout and functions to change your pay and the currency you're using. <br>
 *
 * Older sessions will not be influenced in any way when changing any of this.
 */

public class SettingsActivity extends Activity {

    private SQLHelper sqlHelper;

    private EditText payTextEdit;

    private Spinner currencySpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        sqlHelper = new SQLHelper(this);

        currencySpinner = findViewById(R.id.spinner_currency);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.currencies, R.layout.spinner_dropdown);

        currencySpinner.setAdapter(arrayAdapter);

        payTextEdit = findViewById(R.id.text_edit_pay);

        payTextEdit.setText(String.valueOf(sqlHelper.getPay()));

        currencySpinner.setSelection(arrayAdapter.getPosition(sqlHelper.getCurrency()));
    }

    /**
     * Changes the pay value in the database if the value in the text field is different from the one stored in the database. <br>
     *
     * Changes the currency value in the database if the selected value in the spinner is different from the one stored in the database. <br>
     */
    @Override
    protected void onStop() {
        super.onStop();

        String pay = payTextEdit.getText().toString();

        if (Double.parseDouble(pay) != sqlHelper.getPay())
            sqlHelper.changePay(Double.parseDouble(pay));

        String currencyText = currencySpinner.getSelectedItem().toString();

        if (!currencyText.equals(sqlHelper.getCurrency()))
            sqlHelper.changeCurrency(currencyText);
    }
}
