package com.rupayan_housing.view.fragment.all_report.packeting_report.page_data_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class PacketReportAssociationList {
    @SerializedName("storeID")
    @Expose
    private String storeID;
    @SerializedName("profile_id")
    @Expose
    private String profileId;
    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("FullName")
    @Expose
    private String fullName;
    @SerializedName("DisplayName")
    @Expose
    private String displayName;
    @SerializedName("zoneID")
    @Expose
    private String zoneID;

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getZoneID() {
        return zoneID;
    }

    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
    }
}
