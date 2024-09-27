package com.example.javath;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText startDateEditText, endDateEditText;
    private String selectedStartDate, selectedEndDate;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        Button nextButton = findViewById(R.id.nextButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        calendar = Calendar.getInstance();

        startDateEditText.setOnClickListener(v -> showDatePicker(startDateEditText, true));
        endDateEditText.setOnClickListener(v -> showDatePicker(endDateEditText, false));

        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("startDate", selectedStartDate);
            intent.putExtra("endDate", selectedEndDate);
            startActivity(intent);
        });

        cancelButton.setOnClickListener(v -> {
            // Reset the date fields
            startDateEditText.setText("");
            endDateEditText.setText("");
        });
    }

    private void showDatePicker(final EditText editText, final boolean isStartDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = dateFormat.format(calendar.getTime());

                    editText.setText(formattedDate);
                    if (isStartDate) {
                        selectedStartDate = formattedDate;
                    } else {
                        selectedEndDate = formattedDate;
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}
