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
import java.util.Locale;

public class ThirdActivity extends AppCompatActivity {

    private LinearLayout receiptLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        receiptLayout = findViewById(R.id.receiptLayout);

        String outletCode = getIntent().getStringExtra("outletCode");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");

        System.out.println("Received Parameters: OutletCode=" + outletCode + ", StartDate=" + startDate + ", EndDate=" + endDate);

        if (outletCode != null && startDate != null && endDate != null) {
            new FetchReceiptDetailsTask(outletCode, startDate, endDate).execute();
        } else {
            System.out.println("Error: Missing parameters. OutletCode=" + outletCode +
                    ", StartDate=" + startDate + ", EndDate=" + endDate);
        }
    }

    private class FetchReceiptDetailsTask extends AsyncTask<Void, Void, ArrayList<ReceiptData>> {

        private String outletCode, startDate, endDate;

        public FetchReceiptDetailsTask(String outletCode, String startDate, String endDate) {
            this.outletCode = outletCode;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        @Override
        protected ArrayList<ReceiptData> doInBackground(Void... voids) {
            ArrayList<ReceiptData> receiptDataList = new ArrayList<>();
            Connection connection = null;

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                connection = DriverManager.getConnection(
                        "jdbc:jtds:sqlserver://210.187.179.69/POSNEW;user=sa;password=pdsmsde;trustServerCertificate=true;");

                String query;
                PreparedStatement preparedStatement;

                if ("ALL".equalsIgnoreCase(outletCode)) {
                    // Query without filtering by location
                    query = "SELECT ReceiptNo, CONVERT(VARCHAR(10), ReceiptDate, 120) AS ReceiptDate, " +
                            "Location, Discount, Amount FROM dbo.V_DailySalesBill " +
                            "WHERE ReceiptDate BETWEEN ? AND ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, startDate);
                    preparedStatement.setString(2, endDate);
                } else {
                    // Query with filtering by specific location
                    query = "SELECT ReceiptNo, CONVERT(VARCHAR(10), ReceiptDate, 120) AS ReceiptDate, " +
                            "Location, Discount, Amount FROM dbo.V_DailySalesBill " +
                            "WHERE Location = ? AND ReceiptDate BETWEEN ? AND ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, outletCode);
                    preparedStatement.setString(2, startDate);
                    preparedStatement.setString(3, endDate);
                }

                System.out.println("Query executed: " + query);
                System.out.println("Parameters: Location=" + outletCode + ", StartDate=" + startDate + ", EndDate=" + endDate);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String receiptNo = resultSet.getString("ReceiptNo");
                    String receiptDate = resultSet.getString("ReceiptDate");
                    String location = resultSet.getString("Location");
                    double discount = resultSet.getDouble("Discount");
                    double amount = resultSet.getDouble("Amount");

                    System.out.println("Fetched Receipt: " + receiptNo + ", Date: " + receiptDate + ", Amount: " + amount);

                    receiptDataList.add(new ReceiptData(receiptNo, receiptDate, location, discount, amount));
                }

                resultSet.close();
                preparedStatement.close();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Database Error: " + e.getMessage());
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return receiptDataList;
        }

        @Override
        protected void onPostExecute(ArrayList<ReceiptData> receiptDataList) {
            if (receiptDataList.isEmpty()) {
                System.out.println("No receipts found for the selected criteria.");
                TextView noDataTextView = new TextView(ThirdActivity.this);
                noDataTextView.setText("No receipts found for the selected criteria.");
                noDataTextView.setPadding(15, 15, 15, 15);
                receiptLayout.addView(noDataTextView);
                return;
            }

            TableLayout tableLayout = new TableLayout(ThirdActivity.this);
            tableLayout.setStretchAllColumns(true);

            // Create and add header row
            TableRow headerRow = new TableRow(ThirdActivity.this);
            headerRow.setPadding(10, 10, 10, 10);

            TextView headerReceiptNo = new TextView(ThirdActivity.this);
            headerReceiptNo.setText("Receipt No");
            headerReceiptNo.setTextSize(14);
            headerReceiptNo.setPadding(15, 15, 15, 15);

            TextView headerDate = new TextView(ThirdActivity.this);
            headerDate.setText("Date");
            headerDate.setTextSize(14);
            headerDate.setPadding(15, 15, 15, 15);

            TextView headerDiscount = new TextView(ThirdActivity.this);
            headerDiscount.setText("Discount");
            headerDiscount.setTextSize(14);
            headerDiscount.setPadding(15, 15, 15, 15);

            TextView headerAmount = new TextView(ThirdActivity.this);
            headerAmount.setText("Amount (RM)");
            headerAmount.setTextSize(16);
            headerAmount.setPadding(15, 15, 15, 15);

            headerRow.addView(headerReceiptNo);
            headerRow.addView(headerDate);
            headerRow.addView(headerDiscount);
            headerRow.addView(headerAmount);

            tableLayout.addView(headerRow);

            // Add data rows
            for (ReceiptData receipt : receiptDataList) {
                TableRow row = new TableRow(ThirdActivity.this);
                row.setPadding(10, 10, 10, 10);

                TextView receiptNoView = new TextView(ThirdActivity.this);
                receiptNoView.setText(receipt.getReceiptNo());
                receiptNoView.setTextSize(12);
                receiptNoView.setPadding(15, 15, 15, 15);

                // Set OnClickListener to navigate to FourthActivity
                final String receiptNo = receipt.getReceiptNo();
                receiptNoView.setOnClickListener(view -> {
                    Intent intent = new Intent(ThirdActivity.this, FourthActivity.class);
                    intent.putExtra("receiptNo", receiptNo);
                    startActivity(intent);
                });

                TextView dateView = new TextView(ThirdActivity.this);
                dateView.setText(receipt.getReceiptDate());
                dateView.setTextSize(12);
                dateView.setPadding(15, 15, 15, 15);

                TextView discountView = new TextView(ThirdActivity.this);
                discountView.setText(String.format(Locale.getDefault(), "%.2f", receipt.getDiscount()));
                discountView.setTextSize(12);
                discountView.setPadding(15, 15, 15, 15);

                TextView amountView = new TextView(ThirdActivity.this);
                amountView.setText(String.format(Locale.getDefault(), "%.2f", receipt.getAmount()));
                amountView.setTextSize(16);
                amountView.setPadding(15, 15, 15, 15);

                row.addView(receiptNoView);
                row.addView(dateView);
                row.addView(discountView);
                row.addView(amountView);

                tableLayout.addView(row);
            }

            receiptLayout.addView(tableLayout);
        }
    }

    private class ReceiptData {
        private String receiptNo, receiptDate, location;
        private double discount, amount;

        public ReceiptData(String receiptNo, String receiptDate, String location, double discount, double amount) {
            this.receiptNo = receiptNo;
            this.receiptDate = receiptDate;
            this.location = location;
            this.discount = discount;
            this.amount = amount;
        }

        public String getReceiptNo() {
            return receiptNo;
        }

        public String getReceiptDate() {
            return receiptDate;
        }

        public double getDiscount() {
            return discount;
        }

        public double getAmount() {
            return amount;
        }
    }
}
