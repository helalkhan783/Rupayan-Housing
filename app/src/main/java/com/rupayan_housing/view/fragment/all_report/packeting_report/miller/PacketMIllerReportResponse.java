package com.rupayan_housing.view.fragment.all_report.packeting_report.miller;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rupayan_housing.view.fragment.all_report.packeting_report.page_data_response.PacketReportMillerList;

import java.util.List;

import lombok.Data;

@Data
public class PacketMIllerReportResponse {


    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("miller_list")
    @Expose
    private List<PacketReportMillerList> millerList = null;

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

    public List<PacketReportMillerList> getMillerList() {
        return millerList;
    }

    public void setMillerList(List<PacketReportMillerList> millerList) {
        this.millerList = millerList;
    }
}
