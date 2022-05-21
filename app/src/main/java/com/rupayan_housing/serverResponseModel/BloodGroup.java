package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class BloodGroup {
    @SerializedName("blood_group_id")
    @Expose
    private String bloodGroupId;
    @SerializedName("gruop_name")
    @Expose
    private String gruopName;
}
