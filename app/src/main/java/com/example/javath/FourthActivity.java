package com.example.javath;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.HorizontalScrollView;
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

public class FourthActivity extends AppCompatActivity {

    private LinearLayout receiptDetailLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        // Wrapping the content in a scrollable view
        ScrollView verticalScrollView = new ScrollView(this);
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);

        receiptDetailLayout = new LinearLayout(this);
        receiptDetailLayout.setOrientation(LinearLayout.VERTICAL);

        horizontalScrollView.addView(receiptDetailLayout);
        verticalScrollView.addView(horizontalScrollView);
        setContentView(verticalScrollView);

        String receiptNo = getIntent().getStringExtra("receiptNo");

        new FetchReceiptDetailsTask(receiptNo).execute();
    }

    private class FetchReceiptDetailsTask extends AsyncTask<Void, Void, ArrayList<ReceiptDetail>> {

        private String receiptNo;

        public FetchReceiptDetailsTask(String receiptNo) {
            this.receiptNo = receiptNo;
        }

        @Override
        protected ArrayList<ReceiptDetail> doInBackground(Void... voids) {
            ArrayList<ReceiptDetail> receiptDetails = new ArrayList<>();
            Connection connection = null;

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                connection = DriverManager.getConnection(
                        "jdbc:jtds:sqlserver://210.187.179.69/POSNEW;user=sa;password=pdsmsde;trustServerCertificate=true;"
                );

                String query = "SELECT POD_ITEM_CD, POD_DESC, POD_QTY, POD_DISCOUNT, POD_AMOUNT " +
                        "FROM dbo.V_DailySalesBillDetail WHERE PO_ID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, receiptNo);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String itemCode = resultSet.getString("POD_ITEM_CD");
                    String description = resultSet.getString("POD_DESC");
                    int quantity = resultSet.getInt("POD_QTY");
                    double discount = resultSet.getDouble("POD_DISCOUNT");
                    double amount = resultSet.getDouble("POD_AMOUNT");

                    ReceiptDetail detail = new ReceiptDetail(itemCode, description, quantity, discount, amount);
                    receiptDetails.add(detail);
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
            return receiptDetails;
        }

        @Override
        protected void onPostExecute(ArrayList<ReceiptDetail> receiptDetails) {
            TableLayout tableLayout = new TableLayout(FourthActivity.this);
            tableLayout.setStretchAllColumns(true);

            // Header Row
            TableRow headerRow = new TableRow(FourthActivity.this);
            headerRow.setPadding(20, 10, 20, 10);

            TextView headerItemCode = new TextView(FourthActivity.this);
            headerItemCode.setText("Item Code");
            headerItemCode.setTextSize(14);
            headerItemCode.setPadding(10, 0, 10, 0);

            TextView headerDescription = new TextView(FourthActivity.this);
            headerDescription.setText("Description");
            headerDescription.setTextSize(14);
            headerDescription.setPadding(10, 0, 10, 0);

            TextView headerQuantity = new TextView(FourthActivity.this);
            headerQuantity.setText("Quantity");
            headerQuantity.setTextSize(14);
            headerQuantity.setPadding(10, 0, 10, 0);

            TextView headerDiscount = new TextView(FourthActivity.this);
            headerDiscount.setText("Discount");
            headerDiscount.setTextSize(14);
            headerDiscount.setPadding(10, 0, 10, 0);

            TextView headerAmount = new TextView(FourthActivity.this);
            headerAmount.setText("Amount (RM)  ");
            headerAmount.setTextSize(14);
            headerAmount.setPadding(10, 0, 10, 0);

            // Add headers to the row
            headerRow.addView(headerItemCode);
            headerRow.addView(headerDescription);
            headerRow.addView(headerQuantity);
            headerRow.addView(headerDiscount);
            headerRow.addView(headerAmount);

            // Add the header row to the table
            tableLayout.addView(headerRow);

            // Data Rows
            for (ReceiptDetail detail : receiptDetails) {
                TableRow row = new TableRow(FourthActivity.this);
                row.setPadding(20, 10, 20, 10);

                TextView itemCodeView = new TextView(FourthActivity.this);
                itemCodeView.setText(detail.getItemCode());
                itemCodeView.setPadding(10, 0, 10, 0);

                TextView descriptionView = new TextView(FourthActivity.this);
                descriptionView.setText(detail.getDescription());
                descriptionView.setPadding(10, 0, 10, 0);

                TextView quantityView = new TextView(FourthActivity.this);
                quantityView.setText(String.valueOf(detail.getQuantity()));
                quantityView.setPadding(10, 0, 10, 0);

                TextView discountView = new TextView(FourthActivity.this);
                discountView.setText(String.valueOf(detail.getDiscount()));
                discountView.setPadding(10, 0, 10, 0);

                TextView amountView = new TextView(FourthActivity.this);
                amountView.setText(String.valueOf(detail.getAmount()));
                amountView.setPadding(10, 0, 10, 0);

                row.addView(itemCodeView);
                row.addView(descriptionView);
                row.addView(quantityView);
                row.addView(discountView);
                row.addView(amountView);

                tableLayout.addView(row);
            }

            receiptDetailLayout.addView(tableLayout);
        }
    }

    private class ReceiptDetail {
        private String itemCode, description;
        private int quantity;
        private double discount, amount;

        public ReceiptDetail(String itemCode, String description, int quantity, double discount, double amount) {
            this.itemCode = itemCode;
            this.description = description;
            this.quantity = quantity;
            this.discount = discount;
            this.amount = amount;
        }

        public String getItemCode() { return itemCode; }
        public String getDescription() { return description; }
        public int getQuantity() { return quantity; }
        public double getDiscount() { return discount; }
        public double getAmount() { return amount; }
    }
}
