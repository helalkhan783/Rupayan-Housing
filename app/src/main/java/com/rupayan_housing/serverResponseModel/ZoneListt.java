package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZoneListt {
    @SerializedName("zoneID")
    @Expose
    private String zoneID;
    @SerializedName("zoneName")
    @Expose
    private String zoneName;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("associationID")
    @Expose
    private Object associationID;

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

    public Object getAssociationID() {
        return associationID;
    }

    public void setAssociationID(Object associationID) {
        this.associationID = associationID;
    }
}
