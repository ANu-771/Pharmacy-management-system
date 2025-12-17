package lk.ijse.pharmacy.model;

import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.SupplierDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierModel {


    public boolean save(SupplierDTO supplier) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO supplier (supplier_name, email, contact_num) VALUES (?,?,?)";

        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getSupplierName());
            pstmt.setString(2, supplier.getEmail());
            pstmt.setString(3, supplier.getContactNum());

            return pstmt.executeUpdate() > 0;
        }
    }


    public boolean update(SupplierDTO supplier) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE supplier SET supplier_name = ?, email = ?, contact_num = ? WHERE supplier_id = ?";

        Connection conn = DBConnection.getInstance().getConnection();

        try(PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, supplier.getSupplierName());
            pstm.setString(2, supplier.getEmail());
            pstm.setString(3, supplier.getContactNum());
            pstm.setInt(4, supplier.getSupplierId());

            return pstm.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM supplier WHERE supplier_id = ?";

        Connection conn = DBConnection.getInstance().getConnection();

        try(PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);
            return pstm.executeUpdate() > 0;
        }
    }

    public List<SupplierDTO> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM supplier";

        Connection conn = DBConnection.getInstance().getConnection();
        List<SupplierDTO> list = new ArrayList<>();

        try(ResultSet resultSet = conn.createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                list.add(new SupplierDTO(
                        resultSet.getInt("supplier_id"),
                        resultSet.getString("supplier_name"),
                        resultSet.getString("email"),
                        resultSet.getString("contact_num")
                ));
            }
        }
        return list;
    }

    public SupplierDTO search(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM supplier WHERE supplier_id = ?";

        Connection conn = DBConnection.getInstance().getConnection();
        try(PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.setInt(1, id);

            ResultSet resultSet = pstm.executeQuery();
            if (resultSet.next()) {
                return new SupplierDTO(
                        resultSet.getInt("supplier_id"),
                        resultSet.getString("supplier_name"),
                        resultSet.getString("email"),
                        resultSet.getString("contact_num")
                );
            }
        }
        return null;
    }
}