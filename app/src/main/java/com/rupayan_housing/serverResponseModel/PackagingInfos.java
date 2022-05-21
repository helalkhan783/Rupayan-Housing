package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PackagingInfos {
    @SerializedName("packagingSLID")
    @Expose
    private String packagingSLID;
    @SerializedName("packagingID")
    @Expose
    private String packagingID;
    @SerializedName("packagingTypeID")
    @Expose
    private String packagingTypeID;
    @SerializedName("packagingRefNo")
    @Expose
    private Object packagingRefNo;
    @SerializedName("packagingAlterRef")
    @Expose
    private String packagingAlterRef;
    @SerializedName("refUserID")
    @Expose
    private String refUserID;
    @SerializedName("packagingDate")
    @Expose
    private String packagingDate;
    @SerializedName("entryUserID")
    @Expose
    private String entryUserID;
    @SerializedName("packagingEntryDateTime")
    @Expose
    private String packagingEntryDateTime;
    @SerializedName("modifiedUserID")
    @Expose
    private Object modifiedUserID;
    @SerializedName("modifiedDateTime")
    @Expose
    private Object modifiedDateTime;
    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("storeID")
    @Expose
    private String storeID;
    @SerializedName("packagingStatus")
    @Expose
    private String packagingStatus;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("customer_fname")
    @Expose
    private String customerFname;

    public String getPackagingSLID() {
        return packagingSLID;
    }

    public void setPackagingSLID(String packagingSLID) {
        this.packagingSLID = packagingSLID;
    }

    public String getPackagingID() {
        return packagingID;
    }

    public void setPackagingID(String packagingID) {
        this.packagingID = packagingID;
    }

    public String getPackagingTypeID() {
        return packagingTypeID;
    }

    public void setPackagingTypeID(String packagingTypeID) {
        this.packagingTypeID = packagingTypeID;
    }

    public Object getPackagingRefNo() {
        return packagingRefNo;
    }

    public void setPackagingRefNo(Object packagingRefNo) {
        this.packagingRefNo = packagingRefNo;
    }

    public String getPackagingAlterRef() {
        return packagingAlterRef;
    }

    public void setPackagingAlterRef(String packagingAlterRef) {
        this.packagingAlterRef = packagingAlterRef;
    }

    public String getRefUserID() {
        return refUserID;
    }

    public void setRefUserID(String refUserID) {
        this.refUserID = refUserID;
    }

    public String getPackagingDate() {
        return packagingDate;
    }

    public void setPackagingDate(String packagingDate) {
        this.packagingDate = packagingDate;
    }

    public String getEntryUserID() {
        return entryUserID;
    }

    public void setEntryUserID(String entryUserID) {
        this.entryUserID = entryUserID;
    }

    public String getPackagingEntryDateTime() {
        return packagingEntryDateTime;
    }

    public void setPackagingEntryDateTime(String packagingEntryDateTime) {
        this.packagingEntryDateTime = packagingEntryDateTime;
    }

    public Object getModifiedUserID() {
        return modifiedUserID;
    }

    public void setModifiedUserID(Object modifiedUserID) {
        this.modifiedUserID = modifiedUserID;
    }

    public Object getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(Object modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
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

    public String getPackagingStatus() {
        return packagingStatus;
    }

    public void setPackagingStatus(String packagingStatus) {
        this.packagingStatus = packagingStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCustomerFname() {
        return customerFname;
    }

    public void setCustomerFname(String customerFname) {
        this.customerFname = customerFname;
    }
}
