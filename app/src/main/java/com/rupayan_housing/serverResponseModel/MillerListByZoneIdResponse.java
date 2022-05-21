package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MillerListByZoneIdResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("miller_list")
    @Expose
    private List<MillerByZone> millerList = null;

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

    public List<MillerByZone> getMillerList() {
        return millerList;
    }

    public void setMillerList(List<MillerByZone> millerList) {
        this.millerList = millerList;
    }
}
