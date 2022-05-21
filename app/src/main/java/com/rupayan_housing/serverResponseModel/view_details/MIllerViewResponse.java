package com.rupayan_housing.serverResponseModel.view_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class MIllerViewResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("employee_data")
    @Expose
    private EmployeeData employeeData;
    @SerializedName("qc_data")
    @Expose
    private QcData qcData;
    @SerializedName("certificate_data")
    @Expose
    private List<MillerCertificateDatum> certificateData = null;
    @SerializedName("owner_data")
    @Expose
    private List<MillerOwnerDatum> ownerData = null;
    @SerializedName("profile_data")
    @Expose
    private ProfileData profileData;
    @SerializedName("get_details")
    @Expose
    private GetDetails getDetails;
    @SerializedName("owner_info")
    @Expose
    private List<OwnerInfo> ownerInfo = null;
    @SerializedName("certificate_details")
    @Expose
    private List<CertificateDetail> certificateDetails = null;
    @SerializedName("otherInfoDetails")
    @Expose
    private OtherInfoDetails otherInfoDetails;

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

    public EmployeeData getEmployeeData() {
        return employeeData;
    }

    public void setEmployeeData(EmployeeData employeeData) {
        this.employeeData = employeeData;
    }

    public QcData getQcData() {
        return qcData;
    }

    public void setQcData(QcData qcData) {
        this.qcData = qcData;
    }

    public List<MillerCertificateDatum> getCertificateData() {
        return certificateData;
    }

    public void setCertificateData(List<MillerCertificateDatum> certificateData) {
        this.certificateData = certificateData;
    }

    public List<MillerOwnerDatum> getOwnerData() {
        return ownerData;
    }

    public void setOwnerData(List<MillerOwnerDatum> ownerData) {
        this.ownerData = ownerData;
    }

    public ProfileData getProfileData() {
        return profileData;
    }

    public void setProfileData(ProfileData profileData) {
        this.profileData = profileData;
    }

    public GetDetails getGetDetails() {
        return getDetails;
    }

    public void setGetDetails(GetDetails getDetails) {
        this.getDetails = getDetails;
    }

    public List<OwnerInfo> getOwnerInfo() {
        return ownerInfo;
    }

    public void setOwnerInfo(List<OwnerInfo> ownerInfo) {
        this.ownerInfo = ownerInfo;
    }

    public List<CertificateDetail> getCertificateDetails() {
        return certificateDetails;
    }

    public void setCertificateDetails(List<CertificateDetail> certificateDetails) {
        this.certificateDetails = certificateDetails;
    }

    public OtherInfoDetails getOtherInfoDetails() {
        return otherInfoDetails;
    }

    public void setOtherInfoDetails(OtherInfoDetails otherInfoDetails) {
        this.otherInfoDetails = otherInfoDetails;
    }
}
