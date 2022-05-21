package com.rupayan_housing.view.fragment.all_report.reconcilation_report.miller_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class ReconciliationReportMillerResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("miller_list")
    @Expose
    private List<ReconciliationMillerList> millerList = null;

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

    public List<ReconciliationMillerList> getMillerList() {
        return millerList;
    }

    public void setMillerList(List<ReconciliationMillerList> millerList) {
        this.millerList = millerList;
    }
}
