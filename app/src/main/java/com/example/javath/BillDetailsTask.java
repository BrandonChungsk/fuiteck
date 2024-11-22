package com.example.javath;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillDetailsTask extends AsyncTask<Void, Void, List<BillData>> {

    private String poLocCd;
    private BillDetailsListener listener;

    public interface BillDetailsListener {
        void onDetailsFetched(List<BillData> billDataList);
        void onError(String errorMessage);
    }

    public BillDetailsTask(String poLocCd, BillDetailsListener listener) {
        this.poLocCd = poLocCd;
        this.listener = listener;
    }

    @Override
    protected List<BillData> doInBackground(Void... voids) {
        List<BillData> billDataList = new ArrayList<>();
        Connection connection = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:jtds:sqlserver://10.0.0.59/POSNEW;user=sa;password=pdsmsde;trustServerCertificate=true;");

            String query = "SELECT ReceiptNo, ReceiptDate, Location, Discount, Amount FROM dbo.V_DailySalesBill WHERE Location = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, poLocCd);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String receiptNo = resultSet.getString("ReceiptNo");
                String receiptDate = resultSet.getString("ReceiptDate");
                String location = resultSet.getString("Location");
                double discount = resultSet.getDouble("Discount");
                double amount = resultSet.getDouble("Amount");

                BillData billData = new BillData(receiptNo, receiptDate, location, discount, amount);
                billDataList.add(billData);
            }

        } catch (SQLException | ClassNotFoundException e) {
            Log.e("BillDetailsTask", "Error: " + e.getMessage());
            if (listener != null) {
                listener.onError(e.getMessage());
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    Log.e("BillDetailsTask", "Error closing connection: " + e.getMessage());
                }
            }
        }
        return billDataList;
    }

    @Override
    protected void onPostExecute(List<BillData> billDataList) {
        if (listener != null) {
            if (billDataList.isEmpty()) {
                listener.onError("No data found.");
            } else {
                listener.onDetailsFetched(billDataList);
            }
        }
    }
}
