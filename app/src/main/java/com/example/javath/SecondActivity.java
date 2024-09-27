package com.example.javath;

import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity {

    private TextView dateRangeTextView, totalSalesTextView;
    private TableLayout resultsTable;
    private SimpleDateFormat dateTimeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        dateRangeTextView = findViewById(R.id.dateRangeTextView);
        resultsTable = findViewById(R.id.resultsTable);
        totalSalesTextView = findViewById(R.id.totalSalesTextView);

        dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

        String startDateInput = getIntent().getStringExtra("startDate");
        String endDateInput = getIntent().getStringExtra("endDate");

        String startDate = getStartOfDay(startDateInput);
        String endDate = getEndOfDay(endDateInput);

        if (startDate != null && endDate != null) {
            dateRangeTextView.setText("Selected Start Date: " + startDate + "\nSelected End Date: " + endDate);
        } else {
            dateRangeTextView.setText("Dates not available");
        }

        new DatabaseTask(startDate, endDate, new DatabaseTask.DatabaseTaskListener() {
            @Override
            public void onDataFetched(String result) {
                populateTable(result);
            }

            @Override
            public void onError(String errorMessage) {
                dateRangeTextView.setText("Error: " + errorMessage);
            }
        }).execute();
    }

    private void populateTable(String data) {
        // Assuming that the data is formatted as "Date|Outlet Code|Sale\n..." per row
        String[] rows = data.split("\n");
        double totalSales = 0.0;

        // Clear any previous data in the table
        resultsTable.removeAllViews();

        // Add the table header only once
        if (resultsTable.getChildCount() == 0) {
            TableRow headerRow = new TableRow(this);
            headerRow.addView(createTextView("Date"));
            headerRow.addView(createTextView("Outlet Code"));
            headerRow.addView(createTextView("Sale"));
            resultsTable.addView(headerRow);
        }

        // Loop through the rows and populate the table
        for (String row : rows) {
            String[] columns = row.split("\\|");
            if (columns.length == 3) {
                TableRow tableRow = new TableRow(this);
                tableRow.addView(createTextView(columns[0].trim())); // Date
                tableRow.addView(createTextView(columns[1].trim())); // Outlet Code
                tableRow.addView(createTextView(columns[2].trim())); // Sale

                // Parse sale amount and add to total sales
                try {
                    totalSales += Double.parseDouble(columns[2].trim());
                } catch (NumberFormatException e) {
                    Log.e("SecondActivity", "Error parsing sale amount: " + e.getMessage());
                }

                resultsTable.addView(tableRow);
            }
        }

        // Display the total sales
        totalSalesTextView.setText(String.format(Locale.getDefault(), "Total Sales: %.2f", totalSales));
    }


    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setPadding(8, 8, 8, 8);
        textView.setText(text);
        return textView;
    }

    private String getStartOfDay(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(date));
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return dateTimeFormat.format(calendar.getTime());
        } catch (Exception e) {
            Log.e("SecondActivity", "Error parsing start date: " + e.getMessage());
            return null;
        }
    }

    private String getEndOfDay(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(date));
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            return dateTimeFormat.format(calendar.getTime());
        } catch (Exception e) {
            Log.e("SecondActivity", "Error parsing end date: " + e.getMessage());
            return null;
        }
    }
}
