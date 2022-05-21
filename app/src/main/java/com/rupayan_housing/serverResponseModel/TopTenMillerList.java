package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopTenMillerList {
    @SerializedName("totalSold")
    @Expose
    private String totalSold;
    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("zoneName")
    @Expose
    private String zoneName;

    public String getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(String totalSold) {
        this.totalSold = totalSold;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
}
