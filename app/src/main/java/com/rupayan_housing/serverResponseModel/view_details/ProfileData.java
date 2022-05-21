package com.rupayan_housing.serverResponseModel.view_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ProfileData {
    @SerializedName("DisplayName")
    @Expose
    private String displayName;
    @SerializedName("processTypeName")
    @Expose
    private String processTypeName;
    @SerializedName("ownerTypeName")
    @Expose
    private String ownerTypeName;
    @SerializedName("millTypeName")
    @Expose
    private String millTypeName;
    @SerializedName("division_name")
    @Expose
    private String divisionName;
    @SerializedName("capacity")
    @Expose
    private String capacity;
    @SerializedName("district_name")
    @Expose
    private String districtName;
    @SerializedName("zoneName")
    @Expose
    private String zoneName;
    @SerializedName("upazila_name")
    @Expose
    private String upazilaName;
    @SerializedName("millID")
    @Expose
    private String millID;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("reviewStatus")
    @Expose
    private String reviewStatus;
    @SerializedName("is_submit")
    @Expose
    private Object isSubmit;
    @SerializedName("FullName")
    @Expose
    private String fullName;

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

    public String getProcessTypeName() {
        return processTypeName;
    }

    public void setProcessTypeName(String processTypeName) {
        this.processTypeName = processTypeName;
    }

    public String getOwnerTypeName() {
        return ownerTypeName;
    }

    public void setOwnerTypeName(String ownerTypeName) {
        this.ownerTypeName = ownerTypeName;
    }

    public String getMillTypeName() {
        return millTypeName;
    }

    public void setMillTypeName(String millTypeName) {
        this.millTypeName = millTypeName;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getUpazilaName() {
        return upazilaName;
    }

    public void setUpazilaName(String upazilaName) {
        this.upazilaName = upazilaName;
    }

    public String getMillID() {
        return millID;
    }

    public void setMillID(String millID) {
        this.millID = millID;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Object getIsSubmit() {
        return isSubmit;
    }

    public void setIsSubmit(Object isSubmit) {
        this.isSubmit = isSubmit;
    }
}
