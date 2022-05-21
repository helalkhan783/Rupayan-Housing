package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZoneWiseMonitorignList {
    @SerializedName("zone_name")
    @Expose
    private String zoneName;
    @SerializedName("total_mill")
    @Expose
    private Integer totalMill;
    @SerializedName("total_monitor")
    @Expose
    private String totalMonitor;

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Integer getTotalMill() {
        return totalMill;
    }

    public void setTotalMill(Integer totalMill) {
        this.totalMill = totalMill;
    }

    public String getTotalMonitor() {
        return totalMonitor;
    }

    public void setTotalMonitor(String totalMonitor) {
        this.totalMonitor = totalMonitor;
    }
}
