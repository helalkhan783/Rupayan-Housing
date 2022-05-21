package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class PreviousMillerProfileInfo {
    @SerializedName("is_submit")
    @Expose
    private String isSubmit;
    @SerializedName("storeID")
    @Expose
    private String storeID;
    @SerializedName("associationID")
    @Expose
    private String associationID;
    @SerializedName("profile_details_id")
    @Expose
    private String profileDetailsId;
    @SerializedName("sl")
    @Expose
    private String sl;
    @SerializedName("ref_sl")
    @Expose
    private String refSl;
    @SerializedName("zoneID")
    @Expose
    private String zoneID;
    @SerializedName("processTypeID")
    @Expose
    private String processTypeID;
    @SerializedName("DisplayName")
    @Expose
    private String displayName;
    @SerializedName("profileID")
    @Expose
    private String profileID;
    @SerializedName("millTypeIDs")
    @Expose
    private List<String> millTypeIDs = null;
    @SerializedName("capacity")
    @Expose
    private String capacity;
    @SerializedName("millID")
    @Expose
    private String millID;
    @SerializedName("ownerTypeID")
    @Expose
    private String ownerTypeID;
    @SerializedName("divisionID")
    @Expose
    private String divisionID;
    @SerializedName("districtID")
    @Expose
    private String districtID;
    @SerializedName("upazilaID")
    @Expose
    private String upazilaID;
    @SerializedName("profile_photo")
    @Expose
    private String profilePhoto;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("FullName")
    @Expose
    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIsSubmit() {
        return isSubmit;
    }

    public void setIsSubmit(String isSubmit) {
        this.isSubmit = isSubmit;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getAssociationID() {
        return associationID;
    }

    public void setAssociationID(String associationID) {
        this.associationID = associationID;
    }

    public String getProfileDetailsId() {
        return profileDetailsId;
    }

    public void setProfileDetailsId(String profileDetailsId) {
        this.profileDetailsId = profileDetailsId;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getRefSl() {
        return refSl;
    }

    public void setRefSl(String refSl) {
        this.refSl = refSl;
    }

    public String getZoneID() {
        return zoneID;
    }

    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
    }

    public String getProcessTypeID() {
        return processTypeID;
    }

    public void setProcessTypeID(String processTypeID) {
        this.processTypeID = processTypeID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public List<String> getMillTypeIDs() {
        return millTypeIDs;
    }

    public void setMillTypeIDs(List<String> millTypeIDs) {
        this.millTypeIDs = millTypeIDs;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getMillID() {
        return millID;
    }

    public void setMillID(String millID) {
        this.millID = millID;
    }

    public String getOwnerTypeID() {
        return ownerTypeID;
    }

    public void setOwnerTypeID(String ownerTypeID) {
        this.ownerTypeID = ownerTypeID;
    }

    public String getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(String divisionID) {
        this.divisionID = divisionID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getUpazilaID() {
        return upazilaID;
    }

    public void setUpazilaID(String upazilaID) {
        this.upazilaID = upazilaID;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
