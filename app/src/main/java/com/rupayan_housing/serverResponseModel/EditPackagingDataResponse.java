package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;


public class EditPackagingDataResponse  {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("package_details")
    @Expose
    private PackageDetails packageDetails;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public PackageDetails getPackageDetails() {
        return packageDetails;
    }

    public void setPackageDetails(PackageDetails packageDetails) {
        this.packageDetails = packageDetails;
    }
}
