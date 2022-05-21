package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class BrandResponseForDetectUser {
    @SerializedName("brandID")
    @Expose
    private String brandID;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("is_deleted")
    @Expose
    private String isDeleted;
}
