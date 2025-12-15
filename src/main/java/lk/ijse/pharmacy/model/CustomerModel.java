package lk.ijse.pharmacy.model;

import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.CustomerDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {

    // SAVE Customer
    public static boolean save(CustomerDTO customer) throws SQLException, ClassNotFoundException {
        Connection conn = DBConnection.getInstance().getConnection();

        String sql = "INSERT INTO customer (name, contact, address) VALUES (?,?,?)";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, customer.getName());
        pstmt.setString(2, customer.getContact());
        pstmt.setString(3, customer.getAddress());
        int result = pstmt.executeUpdate();
        return (result > 0);
    }

    }

    // UPDATE Customer
    public static boolean update(CustomerDTO customer) throws SQLException, ClassNotFoundException {
        Connection conn = DBConnection.getInstance().getConnection();

        String sql = "UPDATE customer SET name = ?, contact = ?, address = ? WHERE customer_id = ?";

        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setString(1, customer.getName());
        pstm.setString(2, customer.getContact());
        pstm.setString(3, customer.getAddress());
        pstm.setInt(4, customer.getCustomerId());

        int results = pstm.executeUpdate();

        return results>0;
    }

    // DELETE Customer
    public static boolean delete(int id) throws SQLException, ClassNotFoundException {
        Connection conn = DBConnection.getInstance().getConnection();

        String sql = "DELETE FROM customer WHERE customer_id = ?";

        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setInt(1, id);

        int results = pstm.executeUpdate();

        return results>0;
    }

    // GET ALL Customers TableView
    public static List<CustomerDTO> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM customer";

        Connection conn = DBConnection.getInstance().getConnection();
        ResultSet resultSet = conn.createStatement().executeQuery(sql);

        List<CustomerDTO> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new CustomerDTO(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("name"),
                    resultSet.getString("contact"),
                    resultSet.getString("address")
            ));
        }
        return list;
    }

    // SEARCH Customer
    public static CustomerDTO search(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM customer WHERE customer_id = ?";

        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setInt(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return new CustomerDTO(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("name"),
                    resultSet.getString("contact"),
                    resultSet.getString("address")
            );
        }
        return null;
    }
}