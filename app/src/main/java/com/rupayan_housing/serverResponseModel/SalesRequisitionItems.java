package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class SalesRequisitionItems {
    @SerializedName("productID")
    @Expose
    private String productID;
    @SerializedName("product_title")
    @Expose
    private String productTitle;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("unit_name")
    @Expose
    private String unit_name;
    @SerializedName("quantity")
    @Expose
    private String quantity = null;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
