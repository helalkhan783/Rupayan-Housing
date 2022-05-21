package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class MonitoringReportPageDataResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("zone_list")
    @Expose
    private List<ReportZoneList> zoneList = null;
    @SerializedName("monitoring_type")
    @Expose
    private List<MonitoringTypeList> monitoringType = null;

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

    public List<ReportZoneList> getZoneList() {
        return zoneList;
    }

    public void setZoneList(List<ReportZoneList> zoneList) {
        this.zoneList = zoneList;
    }

    public List<MonitoringTypeList> getMonitoringType() {
        return monitoringType;
    }

    public void setMonitoringType(List<MonitoringTypeList> monitoringType) {
        this.monitoringType = monitoringType;
    }
}
