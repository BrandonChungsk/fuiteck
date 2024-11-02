package com.example.javath;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText startDateText, endDateText;
    private Button nextButton, cancelButton;
    private final Calendar calendar = Calendar.getInstance();
    private Calendar selectedStartDate = Calendar.getInstance(); // To track selected start date
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.endDateText);
        nextButton = findViewById(R.id.nextButton);
        cancelButton = findViewById(R.id.cancelButton);

        startDateText.setText(dateFormatter.format(calendar.getTime()));
        endDateText.setText(dateFormatter.format(calendar.getTime()));

        startDateText.setOnClickListener(v -> showStartDatePickerDialog());

        endDateText.setOnClickListener(v -> showEndDatePickerDialog());

        nextButton.setOnClickListener(v -> {
            String startDate = startDateText.getText().toString();
            String endDate = endDateText.getText().toString();

            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("startDate", startDate);
            intent.putExtra("endDate", endDate);
            startActivity(intent);
        });

        cancelButton.setOnClickListener(v -> {
            startDateText.setText("");
            endDateText.setText("");
        });
    }

    private void showStartDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    selectedStartDate.set(year, monthOfYear, dayOfMonth);
                    startDateText.setText(dateFormatter.format(selectedStartDate.getTime()));
                }, selectedStartDate.get(Calendar.YEAR), selectedStartDate.get(Calendar.MONTH), selectedStartDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showEndDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar selectedEndDate = Calendar.getInstance();
                    selectedEndDate.set(year, monthOfYear, dayOfMonth);
                    endDateText.setText(dateFormatter.format(selectedEndDate.getTime()));
                }, selectedStartDate.get(Calendar.YEAR), selectedStartDate.get(Calendar.MONTH), selectedStartDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
