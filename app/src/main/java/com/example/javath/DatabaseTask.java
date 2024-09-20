package com.example.javath;


import android.os.AsyncTask;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseTask extends AsyncTask<Void, Void, ResultSet> {

    private String startDate;
    private String endDate;

    // Constructor to receive the start and end dates
    public DatabaseTask(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    protected ResultSet doInBackground(Void... voids) {
        ResultSet resultSet = null;
        Connection connection = null;
        Statement statement = null;

        try {
            DatabaseHelper dbHelper = new DatabaseHelper();
            connection = dbHelper.connect();

            // SQL query with the selected dates
            String query = "SELECT * FROM dbo.POS_ORDERS WHERE PO_DT BETWEEN '" + startDate + "' AND '" + endDate + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultSet;
    }

    @Override
    protected void onPostExecute(ResultSet resultSet) {
        super.onPostExecute(resultSet);
        // Handle the result (e.g., update the UI)
    }
}

