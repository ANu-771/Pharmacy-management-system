package lk.ijse.pharmacy.dto;

import java.util.Date;

public class MedicineDTO {

    private int medicineId; // Changed to int for Auto-Increment
    private String medName;
    private String brand;
    private int qtyInStock;
    private double unitPrice;
    private Date expDate;

    public MedicineDTO() {
    }

    public MedicineDTO(String medName, String brand, int qtyInStock, double unitPrice, Date expDate) {
        this.medName = medName;
        this.brand = brand;
        this.qtyInStock = qtyInStock;
        this.unitPrice = unitPrice;
        this.expDate = expDate;
    }

    public MedicineDTO(int medicineId, String medName, String brand, int qtyInStock, double unitPrice, Date expDate) {
        this.medicineId = medicineId;
        this.medName = medName;
        this.brand = brand;
        this.qtyInStock = qtyInStock;
        this.unitPrice = unitPrice;
        this.expDate = expDate;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
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

    public int getQtyInStock() {
        return qtyInStock;
    }

    public void setQtyInStock(int qtyInStock) {
        this.qtyInStock = qtyInStock;
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

    @Override
    public String toString() {
        return "MedicineDTO{" +
                "medicineId=" + medicineId +
                ", medName='" + medName + '\'' +
                ", brand='" + brand + '\'' +
                ", qtyInStock=" + qtyInStock +
                ", unitPrice=" + unitPrice +
                ", expDate=" + expDate +
                '}';
    }
}