package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ZoneListResponse {
    @SerializedName("zoneID")
    @Expose
    private String zoneID;
    @SerializedName("zoneName")
    @Expose
    private String zoneName;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("store_name")
    @Expose
    private String justAssociation;

    public String getJustAssociation() {
        return justAssociation;
    }

    public void setJustAssociation(String justAssociation) {
        this.justAssociation = justAssociation;
    }

    public String getZoneID() {
        return zoneID;
    }

    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
