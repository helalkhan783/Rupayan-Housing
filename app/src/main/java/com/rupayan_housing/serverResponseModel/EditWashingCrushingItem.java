package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class EditWashingCrushingItem {
    @SerializedName("productID")
    @Expose
    private String productID;
    @SerializedName("item")
    @Expose
    private String item;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("unitID")
    @Expose
    private String unitID;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("sold_from")
    @Expose
    private String soldFrom;
}
