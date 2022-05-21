package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ReportPurchaseCategoryList {

    @SerializedName("categoryID")
    @Expose
    private String categoryID;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("category_code")
    @Expose
    private String categoryCode;
}
