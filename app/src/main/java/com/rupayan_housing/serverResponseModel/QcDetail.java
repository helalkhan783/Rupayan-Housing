package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class QcDetail {
    @SerializedName("slID")
    @Expose
    private String slID;
    @SerializedName("refqc_slID")
    @Expose
    private String refqcSlID;
    @SerializedName("qcID")
    @Expose
    private String qcID;
    @SerializedName("parameterName")
    @Expose
    private Object parameterName;
    @SerializedName("testID")
    @Expose
    private String testID;
    @SerializedName("parameterValue")
    @Expose
    private String parameterValue;
    @SerializedName("status")
    @Expose
    private String status;

    public String getSlID() {
        return slID;
    }

    public void setSlID(String slID) {
        this.slID = slID;
    }

    public String getRefqcSlID() {
        return refqcSlID;
    }

    public void setRefqcSlID(String refqcSlID) {
        this.refqcSlID = refqcSlID;
    }

    public String getQcID() {
        return qcID;
    }

    public void setQcID(String qcID) {
        this.qcID = qcID;
    }

    public Object getParameterName() {
        return parameterName;
    }

    public void setParameterName(Object parameterName) {
        this.parameterName = parameterName;
    }

    public String getTestID() {
        return testID;
    }

    public void setTestID(String testID) {
        this.testID = testID;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
