package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class AddNewItemUnit {
    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("name")
    @Expose
    private String name;

}
