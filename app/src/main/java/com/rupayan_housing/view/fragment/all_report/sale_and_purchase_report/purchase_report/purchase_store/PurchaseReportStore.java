package com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.purchase_report.purchase_store;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class PurchaseReportStore {
    @SerializedName("storeID")
    @Expose
    private String storeID;
    @SerializedName("store_name")
    @Expose
    private String storeName;

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
