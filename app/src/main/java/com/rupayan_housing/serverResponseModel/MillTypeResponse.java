package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class MillTypeResponse {
    @SerializedName("millTypeID")
    @Expose
    private String millTypeID;
    @SerializedName("millTypeName")
    @Expose
    private String millTypeName;
    @SerializedName("remarks")
    @Expose
    private String remarks;

    public String getMillTypeID() {
        return millTypeID;
    }

    public void setMillTypeID(String millTypeID) {
        this.millTypeID = millTypeID;
    }

    public String getMillTypeName() {
        return millTypeName;
    }

    public void setMillTypeName(String millTypeName) {
        this.millTypeName = millTypeName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
