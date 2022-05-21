package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;


public class PackageDetails {
    @SerializedName("packagingID")
    @Expose
    private String packagingID;
    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("packaging_infos")
    @Expose
    private PackagingInfos packagingInfos;
    @SerializedName("ref_user_infos")
    @Expose
    private RefUserInfos refUserInfos;
    @SerializedName("packaging_details")
    @Expose
    private List<PackagingDetail> packagingDetails = null;

    public String getPackagingID() {
        return packagingID;
    }

    public void setPackagingID(String packagingID) {
        this.packagingID = packagingID;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public PackagingInfos getPackagingInfos() {
        return packagingInfos;
    }

    public void setPackagingInfos(PackagingInfos packagingInfos) {
        this.packagingInfos = packagingInfos;
    }

    public RefUserInfos getRefUserInfos() {
        return refUserInfos;
    }

    public void setRefUserInfos(RefUserInfos refUserInfos) {
        this.refUserInfos = refUserInfos;
    }

    public List<PackagingDetail> getPackagingDetails() {
        return packagingDetails;
    }

    public void setPackagingDetails(List<PackagingDetail> packagingDetails) {
        this.packagingDetails = packagingDetails;
    }
}
