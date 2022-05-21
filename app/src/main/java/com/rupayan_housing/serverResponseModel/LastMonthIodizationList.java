package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastMonthIodizationList {
    @SerializedName("percentage")
    @Expose
    private String percentage;
    @SerializedName("zone_name")
    @Expose
    private Object zoneName;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("total_mill")
    @Expose
    private Integer totalMill;

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public Object getZoneName() {
        return zoneName;
    }

    public void setZoneName(Object zoneName) {
        this.zoneName = zoneName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Integer getTotalMill() {
        return totalMill;
    }

    public void setTotalMill(Integer totalMill) {
        this.totalMill = totalMill;
    }
}
