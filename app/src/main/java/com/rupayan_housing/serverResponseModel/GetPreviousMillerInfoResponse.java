package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class GetPreviousMillerInfoResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("profile_info")
    @Expose
    private PreviousMillerProfileInfo profileInfo;
    @SerializedName("owner_info")
    @Expose
    private List<PreviousMillerOwnerInfo> ownerInfo = null;
    @SerializedName("certificate_info")
    @Expose
    private List<PreviousMillerCertificateInfo> certificateInfo = null;
    @SerializedName("qc_info")
    @Expose
    private PreviousMillerQcInfo qcInfo;
    @SerializedName("employee_info")
    @Expose
    private PreviousMillerEmployeeInfo employeeInfo;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PreviousMillerProfileInfo getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(PreviousMillerProfileInfo profileInfo) {
        this.profileInfo = profileInfo;
    }

    public List<PreviousMillerOwnerInfo> getOwnerInfo() {
        return ownerInfo;
    }

    public void setOwnerInfo(List<PreviousMillerOwnerInfo> ownerInfo) {
        this.ownerInfo = ownerInfo;
    }

    public List<PreviousMillerCertificateInfo> getCertificateInfo() {
        return certificateInfo;
    }

    public void setCertificateInfo(List<PreviousMillerCertificateInfo> certificateInfo) {
        this.certificateInfo = certificateInfo;
    }

    public PreviousMillerQcInfo getQcInfo() {
        return qcInfo;
    }

    public void setQcInfo(PreviousMillerQcInfo qcInfo) {
        this.qcInfo = qcInfo;
    }

    public PreviousMillerEmployeeInfo getEmployeeInfo() {
        return employeeInfo;
    }

    public void setEmployeeInfo(PreviousMillerEmployeeInfo employeeInfo) {
        this.employeeInfo = employeeInfo;
    }
}
