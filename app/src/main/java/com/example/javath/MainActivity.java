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

        startDateText.setOnClickListener(v -> showDatePickerDialog(startDateText));

        endDateText.setOnClickListener(v -> showDatePickerDialog(endDateText));

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

    private void showDatePickerDialog(final EditText dateText) {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, monthOfYear, dayOfMonth);
                    dateText.setText(dateFormatter.format(selectedDate.getTime()));
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
