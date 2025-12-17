package lk.ijse.pharmacy.dto;

import java.util.Date;

public class OrderDTO {

    private int orderId;
    private int customerId;
    private int userId;
    private double total;
    private Date orderDate;

    public OrderDTO() {
    }

    public OrderDTO(Integer orderId, String customerId, String userId, double total, Date orderDate) {
        this.orderId = (orderId == null) ? 0 : orderId;
        this.customerId = Integer.parseInt(customerId);
        this.userId = Integer.parseInt(userId);
        this.total = total;
        this.orderDate = orderDate;
    }
    public OrderDTO(int orderId, int customerId, int userId, double total, Date orderDate) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.userId = userId;
        this.total = total;
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", userId=" + userId +
                ", total=" + total +
                ", orderDate=" + orderDate +
                '}';
    }
}
