package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastIodineStockList {
    @SerializedName("zone_name")
    @Expose
    private String zoneName;
    @SerializedName("total_mill")
    @Expose
    private Integer totalMill;
    @SerializedName("quantity")
    @Expose
    private Object quantity;

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Integer getTotalMill() {
        return totalMill;
    }

    public void setTotalMill(Integer totalMill) {
        this.totalMill = totalMill;
    }

    public Object getQuantity() {
        return quantity;
    }

    public void setQuantity(Object quantity) {
        this.quantity = quantity;
    }
}
