package lk.ijse.pharmacy.model;

import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.OrderDTO;
import lk.ijse.pharmacy.dto.tm.CartTM;

import java.sql.*;
import java.util.List;

public class OrderModel {


    public String getNextOrderId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1";
        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery(sql);

        if (resultSet.next()) {
            return splitOrderId(resultSet.getString(1));
        }
        return "1";
    }

    private String splitOrderId(String currentId) {
        if(currentId != null) {
            return String.valueOf(Integer.parseInt(currentId) + 1);
        }
        return "1";
    }

    //  THE TRANSACTION METHOD
    public boolean placeOrder(OrderDTO order, List<CartTM> cartList) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();

            connection.setAutoCommit(false);


            String sqlOrder = "INSERT INTO orders (customer_id, user_id, total, order_date) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmOrder = connection.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            pstmOrder.setInt(1, Integer.parseInt(String.valueOf(order.getCustomerId())));
            pstmOrder.setInt(2, 1);
            pstmOrder.setDouble(3, order.getTotal());
            pstmOrder.setDate(4, new java.sql.Date(order.getOrderDate().getTime()));

            boolean isOrderSaved = pstmOrder.executeUpdate() > 0;

            if (isOrderSaved) {

                ResultSet generatedKeys = pstmOrder.getGeneratedKeys();
                int orderId = 0;
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1);
                }


                boolean isDetailsSaved = true;
                boolean isStockUpdated = true;
                boolean isItemsUpdated = true;

                for (CartTM cartItem : cartList) {
                    int medicineId = Integer.parseInt(cartItem.getMedicineId());
                    int qty = cartItem.getQty();


                    // SAVE ORDER DETAILS
                    String sqlDetail = "INSERT INTO order_medicine (order_id, medicine_id, qty, unit_price, line_total) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement pstmDetail = connection.prepareStatement(sqlDetail);
                    pstmDetail.setInt(1, orderId);
                    pstmDetail.setInt(2, medicineId);
                    pstmDetail.setInt(3, qty);
                    pstmDetail.setDouble(4, cartItem.getUnitPrice());
                    pstmDetail.setDouble(5, cartItem.getTotal());

                    if (pstmDetail.executeUpdate() <= 0) {
                        isDetailsSaved = false;
                        break;
                    }


                    // UPDATE MEDICINE STOCK (Decrease Qty)
                    String sqlStock = "UPDATE medicine SET qty_in_stock = qty_in_stock - ? WHERE medicine_id = ?";
                    PreparedStatement pstmStock = connection.prepareStatement(sqlStock);
                    pstmStock.setInt(1, qty);
                    pstmStock.setInt(2, medicineId);

                    if (pstmStock.executeUpdate() <= 0) {
                        isStockUpdated = false;
                        break;
                    }


                    // update MEDICINE_ITEMS
                    String sqlItems = "UPDATE medicine_items SET sold_date = ? WHERE medicine_id = ? AND sold_date IS NULL LIMIT ?";
                    PreparedStatement pstmItems = connection.prepareStatement(sqlItems);
                    pstmItems.setDate(1, new java.sql.Date(System.currentTimeMillis()));
                    pstmItems.setInt(2, medicineId);
                    pstmItems.setInt(3, qty);

                    int updatedRows = pstmItems.executeUpdate();

//                    if (pstmItems.executeUpdate() <= 0) {
//                        isItemsUpdated = false;
//                        break;
//                    }
//                }


                    if (updatedRows < qty) {
                        System.out.println("Warning: Not enough items in medicine_items table for ID " + medicineId);
                    }
                }
                if (isDetailsSaved && isStockUpdated && isItemsUpdated) {
                    // Commit Transaction
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            if (connection != null) connection.rollback();
            return false;
        } finally {
            if (connection != null) connection.setAutoCommit(true);
        }
    }
}

