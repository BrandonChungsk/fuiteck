package com.example.javath;
import android.content.Intent;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText etDateFrom;
    private EditText etDateTo;
    private Button btnCancel;
    private Button btnNext;

    private Date dateFrom = null;
    private Date dateTo = null;

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(16, 16, 16, 16);

        LinearLayout dateFromLayout = new LinearLayout(this);
        dateFromLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView dateFromLabel = new TextView(this);
        dateFromLabel.setText("Select Date From:");
        dateFromLabel.setTextColor(getResources().getColor(android.R.color.black));

        etDateFrom = new EditText(this);
        etDateFrom.setHint("dd/MM/yyyy");
        etDateFrom.setFocusable(false);
        etDateFrom.setClickable(true);
        etDateFrom.setOnClickListener(v -> showDatePickerDialog(etDateFrom, true));

        // Add "Date From" label and EditText to dateFromLayout
        dateFromLayout.addView(dateFromLabel);
        dateFromLayout.addView(etDateFrom);

        // Create the "Select Date To" Label and EditText programmatically
        LinearLayout dateToLayout = new LinearLayout(this);
        dateToLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView dateToLabel = new TextView(this);
        dateToLabel.setText("Select Date To:");
        dateToLabel.setTextColor(getResources().getColor(android.R.color.black));

        etDateTo = new EditText(this);
        etDateTo.setHint("dd/MM/yyyy");
        etDateTo.setFocusable(false);
        etDateTo.setClickable(true);
        etDateTo.setOnClickListener(v -> showDatePickerDialog(etDateTo, false));

        // Add "Date To" label and EditText to dateToLayout
        dateToLayout.addView(dateToLabel);
        dateToLayout.addView(etDateTo);

        // Create Cancel and Next buttons programmatically
        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

        btnCancel = new Button(this);
        btnCancel.setText("Cancel");
        btnCancel.setOnClickListener(v -> {
            etDateFrom.getText().clear();
            etDateTo.getText().clear();
            dateFrom = null;
            dateTo = null;
            Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
        });

        btnNext = new Button(this);
        btnNext.setText("Next");
        btnNext.setOnClickListener(v -> {
            if (dateFrom != null && dateTo != null) {
                Toast.makeText(
                        MainActivity.this,
                        "Proceeding with: " + dateFormatter.format(dateFrom) + " to " + dateFormatter.format(dateTo),
                        Toast.LENGTH_SHORT
                ).show();
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Please select both dates", Toast.LENGTH_SHORT).show();
            }
        });

        buttonLayout.addView(btnCancel);
        buttonLayout.addView(btnNext);

        mainLayout.addView(dateFromLayout);
        mainLayout.addView(dateToLayout);
        mainLayout.addView(buttonLayout);

        setContentView(mainLayout);
    }

    private void showDatePickerDialog(EditText editText, boolean isFrom) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    Date selectedDate = calendar.getTime();

                    if (isFrom) {
                        dateFrom = selectedDate;
                        editText.setText(dateFormatter.format(selectedDate));
                    } else {
                        dateTo = selectedDate;
                        editText.setText(dateFormatter.format(selectedDate));
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }
}
