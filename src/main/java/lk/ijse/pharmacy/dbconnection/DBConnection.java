package lk.ijse.pharmacy.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final String DB_URL = "jdbc:mysql://localhost:3306/pharmacy";
    private final String DB_USERNAME = "root";
    private final String DB_PASSWORD = "mysql";

    private Connection conn;
    private static DBConnection dbc;

    private DBConnection() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    public static DBConnection getInstance() throws SQLException {

        if(dbc==null) {
            dbc = new DBConnection();
        }
        return dbc;
    }

    public Connection getConnection() {
        return conn;
    }
}
