package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class MonitoringDetails {
    @SerializedName("slID")
    @Expose
    private String slID;
    @SerializedName("monitor_id")
    @Expose
    private String monitorId;
    @SerializedName("monitoringDate")
    @Expose
    private String monitoringDate;
    @SerializedName("agencyName")
    @Expose
    private String agencyName;
    @SerializedName("zoneID")
    @Expose
    private String zoneID;
    @SerializedName("millID")
    @Expose
    private String millID;
    @SerializedName("monitoringType")
    @Expose
    private String monitoringType;
    @SerializedName("monitorBy")
    @Expose
    private String monitorBy;
    @SerializedName("otherMonitoringTypeName")
    @Expose
    private Object otherMonitoringTypeName;
    @SerializedName("monitoringSummery")
    @Expose
    private String monitoringSummery;
    @SerializedName("publishDate")
    @Expose
    private String publishDate;
    @SerializedName("document")
    @Expose
    private String document;
    @SerializedName("entryDateTime")
    @Expose
    private String entryDateTime;
    @SerializedName("entryuserID")
    @Expose
    private String entryuserID;
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
    @SerializedName("ref_slID")
    @Expose
    private String refSlID;

    public String getSlID() {
        return slID;
    }

    public void setSlID(String slID) {
        this.slID = slID;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public String getMonitoringDate() {
        return monitoringDate;
    }

    public void setMonitoringDate(String monitoringDate) {
        this.monitoringDate = monitoringDate;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
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

    public String getMonitoringType() {
        return monitoringType;
    }

    public void setMonitoringType(String monitoringType) {
        this.monitoringType = monitoringType;
    }

    public String getMonitorBy() {
        return monitorBy;
    }

    public void setMonitorBy(String monitorBy) {
        this.monitorBy = monitorBy;
    }

    public Object getOtherMonitoringTypeName() {
        return otherMonitoringTypeName;
    }

    public void setOtherMonitoringTypeName(Object otherMonitoringTypeName) {
        this.otherMonitoringTypeName = otherMonitoringTypeName;
    }

    public String getMonitoringSummery() {
        return monitoringSummery;
    }

    public void setMonitoringSummery(String monitoringSummery) {
        this.monitoringSummery = monitoringSummery;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getEntryDateTime() {
        return entryDateTime;
    }

    public void setEntryDateTime(String entryDateTime) {
        this.entryDateTime = entryDateTime;
    }

    public String getEntryuserID() {
        return entryuserID;
    }

    public void setEntryuserID(String entryuserID) {
        this.entryuserID = entryuserID;
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

    public String getRefSlID() {
        return refSlID;
    }

    public void setRefSlID(String refSlID) {
        this.refSlID = refSlID;
    }
}
