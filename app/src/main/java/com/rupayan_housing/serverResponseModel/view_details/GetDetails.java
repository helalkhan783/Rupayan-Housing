package com.rupayan_housing.serverResponseModel.view_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data

public class GetDetails {
    @SerializedName("sl")
    @Expose
    private String sl;
    @SerializedName("profile_details_id")
    @Expose
    private String profileDetailsId;
    @SerializedName("profileID")
    @Expose
    private String profileID;
    @SerializedName("processTypeID")
    @Expose
    private String processTypeID;
    @SerializedName("millTypeID")
    @Expose
    private String millTypeID;
    @SerializedName("capacity")
    @Expose
    private String capacity;
    @SerializedName("zoneID")
    @Expose
    private String zoneID;
    @SerializedName("millID")
    @Expose
    private String millID;
    @SerializedName("ownerTypeID")
    @Expose
    private String ownerTypeID;
    @SerializedName("countryID")
    @Expose
    private String countryID;
    @SerializedName("divisionID")
    @Expose
    private String divisionID;
    @SerializedName("districtID")
    @Expose
    private String districtID;
    @SerializedName("upazilaID")
    @Expose
    private String upazilaID;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("entryTime")
    @Expose
    private String entryTime;
    @SerializedName("entryUserID")
    @Expose
    private String entryUserID;
    @SerializedName("reviewBy")
    @Expose
    private String reviewBy;
    @SerializedName("reviewTime")
    @Expose
    private String reviewTime;
    @SerializedName("reviewStatus")
    @Expose
    private String reviewStatus;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("address_1")
    @Expose
    private Object address1;
    @SerializedName("address_2")
    @Expose
    private Object address2;
    @SerializedName("ref_sl")
    @Expose
    private String refSl;
    @SerializedName("submit_by")
    @Expose
    private Object submitBy;
    @SerializedName("submit_time")
    @Expose
    private Object submitTime;
    @SerializedName("is_submit")
    @Expose
    private Object isSubmit;
    @SerializedName("storeID")
    @Expose
    private String storeID;
    @SerializedName("associationID")
    @Expose
    private String associationID;
    @SerializedName("millTypeIDs")
    @Expose
    private String millTypeIDs;
    @SerializedName("profile_photo")
    @Expose
    private String profilePhoto;
    @SerializedName("DisplayName")
    @Expose
    private String displayName;
    @SerializedName("FullName")
    @Expose
    private String fullName;
    @SerializedName("PrimaryMobile")
    @Expose
    private String primaryMobile;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("profile_id")
    @Expose
    private String profileId;
    @SerializedName("profile_type_id")
    @Expose
    private String profileTypeId;
    @SerializedName("processTypeName")
    @Expose
    private String processTypeName;
    @SerializedName("millTypeName")
    @Expose
    private String millTypeName;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("upazila_name")
    @Expose
    private String upazilaName;
    @SerializedName("division_name")
    @Expose
    private String divisionName;
    @SerializedName("district_name")
    @Expose
    private String districtName;

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getProfileDetailsId() {
        return profileDetailsId;
    }

    public void setProfileDetailsId(String profileDetailsId) {
        this.profileDetailsId = profileDetailsId;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getProcessTypeID() {
        return processTypeID;
    }

    public void setProcessTypeID(String processTypeID) {
        this.processTypeID = processTypeID;
    }

    public String getMillTypeID() {
        return millTypeID;
    }

    public void setMillTypeID(String millTypeID) {
        this.millTypeID = millTypeID;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getZoneID() {
        return zoneID;
    }

    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
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

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getEntryUserID() {
        return entryUserID;
    }

    public void setEntryUserID(String entryUserID) {
        this.entryUserID = entryUserID;
    }

    public String getReviewBy() {
        return reviewBy;
    }

    public void setReviewBy(String reviewBy) {
        this.reviewBy = reviewBy;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public Object getAddress1() {
        return address1;
    }

    public void setAddress1(Object address1) {
        this.address1 = address1;
    }

    public Object getAddress2() {
        return address2;
    }

    public void setAddress2(Object address2) {
        this.address2 = address2;
    }

    public String getRefSl() {
        return refSl;
    }

    public void setRefSl(String refSl) {
        this.refSl = refSl;
    }

    public Object getSubmitBy() {
        return submitBy;
    }

    public void setSubmitBy(Object submitBy) {
        this.submitBy = submitBy;
    }

    public Object getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Object submitTime) {
        this.submitTime = submitTime;
    }

    public Object getIsSubmit() {
        return isSubmit;
    }

    public void setIsSubmit(Object isSubmit) {
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

    public String getMillTypeIDs() {
        return millTypeIDs;
    }

    public void setMillTypeIDs(String millTypeIDs) {
        this.millTypeIDs = millTypeIDs;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPrimaryMobile() {
        return primaryMobile;
    }

    public void setPrimaryMobile(String primaryMobile) {
        this.primaryMobile = primaryMobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getProfileTypeId() {
        return profileTypeId;
    }

    public void setProfileTypeId(String profileTypeId) {
        this.profileTypeId = profileTypeId;
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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getUpazilaName() {
        return upazilaName;
    }

    public void setUpazilaName(String upazilaName) {
        this.upazilaName = upazilaName;
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
}
