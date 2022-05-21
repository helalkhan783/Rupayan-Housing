package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ProductionOutputList {
    @SerializedName("productID")
    @Expose
    private String productID;
    @SerializedName("product_title")
    @Expose
    private String productTitle;
}
