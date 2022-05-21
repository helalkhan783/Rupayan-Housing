package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ThanaList {
    @SerializedName("upazila_id")
    @Expose
    private String upazilaId;
    @SerializedName("name")
    @Expose
    private String name;
}
