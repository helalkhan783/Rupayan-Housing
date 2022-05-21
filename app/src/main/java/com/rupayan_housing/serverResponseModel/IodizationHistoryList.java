package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class IodizationHistoryList {
    @SerializedName("orderID")
    @Expose
    private String orderID;
    @SerializedName("serialID")
    @Expose
    private String serialID;
    @SerializedName("order_vendorID")
    @Expose
    private String order_vendorID;
    @SerializedName("product_title")
    @Expose
    private String productTitle;
    @SerializedName("productID")
    @Expose
    private String productID;
    @SerializedName("customer_fname")
    @Expose
    private String customerFname;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("entry_date")
    @Expose
    private String entryDate;
    @SerializedName("buying_price")
    @Expose
    private String buyingPrice;
    @SerializedName("selling_price")
    @Expose
    private Object sellingPrice;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("enterprise_name")
    @Expose
    private String enterpriseName;
    @SerializedName("name")
    @Expose
    private String name;

}
