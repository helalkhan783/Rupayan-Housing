package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class CustomerEditInfo {
    @SerializedName("customerID")
    @Expose
    private String customerID;
    @SerializedName("customer_fname")
    @Expose
    private String customerFname;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("customer_lname")
    @Expose
    private Object customerLname;
    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("typeID")
    @Expose
    private String typeID;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("alt_phone")
    @Expose
    private String altPhone;
    @SerializedName("age")
    @Expose
    private Object age;
    @SerializedName("entry_userID")
    @Expose
    private String entryUserID;
    @SerializedName("is_deleted")
    @Expose
    private String isDeleted;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("storeID")
    @Expose
    private String storeID;
    @SerializedName("thana")
    @Expose
    private String thana;
    @SerializedName("division")
    @Expose
    private String division;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("nid")
    @Expose
    private String nid;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("tin")
    @Expose
    private String tin;
    @SerializedName("bazar")
    @Expose
    private String bazar;
    @SerializedName("due_limit")
    @Expose
    private String dueLimit;
    @SerializedName("profile_id")
    @Expose
    private String profileId;
    @SerializedName("image")
    @Expose
    private Object image;
    @SerializedName("customer_note")
    @Expose
    private String customerNote;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Object getCustomerLname() {
        return customerLname;
    }

    public void setCustomerLname(Object customerLname) {
        this.customerLname = customerLname;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAltPhone() {
        return altPhone;
    }

    public void setAltPhone(String altPhone) {
        this.altPhone = altPhone;
    }

    public Object getAge() {
        return age;
    }

    public void setAge(Object age) {
        this.age = age;
    }

    public String getEntryUserID() {
        return entryUserID;
    }

    public void setEntryUserID(String entryUserID) {
        this.entryUserID = entryUserID;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getThana() {
        return thana;
    }

    public void setThana(String thana) {
        this.thana = thana;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getBazar() {
        return bazar;
    }

    public void setBazar(String bazar) {
        this.bazar = bazar;
    }

    public String getDueLimit() {
        return dueLimit;
    }

    public void setDueLimit(String dueLimit) {
        this.dueLimit = dueLimit;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public String getCustomerNote() {
        return customerNote;
    }

    public void setCustomerNote(String customerNote) {
        this.customerNote = customerNote;
    }
}
