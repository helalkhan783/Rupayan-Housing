package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class OwnerTypeResponse {
    @SerializedName("ownerTypeID")
    @Expose
    private String ownerTypeID;
    @SerializedName("ownerTypeName")
    @Expose
    private String ownerTypeName;

    public String getOwnerTypeID() {
        return ownerTypeID;
    }

    public void setOwnerTypeID(String ownerTypeID) {
        this.ownerTypeID = ownerTypeID;
    }

    public String getOwnerTypeName() {
        return ownerTypeName;
    }

    public void setOwnerTypeName(String ownerTypeName) {
        this.ownerTypeName = ownerTypeName;
    }
}
