package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class UpdateMonitoringPageResponse {
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
    private List<ZoneListResponse> zoneList = null;
    @SerializedName("miller")
    @Expose
    private List<MillerListResponse> miller = null;

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

    public List<ZoneListResponse> getZoneList() {
        return zoneList;
    }

    public void setZoneList(List<ZoneListResponse> zoneList) {
        this.zoneList = zoneList;
    }

    public List<MillerListResponse> getMiller() {
        return miller;
    }

    public void setMiller(List<MillerListResponse> miller) {
        this.miller = miller;
    }
}
