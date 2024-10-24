package com.example.javath;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;
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

public class SecondActivity extends AppCompatActivity {

    private TableLayout resultsLayout;  // Modified to TableLayout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        resultsLayout = findViewById(R.id.resultsLayout);  // Corrected to TableLayout

        // Added start and end date TextView handling
        TextView startDateTextView = findViewById(R.id.startDateTextView);
        TextView endDateTextView = findViewById(R.id.endDateTextView);

        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");

        startDateTextView.setText("Start Date: " + startDate);
        endDateTextView.setText("End Date: " + endDate);

        new FetchSalesTask(startDate, endDate).execute();
    }

    private class FetchSalesTask extends AsyncTask<Void, Void, ArrayList<SaleData>> {

        private String startDate, endDate;

        public FetchSalesTask(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        @Override
        protected ArrayList<SaleData> doInBackground(Void... voids) {
            ArrayList<SaleData> saleDataList = new ArrayList<>();
            Connection connection = null;

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                connection = DriverManager.getConnection(
                        "jdbc:jtds:sqlserver://210.187.179.69/POSTEST;user=sa;password=pdsmsde;trustServerCertificate=true;");

                String query = "SELECT PO_LOC_CD, GrandTotal FROM dbo.v_DailySales WHERE PO_DT BETWEEN ? AND ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, startDate);
                preparedStatement.setString(2, endDate);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String outletCode = resultSet.getString("PO_LOC_CD");
                    double grandTotal = resultSet.getDouble("GrandTotal");

                    SaleData saleData = new SaleData(outletCode, grandTotal);
                    saleDataList.add(saleData);
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
            return saleDataList;
        }

        @Override
        protected void onPostExecute(ArrayList<SaleData> saleDataList) {
            for (SaleData saleData : saleDataList) {
                TableRow row = new TableRow(SecondActivity.this);

                TextView outletCodeTextView = new TextView(SecondActivity.this);
                outletCodeTextView.setText(saleData.getOutletCode());
                outletCodeTextView.setPadding(10, 10, 10, 10);
                outletCodeTextView.setGravity(android.view.Gravity.CENTER);

                TextView grandTotalTextView = new TextView(SecondActivity.this);
                grandTotalTextView.setText(String.valueOf(saleData.getGrandTotal()));
                grandTotalTextView.setPadding(10, 10, 10, 10);
                grandTotalTextView.setGravity(android.view.Gravity.CENTER);

                // Click listener for outlet code
                final String outletCode = saleData.getOutletCode();
                outletCodeTextView.setOnClickListener(view -> {
                    Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                    intent.putExtra("outletCode", outletCode);
                    intent.putExtra("startDate", startDate);
                    intent.putExtra("endDate", endDate);
                    startActivity(intent);
                });

                row.addView(outletCodeTextView);
                row.addView(grandTotalTextView);

                resultsLayout.addView(row);
            }
        }
    }

    private class SaleData {
        private String outletCode;
        private double grandTotal;

        public SaleData(String outletCode, double grandTotal) {
            this.outletCode = outletCode;
            this.grandTotal = grandTotal;
        }

        public String getOutletCode() {
            return outletCode;
        }

        public double getGrandTotal() {
            return grandTotal;
        }
    }
}
