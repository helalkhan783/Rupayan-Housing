package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class QcqaList {
    @SerializedName("slID")
    @Expose
    public String slID;
    @SerializedName("qcID")
    @Expose
    public String qcID;
    @SerializedName("testType")
    @Expose
    public Object testType;
    @SerializedName("testedBy")
    @Expose
    public Object testedBy;
    @SerializedName("licenecIssuerID")
    @Expose
    public Object licenecIssuerID;
    @SerializedName("testName")
    @Expose
    public Object testName;
    @SerializedName("short_name")
    @Expose
    public Object shortName;
    @SerializedName("reference")
    @Expose
    public Object reference;
    @SerializedName("testDate")
    @Expose
    public String testDate;
    @SerializedName("entryDateTime")
    @Expose
    public String entryDateTime;
    @SerializedName("entryUserID")
    @Expose
    public String entryUserID;
    @SerializedName("reviewStatus")
    @Expose
    public String reviewStatus;
    @SerializedName("reviewTime")
    @Expose
    public Object reviewTime;
    @SerializedName("reviewBy")
    @Expose
    public Object reviewBy;
    @SerializedName("model")
    @Expose
    public String model;
    @SerializedName("note")
    @Expose
    public String note;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("declined_remarks")
    @Expose
    public Object declinedRemarks;
    @SerializedName("vendorID")
    @Expose
    public String vendorID;
    @SerializedName("storeID")
    @Expose
    public String storeID;
    @SerializedName("ref_slID")
    @Expose
    public String refSlID;

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

    public Object getTestType() {
        return testType;
    }

    public void setTestType(Object testType) {
        this.testType = testType;
    }

    public Object getTestedBy() {
        return testedBy;
    }

    public void setTestedBy(Object testedBy) {
        this.testedBy = testedBy;
    }

    public Object getLicenecIssuerID() {
        return licenecIssuerID;
    }

    public void setLicenecIssuerID(Object licenecIssuerID) {
        this.licenecIssuerID = licenecIssuerID;
    }

    public Object getTestName() {
        return testName;
    }

    public void setTestName(Object testName) {
        this.testName = testName;
    }

    public Object getShortName() {
        return shortName;
    }

    public void setShortName(Object shortName) {
        this.shortName = shortName;
    }

    public Object getReference() {
        return reference;
    }

    public void setReference(Object reference) {
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

    public Object getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Object reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Object getReviewBy() {
        return reviewBy;
    }

    public void setReviewBy(Object reviewBy) {
        this.reviewBy = reviewBy;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
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
}
