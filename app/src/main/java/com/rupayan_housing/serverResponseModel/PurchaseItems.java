package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class PurchaseItems {
    @SerializedName("productID")
    @Expose
    private String productID;
    @SerializedName("order_detailsID")
    @Expose
    private String order_detailsID;
    @SerializedName("item")
    @Expose
    private String item;
    @SerializedName("selling_price")
    @Expose
    private String sellingPrice;
    @SerializedName("buying_price")
    @Expose
    private String buyingPrice;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("unitID")
    @Expose
    private String unitID;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("total_amount")
    @Expose
    private Integer totalAmount;
    @SerializedName("sold_from")
    @Expose
    private String soldFrom;
}
