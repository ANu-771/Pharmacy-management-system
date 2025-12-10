package lk.ijse.pharmacy.dto;

import java.util.Date;

public class OrderDTO {

    private String orderId;
    private String customerId;
    private String userId;
    private double total;
    private Date orderDate;

    // Constructors
    public OrderDTO() {
    }

    public OrderDTO(String orderId, String customerId, String userId, double total, Date orderDate) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.userId = userId;
        this.total = total;
        this.orderDate = orderDate;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "OrderDTO{" + "orderId=" + orderId + ", customerId=" + customerId + ", userId=" + userId + ", total=" + total + ", orderDate=" + orderDate + '}';
    }

}
