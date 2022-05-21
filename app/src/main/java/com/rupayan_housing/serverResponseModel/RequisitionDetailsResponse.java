package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class RequisitionDetailsResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("details")
    @Expose
    private SingleRequisitionDetailsResponse details;
}
