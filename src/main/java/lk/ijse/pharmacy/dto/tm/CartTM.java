package lk.ijse.pharmacy.dto.tm;

import javafx.scene.control.Button;

public class CartTM {
    private String medicineId;
    private String description;
    private int qty;
    private double unitPrice;
    private double total;
    private Button btnRemove;

    public CartTM() {
    }

    public CartTM(String medicineId, String description, int qty, double unitPrice, double total, Button btnRemove) {
        this.medicineId = medicineId;
        this.description = description;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.total = total;
        this.btnRemove = btnRemove;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Button getBtnRemove() {
        return btnRemove;
    }

    public void setBtnRemove(Button btnRemove) {
        this.btnRemove = btnRemove;
    }

    @Override
    public String toString() {
        return "CartTM{" +
                "medicineId='" + medicineId + '\'' +
                ", description='" + description + '\'' +
                ", qty=" + qty +
                ", unitPrice=" + unitPrice +
                ", total=" + total +
                ", btnRemove=" + btnRemove +
                '}';
    }
}