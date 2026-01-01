package lk.ijse.pharmacy.model;

import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.tm.ReportTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportModel {

    //Get Total Orders Count
    public int getTotalOrders() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders";
        Connection connection = DBConnection.getInstance().getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    // Total Items Sold
    public int getItemsSold() throws SQLException {
        String sql = "SELECT SUM(qty) FROM order_medicine";
        Connection connection = DBConnection.getInstance().getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    //Get All Orders for the Table
    public List<ReportTM> getAllOrderDetails() throws SQLException {
        List<ReportTM> list = new ArrayList<>();
        String sql = "SELECT o.order_id, c.name, o.order_date, o.total " +
                "FROM orders o " +
                "JOIN customer c ON o.customer_id = c.customer_id " +
                "ORDER BY o.order_date DESC, o.order_id DESC";

        Connection connection = DBConnection.getInstance().getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(sql);

        while (resultSet.next()) {
            list.add(new ReportTM(
                    resultSet.getString("order_id"),
                    resultSet.getString("name"),
                    resultSet.getString("order_date"),
                    resultSet.getDouble("total")
            ));
        }
        return list;
    }

    //Get Paid Amount for a specific Order (for Bill Reprint)
    public double getPaidAmount(int orderId) throws SQLException {
        String sql = "SELECT amount FROM payment WHERE order_id = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, orderId);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return resultSet.getDouble("amount");
        }
        return 0.0;
    }
}