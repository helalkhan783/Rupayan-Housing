package com.rupayan_housing.serverResponseModel.list_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class MillerLisenceReportList {
    @SerializedName("slID")
    @Expose
    private String slID;
    @SerializedName("profileID")
    @Expose
    private String profileID;
    @SerializedName("certificateTypeID")
    @Expose
    private String certificateTypeID;
    @SerializedName("issuerName")
    @Expose
    private String issuerName;
    @SerializedName("issueDate")
    @Expose
    private String issueDate;
    @SerializedName("certificateDate")
    @Expose
    private String certificateDate;
    @SerializedName("certificateImage")
    @Expose
    private String certificateImage;
    @SerializedName("renewDate")
    @Expose
    private String renewDate;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("reviewStatus")
    @Expose
    private String reviewStatus;
    @SerializedName("reviewTime")
    @Expose
    private String reviewTime;
    @SerializedName("reviewBy")
    @Expose
    private String reviewBy;
    @SerializedName("ref_slId")
    @Expose
    private String refSlId;
    @SerializedName("miller")
    @Expose
    private String miller;
    @SerializedName("certificate_name")
    @Expose
    private String certificateName;

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getSlID() {
        return slID;
    }

    public void setSlID(String slID) {
        this.slID = slID;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getCertificateTypeID() {
        return certificateTypeID;
    }

    public void setCertificateTypeID(String certificateTypeID) {
        this.certificateTypeID = certificateTypeID;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getCertificateDate() {
        return certificateDate;
    }

    public void setCertificateDate(String certificateDate) {
        this.certificateDate = certificateDate;
    }

    public String getCertificateImage() {
        return certificateImage;
    }

    public void setCertificateImage(String certificateImage) {
        this.certificateImage = certificateImage;
    }

    public String getRenewDate() {
        return renewDate;
    }

    public void setRenewDate(String renewDate) {
        this.renewDate = renewDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getReviewBy() {
        return reviewBy;
    }

    public void setReviewBy(String reviewBy) {
        this.reviewBy = reviewBy;
    }

    public String getRefSlId() {
        return refSlId;
    }

    public void setRefSlId(String refSlId) {
        this.refSlId = refSlId;
    }

    public String getMiller() {
        return miller;
    }

    public void setMiller(String miller) {
        this.miller = miller;
    }
}
