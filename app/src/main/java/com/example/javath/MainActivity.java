package com.example.javath;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText startDateEditText;
    private EditText endDateEditText;
    private Button nextButton;

    private Calendar calendar;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the UI components
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        nextButton = findViewById(R.id.nextButton);

        // Get the current date
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        // Set click listener for startDateEditText
        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(startDateEditText);
            }
        });

        // Set click listener for endDateEditText
        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(endDateEditText);
            }
        });

        // Set click listener for Next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch dates from EditText fields
                String startDate = startDateEditText.getText().toString();
                String endDate = endDateEditText.getText().toString();

                // Pass the selected dates to the AsyncTask
                new DatabaseTask(startDate, endDate).execute();
            }
        });
    }

    // Method to show DatePickerDialog
    private void showDatePickerDialog(final EditText editText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Set the date in the EditText after selecting from the DatePicker
                    String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    editText.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
}
