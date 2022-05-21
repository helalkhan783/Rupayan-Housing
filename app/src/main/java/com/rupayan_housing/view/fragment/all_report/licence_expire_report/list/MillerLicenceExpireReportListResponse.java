package com.rupayan_housing.view.fragment.all_report.licence_expire_report.list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class MillerLicenceExpireReportListResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("expire_list")
    @Expose
    private List<MillerLicenceExpireReportList> expireList = null;

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

    public List<MillerLicenceExpireReportList> getExpireList() {
        return expireList;
    }

    public void setExpireList(List<MillerLicenceExpireReportList> expireList) {
        this.expireList = expireList;
    }
}
