package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class PendingRequisitionResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("lists")
    @Expose
    private List<PendingRequisitionListResponse> lists = null;
}
