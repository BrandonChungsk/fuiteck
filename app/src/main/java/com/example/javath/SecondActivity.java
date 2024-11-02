package com.example.javath;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

    private LinearLayout resultsLayout;
    private TextView totalAmountTextView;
    private TextView startDateTextView, endDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        resultsLayout = findViewById(R.id.resultsLayout);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        startDateTextView = findViewById(R.id.startDateTextView);
        endDateTextView = findViewById(R.id.endDateTextView);

        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");

        startDateTextView.setText("Start Date: " + startDate);
        endDateTextView.setText("End Date: " + endDate);

        new FetchSalesTask(startDate, endDate).execute();
    }

    private class FetchSalesTask extends AsyncTask<Void, Void, ArrayList<SaleData>> {

        private String startDate, endDate;
        private double totalSales;

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
                    totalSales += grandTotal;

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
            TableLayout tableLayout = new TableLayout(SecondActivity.this);
            tableLayout.setStretchAllColumns(true);

            TableRow headerRow = new TableRow(SecondActivity.this);
            headerRow.setPadding(30, 30, 30, 30);

            TextView headerOutletCode = new TextView(SecondActivity.this);
            headerOutletCode.setText("Outlet Code");
            headerOutletCode.setTextSize(18);
            headerOutletCode.setPadding(30, 30, 30, 30);
            headerOutletCode.setGravity(android.view.Gravity.START);

            TextView headerSales = new TextView(SecondActivity.this);
            headerSales.setText("Sales");
            headerSales.setTextSize(18);
            headerSales.setPadding(30, 30, 90, 30);
            headerSales.setGravity(android.view.Gravity.END);

            headerRow.addView(headerOutletCode);
            headerRow.addView(headerSales);
            tableLayout.addView(headerRow);

            for (SaleData saleData : saleDataList) {
                TableRow row = new TableRow(SecondActivity.this);
                row.setPadding(30, 10, 30, 10);

                TextView outletCodeView = new TextView(SecondActivity.this);
                outletCodeView.setText(saleData.getOutletCode());
                outletCodeView.setTextSize(16);
                outletCodeView.setPadding(30, 10, 30, 10);
                outletCodeView.setGravity(android.view.Gravity.START);

                TextView salesView = new TextView(SecondActivity.this);
                salesView.setText(String.format(Locale.getDefault(), "%.2f", saleData.getGrandTotal()));
                salesView.setTextSize(16);
                salesView.setPadding(30, 10, 90, 45);
                salesView.setGravity(android.view.Gravity.END);

                row.addView(outletCodeView);
                row.addView(salesView);
                tableLayout.addView(row);

                final String outletCode = saleData.getOutletCode();

                outletCodeView.setOnClickListener(view -> {
                    Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                    intent.putExtra("outletCode", outletCode);
                    intent.putExtra("startDate", startDate);
                    intent.putExtra("endDate", endDate);
                    startActivity(intent);
                });
            }

            resultsLayout.addView(tableLayout);
            totalAmountTextView.setText("Total Sales: RM " + String.format(Locale.getDefault(), "%.2f", totalSales));
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
