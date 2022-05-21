package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class PendingWashingCrushingItems {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("items")
    @Expose
    private List<PendingWashingCrushingItem> items = null;
    @SerializedName("customer")
    @Expose
    private Customer customer;
    @SerializedName("output_item")
    @Expose
    private String outputItem;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<PendingWashingCrushingItem> getItems() {
        return items;
    }

    public void setItems(List<PendingWashingCrushingItem> items) {
        this.items = items;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getOutputItem() {
        return outputItem;
    }

    public void setOutputItem(String outputItem) {
        this.outputItem = outputItem;
    }
}
