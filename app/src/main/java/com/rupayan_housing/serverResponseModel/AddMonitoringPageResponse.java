package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class AddMonitoringPageResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("zone_list")
    @Expose
    private List<ZoneListResponse> zoneList = null;
    @SerializedName("miller_list")
    @Expose
    private List<MillerListResponse> millerList = null;
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

    public List<ZoneListResponse> getZoneList() {
        return zoneList;
    }

    public void setZoneList(List<ZoneListResponse> zoneList) {
        this.zoneList = zoneList;
    }

    public List<MillerListResponse> getMillerList() {
        return millerList;
    }

    public void setMillerList(List<MillerListResponse> millerList) {
        this.millerList = millerList;
    }

    public List<MonitoringTypeList> getMonitoringType() {
        return monitoringType;
    }

    public void setMonitoringType(List<MonitoringTypeList> monitoringType) {
        this.monitoringType = monitoringType;
    }
}
