package lk.ijse.pharmacy.dto.tm;

public class ReportTM {
    private String orderId;
    private String customerName;
    private String date;
    private Double total;

    public ReportTM() {
    }

    public ReportTM(String orderId, String customerName, String date, Double total) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.date = date;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ReportTM{" +
                "orderId='" + orderId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", date='" + date + '\'' +
                ", total=" + total +
                '}';
    }
}
