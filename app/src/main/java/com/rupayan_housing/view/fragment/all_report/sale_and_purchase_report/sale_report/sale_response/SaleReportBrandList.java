package com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.sale_report.sale_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class SaleReportBrandList {
    @SerializedName("brandID")
    @Expose
    private String brandID;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("distributor")
    @Expose
    private String distributor;
    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("storeID")
    @Expose
    private String storeID;
    @SerializedName("isdeleted")
    @Expose
    private String isdeleted;
}
