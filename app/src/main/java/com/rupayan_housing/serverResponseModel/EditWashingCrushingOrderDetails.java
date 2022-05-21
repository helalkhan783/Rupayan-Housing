package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class EditWashingCrushingOrderDetails {
    @SerializedName("orderID")
    @Expose
    private String orderID;
    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("items")
    @Expose
    private List<EditWashingCrushingItem> items = null;
    @SerializedName("customer")
    @Expose
    private Customer customer;
    @SerializedName("output_item")
    @Expose
    private String outputItem;
}
