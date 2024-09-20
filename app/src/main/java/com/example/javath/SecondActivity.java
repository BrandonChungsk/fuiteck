package com.example.javath;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Date;

public class SecondActivity extends AppCompatActivity {

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Find the result TextView in the layout
        resultTextView = findViewById(R.id.resultTextView);

        // Retrieve the start and end dates passed from MainActivity
        long startDateMillis = getIntent().getLongExtra("startDate", -1);
        long endDateMillis = getIntent().getLongExtra("endDate", -1);

        Date startDate = new Date(startDateMillis);
        Date endDate = new Date(endDateMillis);

        // Start the AsyncTask to fetch data from the database
        new DatabaseTask(startDate, endDate, new DatabaseTask.DatabaseTaskListener() {
            @Override
            public void onDataFetched(String data) {
                // Update the TextView with the result data
                resultTextView.setText(data);
            }
        }).execute();
    }
}
