package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ZoneList {
    @SerializedName("zoneID")
    @Expose
    private String zoneID;
    @SerializedName("zoneName")
    @Expose
    private String zoneName;
    @SerializedName("remarks")
    @Expose
    private String remarks;
}
