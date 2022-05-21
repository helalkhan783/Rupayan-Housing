package com.rupayan_housing.view.fragment.all_report.packaging_report.store;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class PackageReportStore {
    @SerializedName("storeID")
    @Expose
    private String storeID;
    @SerializedName("store_name")
    @Expose
    private String storeName;
}
