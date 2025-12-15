package lk.ijse.pharmacy.dto;

public class SupplierDTO {

    private int supplierId;
    private String supplierName;
    private String email;
    private String contactNum;

    // Constructors
    public SupplierDTO() {
    }

    public SupplierDTO(int supplierId, String supplierName, String email, String contactNum) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.email = email;
        this.contactNum = contactNum;
    }

    // Getters and Setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    @Override
    public String toString() {
        return "SupplierDTO{" + "supplierId=" + supplierId + ", supplierName=" + supplierName + ", email=" + email + ", contactNum=" + contactNum + '}';
    }

}
