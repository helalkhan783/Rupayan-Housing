package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerEditHistoryList {
    @SerializedName("customerID")
    @Expose
    private String customerID;
    @SerializedName("customer_fname")
    @Expose
    private String customerFname;
    @SerializedName("customer_lname")
    @Expose
    private Object customerLname;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("typeID")
    @Expose
    private String typeID;
    @SerializedName("bazar")
    @Expose
    private String bazar;
    @SerializedName("is_deleted")
    @Expose
    private String isDeleted;
    @SerializedName("due_limit")
    @Expose
    private String dueLimit;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("edit_attempt_time")
    @Expose
    private String editAttemptTime;
    @SerializedName("edit_attempt_by")
    @Expose
    private String editAttemptBy;
    @SerializedName("division")
    @Expose
    private String division;
    @SerializedName("thana")
    @Expose
    private String thana;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("edit_attempt_by_name")
    @Expose
    private String editAttemptByName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEditAttemptByName() {
        return editAttemptByName;
    }

    public void setEditAttemptByName(String editAttemptByName) {
        this.editAttemptByName = editAttemptByName;
    }

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getBazar() {
        return bazar;
    }

    public void setBazar(String bazar) {
        this.bazar = bazar;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getDueLimit() {
        return dueLimit;
    }

    public void setDueLimit(String dueLimit) {
        this.dueLimit = dueLimit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEditAttemptTime() {
        return editAttemptTime;
    }

    public void setEditAttemptTime(String editAttemptTime) {
        this.editAttemptTime = editAttemptTime;
    }

    public String getEditAttemptBy() {
        return editAttemptBy;
    }

    public void setEditAttemptBy(String editAttemptBy) {
        this.editAttemptBy = editAttemptBy;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getThana() {
        return thana;
    }

    public void setThana(String thana) {
        this.thana = thana;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
