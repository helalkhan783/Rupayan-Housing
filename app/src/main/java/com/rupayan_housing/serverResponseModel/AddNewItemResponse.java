package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class AddNewItemResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("category")
    @Expose
    private List<AddNewItemCategory> category = null;
    @SerializedName("brand")
    @Expose
    private List<AddNewItemBrand> brand = null;
    @SerializedName("unit")
    @Expose
    private List<AddNewItemUnit> unit = null;
    @SerializedName("store")
    @Expose
    private List<AddNewItemStore> store = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AddNewItemCategory> getCategory() {
        return category;
    }

    public void setCategory(List<AddNewItemCategory> category) {
        this.category = category;
    }

    public List<AddNewItemBrand> getBrand() {
        return brand;
    }

    public void setBrand(List<AddNewItemBrand> brand) {
        this.brand = brand;
    }

    public List<AddNewItemUnit> getUnit() {
        return unit;
    }

    public void setUnit(List<AddNewItemUnit> unit) {
        this.unit = unit;
    }

    public List<AddNewItemStore> getStore() {
        return store;
    }

    public void setStore(List<AddNewItemStore> store) {
        this.store = store;
    }
}
