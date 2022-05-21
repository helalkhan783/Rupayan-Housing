package com.rupayan_housing.view.fragment.all_report.employee_report.list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class EmployeeReportListResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("profuct_list")
    @Expose
    private List<EmployeeReportProfuctList> profuctList = null;

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

    public List<EmployeeReportProfuctList> getProfuctList() {
        return profuctList;
    }

    public void setProfuctList(List<EmployeeReportProfuctList> profuctList) {
        this.profuctList = profuctList;
    }
}
