package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class MonitoringModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("monitoring_type")
    @Expose
    private  List<MonitoringType> monitoringType = null;
    @SerializedName("lists")
    @Expose
    private List<ListMonitorModel> lists = null;
    @SerializedName("zone_list")
    @Expose
    private  List<ZoneListt> zoneList = null;

    public List<ZoneListt> getZoneList() {
        return zoneList;
    }

    public void setZoneList(List<ZoneListt> zoneList) {
        this.zoneList = zoneList;
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

    public List<MonitoringType> getMonitoringType() {
        return monitoringType;
    }

    public void setMonitoringType(List<MonitoringType> monitoringType) {
        this.monitoringType = monitoringType;
    }

    public List<ListMonitorModel> getLists() {
        return lists;
    }

    public void setLists(List<ListMonitorModel> lists) {
        this.lists = lists;
    }
}
