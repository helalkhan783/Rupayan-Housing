package com.rupayan_housing.view.fragment.all_report.reconcilation_report.page_data_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ReconciliationReportItemList {
    @SerializedName("product_title")
    @Expose
    private String productTitle;
    @SerializedName("productID")
    @Expose
    private String productID;

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
