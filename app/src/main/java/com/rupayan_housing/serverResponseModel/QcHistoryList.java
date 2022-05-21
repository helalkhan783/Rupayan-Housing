package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class QcHistoryList {
    @SerializedName("slID")
    @Expose
    private String slID;
    @SerializedName("qcID")
    @Expose
    private String qcID;
    @SerializedName("testType")
    @Expose
    private String testType;
    @SerializedName("testedBy")
    @Expose
    private Object testedBy;
    @SerializedName("licenecIssuerID")
    @Expose
    private String licenecIssuerID;
    @SerializedName("testName")
    @Expose
    private String testName;
    @SerializedName("short_name")
    @Expose
    private Object shortName;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("testDate")
    @Expose
    private String testDate;
    @SerializedName("entryDateTime")
    @Expose
    private String entryDateTime;
    @SerializedName("entryUserID")
    @Expose
    private String entryUserID;
    @SerializedName("reviewStatus")
    @Expose
    private String reviewStatus;
    @SerializedName("reviewTime")
    @Expose
    private String reviewTime;
    @SerializedName("reviewBy")
    @Expose
    private String reviewBy;
    @SerializedName("model")
    @Expose
    private Object model;
    @SerializedName("note")
    @Expose
    private Object note;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("declined_remarks")
    @Expose
    private Object declinedRemarks;
    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("storeID")
    @Expose
    private String storeID;
    @SerializedName("ref_slID")
    @Expose
    private String refSlID;
    @SerializedName("store_name")
    @Expose
    private String storeName;

    public String getSlID() {
        return slID;
    }

    public void setSlID(String slID) {
        this.slID = slID;
    }

    public String getQcID() {
        return qcID;
    }

    public void setQcID(String qcID) {
        this.qcID = qcID;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public Object getTestedBy() {
        return testedBy;
    }

    public void setTestedBy(Object testedBy) {
        this.testedBy = testedBy;
    }

    public String getLicenecIssuerID() {
        return licenecIssuerID;
    }

    public void setLicenecIssuerID(String licenecIssuerID) {
        this.licenecIssuerID = licenecIssuerID;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Object getShortName() {
        return shortName;
    }

    public void setShortName(Object shortName) {
        this.shortName = shortName;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getEntryDateTime() {
        return entryDateTime;
    }

    public void setEntryDateTime(String entryDateTime) {
        this.entryDateTime = entryDateTime;
    }

    public String getEntryUserID() {
        return entryUserID;
    }

    public void setEntryUserID(String entryUserID) {
        this.entryUserID = entryUserID;
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

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public Object getNote() {
        return note;
    }

    public void setNote(Object note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getDeclinedRemarks() {
        return declinedRemarks;
    }

    public void setDeclinedRemarks(Object declinedRemarks) {
        this.declinedRemarks = declinedRemarks;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getRefSlID() {
        return refSlID;
    }

    public void setRefSlID(String refSlID) {
        this.refSlID = refSlID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
