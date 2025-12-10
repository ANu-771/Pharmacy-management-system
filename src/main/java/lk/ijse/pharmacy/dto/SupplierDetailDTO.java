package lk.ijse.pharmacy.dto;

import java.util.Date;

public class SupplierDetailDTO {

    private String supplierId;
    private String medicineId;
    private Date supplyDate;
    private int quantitySupplied;

    // Constructors
    public SupplierDetailDTO() {
    }

    public SupplierDetailDTO(String supplierId, String medicineId, Date supplyDate, int quantitySupplied) {
        this.supplierId = supplierId;
        this.medicineId = medicineId;
        this.supplyDate = supplyDate;
        this.quantitySupplied = quantitySupplied;
    }

    // Getters and Setters
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public Date getSupplyDate() {
        return supplyDate;
    }

    public void setSupplyDate(Date supplyDate) {
        this.supplyDate = supplyDate;
    }

    public int getQuantitySupplied() {
        return quantitySupplied;
    }

    public void setQuantitySupplied(int quantitySupplied) {
        this.quantitySupplied = quantitySupplied;
    }

    @Override
    public String toString() {
        return "SupplierDetailDTO{" + "supplierId=" + supplierId + ", medicineId=" + medicineId + ", supplyDate=" + supplyDate + ", quantitySupplied=" + quantitySupplied + '}';
    }

}
