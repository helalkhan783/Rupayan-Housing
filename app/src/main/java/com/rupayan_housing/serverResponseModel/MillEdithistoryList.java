package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MillEdithistoryList {

    @SerializedName("sl")
    @Expose
    private String sl;
    @SerializedName("entryTime")
    @Expose
    private String entryTime;
    @SerializedName("DisplayName")
    @Expose
    private String displayName;
    @SerializedName("processTypeName")
    @Expose
    private String processTypeName;
    @SerializedName("millTypeName")
    @Expose
    private String millTypeName;
    @SerializedName("capacity")
    @Expose
    private String capacity;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("division_name")
    @Expose
    private String divisionName;
    @SerializedName("district_name")
    @Expose
    private String districtName;
    @SerializedName("upazila_name")
    @Expose
    private String upazilaName;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("mill_id")
    @Expose
    private String millId;
    @SerializedName("login_id")
    @Expose
    private String loginId;
    @SerializedName("profile_id")
    @Expose
    private String profileId;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("review_time")
    @Expose
    private String reviewTime;
    @SerializedName("zone_name")
    @Expose
    private String zoneName;

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
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

    public String getMillTypeName() {
        return millTypeName;
    }

    public void setMillTypeName(String millTypeName) {
        this.millTypeName = millTypeName;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getUpazilaName() {
        return upazilaName;
    }

    public void setUpazilaName(String upazilaName) {
        this.upazilaName = upazilaName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMillId() {
        return millId;
    }

    public void setMillId(String millId) {
        this.millId = millId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
}
