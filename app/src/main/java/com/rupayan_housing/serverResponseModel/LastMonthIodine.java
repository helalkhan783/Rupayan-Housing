package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class LastMonthIodine {
    @SerializedName("purchase")
    @Expose
    public String purchase;
    @SerializedName("sell")
    @Expose
    public String sell;
    @SerializedName("in_stock")
    @Expose
    public Double inStock;

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public Double getInStock() {
        return inStock;
    }

    public void setInStock(Double inStock) {
        this.inStock = inStock;
    }
}
