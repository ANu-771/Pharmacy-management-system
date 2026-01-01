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
        if (currentId != null) {
            return String.valueOf(Integer.parseInt(currentId) + 1);
        }
        return "1";
    }

    //  THE TRANSACTION METHOD
    public String placeOrder(OrderDTO order, List<CartTM> cartList, String paymentMethod, double cashAmount) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);


            String sqlOrder = "INSERT INTO orders (customer_id, user_id, total, order_date, order_time) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmOrder = connection.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            pstmOrder.setInt(1, Integer.parseInt(String.valueOf(order.getCustomerId())));
            pstmOrder.setInt(2, 1);
            pstmOrder.setDouble(3, order.getTotal());
            pstmOrder.setDate(4, new java.sql.Date(order.getOrderDate().getTime()));

            // SET TIME set the time from Java
            pstmOrder.setTime(5, new java.sql.Time(order.getOrderDate().getTime()));

            boolean isOrderSaved = pstmOrder.executeUpdate() > 0;
            String generatedOrderId = null;

            if (isOrderSaved) {

                ResultSet generatedKeys = pstmOrder.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Assign the ID to the String variable used later
                    generatedOrderId = String.valueOf(generatedKeys.getInt(1));
                }


                boolean isDetailsSaved = true;
                boolean isStockUpdated = true;

                for (CartTM cartItem : cartList) {
                    int medicineId = Integer.parseInt(cartItem.getMedicineId());
                    int qty = cartItem.getQty();


                    // insert ORDER DETAILS
                    String sqlDetail = "INSERT INTO order_medicine (order_id, medicine_id, qty, unit_price, line_total) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement pstmDetail = connection.prepareStatement(sqlDetail);

                    pstmDetail.setInt(1, Integer.parseInt(generatedOrderId));
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
                }

                //: SAVE PAYMENT
                boolean isPaymentSaved = true;
                if (isDetailsSaved && isStockUpdated) {

                    String sqlPayment = "INSERT INTO payment (order_id, amount, payment_method, payment_date) VALUES (?, ?, ?, ?)";
                    PreparedStatement pstmPayment = connection.prepareStatement(sqlPayment);

                    pstmPayment.setInt(1, Integer.parseInt(generatedOrderId));
                    pstmPayment.setDouble(2, cashAmount);
                    pstmPayment.setString(3, paymentMethod);
                    pstmPayment.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));

                    if (pstmPayment.executeUpdate() <= 0) {
                        isPaymentSaved = false;

                    }
                }

                //COMMIT IF ALL SUCCESS
                if (isDetailsSaved && isStockUpdated && isPaymentSaved) {
                    // Commit Transaction
                    connection.commit();
                    return generatedOrderId;
                }
            }

            // Rollback if any step failed
            connection.rollback();
            return null; // FAIL: Return null

        } catch (Exception e) {
            e.printStackTrace();
            if (connection != null) connection.rollback();
            return null;
        } finally {
            if (connection != null) connection.setAutoCommit(true);
        }
    }
}

