package lk.ijse.pharmacy.dto;

public class OrderMedicineDTO {

    private String orderId;
    private String medicineId;
    private int qty;
    private double unitPrice;
    private double lineTotal;

    // Constructors
    public OrderMedicineDTO() {
    }

    public OrderMedicineDTO(String orderId, String medicineId, int qty, double unitPrice, double lineTotal) {
        this.orderId = orderId;
        this.medicineId = medicineId;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }

    @Override
    public String toString() {
        return "OrderMedicineDTO{" + "orderId=" + orderId + ", medicineId=" + medicineId + ", qty=" + qty + ", unitPrice=" + unitPrice + ", lineTotal=" + lineTotal + '}';
    }

}
