package lk.ijse.pharmacy.model;

import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserModel {

    // 1. Get All Users for Table
    public List<UserDTO> getAll() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM user";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<UserDTO> userList = new ArrayList<>();
        while (resultSet.next()) {
            userList.add(new UserDTO(
                    resultSet.getInt("user_id"),
                    resultSet.getString("username"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role")
            ));
        }
        return userList;
    }

    // 2. Save New User
    public boolean save(UserDTO user, String role) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO user (username, email, password, role) VALUES (?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, user.getUsername());
        pstm.setString(2, user.getEmail());
        pstm.setString(3, user.getPassword());
        pstm.setString(4, role);

        return pstm.executeUpdate() > 0;
    }

    // 3. Update User
    public boolean update(UserDTO user, String role) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "UPDATE user SET username = ?, email = ?, password = ?, role = ? WHERE user_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, user.getUsername());
        pstm.setString(2, user.getEmail());
        pstm.setString(3, user.getPassword());
        pstm.setString(4, role);
        pstm.setInt(5, user.getUserId());

        return pstm.executeUpdate() > 0;
    }

    // 4. Delete User
    public boolean delete(String userId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM user WHERE user_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, userId);

        return pstm.executeUpdate() > 0;
    }

    // 5. Search User
    public UserDTO search(String id) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM user WHERE user_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            UserDTO user = new UserDTO(
                    resultSet.getInt("user_id"),
                    resultSet.getString("username"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role")
            );
            return user;
        }
        return null;
    }

    // 6. Get Role for a specific User (Helper for Search)
    public String getRole(String id) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT role FROM user WHERE user_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);
        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) return resultSet.getString("role");
        return null;
    }

    // --- LOGIN LOGIC ---
    // Returns the User's Role if login success, or null if failed
    public String checkLogin(String username, String password) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT role FROM user WHERE username = ? AND password = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, username);
        pstm.setString(2, password);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("role"); // Return "admin" or "employee"
        }
        return null; // Login Failed
    }

    // Add this method inside your UserModel class
    public boolean isUsernameExists(String username) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT username FROM user WHERE username = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, username);
        return pstm.executeQuery().next(); // Returns true if username is found
    }
}