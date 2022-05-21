package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class NextPackagingId {
    @SerializedName("packagingID")
    @Expose
    private Integer packagingID;
}
