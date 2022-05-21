package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rupayan_housing.view.fragment.monitoring.MonitoringDetails;

import java.util.List;

import lombok.Data;

@Data
public class MonitoringViewResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("monitoring_details")
    @Expose
    private MonitoringDetails monitoringDetails;
    @SerializedName("zone_list")
    @Expose
    private List<ZoneList> zoneList = null;
    @SerializedName("miller")
    @Expose
    private List<Miller> miller = null;
    @SerializedName("monitoring_type")
    @Expose
    private  List<MonitoringType> monitoringType = null;

    public List<MonitoringType> getMonitoringType() {
        return monitoringType;
    }

    public void setMonitoringType(List<MonitoringType> monitoringType) {
        this.monitoringType = monitoringType;
    }

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

    public MonitoringDetails getMonitoringDetails() {
        return monitoringDetails;
    }

    public void setMonitoringDetails(MonitoringDetails monitoringDetails) {
        this.monitoringDetails = monitoringDetails;
    }

    public List<ZoneList> getZoneList() {
        return zoneList;
    }

    public void setZoneList(List<ZoneList> zoneList) {
        this.zoneList = zoneList;
    }

    public List<Miller> getMiller() {
        return miller;
    }

    public void setMiller(List<Miller> miller) {
        this.miller = miller;
    }
}
