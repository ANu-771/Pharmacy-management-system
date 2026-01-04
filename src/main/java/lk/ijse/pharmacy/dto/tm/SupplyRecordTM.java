package lk.ijse.pharmacy.dto.tm;

public class SupplyRecordTM {
    private String date;
    private int supplierId;
    private String supplierName;
    private String medicineName;
    private int qty;
    private double unitCost;
    private double totalCost;


    public SupplyRecordTM() {
    }

    public SupplyRecordTM(String date, int supplierId, String supplierName, String medicineName, int qty, double unitCost, double totalCost) {
        this.date = date;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.medicineName = medicineName;
        this.qty = qty;
        this.unitCost = unitCost;
        this.totalCost = totalCost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "SupplyRecordTM{" +
                "date='" + date + '\'' +
                ", supplierId=" + supplierId +
                ", supplierName='" + supplierName + '\'' +
                ", medicineName='" + medicineName + '\'' +
                ", qty=" + qty +
                ", unitCost=" + unitCost +
                ", totalCost=" + totalCost +
                '}';
    }
}