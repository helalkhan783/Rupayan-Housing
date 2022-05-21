package com.rupayan_housing.serverResponseModel;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rupayan_housing.utils.CustomViewModel;

import java.util.List;

import lombok.Data;

public class PackagingDetailsResponse   {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("details")
    @Expose
    private AllDetails details;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public AllDetails getDetails() {
        return details;
    }

    public void setDetails(AllDetails details) {
        this.details = details;
    }
}
