package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class GetEditedStock {
    @SerializedName("productID")
    @Expose
    private String productID;
    @SerializedName("storeID")
    @Expose
    private java.util.List<String> storeID = null;
    @SerializedName("stock_qty")
    @Expose
    private String stockQty;
    @SerializedName("unit_name")
    @Expose
    private String unitName;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public List<String> getStoreID() {
        return storeID;
    }

    public void setStoreID(List<String> storeID) {
        this.storeID = storeID;
    }

    public String getStockQty() {
        return stockQty;
    }

    public void setStockQty(String stockQty) {
        this.stockQty = stockQty;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
