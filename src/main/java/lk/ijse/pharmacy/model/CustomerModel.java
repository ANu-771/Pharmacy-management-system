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

        String sql = "INSERT INTO customer (customer_id, name, contact, address) VALUES (?,?,?,?)";

        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setString(1, customer.getCustomerId());
        pstm.setString(2, customer.getName());
        pstm.setString(3, customer.getContact());
        pstm.setString(4, customer.getAddress());

       // return pstm.executeUpdate() > 0;
        int results = pstm.executeUpdate();

        return (results > 0);
    }

    // UPDATE Customer
    public static boolean update(CustomerDTO customer) throws SQLException, ClassNotFoundException {
        Connection conn = DBConnection.getInstance().getConnection();

        String sql = "UPDATE customer SET name = ?, contact = ?, address = ? WHERE customer_id = ?";

        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setString(1, customer.getName());
        pstm.setString(2, customer.getContact());
        pstm.setString(3, customer.getAddress());
        pstm.setString(4, customer.getCustomerId());

        int results = pstm.executeUpdate();

        return results>0;
    }

    // DELETE Customer
    public static boolean delete(String id) throws SQLException, ClassNotFoundException {
        Connection conn = DBConnection.getInstance().getConnection();

        String sql = "DELETE FROM customer WHERE customer_id = ?";

        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setString(1, id);

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
                    resultSet.getString("customer_id"),
                    resultSet.getString("name"),
                    resultSet.getString("contact"),
                    resultSet.getString("address")
            ));
        }
        return list;
    }

    // SEARCH Customer
    public static CustomerDTO search(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM customer WHERE customer_id = ?";

        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return new CustomerDTO(
                    resultSet.getString("customer_id"),
                    resultSet.getString("name"),
                    resultSet.getString("contact"),
                    resultSet.getString("address")
            );
        }
        return null;
    }
}