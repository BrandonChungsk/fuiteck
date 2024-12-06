package com.example.javath;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity {

    private TableLayout resultsLayout;
    private TextView totalAmountTextView;
    private TextView startDateTextView, endDateTextView;
    private Button sortButton;
    private ArrayList<SalesData> salesDataList = new ArrayList<>();
    private String location;
    private double totalSales = 0.0;
    private int sortMode = 0; // 0: Date Asc, 1: Date Desc, 2: Sales Asc, 3: Sales Desc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        resultsLayout = findViewById(R.id.resultsLayout);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        startDateTextView = findViewById(R.id.startDateTextView);
        endDateTextView = findViewById(R.id.endDateTextView);
        sortButton = findViewById(R.id.sortButton);

        location = getIntent().getStringExtra("location");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");

        startDateTextView.setText("Start Date: " + startDate);
        endDateTextView.setText("End Date: " + endDate);

        // Fetch sales data
        new FetchSalesTask(location, startDate, endDate).execute();

        // Sort button listener
        sortButton.setOnClickListener(v -> {
            sortMode = (sortMode + 1) % 4; // Cycle through 0, 1, 2, 3
            sortAndDisplayData();
        });
    }

    private void sortAndDisplayData() {
        if (salesDataList.isEmpty()) return;

        salesDataList.sort((a, b) -> {
            switch (sortMode) {
                case 0: // Date Ascending
                    return a.getDate().compareTo(b.getDate());
                case 1: // Date Descending
                    return b.getDate().compareTo(a.getDate());
                case 2: // Sales Ascending
                    return Double.compare(a.getSales(), b.getSales());
                case 3: // Sales Descending
                    return Double.compare(b.getSales(), a.getSales());
            }
            return 0;
        });

        sortButton.setText(sortMode == 0 ? "Sort: Date ↓"
                : sortMode == 1 ? "Sort: Date ↑"
                : sortMode == 2 ? "Sort: Sales ↓"
                : "Sort: Sales ↑");

        displaySalesData();
    }

    private void displaySalesData() {
        resultsLayout.removeAllViews(); // Clear existing rows

        // Dynamically add the header based on the location
        TableRow headerRow = new TableRow(SecondActivity.this);
        headerRow.setPadding(10, 10, 10, 10);

        if ("ALL".equals(location)) {
            TextView numberHeader = new TextView(SecondActivity.this);
            numberHeader.setText("No.");
            numberHeader.setTextSize(16);
            numberHeader.setPadding(10, 10, 10, 10);

            TextView outletHeader = new TextView(SecondActivity.this);
            outletHeader.setText("Outlet Code");
            outletHeader.setTextSize(16);
            outletHeader.setPadding(10, 10, 10, 10);

            TextView dateHeader = new TextView(SecondActivity.this);
            dateHeader.setText("Date");
            dateHeader.setTextSize(16);
            dateHeader.setPadding(10, 10, 10, 10);

            TextView salesHeader = new TextView(SecondActivity.this);
            salesHeader.setText("Sales");
            salesHeader.setTextSize(16);
            salesHeader.setPadding(10, 10, 10, 10);
            salesHeader.setGravity(Gravity.END);

            headerRow.addView(numberHeader);
            headerRow.addView(outletHeader);
            headerRow.addView(dateHeader);
            headerRow.addView(salesHeader);
        } else {
            TextView numberHeader = new TextView(SecondActivity.this);
            numberHeader.setText("No.");
            numberHeader.setTextSize(16);
            numberHeader.setPadding(10, 10, 10, 10);

            TextView dateHeader = new TextView(SecondActivity.this);
            dateHeader.setText("Date");
            dateHeader.setTextSize(16);
            dateHeader.setPadding(10, 10, 10, 10);

            TextView salesHeader = new TextView(SecondActivity.this);
            salesHeader.setText("Sales");
            salesHeader.setTextSize(16);
            salesHeader.setPadding(10, 10, 10, 10);
            salesHeader.setGravity(Gravity.END);

            headerRow.addView(numberHeader);
            headerRow.addView(dateHeader);
            headerRow.addView(salesHeader);
        }

        resultsLayout.addView(headerRow);

        // Add the data rows
        int count = 1;
        for (SalesData data : salesDataList) {
            TableRow row = new TableRow(SecondActivity.this);
            row.setPadding(10, 10, 10, 10);

            TextView numberText = new TextView(SecondActivity.this);
            numberText.setText(String.valueOf(count));
            numberText.setPadding(10, 10, 10, 10);

            if ("ALL".equals(location)) {
                TextView outletCodeText = new TextView(SecondActivity.this);
                outletCodeText.setText(data.getOutletCode());
                outletCodeText.setPadding(10, 10, 10, 10);

                TextView dateText = new TextView(SecondActivity.this);
                dateText.setText(data.getDate());
                dateText.setPadding(10, 10, 10, 10);

                TextView salesText = new TextView(SecondActivity.this);
                salesText.setText(String.format(Locale.getDefault(), "%.2f", data.getSales()));
                salesText.setPadding(10, 10, 10, 10);
                salesText.setGravity(Gravity.END);

                // Extract the date without prefix and pass it to ThirdActivity
                String startDate = startDateTextView.getText().toString().replace("Start Date: ", "").trim();
                String endDate = endDateTextView.getText().toString().replace("End Date: ", "").trim();

                // Make the outlet code clickable
                outletCodeText.setOnClickListener(v -> {
                    Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                    intent.putExtra("outletCode", data.getOutletCode());
                    intent.putExtra("startDate", startDate);
                    intent.putExtra("endDate", endDate);
                    startActivity(intent);
                });

                row.addView(numberText);
                row.addView(outletCodeText);
                row.addView(dateText);
                row.addView(salesText);
            } else {
                TextView dateText = new TextView(SecondActivity.this);
                dateText.setText(data.getDate());
                dateText.setPadding(10, 10, 10, 10);

                TextView salesText = new TextView(SecondActivity.this);
                salesText.setText(String.format(Locale.getDefault(), "%.2f", data.getSales()));
                salesText.setPadding(10, 10, 10, 10);
                salesText.setGravity(Gravity.END);

                // Extract the date without prefix and pass it to ThirdActivity
                String startDate = startDateTextView.getText().toString().replace("Start Date: ", "").trim();
                String endDate = endDateTextView.getText().toString().replace("End Date: ", "").trim();

                // Make the date clickable
                dateText.setOnClickListener(v -> {
                    Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                    intent.putExtra("outletCode", location); // Pass the selected location
                    intent.putExtra("startDate", startDate);
                    intent.putExtra("endDate", endDate);
                    startActivity(intent);
                });

                row.addView(numberText);
                row.addView(dateText);
                row.addView(salesText);
            }

            resultsLayout.addView(row);
            count++;
        }
    }

    private class FetchSalesTask extends AsyncTask<Void, Void, Void> {

        private String location, startDate, endDate;

        public FetchSalesTask(String location, String startDate, String endDate) {
            this.location = location;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Connection connection = null;

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                connection = DriverManager.getConnection(
                        "jdbc:jtds:sqlserver://210.187.179.69/POSNEW;user=sa;password=pdsmsde;trustServerCertificate=true;");

                String query;
                if ("ALL".equals(location)) {
                    query = "SELECT PO_LOC_CD, PO_DT, SUM(GrandTotal) AS GrandTotal " +
                            "FROM dbo.v_DailySales " +
                            "WHERE PO_DT BETWEEN ? AND ? " +
                            "GROUP BY PO_LOC_CD, PO_DT";
                } else {
                    query = "SELECT PO_DT, SUM(GrandTotal) AS GrandTotal " +
                            "FROM dbo.v_DailySales " +
                            "WHERE PO_LOC_CD = ? AND PO_DT BETWEEN ? AND ? " +
                            "GROUP BY PO_DT";
                }

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                if ("ALL".equals(location)) {
                    preparedStatement.setString(1, startDate);
                    preparedStatement.setString(2, endDate);
                } else {
                    preparedStatement.setString(1, location);
                    preparedStatement.setString(2, startDate);
                    preparedStatement.setString(3, endDate);
                }

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    if ("ALL".equals(location)) {
                        String outletCode = resultSet.getString("PO_LOC_CD");
                        String date = resultSet.getString("PO_DT").split(" ")[0];
                        double sales = resultSet.getDouble("GrandTotal");
                        totalSales += sales;

                        salesDataList.add(new SalesData(outletCode, date, sales));
                    } else {
                        String date = resultSet.getString("PO_DT").split(" ")[0];
                        double sales = resultSet.getDouble("GrandTotal");
                        totalSales += sales;

                        salesDataList.add(new SalesData("", date, sales));
                    }
                }

                resultSet.close();
                preparedStatement.close();
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

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            displaySalesData();
            totalAmountTextView.setText("Total Sales: RM " + String.format(Locale.getDefault(), "%.2f", totalSales));
        }
    }

    private static class SalesData {
        private String outletCode;
        private String date;
        private double sales;

        public SalesData(String outletCode, String date, double sales) {
            this.outletCode = outletCode;
            this.date = date;
            this.sales = sales;
        }

        public String getOutletCode() {
            return outletCode;
        }

        public String getDate() {
            return date;
        }

        public double getSales() {
            return sales;
        }
    }
}
