package com.rupayan_housing.serverResponseModel.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MillStatus {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
