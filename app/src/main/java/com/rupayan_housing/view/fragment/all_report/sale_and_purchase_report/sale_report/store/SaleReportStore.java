package com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.sale_report.store;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class SaleReportStore {
    @SerializedName("storeID")
    @Expose
    private String storeID;
    @SerializedName("store_name")
    @Expose
    private String storeName;
}
