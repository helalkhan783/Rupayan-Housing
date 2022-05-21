package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CertificateInfo {
    @SerializedName("SLID")
    @Expose
    private String slid;
    @SerializedName("certificateTypeID")
    @Expose
    private String certificateTypeID;
    @SerializedName("certificateProvider")
    @Expose
    private String certificateProvider;
    @SerializedName("certificateTypeName")
    @Expose
    private String certificateTypeName;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("entryUserID")
    @Expose
    private String entryUserID;
    @SerializedName("entryTime")
    @Expose
    private String entryTime;
    @SerializedName("certificateTypeStatus")
    @Expose
    private String certificateTypeStatus;
    @SerializedName("reviewStatus")
    @Expose
    private String reviewStatus;
    @SerializedName("reviewBy")
    @Expose
    private String reviewBy;
    @SerializedName("reviewTime")
    @Expose
    private String reviewTime;
    @SerializedName("refSLID")
    @Expose
    private String refSLID;

    public String getSlid() {
        return slid;
    }

    public void setSlid(String slid) {
        this.slid = slid;
    }

    public String getCertificateTypeID() {
        return certificateTypeID;
    }

    public void setCertificateTypeID(String certificateTypeID) {
        this.certificateTypeID = certificateTypeID;
    }

    public String getCertificateProvider() {
        return certificateProvider;
    }

    public void setCertificateProvider(String certificateProvider) {
        this.certificateProvider = certificateProvider;
    }

    public String getCertificateTypeName() {
        return certificateTypeName;
    }

    public void setCertificateTypeName(String certificateTypeName) {
        this.certificateTypeName = certificateTypeName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getEntryUserID() {
        return entryUserID;
    }

    public void setEntryUserID(String entryUserID) {
        this.entryUserID = entryUserID;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getCertificateTypeStatus() {
        return certificateTypeStatus;
    }

    public void setCertificateTypeStatus(String certificateTypeStatus) {
        this.certificateTypeStatus = certificateTypeStatus;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
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

    public String getRefSLID() {
        return refSLID;
    }

    public void setRefSLID(String refSLID) {
        this.refSLID = refSLID;
    }
}
