// File: DatabaseHelper.java

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {

    private static final String URL = "jdbc:jtds:sqlserver://10.0.0.23:55097/POSTEST;";
    private static final String USER = "sa";
    private static final String PASSWORD = "1234";

    public Connection connect() throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error connecting to database: " + e.getMessage());
        }
        return conn;
    }
}
