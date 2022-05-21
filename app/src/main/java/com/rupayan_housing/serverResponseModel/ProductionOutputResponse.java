package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class ProductionOutputResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("items")
    @Expose
    private List<ProductionOutputList> items = null;
}
