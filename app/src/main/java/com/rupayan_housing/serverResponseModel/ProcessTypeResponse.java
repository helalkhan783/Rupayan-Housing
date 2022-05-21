package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ProcessTypeResponse {
    @SerializedName("processTypeID")
    @Expose
    private String processTypeID;
    @SerializedName("processTypeName")
    @Expose
    private String processTypeName;
}
