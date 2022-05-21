package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AgencyMonirtoringList {
    @SerializedName("total_monitor")
    @Expose
    private String totalMonitor;
    @SerializedName("entryuserID")
    @Expose
    private String entryuserID;
    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("store_name")
    @Expose
    private String storeName;

    public String getTotalMonitor() {
        return totalMonitor;
    }

    public void setTotalMonitor(String totalMonitor) {
        this.totalMonitor = totalMonitor;
    }

    public String getEntryuserID() {
        return entryuserID;
    }

    public void setEntryuserID(String entryuserID) {
        this.entryuserID = entryuserID;
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
}
