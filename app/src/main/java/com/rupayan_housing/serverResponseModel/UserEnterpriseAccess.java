package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class UserEnterpriseAccess {
    @SerializedName("store_access")
    @Expose
    private String storeAccess;
    @SerializedName("profile_id")
    @Expose
    private String profileId;
}
