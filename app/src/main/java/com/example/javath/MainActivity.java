package com.example.javath;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText startDateText, endDateText;
    private Button nextButton, cancelButton;
    private Spinner locationSpinner;
    private final Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.endDateText);
        nextButton = findViewById(R.id.nextButton);
        cancelButton = findViewById(R.id.cancelButton);
        locationSpinner = findViewById(R.id.locationSpinner);

        // Set default dates
        startDateText.setText(dateFormatter.format(calendar.getTime()));
        endDateText.setText(dateFormatter.format(calendar.getTime()));

        // Initialize spinner with a default adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        // Load locations asynchronously
        new LoadLocationsTask().execute();

        // Set listeners for date pickers
        startDateText.setOnClickListener(v -> showDatePickerDialog(startDateText));
        endDateText.setOnClickListener(v -> showDatePickerDialog(endDateText));

        // Next button listener
        nextButton.setOnClickListener(v -> {
            String selectedLocation = (String) locationSpinner.getSelectedItem();
            String startDate = startDateText.getText().toString();
            String endDate = endDateText.getText().toString();

            if (selectedLocation == null || selectedLocation.isEmpty()) {
                Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT).show();
                return;
            }

            if (startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Please select valid start and end dates", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("location", selectedLocation);
            intent.putExtra("startDate", startDate);
            intent.putExtra("endDate", endDate);
            startActivity(intent);
        });

        // Cancel button listener
        cancelButton.setOnClickListener(v -> {
            startDateText.setText("");
            endDateText.setText("");
        });
    }

    private void showDatePickerDialog(final EditText dateText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, monthOfYear, dayOfMonth);
                    dateText.setText(dateFormatter.format(selectedDate.getTime()));
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private class LoadLocationsTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> locations = new ArrayList<>();
            Connection connection = null;

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                connection = DriverManager.getConnection(
                        "jdbc:jtds:sqlserver://210.187.179.69/POSNEW;user=sa;password=pdsmsde;trustServerCertificate=true;");

                // Query for distinct location codes
                String query = "SELECT DISTINCT PO_LOC_CD FROM dbo.POS_ORDERS";
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                ResultSet resultSet = preparedStatement.executeQuery();
                locations.add("ALL"); // Add "ALL" as the default option
                while (resultSet.next()) {
                    locations.add(resultSet.getString("PO_LOC_CD"));
                }

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return locations;
        }

        @Override
        protected void onPostExecute(ArrayList<String> locations) {
            if (!locations.isEmpty()) {
                adapter.clear();
                adapter.addAll(locations);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this, "Failed to load locations", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
