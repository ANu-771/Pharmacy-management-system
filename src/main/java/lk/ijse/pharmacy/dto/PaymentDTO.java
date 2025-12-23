package lk.ijse.pharmacy.dto;

import java.util.Date;

public class PaymentDTO {

    private int paymentId;
    private int orderId;
    private double amount;
    private Date paymentDate;
    private String paymentMethod;

    // Constructors
    public PaymentDTO() {
    }

    public PaymentDTO(int paymentId, int orderId, double amount, Date paymentDate, String paymentMethod) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "PaymentDTO{" + "paymentId=" + paymentId + ", orderId=" + orderId + ", amount=" + amount + ", paymentDate=" + paymentDate + ", paymentMethod=" + paymentMethod + '}';
    }

    
}
