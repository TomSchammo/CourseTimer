package com.tomschammo.coursetimer.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tomschammo.coursetimer.R;


/**
 * Activity to handle Standard Popups. <br>
 *
 * Standard PopUps contain a Editable text field and a 'add' button only. <br>
 *
 * If you need to use Time/Date Pickers refer to the {@link PickerPopUpActivity} instead.
 *
 */

public class StandardPopUpActivity extends Activity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_standard);

        editText = findViewById(R.id.text_edit_content);

        Button add = findViewById(R.id.add_button);

        add.setOnClickListener(view -> {
            Intent intent = new Intent();

            intent.putExtra("name", editText.getText().toString());

            setResult(RESULT_OK, intent);

            finish();
            
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
    }
}
