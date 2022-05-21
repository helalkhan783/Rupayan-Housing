package com.rupayan_housing.serverResponseModel.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rupayan_housing.serverResponseModel.MillType;
import com.rupayan_housing.serverResponseModel.ProcessType;
import com.rupayan_housing.serverResponseModel.miller_response.LicenceMillerList;

import java.util.List;

import lombok.Data;

@Data
public class MillerLicenceReportResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("asociation_list")
    @Expose
    private List<MillerReportAsociationList> asociationList = null;
    @SerializedName("certificate_types")
    @Expose
    private List<MIllerLienceReportCertificateType> certificateTypes = null;
    @SerializedName("miller_list")
    @Expose
    private List<LicenceMillerList> millerList = null;
    @SerializedName("associationID")
    @Expose
    private String associationID;
    @SerializedName("process_type")
    @Expose
    private List<ProcessType> processType = null;
    @SerializedName("mill_types")
    @Expose
    private List<MillType> millTypes = null;
    @SerializedName("mill_status")
    @Expose
    private List<MillStatus> millStatus = null;

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

    public List<MillerReportAsociationList> getAsociationList() {
        return asociationList;
    }

    public void setAsociationList(List<MillerReportAsociationList> asociationList) {
        this.asociationList = asociationList;
    }

    public List<MIllerLienceReportCertificateType> getCertificateTypes() {
        return certificateTypes;
    }

    public void setCertificateTypes(List<MIllerLienceReportCertificateType> certificateTypes) {
        this.certificateTypes = certificateTypes;
    }

    public List<LicenceMillerList> getMillerList() {
        return millerList;
    }

    public void setMillerList(List<LicenceMillerList> millerList) {
        this.millerList = millerList;
    }

    public String getAssociationID() {
        return associationID;
    }

    public void setAssociationID(String associationID) {
        this.associationID = associationID;
    }

    public List<ProcessType> getProcessType() {
        return processType;
    }

    public void setProcessType(List<ProcessType> processType) {
        this.processType = processType;
    }

    public List<MillType> getMillTypes() {
        return millTypes;
    }

    public void setMillTypes(List<MillType> millTypes) {
        this.millTypes = millTypes;
    }

    public List<MillStatus> getMillStatus() {
        return millStatus;
    }

    public void setMillStatus(List<MillStatus> millStatus) {
        this.millStatus = millStatus;
    }
}
