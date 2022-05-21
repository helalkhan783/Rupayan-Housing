package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RefUserInfos {
    @SerializedName("customerID")
    @Expose
    private String customerID;
    @SerializedName("customer_fname")
    @Expose
    private String customerFname;
    @SerializedName("customer_lname")
    @Expose
    private Object customerLname;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("present_address")
    @Expose
    private Object presentAddress;
    @SerializedName("company_name")
    @Expose
    private String companyName;

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerFname() {
        return customerFname;
    }

    public void setCustomerFname(String customerFname) {
        this.customerFname = customerFname;
    }

    public Object getCustomerLname() {
        return customerLname;
    }

    public void setCustomerLname(Object customerLname) {
        this.customerLname = customerLname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(Object presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
