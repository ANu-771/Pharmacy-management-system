package lk.ijse.pharmacy.dto;

public class CustomerDTO {

    private int customerId;
    private String name;
    private String contact;
    private String address;

    // Constructors
    public CustomerDTO() {
    }

    public CustomerDTO(int customerId, String name, String contact, String address) {
        this.customerId = customerId;
        this.name = name;
        this.contact = contact;
        this.address = address;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "CustomerDTO{" + "customerId=" + customerId + ", name=" + name + ", contact=" + contact + ", address=" + address + '}';
    }

}
