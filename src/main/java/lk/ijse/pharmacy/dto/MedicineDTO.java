package lk.ijse.pharmacy.dto;

import java.util.Date;

public class MedicineDTO {

    private String medicineId;
    private String medName;
    private String brand;
    private double unitPrice;
    private Date expDate;
    private int qtyInStock;

    // Constructors
    public MedicineDTO() {
    }

    public MedicineDTO(String medicineId, String medName, String brand, double unitPrice, Date expDate, int qtyInStock) {
        this.medicineId = medicineId;
        this.medName = medName;
        this.brand = brand;
        this.unitPrice = unitPrice;
        this.expDate = expDate;
        this.qtyInStock = qtyInStock;
    }

    // Getters and Setters
    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public int getQtyInStock() {
        return qtyInStock;
    }

    public void setQtyInStock(int qtyInStock) {
        this.qtyInStock = qtyInStock;
    }

    @Override
    public String toString() {
        return "MedicineDTO{" + "medicineId=" + medicineId + ", medName=" + medName + ", brand=" + brand + ", unitPrice=" + unitPrice + ", expDate=" + expDate + ", qtyInStock=" + qtyInStock + '}';
    }

}
