package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class MonitoringType {
    @SerializedName("SLID")
    @Expose
    private String slid;
    @SerializedName("typeID")
    @Expose
    private String typeID;
    @SerializedName("monitoringTypeName")
    @Expose
    private String monitoringTypeName;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("entryUserID")
    @Expose
    private String entryUserID;
    @SerializedName("entryTime")
    @Expose
    private String entryTime;
    @SerializedName("status")
    @Expose
    private String status;
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

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getMonitoringTypeName() {
        return monitoringTypeName;
    }

    public void setMonitoringTypeName(String monitoringTypeName) {
        this.monitoringTypeName = monitoringTypeName;
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
