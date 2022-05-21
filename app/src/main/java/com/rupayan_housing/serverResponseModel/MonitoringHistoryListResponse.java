package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class MonitoringHistoryListResponse {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("monitoring_type")
    @Expose
    public  List<MonitoringType> monitoringType = null;
    @SerializedName("lists")
    @Expose
    public List<MonitoringHisList> lists = null;

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

    public List<MonitoringHisList> getLists() {
        return lists;
    }

    public void setLists(List<MonitoringHisList> lists) {
        this.lists = lists;
    }
}
