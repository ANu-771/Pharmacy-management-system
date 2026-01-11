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

    public boolean delete(String userId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM user WHERE user_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, userId);

        return pstm.executeUpdate() > 0;
    }

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

    public String checkLogin(String username, String password) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT role FROM user WHERE username = ? AND password = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, username);
        pstm.setString(2, password);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("role");
        }
        return null;
    }

    public boolean isUsernameExists(String username) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT username FROM user WHERE username = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, username);
        return pstm.executeQuery().next();
    }

    public UserDTO getUserByEmail(String email) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM user WHERE email = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, email);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            return new UserDTO(
                    resultSet.getInt("user_id"),
                    resultSet.getString("username"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role")
            );
        }
        return null;
    }
}