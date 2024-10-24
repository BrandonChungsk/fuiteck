package com.example.javath;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseTask extends AsyncTask<Void, Void, String> {

    private String startDate;
    private String endDate;
    private DatabaseTaskListener listener;

    public interface DatabaseTaskListener {
        void onDataFetched(String result);
        void onError(String errorMessage);
    }

    public DatabaseTask(String startDate, String endDate, DatabaseTaskListener listener) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = "";
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
            StringBuilder sb = new StringBuilder();

            double totalSale = 0.0;

            while (resultSet.next()) {
                String outletCode = resultSet.getString("PO_LOC_CD");
                double sale = resultSet.getDouble("GrandTotal");
                totalSale += sale;

                sb.append(outletCode).append(" | ").append(sale).append("\n");
            }

            sb.append("\nTotal Sales: ").append(totalSale);

            result = sb.toString();

        } catch (SQLException | ClassNotFoundException e) {
            Log.e("DatabaseTask", "Error: " + e.getMessage());
            if (listener != null) {
                listener.onError(e.getMessage());
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    Log.e("DatabaseTask", "Error closing connection: " + e.getMessage());
                }
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (listener != null) {
            if (result.isEmpty()) {
                listener.onError("No data found for the selected date range.");
            } else {
                listener.onDataFetched(result);
            }
        }
    }
}



// Notes on various IPs, ports, and connection attempts:
// public ipv4: 183.171.158.9 - x
// 10.0.0.232:55097 works regardless of ports
// 210.187.179.69 - x
// 210.187.179.69:9090 -> 10.0.0.232:9090
// Currently port is 9090
// Default SQL port: 1433
// fuiteckmssql2019.c900wc6ac9gb.ap-southeast-2.rds.amazonaws.com:1433 - x
// misc: 208.67.222.222 - x
// 183.171.158.14 - myip.opendns - x
// 10.0.0.59  - x
// 10.0.0.248
// 8585
// "jdbc:jtds:sqlserver://10.0.0.59:1433/POSNEW;instance=SQLEXPRESS;user=sa;password=pdsmsde;trustServerCertificate=true;"
// "jdbc:jtds:sqlserver://10.0.0.59:9090/POSTEST;instance=SQLEXPRESS;user=sa;password=1234;trustServerCertificate=true;"
//
//

